package com.finalYearProject.product.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.finalYearProject.product.entity.PasswordResetToken;
import com.finalYearProject.product.entity.User;
import com.finalYearProject.product.entity.dto.TokenDto;
import com.finalYearProject.product.entity.redis.RefreshToken;
import com.finalYearProject.product.entity.request.*;
import com.finalYearProject.product.exception.InvalidCodeException;
import com.finalYearProject.product.exception.InvalidUsernameException;
import com.finalYearProject.product.exception.TokenExpiredException;
import com.finalYearProject.product.exception.UserAlreadyExistsException;
import com.finalYearProject.product.repository.AccessTokenRepository;
import com.finalYearProject.product.repository.PasswordResetTokenRepository;
import com.finalYearProject.product.repository.RefreshTokenRepository;
import com.finalYearProject.product.repository.UserRepository;

import com.finalYearProject.product.utilities.RandomUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@Getter
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final RestTemplate restTemplate;

    private final AccessTokenRepository accessTokenRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final  UserService userService;


    private Integer deleteUserControlDayCount=30;





    public TokenDto register(RegisterRequest registerRequest) {

        String emailPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Matcher emailMatcher = Pattern.compile(emailPattern).matcher(registerRequest.getEmail());
        if (!emailMatcher.matches()) {
            throw new InvalidUsernameException();
        }

        Optional<User> user = userRepository.findUserByEmail(registerRequest.getEmail());
        if (user.isPresent()) {
            throw new UserAlreadyExistsException();
        }
        User newUser = new User();
        newUser.setEmail(registerRequest.getEmail());
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        newUser.setPassword(encodedPassword);
        newUser = userRepository.save(newUser);
        return tokenProvider.generateToken(newUser.getId());
    }

    public TokenDto login(LoginRequest loginRequest) {

        if (!StringUtils.hasText(loginRequest.getEmail())
                || !StringUtils.hasText(loginRequest.getPassword())) {
            throw new BadCredentialsException("Invalid mail");
        }

        String emailPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Matcher emailMatcher = Pattern.compile(emailPattern).matcher(loginRequest.getEmail());
        if (!emailMatcher.matches()) {
            throw new InvalidUsernameException();
        }

        Optional<User> optionalUser;
        optionalUser = userRepository.findUserByEmail(loginRequest.getEmail());

        User user = optionalUser.orElseThrow(()->new InvalidUsernameException());

        String authUsername = user.getEmail();
        String authPassword = loginRequest.getPassword();

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authUsername, authPassword));




        } catch (AuthenticationException authenticationEx) {
            log.error(authenticationEx.getMessage());
            throw new BadCredentialsException("Invalid mail");
        }

        return tokenProvider.generateToken(user.getId());
    }



    @Transactional
    public TokenDto refreshToken(RefreshTokenRequest refreshTokenRequest) {
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findById(refreshTokenRequest.getRefreshToken());

        if (refreshTokenOptional.isPresent()) {
            RefreshToken refreshToken = refreshTokenOptional.get();
            if (!refreshToken.getValid()) {
                throw new TokenExpiredException();
            }
            if (!refreshToken.getAccessToken().equals(refreshTokenRequest.getToken())) {
                throw new TokenExpiredException();
            }

            refreshToken.setValid(Boolean.FALSE);

            accessTokenRepository.findById(refreshTokenRequest.getToken()).ifPresent(accessToken -> {
                        accessToken.setValid(Boolean.FALSE);
                        accessTokenRepository.save(accessToken);
                    }
            );

            refreshTokenRepository.save(refreshToken);

            return tokenProvider.generateToken(refreshToken.getUserId());
        } else {
            throw new TokenExpiredException();
        }
    }

    public String logoutUser(LogoutRequest logoutRequest) {
        tokenProvider.removeToken(logoutRequest);

        return "SUCCESS_RESPONSE";
    }

    public Boolean forgotPassword(ForgetPasswordRequest forgetPasswordRequest) throws Exception {
        User user = userRepository.findUserByEmail(forgetPasswordRequest.getEmail())
                .orElseThrow(()->new BadCredentialsException(""));

        User finalUser = user;
        PasswordResetToken passwordResetToken;

        List<PasswordResetToken> passwordResetTokenList = passwordResetTokenRepository.getActiveByUserAndEndDateAfter(user, LocalDateTime.now());
        if (!passwordResetTokenList.isEmpty()) {
            passwordResetToken = passwordResetTokenList.get(0);
        } else {
            PasswordResetToken _passwordResetToken = new PasswordResetToken();
            _passwordResetToken.setEndDate(LocalDateTime.now().plusMinutes(PasswordResetToken.EXPIRATION));
            _passwordResetToken.setUser(finalUser);
            String authCode;
            LocalDateTime now = LocalDateTime.now();
            do {
                authCode = RandomUtil.randomNumString(8);
            } while (passwordResetTokenRepository.existsByPasswordResetAuthCodeAndStatusAndEndDateAfter(authCode, 1, now));
            _passwordResetToken.setPasswordResetAuthCode(authCode);
            _passwordResetToken = passwordResetTokenRepository.save(_passwordResetToken);
            passwordResetToken = _passwordResetToken;
        }


        Map<String, Object> parameters = new HashMap<>();
        parameters.put("code", passwordResetToken.getPasswordResetAuthCode());
        //Mail Eklenecek
        //mailService.queueMail(companyUser.getUser().getEmail(), parameters, "passwordForgotCode.ftl", "Kolay Fatura Şifre Sıfırlama");
        return true;
    }

    public Boolean resetPassword(ResetPasswordRequest resetPasswordRequest) throws Exception {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.getActiveByTokenAndEndDateAfter(resetPasswordRequest.getAuthCode(), LocalDateTime.now())
                .orElseThrow(()->new InvalidCodeException());
        User user = passwordResetToken.getUser();


        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        userRepository.save(user);
        passwordResetTokenRepository.updateStatusPassive(passwordResetToken.getId());
        return true;
    }

}