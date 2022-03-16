package com.work.bookseller.security.jwt;

import com.work.bookseller.security.UserPrincipal;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface JwtProvider {

    String generateToken(UserPrincipal userPrincipal);

    Authentication getAuthentication(HttpServletRequest request);

    Boolean validateToken(HttpServletRequest request);

}
