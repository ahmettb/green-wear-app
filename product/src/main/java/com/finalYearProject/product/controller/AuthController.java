package com.finalYearProject.product.controller;

import com.finalYearProject.product.entity.dto.TokenDto;
import com.finalYearProject.product.entity.request.*;
import com.finalYearProject.product.service.AuthService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController extends BaseController{

    private final AuthService authService;

    @PostMapping(AUTH_REGISTER)
    @CrossOrigin
    public TokenDto register(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping(AUTH_LOGIN)
    @CrossOrigin

    public TokenDto login(@RequestBody LoginRequest loginRequest) throws Exception {
        return authService.login(loginRequest);
    }



}
