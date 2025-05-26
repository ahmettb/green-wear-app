package com.finalYearProject.product.service;


import com.finalYearProject.product.constant.MaterialType;
import com.finalYearProject.product.constant.RANK;
import com.finalYearProject.product.entity.*;
import com.finalYearProject.product.entity.redis.AccessToken;
import com.finalYearProject.product.entity.request.UserUpdateRequest;
import com.finalYearProject.product.entity.response.ProductResponse;
import com.finalYearProject.product.entity.response.UserDtoResponse;
import com.finalYearProject.product.entity.response.WardrobeResponse;
import com.finalYearProject.product.exception.EntityNotFoundException;
import com.finalYearProject.product.mapper.ProductMapper;
import com.finalYearProject.product.repository.AccessTokenRepository;
import com.finalYearProject.product.repository.PaymentInfoRepository;
import com.finalYearProject.product.repository.ProductRepository;
import com.finalYearProject.product.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PaymentInfoRepository paymentInfoRepository;
    private final AccessTokenRepository accessTokenRepository;


    public UserDtoResponse updateUser(UserUpdateRequest request) throws Exception {

        // Kullanıcının varlığını kontrol et
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NoSuchElementException("Kullanıcı bulunamadı: " + request.getUserId()));

        user.setName(request.getName());
        user.setSurname(request.getSurname());

      Optional<User>user1=  userRepository.findUserByEmail(request.getMail());

      if(user1.isPresent())
      {
          if(user1.get().getId() !=user.getId())
          {
              throw new Exception("Email Already Exist");
          }
      }
        user.setEmail(request.getMail());
      userRepository.save(user);

      UserDtoResponse newUserDto= new UserDtoResponse();
      newUserDto.setName(user.getName());
      newUserDto.setSurname(user.getSurname());
      newUserDto.setMail(user.getEmail());

      return newUserDto;
    }


    @Transactional(readOnly = true)
    public WardrobeResponse getWardrobeByUserId(Long userId) {

        // Kullanıcının varlığını kontrol et
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Kullanıcı bulunamadı: " + userId));

        // Tüm PaymentInfo'ları çek ve Java tarafında filtrele
        // DİKKAT: Bu yaklaşım, büyük veri setlerinde performans sorunlarına yol açabilir.
        // Daha önce önerilen @Query ile veritabanı seviyesinde filtreleme daha verimlidir.
        List<PaymentInfo> paymentInfoList = paymentInfoRepository.findAll().stream()
                .filter(paymentInfo -> paymentInfo.getUser() != null && paymentInfo.getUser().getId().equals(userId))
                .toList();

        Double totalCo2 = 0.0;
        Double totalWaterUsage = 0.0;
        Double totalWaste=0.0;
        Double totalEnergyUsage = 0.0;
        Double totalRecyclabilityScoreSum = 0.0;
        int totalProductQuantity = 0;

        Map<String, Integer> materialCounts = new HashMap<>();

        List<ProductResponse> productResponses = new ArrayList<>();

        for (PaymentInfo paymentInfo : paymentInfoList) {
            if (paymentInfo.getOrderItems() == null) {
                continue; // OrderItem'lar null ise atla
            }
            for (OrderItem item : paymentInfo.getOrderItems()) {
                if (item == null || item.getProduct() == null) {
                    continue; // Geçersiz orderItem'ı atla
                }

                Product product = item.getProduct();
                EnvironmentalImpact impact = product.getEnvironmentalImpact();
                Integer quantity = item.getQuantity() != null ? item.getQuantity() : 1;

                productResponses.add(ProductMapper.mapToResponse(product));

                if (impact != null) {
                    totalCo2 += (impact.getCarbonFootprintKg() != null ? impact.getCarbonFootprintKg() : 0.0) * quantity;
                    totalWaterUsage += (impact.getWaterUsageL() != null ? impact.getWaterUsageL() : 0.0) * quantity;
                    totalEnergyUsage += (impact.getEnergy() != null ? impact.getEnergy() : 0.0) * quantity;
                    totalRecyclabilityScoreSum += (impact.getRecyclabilityPercent() != null ? impact.getRecyclabilityPercent() : 0.0) * quantity;
                    totalWaste += (impact.getWasteGenerated() != null ? impact.getWasteGenerated() : 0.0) * quantity;
                }

                if (product.getType() != null) {
                    String typeName = product.getType().getDisplayName();
                    materialCounts.put(typeName, materialCounts.getOrDefault(typeName, 0) + quantity);
                }

                totalProductQuantity += quantity;
            }
        }

        Double averageRecyclabilityScore = 0.0;
        if (totalProductQuantity > 0) {
            averageRecyclabilityScore = totalRecyclabilityScoreSum / totalProductQuantity;
        }

        WardrobeResponse wardrobeResponse = new WardrobeResponse();
        wardrobeResponse.setProductResponses(productResponses);
        wardrobeResponse.setTotalWaste(totalWaste);
        wardrobeResponse.setTotalCo2(totalCo2);
        wardrobeResponse.setTotalWaterUsage(totalWaterUsage);
        wardrobeResponse.setTotalEnergyUsage(totalEnergyUsage);
        wardrobeResponse.setAverageRecyclabilityScore(averageRecyclabilityScore);
        wardrobeResponse.setMaterialList(materialCounts);
        wardrobeResponse.setRankName(user.getRank().getRank().name());

        return wardrobeResponse;
    }

    public UserDtoResponse getUserInfoByToken(String token)
    {

        AccessToken accessToken = accessTokenRepository.findById(token)
                .orElseThrow(() -> new IllegalArgumentException("Token not in redis"));

        if (!accessToken.getValid()) {
            throw new IllegalArgumentException("Token not in redis");
        }

        User user= userRepository.findById(accessToken.getUserId()).orElseThrow(()->new EntityNotFoundException(accessToken.getId(),User.class));

        UserDtoResponse userDtoResponse = new UserDtoResponse();

        userDtoResponse.setUserId(user.getId() != null ? user.getId() : accessToken.getUserId());
        userDtoResponse.setMail(user.getEmail()!=null ? user.getEmail() : "");
        userDtoResponse.setName(user.getName()!=null?user.getName():"");
        userDtoResponse.setSurname(user.getSurname()!=null?user.getSurname():"");
        userDtoResponse.setRank(user.getRank()!=null ? user.getRank().getRank() : null);
        userDtoResponse.setPoint(user.getSustainableScore() != null ? user.getSustainableScore() : 0);
        userDtoResponse.setUsername(user.getUsername()!=null ? user.getUsername() : null);

        if(user.getRank().equals(RANK.TOHUM)) userDtoResponse.setNextRankMinPoint(100.0);
        if(user.getRank().equals(RANK.DOGA_DOSTU)) userDtoResponse.setNextRankMinPoint(1000.0);
        if(user.getRank().equals(RANK.YESIL_KAHRAMAN)) userDtoResponse.setNextRankMinPoint(3000.0);


        return userDtoResponse;
    }
}
