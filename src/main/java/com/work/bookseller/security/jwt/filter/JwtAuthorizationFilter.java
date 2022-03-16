package com.work.bookseller.security.jwt.filter;

import com.work.bookseller.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider;

//    private HandlerExceptionResolver handlerExceptionResolver;

//    public JwtAuthorizationFilter(HandlerExceptionResolver handlerExceptionResolver) {
//        this.handlerExceptionResolver = handlerExceptionResolver;
//    }

    // InternalApiAuthenticationFilter çalıştığında buraya girmesin diye, bu url i engelledik.
    // true döndüğünde bu filter çalışmasın demek oluyor.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException
    {
        return request.getRequestURI().startsWith("/api/internal");
    }

    // FİLTRE DAHA ÖNCEDEN UYGULANMIŞSA BU METHOD SAYESİNDE FİLTREYİ EXECUTE ETMEMİZE OLANAK SAĞLAR.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

            Authentication authentication = jwtProvider.getAuthentication(request);

            if(authentication != null && jwtProvider.validateToken(request)){
                // kullanıcının kimliği başarıyla doğrulandı, bu sebeple SpringContext'te tanımlıyoruz.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

//            handlerExceptionResolver.resolveException(request, response, null, ex);

        filterChain.doFilter(request, response);
    }
}