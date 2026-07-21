package com.jcoder.emsbp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTUtilizer {
    private final String SECRET_KEY_STRING = "9u9Jw/Cx2GvtxEjXj137lqEwBCHqHOXYXgWtHwM9x8I=";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());

    public String generateJWTToken(String username,String role){
        Map<String, Object> mp = new HashMap<>();
        mp.put("username", username); // Username
        mp.put("role", role); //Role(Admin/Manager/Employee)

        return Jwts.builder()
                .claims(mp)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2)) // 2 hours
                .signWith(key)
                .compact();


    }

    public Map<String,String> validateToken(String token) {
        Map<String,String> res = new HashMap<>();
        try {
             Claims c = Jwts.parser() // used to configure the parser
                    .verifyWith(key) // Secret key used to verify
                    .build()
                    .parseSignedClaims(token) // it verifies and parses token
                    .getPayload(); // returns the actual data

            res.put("username", c.get("username", String.class));
            res.put("role", c.get("role", String.class));
            res.put("code","200");
            return res;
        } catch (ExpiredJwtException e) {
            res.put("code", "401");
            res.put("error","Token Expired! Please login again");
            return res;
        }
    }
}
