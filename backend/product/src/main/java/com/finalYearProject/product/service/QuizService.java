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


// Assume you have:
// private QuizRepository quizRepository;
// private UserRepository userRepository;
// private UserQuizRepository userQuizRepository; // Add this

    public QuizStatusResponse solveQuizByUser(SolveQuizRequest request) {
        Quiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() -> new EntityNotFoundException("Quiz not found with ID: " + request.getQuizId()));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + request.getUserId()));

        // --- START OF FIX FOR NonUniqueResultException ---
        // Fetch all UserQuiz entries for this user and quiz
        List<UserQuiz> existingUserQuizzes = userQuizRepository.findByUserAndQuiz(user, quiz);

        UserQuiz userQuiz;

        if (existingUserQuizzes.isEmpty()) {
            // No existing entry, create a new one
            userQuiz = new UserQuiz();
            userQuiz.setUser(user);
            userQuiz.setQuiz(quiz);
            // userQuizRepository.save(userQuiz); // Don't save yet, will save at the end
        } else {
            // Multiple entries found (or one), try to find an active/incomplete one.
            // If there are multiple, it indicates a data inconsistency.
            // We'll try to use the first one that is NOT completed, or just the first one if all are completed.
            Optional<UserQuiz> incompleteQuiz = existingUserQuizzes.stream()
                    .filter(uq -> uq.getIsCompleted() == null || !uq.getIsCompleted())
                    .findFirst();

            if (incompleteQuiz.isPresent()) {
                userQuiz = incompleteQuiz.get();
                // If there were more than one incomplete quiz, you might want to log a warning here.
                if (existingUserQuizzes.size() > 1) {
                    System.err.println("Warning: Multiple UserQuiz entries found for user " + user.getId() + " and quiz " + quiz.getId() + ". Using incomplete entry: " + userQuiz.getId());
                    // Optionally, you could delete the other incomplete ones here if you are certain
                    // that only one incomplete entry should ever exist.
                }
            } else {
                // All found quizzes are already completed or passed. Use the first one.
                userQuiz = existingUserQuizzes.get(0);
                System.err.println("Warning: Multiple UserQuiz entries found for user " + user.getId() + " and quiz " + quiz.getId() + ". All are completed. Using the first one: " + userQuiz.getId());
            }
        }
        // --- END OF FIX ---

        QuizStatusResponse response = new QuizStatusResponse();

        // If the quiz is already completed/failed for this user, return immediately
        if (userQuiz.getIsCompleted() != null && userQuiz.getIsCompleted()) {
            response.setStatus("ALREADY_COMPLETED_OR_FAILED");
            // Optionally, populate other fields from userQuiz if you want to show their previous result
            // E.g., response.setIsPassed(userQuiz.getIsPassed());
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
            // Increment user's score for this specific quiz (optional, if you track per-quiz score)
            // userQuiz.setUserScore(userQuiz.getUserScore() == null ? 0 : userQuiz.getUserScore() + 1);
        }

        response.setStatus(currentQuestionStatus); // Set status for the current question

        // Check if this is the last question in the quiz
        if (request.getQuestionNo() == totalQuestionCount) {
            userQuiz.setIsCompleted(true); // Mark quiz as completed for this user

            if (currentQuestionStatus.equals("CORRECT")) {
                userQuiz.setIsPassed(true); // User passed the quiz
                response.setStatus("SUCCESS");
                // userQuiz.setIsCompleted(true); // This line is redundant as it's set above

                // Update user's sustainable score
                user.setSustainableScore(user.getSustainableScore() == null ? 0 : user.getSustainableScore() + quiz.getPoint());

                // Add coupon if not already present
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

            } else { // Last question and answer was WRONG
                userQuiz.setIsPassed(false);
                userQuiz.setIsCompleted(true); // User failed the quiz
                response.setStatus("NOT_SUCCESS");
            }
            // Save the updated user and userQuiz status
            userRepository.save(user);
            userQuizRepository.save(userQuiz);

        } else if (currentQuestionStatus.equals("WRONG")) {
            // If a wrong answer is given at any point, the quiz is disabled for this user.
            userQuiz.setIsCompleted(true);
            userQuiz.setIsPassed(false);
            userQuizRepository.save(userQuiz);
            // The `response.setStatus` is already "WRONG" from above, so no need to re-set.
            // You might want a more specific status like "FAILED_MID_QUIZ" if that's relevant for your frontend.
        } else {
            // If the quiz is not yet completed and the answer was correct,
            // and it's not the last question, we still need to save userQuiz
            // to persist any score increments or other progress tracking you might add.
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

        // Filter userQuizzes based on completion status (e.g., show only completed/passed quizzes, or all of them)
        // For example, if you want to show only quizzes the user has completed:
        return user.getUserQuizzes().stream()
                .map(userQuiz -> {
                    // Get the actual Quiz entity from the UserQuiz
                    Quiz quiz = userQuiz.getQuiz();

                    QuizResponse resp = new QuizResponse();
                    resp.setUserId(user.getId());
                    resp.setQuizId(quiz.getId()); // Use quiz.getId()
                    resp.setTitle(quiz.getTitle());
                    resp.setDescription(quiz.getDescription());

                    // Set the status based on the user's completion of THIS quiz
                    // You can add more nuanced status here (e.g., "COMPLETED_PASSED", "COMPLETED_FAILED", "IN_PROGRESS")
                    if (userQuiz.getIsCompleted()) {
                        resp.setStatus(false);   }
                        else
                        {
                            resp.setStatus(true); // Or any other status you deem fit for incomplete

                        }



                    resp.setCouponId(quiz.getCouponCode() != null
                            ? quiz.getCouponCode().getId()
                            : null);
                    resp.setMinPoint(quiz.getPoint()); // This is the quiz's total point, not user's score

                    // Map questions (assuming you want to show all questions for a completed quiz)
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
