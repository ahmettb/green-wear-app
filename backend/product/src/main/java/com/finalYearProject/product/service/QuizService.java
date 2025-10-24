package com.finalYearProject.product.service;

import com.finalYearProject.product.entity.*;
import com.finalYearProject.product.entity.request.SolveQuizRequest;
import com.finalYearProject.product.entity.response.OptionResponse;
import com.finalYearProject.product.entity.response.QuestionResponse;
import com.finalYearProject.product.entity.response.QuizResponse;
import com.finalYearProject.product.entity.response.QuizStatusResponse;
import com.finalYearProject.product.repository.QuizRepository;
import com.finalYearProject.product.repository.UserQuizRepository;
import com.finalYearProject.product.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final UserQuizRepository userQuizRepository;




    public QuizStatusResponse solveQuizByUser(SolveQuizRequest request) {
        Quiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() -> new EntityNotFoundException("Quiz not found with ID: " + request.getQuizId()));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + request.getUserId()));

        List<UserQuiz> existingUserQuizzes = userQuizRepository.findByUserAndQuiz(user, quiz);

        UserQuiz userQuiz;

        if (existingUserQuizzes.isEmpty()) {
            userQuiz = new UserQuiz();
            userQuiz.setUser(user);
            userQuiz.setQuiz(quiz);
        } else {

            Optional<UserQuiz> incompleteQuiz = existingUserQuizzes.stream()
                    .filter(uq -> uq.getIsCompleted() == null || !uq.getIsCompleted())
                    .findFirst();

            if (incompleteQuiz.isPresent()) {
                userQuiz = incompleteQuiz.get();
                if (existingUserQuizzes.size() > 1) {
                    System.err.println("Warning: Multiple UserQuiz entries found for user " + user.getId() + " and quiz " + quiz.getId() + ". Using incomplete entry: " + userQuiz.getId());

                }
            } else {
                userQuiz = existingUserQuizzes.get(0);
                System.err.println("Warning: Multiple UserQuiz entries found for user " + user.getId() + " and quiz " + quiz.getId() + ". All are completed. Using the first one: " + userQuiz.getId());
            }
        }

        QuizStatusResponse response = new QuizStatusResponse();

        if (userQuiz.getIsCompleted() != null && userQuiz.getIsCompleted()) {
            response.setStatus("ALREADY_COMPLETED_OR_FAILED");

            return response;
        }

        int totalQuestionCount = quiz.getQuestions().size();
        String currentQuestionStatus = "WRONG";

        QuizQuestion quizQuestion = quiz.getQuestions().stream()
                .filter(q -> q.getId().equals(request.getQuestionId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Quiz Question not found with ID: " + request.getQuestionId()));

        Option correctOption = quizQuestion.getAnswers().stream()
                .filter(Option::isCorrect)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No correct answer found for question: " + quizQuestion.getId()));

        if (request.getAnswerChoice().equals(correctOption.getText())) {
            currentQuestionStatus = "CORRECT";

        }

        response.setStatus(currentQuestionStatus); // Set status for the current question

        if (request.getQuestionNo() == totalQuestionCount) {
            userQuiz.setIsCompleted(true);

            if (currentQuestionStatus.equals("CORRECT")) {
                userQuiz.setIsPassed(true); // User passed the quiz
                response.setStatus("SUCCESS");

                user.setSustainableScore(user.getSustainableScore() == null ? 0 : user.getSustainableScore() + quiz.getPoint());

                if (quiz.getCouponCode() != null) {
                    boolean hasCoupon = user.getCouponCodes().stream()
                            .anyMatch(couponCode -> couponCode.getId().equals(quiz.getCouponCode().getId()));
                    if (!hasCoupon) {
                        user.getCouponCodes().add(quiz.getCouponCode());
                    }
                    response.setCouponId(quiz.getCouponCode().getId());
                    response.setCouponName(quiz.getCouponCode().getTitle());
                    response.setCouponMinValue(quiz.getCouponCode().getMaxPrice());
                    response.setCouponType(quiz.getCouponCode().getCouponType().name());
                }
                response.setPoint(quiz.getPoint());

            } else {
                userQuiz.setIsPassed(false);
                userQuiz.setIsCompleted(true);
                response.setStatus("NOT_SUCCESS");
            }
            userRepository.save(user);
            userQuizRepository.save(userQuiz);

        } else if (currentQuestionStatus.equals("WRONG")) {
            userQuiz.setIsCompleted(true);
            userQuiz.setIsPassed(false);
            userQuizRepository.save(userQuiz);

        } else {

            userQuizRepository.save(userQuiz);
        }

        return response;
    }

    public QuizResponse getQuizForUser(Long userId, Long quizId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        Quiz quiz =     quizRepository.findById(quizId)
                .orElseThrow(() -> new EntityNotFoundException("Quiz not found: " + quizId));


        QuizResponse resp = new QuizResponse();
        resp.setUserId(user.getId());

        resp.setTitle(quiz.getTitle());
        resp.setDescription(quiz.getDescription());
        resp.setCouponId(quiz.getCouponCode() != null
                ? quiz.getCouponCode().getId()
                : null);
        resp.setMinPoint(quiz.getPoint());

        List<QuestionResponse> qs = quiz.getQuestions().stream().map(q -> {
            QuestionResponse qr = new QuestionResponse();
            qr.setId(q.getId());
            qr.setQuestionText(q.getQuestionText());
            qr.setPoint(q.getPoint());
            List<OptionResponse> opts = q.getAnswers().stream()
                    .map(a -> new OptionResponse(a.getText(), a.isCorrect()))
                    .collect(Collectors.toList());
            qr.setOptions(opts);
            return qr;
        }).collect(Collectors.toList());

        resp.setQuestions(qs);
        resp.setMinPoint(quiz.getPoint());
        return resp;
    }




    public List<QuizResponse> getAllQuizzesForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        return user.getUserQuizzes().stream()
                .map(userQuiz -> {
                    Quiz quiz = userQuiz.getQuiz();

                    QuizResponse resp = new QuizResponse();
                    resp.setUserId(user.getId());
                    resp.setQuizId(quiz.getId()); // Use quiz.getId()
                    resp.setTitle(quiz.getTitle());
                    resp.setDescription(quiz.getDescription());

                    if (userQuiz.getIsCompleted()) {
                        resp.setStatus(false);   }
                        else
                        {
                            resp.setStatus(true);

                        }



                    resp.setCouponId(quiz.getCouponCode() != null
                            ? quiz.getCouponCode().getId()
                            : null);
                    resp.setMinPoint(quiz.getPoint());

                    List<QuestionResponse> qs = quiz.getQuestions().stream().map(q -> {
                        QuestionResponse qr = new QuestionResponse();
                        qr.setQuestionText(q.getQuestionText());
                        qr.setPoint(q.getPoint());
                        List<OptionResponse> opts = q.getAnswers().stream()
                                .map(a -> new OptionResponse(a.getText(), a.isCorrect()))
                                .collect(Collectors.toList());
                        qr.setOptions(opts);
                        return qr;
                    }).collect(Collectors.toList());

                    resp.setQuestions(qs);
                    resp.setMinPoint(quiz.getPoint());
                    return resp;
                }).collect(Collectors.toList());
    }

}
