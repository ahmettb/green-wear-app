package com.finalYearProject.product.controller;


import com.finalYearProject.product.entity.Quiz;
import com.finalYearProject.product.entity.request.CreateQuizRequest;
import com.finalYearProject.product.entity.request.SolveQuizRequest;
import com.finalYearProject.product.entity.response.ProductResponse;
import com.finalYearProject.product.entity.response.QuizResponse;
import com.finalYearProject.product.service.ProductService;
import com.finalYearProject.product.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/product")
public class ProductController {

    @Autowired
    ProductService productService;


    @Autowired
    QuizService quizService;

    @GetMapping("get-by-id/{id}")
    @CrossOrigin
    public ResponseEntity<ProductResponse> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);

    }

    @GetMapping("get-filter")
    @CrossOrigin
    public ResponseEntity<List<ProductResponse>> filterProduct(@RequestParam(name = "type") String type)
    {
        return  new ResponseEntity<>(productService.filterProducts(type),HttpStatus.OK);


    }


    @PostMapping("solve")
    @CrossOrigin
    public ResponseEntity<?> getById(@RequestBody SolveQuizRequest request) {
        return new ResponseEntity<>(quizService.solveQuizByUser(request), HttpStatus.OK);

    }

    @GetMapping("get-by-filter")
    @CrossOrigin
    public ResponseEntity<List<ProductResponse>> getByFilter(
            @RequestParam(name = "filterText") String filterText

    ) {
        return new ResponseEntity<>(productService.searchProductFilter("", "", filterText, "", ""), HttpStatus.OK);

    }


    @GetMapping("get-quiz/{id}/{quizId}")
    @CrossOrigin
    public ResponseEntity<QuizResponse> getQuiz(

            @PathVariable("id") Long id,
                        @PathVariable("quizId") Long quiz

    ) {
        return new ResponseEntity<>(quizService.getQuizForUser(id,quiz), HttpStatus.OK);

    }

    @GetMapping("get-quiz/{id}")
    @CrossOrigin
    public ResponseEntity<List<QuizResponse>> getQuiz(

            @PathVariable("id") Long id

    ) {
        return new ResponseEntity<>(quizService.getAllQuizzesForUser(id), HttpStatus.OK);

    }

    @PostMapping("create-quiz")
    @CrossOrigin
    public ResponseEntity<Quiz> createQuiz(
            @RequestBody CreateQuizRequest request
    ) {
        return new ResponseEntity<>(productService.createQuiz(request), HttpStatus.OK);

    }


    @GetMapping("get-all")
    @CrossOrigin
    public ResponseEntity<List<ProductResponse>> getAll() {
        return new ResponseEntity<>(productService.getAllProuct(), HttpStatus.OK);

    }
}
