package com.work.bookseller.config;

import com.work.bookseller.enumeration.Role;
import com.work.bookseller.exception.jwt.RestAccessDeniedHandler;
import com.work.bookseller.exception.jwt.RestAuthenticationEntryPoint;
import com.work.bookseller.security.CustomUserDetailsService;
import com.work.bookseller.security.jwt.filter.InternalApiAuthenticationFilter;
import com.work.bookseller.security.jwt.filter.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(){
        return new JwtAuthorizationFilter();
    }

    @Value("${authentication.internal-api-key}")
    private String internalApiAccessKey;

    @Bean
    public InternalApiAuthenticationFilter internalApiAuthenticationFilter(){
        return new InternalApiAuthenticationFilter(internalApiAccessKey);
    }

    // AuthenticationManager kimlik do??rulamadan sorumlu
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public RestAccessDeniedHandler accessDeniedHandler() {
        return new RestAccessDeniedHandler();
    }

    @Bean
    public RestAuthenticationEntryPoint authenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // cross-side: localhost:8080, localhost:4200... backend ve frontend farkl?? domain i??in bunu aktif ediyoruz.
        http.cors();
        // Cross Side Request Forgery(CSRF) tek t??klama ile veya session ile yap??lan sald??r?? olarakta bilinir.
        // sald??rganlar session de??erini kullanabilir ve bunu web g??venlik a???????? olarak kullanabilir.
        // biz session kimlik do??rulamas?? kullanmayaca????m??z i??in disable ettik
        http.csrf().disable();
        // kullan??c??n??n kimli??i do??ruland??ktan sonra di??er istekelrini g??nderirken kullan??c??n??n kulland??????
        // kimlik do??rulama y??ntemidir. Ayr??ca session oturumun ne kadar s??rece??inide belirtir.
        //  * stateless: Spring Security taraf??ndan hi??bir oturum olu??turulmayacak veya kullan??lmayacakt??r.
        // bu sebeple her bir iste??in tekrardan do??rulanmas?? gerekir. ????nk?? biz session-id kullanm??yacaz
        // JWT yi kimlik do??rulama olarak kullanaca????z.
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                /*
                 *  Swagger permitted
                 */
                .antMatchers(AUTH_WHITELIST).permitAll()
                /*
                 *  AuthController
                 */
                .antMatchers("/api/auth/**").permitAll()
                /*
                 *  BookController authorization configuration
                 */
                .antMatchers(HttpMethod.GET, "/api/books").permitAll()
                .antMatchers(HttpMethod.GET, "/api/books/**").permitAll()
                .antMatchers(HttpMethod.POST, "api/books").hasRole(Role.ADMIN.name())
                .antMatchers("/api/books/**").hasRole(Role.ADMIN.name())
                /*
                 *  UserController authorization configuration
                 */
                .antMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole(Role.USER.name(),Role.ADMIN.name())
                .antMatchers("/api/users/**").hasRole(Role.ADMIN.name())
                /*
                 *  PurchaseController authorization configuration
                 */
                .antMatchers("/api/purchases").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                /*
                 *  InternalApiController authorization configuration
                 */
                .antMatchers("/api/internal/**").hasRole(Role.SYSTEM_MANAGER.name())
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint());

        // internal > jwt > authentication
        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(internalApiAuthenticationFilter(), JwtAuthorizationFilter.class);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*");
            }
        };
    }

    //Overloading Method
    //For Swagger
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/v3/api-docs",
//                "/configuration/ui",
//                "/swagger-resources/**",
//                "/configuration/security",
//                "/swagger-ui.html",
//                "/swagger-ui/**",
//                "/webjars/**");
//    }

    private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**"
            // other public endpoints of your API may be appended to this array
    };
}