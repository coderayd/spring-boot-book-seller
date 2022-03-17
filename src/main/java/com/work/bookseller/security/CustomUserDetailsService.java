package com.work.bookseller.security;

import com.work.bookseller.model.User;
import com.work.bookseller.repository.UserRepository;
import com.work.bookseller.util.SecurityUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Kimliği doğrulanmış kullanıcıların kimlik bilgilerini ve rollerini barındırır.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        //Burada Set kullanılmış ama sanki içine her zaman sadece 1 rol gelecekmiş gibime geliyor.
        Set<GrantedAuthority> authorities = Set.of(SecurityUtil.covertToAuthority(user.getRole().name()));

        return UserPrincipal.builder()
                .user(user).id(user.getId())
                .username(username)
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
