package com.work.bookseller.security.jwt.filter;

import com.work.bookseller.security.UserPrincipal;
import com.work.bookseller.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class InternalApiAuthenticationFilter extends OncePerRequestFilter {

    private String accessKey;

    public InternalApiAuthenticationFilter(String accessKey){
        this.accessKey = accessKey;
    }

    // OncePerRequestFilter dan gelen bu method ile filtre yollarını kısıtlayabiliriz.
    // bu method true döndüğünde verilen isteği engelliyor bu sebeple başına ! koyduk.
    // /api/internal da çalışsın diğerlerinde çalışmasın.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getRequestURI().startsWith("/api/internal");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            String requestKey = SecurityUtil.extractAuthTokenFromRequest(request);
            if(requestKey == null || !requestKey.equals(accessKey)){
                log.warn("Internal key endpoint requested without/wrong key uri: {}", request.getRequestURI());
                throw new RuntimeException("UNAUTHORIZED");
            }

            UserPrincipal user = UserPrincipal.createSuperUser();

            // Kimlik doğrulama nesneleri için bir sağlayıcıdır.
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            // Kimlik doğrulamasını bulduktan sonra kullanıcıyı SecurityContext te tanımlıyoruz.
            // Bir başka değişle kullanıcının kimliği başarıyla doğrulandı diyoruz.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (Exception ex){
            log.error("Could not set user authentication in security context", ex);
        }
        filterChain.doFilter(request,response);
    }
}
