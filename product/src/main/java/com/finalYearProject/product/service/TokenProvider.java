package com.finalYearProject.product.service;

import com.finalYearProject.product.entity.dto.TokenDto;
import com.finalYearProject.product.entity.redis.AccessToken;
import com.finalYearProject.product.entity.redis.RefreshToken;
import com.finalYearProject.product.entity.request.LogoutRequest;
import com.finalYearProject.product.repository.AccessTokenRepository;
import com.finalYearProject.product.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {

    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenDto generateToken(Long userId) {
        String accessTokenString = UUID.randomUUID().toString();
        String refreshTokenString = UUID.randomUUID().toString();

        AccessToken accessToken = new AccessToken(accessTokenString, userId, Boolean.TRUE);
        accessTokenRepository.save(accessToken);

        RefreshToken refreshToken = new RefreshToken(refreshTokenString, userId, accessTokenString, Boolean.TRUE);
        refreshTokenRepository.save(refreshToken);

        return new TokenDto(accessTokenString,refreshTokenString);
    }

    public String removeToken(LogoutRequest logoutRequest) {
        Optional<AccessToken> accessToken = accessTokenRepository.findById(logoutRequest.getToken());
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findById(logoutRequest.getRefreshToken());

        accessToken.ifPresent(token -> accessTokenRepository.deleteById(token.getId()));

        if (refreshToken.isPresent()) {
            if (refreshToken.get().getAccessToken().equals(logoutRequest.getToken())) {
                refreshTokenRepository.deleteById(refreshToken.get().getId());
            }
        }
        return "SUCCESS";
    }

}