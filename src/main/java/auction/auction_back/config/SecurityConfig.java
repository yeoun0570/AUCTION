//package auction.auction_back.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//        //BCrypt 알고리즘을 사용하여 비밀번호를 해시(암호화)한다.
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//
//                .authorizeHttpRequests((authorizeRequest) ->
//                        authorizeRequest
//                                .requestMatchers("/admin/**")
//                                .hasAnyRole("ADMIN")
//                                .anyRequest().authenticated())
//
//
//                .formLogin((formLogin) ->
//                        formLogin
//                                .loginPage("/login").permitAll()
//                                .usernameParameter("username")
//                                .passwordParameter("password")
//                                .loginProcessingUrl("/login/login-proc")
//                                .defaultSuccessUrl("/", true)
//                                .failureUrl("/login"))
//
//
//                .logout((logout) ->
//                        logout
//                                .logoutSuccessUrl("/login")
//                                .deleteCookies("JSESSIONID", "remember-me"));
//
//
////                .userDetailsService(CustomUserDetailsService);
//
//        return http.build();
//    }
//
//}
