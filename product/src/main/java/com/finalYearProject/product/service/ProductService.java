package com.finalYearProject.product.service;


import com.finalYearProject.product.entity.*;
import com.finalYearProject.product.entity.request.CreateQuizRequest;
import com.finalYearProject.product.entity.request.OptionRequest;
import com.finalYearProject.product.entity.request.QuestionRequest;
import com.finalYearProject.product.entity.response.ProductResponse;
import com.finalYearProject.product.mapper.ProductMapper;
import com.finalYearProject.product.repository.*;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.finalYearProject.product.service.ProductSpecification.nameLike;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final EnvironmentalFeaturesRepository environmentalFeaturesRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CouponCodeRepository couponCodeRepository;
    private final QuizRepository quizRepository;


    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Ürün bulunamadı."));
        Category category = categoryRepository.findAll().stream()
                .filter(c -> c.getName().equals(product.getCategory().getName()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Kategori bulunamadı."));

        ProductResponse response = ProductMapper.mapToResponse(product);

        Random random=new Random();
        int r1 = random.nextInt(3);

        if (r1 == 1) {
            List<Product> lowerImpactProducts = productRepository.findAll().stream()
                    .filter(p -> p.getCategory().getName().equals(category.getName()) &&
                            (p.getEnvironmentalImpact().getCarbonFootprintKg() < product.getEnvironmentalImpact().getCarbonFootprintKg() ||
                                    p.getEnvironmentalImpact().getWaterUsageL() < product.getEnvironmentalImpact().getWaterUsageL()
                                    ))
                    .collect(Collectors.toList());

            if (!lowerImpactProducts.isEmpty()) {
                int randomIndex = random.nextInt(lowerImpactProducts.size());
                Product suggestionProduct = lowerImpactProducts.get(randomIndex);
                response.setSuggestionProductId(String.valueOf(suggestionProduct.getId()));
                response.setSuggestionProductName(suggestionProduct.getName());
                response.setSuggestioncarbonFootprint(suggestionProduct.getEnvironmentalImpact().getCarbonFootprintKg());
                response.setSuggestionCategory(suggestionProduct.getCategory().getName());
                response.setSuggestionwaterUsage(suggestionProduct.getEnvironmentalImpact().getWaterUsageL());
              //  response.setSuggestionenergyUsage(suggestionProduct.getEnvironmentalImpact().);

                // Öneri Nedenini Belirle
                if (suggestionProduct.getEnvironmentalImpact().getCarbonFootprintKg() < product.getEnvironmentalImpact().getCarbonFootprintKg()) {
                    response.setSuggestionReason("Daha düşük karbon ayak izine sahip.");
                } else if (suggestionProduct.getEnvironmentalImpact().getWaterUsageL() < product.getEnvironmentalImpact().getWaterUsageL()) {
                    response.setSuggestionReason("Daha az su tüketiyor.");
                }
            }
        }

        return response;
    }


    public List<ProductResponse> getAllProuct() {
        List<Product> product = productRepository.findAll();
        List<ProductResponse> productResponseList = new ArrayList<>();

        for (Product product1 : product) {
            productResponseList.add(ProductMapper.mapToResponse(product1));

        }

        searchProductFilter("", "", "o", "", "");

        return productResponseList;
    }

    public List<ProductResponse> filterProducts(String filterType) {

        List<Product> productList = productRepository.findAll();
        List<ProductResponse> productResponseList = ProductMapper.mapToList(productList);

        switch (filterType) {

            case "INCREASE_PRICE": {
                productResponseList= productResponseList.stream().sorted(Comparator.comparing(ProductResponse::getPrice)).collect(Collectors.toList());
                break;
            }

            case "DECREASE_PRICE": {
                productResponseList= productResponseList.stream().sorted(Comparator.comparing(ProductResponse::getPrice).reversed()).collect(Collectors.toList());
                break;
            }
            case "INCREASE_CARBON": {
                productResponseList= productResponseList.stream().sorted(Comparator.comparing(ProductResponse::getCarbonFootprint).reversed()).collect(Collectors.toList());
                break;
            }
            case "DECREASE_CARBON": {
                productResponseList= productResponseList.stream().sorted(Comparator.comparing(ProductResponse::getCarbonFootprint).reversed()).collect(Collectors.toList());
                break;
            }
        }
        return productResponseList;

    }


    public Quiz createQuiz(CreateQuizRequest request) {
        Quiz quiz = new Quiz();
        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setStatus(request.getStatus());
        quiz.setPoint(request.getMinPoint());

        if (request.getCouponId() != null) {
            CouponCode coupon = couponCodeRepository.findById(request.getCouponId())
                    .orElseThrow(() -> new EntityNotFoundException("Coupon not found: " + request.getCouponId()));
            quiz.setCouponCode(coupon);
        }

        for (Long uid : request.getUserId()) {
            User user = userRepository.findById(uid)
                    .orElseThrow(() -> new EntityNotFoundException("User not found: " + uid));
            quiz.getParticipants().add(user);
            user.getQuizzes().add(quiz);
        }

        for (QuestionRequest qr : request.getQuestions()) {
            QuizQuestion quizQuestion = new QuizQuestion();
            quizQuestion.setQuestionText(qr.getQuestionText());
            quizQuestion.setPoint(qr.getPoint());
            quizQuestion.setQuiz(quiz);
            quiz.getQuestions().add(quizQuestion);

            for (OptionRequest or : qr.getOptions()) {
                Option opt = new Option();
                opt.setText(or.getText());
                opt.setCorrect(or.getCorrect());
                opt.setQuestion(quizQuestion);
                quizQuestion.getAnswers().add(opt);
            }
        }

        return quizRepository.save(quiz);

    }


    public List<ProductResponse> searchProductFilter(String priceSort, String carbonSort, String filterText, String brand, String productType) {

        Specification<Product> filters = Specification.where(StringUtils.isBlank(filterText) ? null : nameLike(filterText));

        List<Product> productList = productRepository.findAll(filters);

        // List<Product > product = productRepository.findAll();
        List<ProductResponse> productResponseList = new ArrayList<>();

        for (Product product1 : productList) {
            productResponseList.add(ProductMapper.mapToResponse(product1));

        }


        return productResponseList;
    }

}