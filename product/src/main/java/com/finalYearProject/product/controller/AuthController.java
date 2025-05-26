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


    @GetMapping(AUTH_REFRESH)
    @CrossOrigin
    public TokenDto refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }
    @PostMapping(AUTH_LOGOUT)
    @ApiOperation("Gonderilen tokeni siler")
    @CrossOrigin
    public String logoutUser(@RequestBody LogoutRequest logOutRequest) {
        return authService.logoutUser(logOutRequest);
    }

    @PostMapping(AUTH_FORGOT_PASSWORD)
    @CrossOrigin
    public Boolean forgotPassword(@RequestBody ForgetPasswordRequest forgetPasswordRequest) throws Exception {
        return authService.forgotPassword(forgetPasswordRequest);
    }

    @PostMapping(AUTH_RESET_PASSWORD)
    @CrossOrigin
    public Boolean resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) throws Exception {
        return authService.resetPassword(resetPasswordRequest);
    }


}
