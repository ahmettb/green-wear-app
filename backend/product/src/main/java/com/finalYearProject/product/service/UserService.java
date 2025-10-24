package com.finalYearProject.product.service;

import com.finalYearProject.product.constant.MaterialType;
import com.finalYearProject.product.constant.RANK;
import com.finalYearProject.product.entity.*;
import com.finalYearProject.product.entity.redis.AccessToken;
import com.finalYearProject.product.entity.request.UserUpdateRequest;
import com.finalYearProject.product.entity.request.WardrobRequest;
import com.finalYearProject.product.entity.response.ImpactEnviroment;
import com.finalYearProject.product.entity.response.ProductResponse;
import com.finalYearProject.product.entity.response.UserDtoResponse;
import com.finalYearProject.product.entity.response.WardrobeResponse;
import com.finalYearProject.product.exception.EntityNotFoundException;
import com.finalYearProject.product.mapper.ProductMapper;
import com.finalYearProject.product.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PaymentInfoRepository paymentInfoRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final RankRepository rankRepository;

    @Transactional
    public void userRank(Long userId, Double point) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found with ID: " + userId));

        Double totalSustainableScore = user.getSustainableScore() + point;
        List<UserRank> allRanks = rankRepository.findAllByOrderByMinLimitPointAsc();

        UserRank newRank = user.getRank();
        for (UserRank rank : allRanks) {
            if (totalSustainableScore >= rank.getMinLimitPoint() &&
                    (rank.getMaxLimitPoint() == null || totalSustainableScore <= rank.getMaxLimitPoint())) {
                newRank = rank;
            }
        }

        if (!newRank.equals(user.getRank())) {
            user.setRank(newRank);
            System.out.println("User " + user.getUsername() + " rank updated to: " + newRank.getRank());
        }

        user.setSustainableScore(totalSustainableScore);
        System.out.println("User " + user.getUsername() + " total sustainable score: " + totalSustainableScore);

        userRepository.save(user);
    }

    public UserDtoResponse updateUser(UserUpdateRequest request) throws Exception {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found: " + request.getUserId()));

        user.setName(request.getName());
        user.setSurname(request.getSurname());

        Optional<User> user1 = userRepository.findUserByEmail(request.getMail());

        if (user1.isPresent()) {
            if (!Objects.equals(user1.get().getId(), user.getId())) {
                throw new Exception("Email already exists");
            }
        }
        user.setEmail(request.getMail());
        userRepository.save(user);

        UserDtoResponse newUserDto = new UserDtoResponse();
        newUserDto.setName(user.getName());
        newUserDto.setSurname(user.getSurname());
        newUserDto.setMail(user.getEmail());

        return newUserDto;
    }

    public WardrobeResponse getWardrobeByUserId(WardrobRequest request) {
        Long userId = request.getUserId();
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));

        List<PaymentInfo> paymentInfoList = new ArrayList<>();
        if (startDate == null && endDate == null) {
            paymentInfoList = paymentInfoRepository.findAll().stream()
                    .filter(paymentInfo -> paymentInfo.getUser() != null && paymentInfo.getUser().getId().equals(userId))
                    .toList();
        } else if (startDate != null && endDate != null) {
            paymentInfoList = paymentInfoRepository.findAll().stream()
                    .filter(paymentInfo -> {
                        if (paymentInfo.getUser() == null || !paymentInfo.getUser().getId().equals(userId)) {
                            return false;
                        }
                        LocalDate createdDate = paymentInfo.getCreatedDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        return (createdDate.isEqual(startDate) || createdDate.isAfter(startDate)) &&
                                (createdDate.isEqual(endDate) || createdDate.isBefore(endDate));
                    })
                    .toList();
        }

        Double totalCo2 = 0.0;
        Double totalWaterUsage = 0.0;
        Double totalWaste = 0.0;
        Double totalEnergyUsage = 0.0;
        Double totalRecyclabilityScoreSum = 0.0;
        int totalProductQuantity = 0;

        Map<String, Integer> materialCounts = new HashMap<>();
        List<ProductResponse> productResponses = new ArrayList<>();

        for (PaymentInfo paymentInfo : paymentInfoList) {
            if (paymentInfo.getOrderItems() == null) {
                continue;
            }
            for (OrderItem item : paymentInfo.getOrderItems()) {
                if (item == null || item.getProduct() == null) {
                    continue;
                }

                Product product = item.getProduct();
                EnvironmentalImpact impact = product.getEnvironmentalImpact();
                Integer quantity = item.getQuantity() != null ? item.getQuantity() : 1;

                productResponses.add(ProductMapper.mapToResponse(product));

                if (impact != null) {
                    totalCo2 += (impact.getCarbonFootprintKg() != null ? impact.getCarbonFootprintKg() : 0.0) * quantity;
                    totalWaterUsage += (impact.getWaterUsageL() != null ? impact.getWaterUsageL() : 0.0) * quantity;
                    totalEnergyUsage += (impact.getEnergy() != null ? impact.getEnergy() : 0.0) * quantity;
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
        wardrobeResponse.setScore(user.getSustainableScore());
        wardrobeResponse.setTotalEnergyUsage(totalEnergyUsage);
        wardrobeResponse.setAverageRecyclabilityScore(averageRecyclabilityScore);
        wardrobeResponse.setMaterialList(materialCounts);
        wardrobeResponse.setRankName(user.getRank() != null ? user.getRank().getRank().name() : null);

        return wardrobeResponse;
    }

    public List<ImpactEnviroment> getWardrobeMonthlyImpact(WardrobRequest request) {
        Long userId = request.getUserId();
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));

        Map<String, ImpactEnviroment> monthlyData = new LinkedHashMap<>();
        LocalDate iterDate = startDate.withDayOfMonth(1);
        while (!iterDate.isAfter(endDate)) {
            String yearMonth = iterDate.getYear() + "-" + String.format("%02d", iterDate.getMonthValue());
            monthlyData.put(yearMonth, new ImpactEnviroment(yearMonth, 0.0, 0.0, 0.0, 0.0));
            iterDate = iterDate.plusMonths(1);
        }

        List<PaymentInfo> paymentInfoList = paymentInfoRepository.findAll().stream()
                .filter(paymentInfo -> {
                    if (paymentInfo.getUser() == null || !paymentInfo.getUser().getId().equals(userId)) return false;
                    LocalDate createdDate = paymentInfo.getCreatedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return (!createdDate.isBefore(startDate) && !createdDate.isAfter(endDate));
                })
                .toList();

        for (PaymentInfo paymentInfo : paymentInfoList) {
            LocalDate createdDate = paymentInfo.getCreatedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String yearMonth = createdDate.getYear() + "-" + String.format("%02d", createdDate.getMonthValue());

            ImpactEnviroment impact = monthlyData.get(yearMonth);
            if (impact == null) continue;

            for (OrderItem item : paymentInfo.getOrderItems()) {
                if (item == null || item.getProduct() == null) continue;
                EnvironmentalImpact e = item.getProduct().getEnvironmentalImpact();
                int quantity = item.getQuantity() != null ? item.getQuantity() : 1;

                if (e != null) {
                    impact.setCarbon(impact.getCarbon() + (e.getCarbonFootprintKg() != null ? e.getCarbonFootprintKg() : 0.0) * quantity);
                    impact.setWater(impact.getWater() + (e.getWaterUsageL() != null ? e.getWaterUsageL() : 0.0) * quantity);
                    impact.setEnergy(impact.getEnergy() + (e.getEnergy() != null ? e.getEnergy() : 0.0) * quantity);
                    impact.setWaste(impact.getWaste() + (e.getWasteGenerated() != null ? e.getWasteGenerated() : 0.0) * quantity);
                }
            }
        }

        return new ArrayList<>(monthlyData.values());
    }

    public UserDtoResponse getUserInfoByToken(String token) {
        AccessToken accessToken = accessTokenRepository.findById(token)
                .orElseThrow(() -> new IllegalArgumentException("Token not found in Redis"));

        if (!accessToken.getValid()) {
            throw new IllegalArgumentException("Invalid or expired token");
        }

        User user = userRepository.findById(accessToken.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(accessToken.getId(), User.class));

        UserDtoResponse userDtoResponse = new UserDtoResponse();

        userDtoResponse.setUserId(user.getId() != null ? user.getId() : accessToken.getUserId());
        userDtoResponse.setMail(user.getEmail() != null ? user.getEmail() : "");
        userDtoResponse.setName(user.getName() != null ? user.getName() : "");
        userDtoResponse.setSurname(user.getSurname() != null ? user.getSurname() : "");
        userDtoResponse.setRank(user.getRank() != null ? user.getRank().getRank() : null);
        userDtoResponse.setPoint(user.getSustainableScore() != null ? user.getSustainableScore() : 0);
        userDtoResponse.setUsername(user.getUsername() != null ? user.getUsername() : null);

        if (user.getRank() != null) {
            if (user.getRank().equals(RANK.TOHUM)) userDtoResponse.setNextRankMinPoint(100.0);
            if (user.getRank().equals(RANK.DOGA_DOSTU)) userDtoResponse.setNextRankMinPoint(1000.0);
            if (user.getRank().equals(RANK.YESIL_KAHRAMAN)) userDtoResponse.setNextRankMinPoint(3000.0);
        }

        return userDtoResponse;
    }

    public UserDtoResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, User.class));

        UserDtoResponse userDtoResponse = new UserDtoResponse();
        userDtoResponse.setUserId(user.getId());
        userDtoResponse.setMail(user.getEmail() != null ? user.getEmail() : "");
        userDtoResponse.setName(user.getName() != null ? user.getName() : "");
        userDtoResponse.setSurname(user.getSurname() != null ? user.getSurname() : "");
        userDtoResponse.setPoint(user.getSustainableScore() != null ? user.getSustainableScore() : 0.0);
        userDtoResponse.setUsername(user.getUsername());

        if (user.getRank() != null) {
            userDtoResponse.setRank(user.getRank().getRank());
            List<UserRank> allRanks = rankRepository.findAllByOrderByMinLimitPointAsc();
            UserRank currentUserRank = user.getRank();
            Double nextRankMinPoint = null;

            for (UserRank rank : allRanks) {
                if (rank.getMinLimitPoint() > currentUserRank.getMinLimitPoint()) {
                    nextRankMinPoint = (double) rank.getMinLimitPoint();
                    break;
                }
            }
            userDtoResponse.setNextRankMinPoint(nextRankMinPoint);
        } else {
            userDtoResponse.setRank(null);
            userDtoResponse.setNextRankMinPoint(null);
        }

        return userDtoResponse;
    }
}
