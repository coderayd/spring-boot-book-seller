package com.work.bookseller.security.jwt.impl;

import com.work.bookseller.dto.request.UserLoginRequestDto;
import com.work.bookseller.dto.response.UserAuthResponseDto;
import com.work.bookseller.security.UserPrincipal;
import com.work.bookseller.security.jwt.AuthenticationService;
import com.work.bookseller.security.jwt.JwtProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private ModelMapper modelMapper = new ModelMapper();

    // Kullanıcı bilgisinden, kimlik doğrulama nesnesine dönüştürmek istiyoruz ve bunu doğruladığımızda
    // spring security de bunun farkında olmalı bunu AuthenticationManager ile yapıcaz.
    // Kullanıcı ve SpringSecurity arasındaki köprüyü bize sağlayacak.
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    // Oturum açma signIn methodumuz, kullanıcıyı parametre olarak alacak ve yanıt olarak jwttoken'ine yollucaz
    @Override
    public UserAuthResponseDto signInAndReturnJWT(UserLoginRequestDto signInRequest){
        // SpringSecurity kimlik bilgileri için belirli bir sınıfa sahiptir(UsernamePasswordAuthenticationToken)
        // bunu doğruladığımızda bir Authentication nesnesini elde ederiz.

        // AuthenticationManager in arkaplanda yaptığı iş UserDetailsService içindeki loadUserByUsername methodunu
        // çağırmaktır.
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String jwt = jwtProvider.generateToken(userPrincipal);

        UserAuthResponseDto signInUser = modelMapper.map(userPrincipal.getUser(), UserAuthResponseDto.class);
        signInUser.setToken(jwt);

        return signInUser;
    }

}
