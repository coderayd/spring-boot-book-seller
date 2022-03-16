package com.work.bookseller.security.jwt.impl;

import com.work.bookseller.security.UserPrincipal;
import com.work.bookseller.security.jwt.JwtProvider;
import com.work.bookseller.util.SecurityUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtProviderImpl implements JwtProvider {

    @Value("${app.jwt.secret.key}")
    private String JWT_SECREY_KEY;

    @Value("${app.jwt.expiration-in-ms}")
    private Integer JWT_EXPIRATION_IN_MS;

    // UserPrincipal nesnesi oturum açtıktan sonra oluşuyordu
    @Override
    public String generateToken(UserPrincipal userPrincipal){
        String authorities = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // Jwt ile ilgili güzel document
        // -> https://github.com/jwtk/jjwt#jws-key-hmacsha

        SecretKey key = Keys.hmacShaKeyFor(JWT_SECREY_KEY.getBytes());

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("roles", authorities)
                .claim("userId", userPrincipal.getId())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_IN_MS))
                .signWith(key, SignatureAlgorithm.HS256)
//                .signWith(SignatureAlgorithm.HS512, JWT_SECREY_KEY)
                .compact();
    }

    @Override
    public Authentication getAuthentication(HttpServletRequest request){

        Claims claims = extractClaims(request);
        if(claims == null){
            return null;
        }
        String username = claims.getSubject();
        Integer userId = claims.get("userId", Integer.class);

        Set<GrantedAuthority> authorities = Arrays.stream(claims.get("roles").toString().split(","))
                .map(SecurityUtil::covertToAuthority)
                .collect(Collectors.toSet());

        UserDetails userDetails = UserPrincipal.builder()
                .username(username)
                .authorities(authorities)
                .id(userId)
                .build();

        if (username == null){
            return null;
        }

        /* UsernamePassword classı
            -> public class UsernamePasswordAuthenticationToken extends AbstractAuthenticationToken {
            AbstractAuthenticationToken i extends alıyor
            ve
            -> public abstract class AbstractAuthenticationToken implements Authentication, CredentialsContainer
            bu class ta Authentication u kendine implement almış bundan dolayı interfacelerdeki kendisini implement
            olarak alan sınıfı kendisine atama yaptırabiliyordu yani
            Authentication deneme = new UsernamePasswordAut... çalışacaktır bu sebeple return kısmında bu nesneyi
            geri döndürebiliriz.
         */
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

    }

    private Claims extractClaims(HttpServletRequest request){

        String token = SecurityUtil.extractAuthTokenFromRequest(request);
        if( token == null ){
            return null;
        }

//        Claims claims = Jwts.parser()
//                .setSigningKey(JWT_SECREY_KEY)
//                .parseClaimsJws(token)
//                .getBody();

        SecretKey key = Keys.hmacShaKeyFor(JWT_SECREY_KEY.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build().parseClaimsJws(token).getBody();
    }

    // Token'in süresi bitmiş mi diye kontrol
    @Override
    public Boolean validateToken(HttpServletRequest request){

        Claims claims = extractClaims(request);
        if(claims == null || claims.getExpiration().before(new Date())){
            return false;
        }
        return true;
    }

}
