package com.finalYearProject.product.service;

import com.finalYearProject.product.entity.Option;
import com.finalYearProject.product.entity.Quiz;
import com.finalYearProject.product.entity.QuizQuestion;
import com.finalYearProject.product.entity.User;
import com.finalYearProject.product.entity.request.SolveQuizRequest;
import com.finalYearProject.product.entity.response.OptionResponse;
import com.finalYearProject.product.entity.response.QuestionResponse;
import com.finalYearProject.product.entity.response.QuizResponse;
import com.finalYearProject.product.entity.response.QuizStatusResponse;
import com.finalYearProject.product.repository.QuizRepository;
import com.finalYearProject.product.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;


    public QuizStatusResponse solveQuizByUser(SolveQuizRequest request) {

        Quiz quiz = quizRepository.findById(request.getQuizId()).orElseThrow(() -> new EntityNotFoundException());

        QuizStatusResponse response1=new QuizStatusResponse();

        int questionCount = quiz.getQuestions().size();
        String response = "WRONG";

        response1.setStatus(response);
        QuizQuestion quizQuestion = quiz.getQuestions().stream().filter(quizQuestion1 -> quizQuestion1.getId() == request.getQuestionId()).findFirst().get();

        Option option = quizQuestion.getAnswers().stream().filter(option1 -> option1.isCorrect()).findFirst().get();

        if (request.getAnswerChoice().equals(option.getText()))

        {response = "CORRECT";
        response1.setStatus(response);
        }

        if (response.equals("CORRECT") && questionCount == request.getQuestionNo()) {

            User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new EntityNotFoundException());
            user.setSustainableScore(user.getSustainableScore()==null ? 0: user.getSustainableScore() + quiz.getPoint());



            boolean control = user.getCouponCodes().stream().anyMatch(couponCode -> couponCode.getId() == quiz.getCouponCode().getId());
            if (!control) {
                user.getCouponCodes().add(quiz.getCouponCode());

            }

            userRepository.save(user);
            response = "SUCCESS";

            response1.setStatus(response);
            response1.setCouponId(quiz.getCouponCode().getId());
            response1.setCouponName(quiz.getCouponCode().getTitle());
            response1.setCouponMinValue(quiz.getCouponCode().getMaxPrice());
            response1.setPoint(quiz.getPoint());
            response1.setCouponType(quiz.getCouponCode().getCouponType().name());
            quiz.setStatus(false);
            quizRepository.save(quiz);

        } else if (response.equals("WRONG") && questionCount == request.getQuestionNo()) {
            response = "NOT_SUCCESS";
            response1.setStatus(response);
            quiz.setStatus(false);
            quizRepository.save(quiz);
        } else if (response.equals("WRONG")) {
            quiz.setStatus(false);
            quizRepository.save(quiz);

        }


        return response1;
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
        resp.setStatus(quiz.getStatus());
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
        return resp;
    }

    public List<QuizResponse> getAllQuizzesForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        return user.getQuizzes().stream().filter(q->q.getStatus()).map(quiz -> {
            QuizResponse resp = new QuizResponse();
            resp.setUserId(user.getId());
            resp.setQuizId(quiz.getId());
            resp.setTitle(quiz.getTitle());
            resp.setDescription(quiz.getDescription());
            resp.setStatus(quiz.getStatus());
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
            return resp;
        }).collect(Collectors.toList());
    }

}
