package com.weilai9.common.config.auth;

import cn.hutool.core.util.StrUtil;
import com.weilai9.common.constant.RedisKey;
import com.weilai9.common.constant.TokenConstants;
import com.weilai9.common.utils.redis.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * JWT 工具类
 */
@Component
public class JwtTokenUtil implements Serializable {
    @Resource
    RedisUtil redisUtil;

    private SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(TokenConstants.SECRET_KEY);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 签发JWT
     */
    public String token(TokenUser tokenUser) {
        if (!"dev".equals(TokenConstants.DEV_MODEL)) {
            //获取旧token
            Object oldToken = redisUtil.get(RedisKey.USER_TOKEN + tokenUser.getCustomerId());
            //清理旧token
            redisUtil.deleteKey(RedisKey.USER_TOKEN + tokenUser.getCustomerId(), RedisKey.TOKEN_USER + oldToken);
        }
        //生成新token
        Map<String, Object> claims = new HashMap<>(16);
        claims.put(TokenConstants.CLAIM_KEY_USERNAME, tokenUser.getCustomerName());
        claims.put("customerId", tokenUser.getCustomerId());
        claims.put("license", TokenConstants.LICENSE);
        claims.put("authorities", tokenUser.getAuthorities());
        claims.put("status", tokenUser.getStatus());
        String token = Jwts.builder()
                .setClaims(claims).setIssuer(TokenConstants.ISSUER_STRING)
//                .setExpiration(new Date(Instant.now().toEpochMilli() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, generalKey())
                .compact();
        //
        redisUtil.set(RedisKey.USER_TOKEN + tokenUser.getCustomerId(), token, TokenConstants.TOKEN_EXPIRE);
        redisUtil.set(RedisKey.TOKEN_USER + token, tokenUser, TokenConstants.TOKEN_EXPIRE);
        return token;
    }

    /**
     * 验证JWT
     */
    public Boolean validateToken(String token) {
        Object o = redisUtil.get(RedisKey.TOKEN_USER + token);
        if (null != o) {
            TokenUser tokenUser = (TokenUser) o;
            String customerName = getUsernameFromToken(token);
            if (!StrUtil.isBlankOrUndefined(customerName) && null != tokenUser) {
                boolean check = customerName.equals(tokenUser.getCustomerName());
                if (check) {
                    redisUtil.expireKey(RedisKey.USER_TOKEN + tokenUser.getCustomerId(), TokenConstants.TOKEN_EXPIRE, TimeUnit.SECONDS);
                    redisUtil.expireKey(RedisKey.TOKEN_USER + token, TokenConstants.TOKEN_EXPIRE, TimeUnit.SECONDS);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取token是否过期
     */
    public Integer getUserStatusFromToken(String token) {
        Integer status = getClaimsFromToken(token).get("status", Integer.class);
        return status;
    }

    /**
     * 根据token获取customerName
     */
    public String getUsernameFromToken(String token) {
        String customerName = getClaimsFromToken(token).getSubject();
        return customerName;
    }

    /**
     * 根据token获取customerId
     */
    public Long getUserIdFromToken(String token) {
        Long customerId = getClaimsFromToken(token).get("customerId", Long.class);
        return customerId;
    }

    /**
     * 根据token获取Authorities
     */
    public List getAuthoritiesFromToken(String token) {
        List authorities = getClaimsFromToken(token).get("authorities", List.class);
        return authorities;
    }

    /**
     * 获取token的过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        Date expiration = getClaimsFromToken(token).getExpiration();
        return expiration;
    }

    /**
     * 解析JWT
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(generalKey())
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

}