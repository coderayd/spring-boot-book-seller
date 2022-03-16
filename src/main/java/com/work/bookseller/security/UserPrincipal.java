package com.work.bookseller.security;

import com.work.bookseller.enumeration.Role;
import com.work.bookseller.model.User;
import com.work.bookseller.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private Integer id;
    private String username;
    // transient kullanıldığında seri hale getirilmiş yerlerde bunlar görünmüyor
    transient private String password;  // don't show up on an serialized places
    transient private User user; // user for only login operation, don't use in JWT

    // SORU
    // Buradaki kısım CustomUserDetailsService class'ındaki loadUserByUsername kısmından gelmektedir.
    // yani buraya atama oradan yapılıyor fakat sanki buraya sadece 1 rol gelecekmiş gibime geliyor.
    // Set neden demiş tam orayı anlamadım.
    // Buradaki kısım aşağıdaki @Override edilen kısma ekleniyor. Ahmet Hoca orasını bu şekilde yapmış
    /*
    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(getUser().getRole().name());
        return Collections.singletonList(authority);
     */
    // ÇÖZÜM
    /*
        Soruyu sordum ve bana bu methodun Set türünde değer istediğini söyledi yani Collection istiyor
        bu sebeple Set yolluyor. Yani biz bu projede 1 kişinin sadece 1 rolü olabilir olarak oluşturmuş olsak bile
        bu method Override edildiğinden ve Collection istediğinden bu şekilde veri yollamamız gerekmektedir.
     */
    private Set<GrantedAuthority> authorities;

    public static UserPrincipal createSuperUser(){
        Set<GrantedAuthority> authorities = Set.of(SecurityUtil.covertToAuthority(Role.SYSTEM_MANAGER.name()));
        return UserPrincipal.builder()
                .id(-1)
                .username("system-administrator")
                .authorities(authorities)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
