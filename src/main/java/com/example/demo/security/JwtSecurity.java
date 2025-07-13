package com.example.demo.security;

import com.example.demo.model.User2;
import com.example.demo.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class JwtSecurity {
    private static final Logger log = LoggerFactory.getLogger(JwtSecurity.class);
    private UserService userService;
    private static final String SECRET_KEY = "746774527A576E5A7134743777217A3B705A226453547A2F43553675375A7823";

    public Map<String,String> generateT(String username) {
        User2 user = userService.loadUserByUsername(username);
        log.info("user info: {}", user);
       return this.generatetocken(user);
    }
        private Map<String,String> generatetocken(User2 user) {
         Map<String, String> claims = Map.of(
                "name", user.getUsername(),
                "id", user.getId()
        );
            final String compact = Jwts.builder()
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // 30 minutes
                    .setSubject(user.getEmail())
                    .setClaims(claims)
                    .signWith(getSignKey(), SignatureAlgorithm.HS256)
                    .compact();
            return Map.of("token", compact);
        }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}

