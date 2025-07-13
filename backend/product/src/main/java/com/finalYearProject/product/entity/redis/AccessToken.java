package com.finalYearProject.product.entity.redis;

import com.finalYearProject.product.config.SecurityConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;


@RedisHash(value = "fasihonApp.accessToken")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AccessToken {

    private String id;

    @Indexed
    private Long userId;

    @TimeToLive
    private Long ttl = SecurityConstants.ACCESS_TOKEN_VALIDITY_SECONDS;

    private Boolean valid;

    public AccessToken(final String id, final Long userId, final Boolean valid) {
        this.id = id;
        this.userId = userId;
        this.valid = valid;
    }

}