package com.finalYearProject.product.controller;

import com.finalYearProject.product.entity.request.UserUpdateRequest;
import com.finalYearProject.product.entity.response.UserDtoResponse;
import com.finalYearProject.product.entity.response.WardrobeResponse;
import com.finalYearProject.product.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserService userService;


    @GetMapping(USER_GET_INFO_BY_TOKEN)
    public ResponseEntity<UserDtoResponse> getUserInfoByToken(@PathVariable("token") String token) {

        UserDtoResponse response = userService.getUserInfoByToken(token);
        return ResponseEntity.ok(response);

    }

    @GetMapping(USER_GET_WARDROBE)
    public ResponseEntity<WardrobeResponse> getWardrobeByUserId(@PathVariable("userId") Long userId) {

        WardrobeResponse response = userService.getWardrobeByUserId(userId);
        return ResponseEntity.ok(response);

    }

    @PutMapping(USER_UPDATE)
    public ResponseEntity<UserDtoResponse> updateUser(@RequestBody UserUpdateRequest request) throws Exception {

        return ResponseEntity.ok(userService.updateUser(request));

    }


}
