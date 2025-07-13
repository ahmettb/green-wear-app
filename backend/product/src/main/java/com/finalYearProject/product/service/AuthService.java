package com.finalYearProject.product.service;

import com.finalYearProject.product.constant.RANK;
import com.finalYearProject.product.entity.User;
import com.finalYearProject.product.entity.UserRank;
import com.finalYearProject.product.entity.dto.TokenDto;
import com.finalYearProject.product.entity.request.*;
import com.finalYearProject.product.exception.InvalidUsernameException;
import com.finalYearProject.product.exception.UserAlreadyExistsException;
import com.finalYearProject.product.repository.AccessTokenRepository;
import com.finalYearProject.product.repository.RankRepository;
import com.finalYearProject.product.repository.RefreshTokenRepository;
import com.finalYearProject.product.repository.UserRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


    private final  UserService userService;
    private final RankRepository rankRepository;


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
        newUser.setName(registerRequest.getName());
        newUser.setEmail(registerRequest.getEmail());
        UserRank rank=   rankRepository.findAll().stream().filter(R->R.getRank().equals(RANK.TOHUM)).findFirst().get();
        newUser.setRank(rank);
        newUser.setSustainableScore(0.0);

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


    public String logoutUser(LogoutRequest logoutRequest) {
        tokenProvider.removeToken(logoutRequest);

        return "SUCCESS";
    }


}