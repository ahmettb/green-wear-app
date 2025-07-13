package com.finalYearProject.product.controller;

import com.finalYearProject.product.entity.request.UserUpdateRequest;
import com.finalYearProject.product.entity.request.WardrobRequest;
import com.finalYearProject.product.entity.response.ImpactEnviroment;
import com.finalYearProject.product.entity.response.UserDtoResponse;
import com.finalYearProject.product.entity.response.WardrobeResponse;
import com.finalYearProject.product.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserService userService;


    @GetMapping(USER_GET_INFO_BY_TOKEN)
    public ResponseEntity<UserDtoResponse> getUserInfoByToken(@PathVariable("token") String token) {

        UserDtoResponse response = userService.getUserInfoByToken(token);
        return ResponseEntity.ok(response);

    }
    @GetMapping(USER_GET_BY_ID)
    public ResponseEntity<UserDtoResponse> getUserInfoByToken(@PathVariable("id") Long id) {

        UserDtoResponse response = userService.getById(id);
        return ResponseEntity.ok(response);

    }
    @PostMapping(USER_GET_WARDROBE)
    public ResponseEntity<WardrobeResponse> getWardrobeByUserId(@RequestParam("userId")Long userId,@RequestParam(value = "startDate",required = false)LocalDate startDate,@RequestParam(value = "endDate",required = false)LocalDate end) {

        WardrobRequest request = new WardrobRequest();
        request.setUserId(userId);
        request.setStartDate(startDate);
        request.setEndDate(end);
        WardrobeResponse response = userService.getWardrobeByUserId(request);
        return ResponseEntity.ok(response);

    }

    @PostMapping(USER_GET_WARDROBE_MONTH)
    public ResponseEntity<List<ImpactEnviroment>> getWardrobeByUserIdS(@RequestParam("userId")Long userId, @RequestParam(value = "startDate",required = false)LocalDate startDate, @RequestParam(value = "endDate",required = false)LocalDate end) {

        WardrobRequest request = new WardrobRequest();
        request.setUserId(userId);
        request.setStartDate(startDate);
        request.setEndDate(end);
        List<ImpactEnviroment> response = userService.getWardrobeMonthlyImpact(request);
        return ResponseEntity.ok(response);

    }

    @PutMapping(USER_UPDATE)
    public ResponseEntity<UserDtoResponse> updateUser(@RequestBody   UserUpdateRequest request) throws Exception {

        return ResponseEntity.ok(userService.updateUser(request));

    }


}
