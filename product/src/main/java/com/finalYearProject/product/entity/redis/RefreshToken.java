package com.finalYearProject.product.entity.redis;

import com.finalYearProject.product.config.SecurityConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@RedisHash(value = "kolayfatura.refreshToken")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class RefreshToken implements Serializable {

    private String id;

    @Indexed
    private Long userId;

    @TimeToLive
    private Long ttl = SecurityConstants.ACCESS_TOKEN_VALIDITY_SECONDS * 3;

    private String accessToken;

    private Boolean valid;

    public RefreshToken(String id, Long userId, String accessTokenString, Boolean valid) {
        this.id = id;
        this.userId = userId;
        this.accessToken = accessTokenString;
        this.valid = valid;
    }

}