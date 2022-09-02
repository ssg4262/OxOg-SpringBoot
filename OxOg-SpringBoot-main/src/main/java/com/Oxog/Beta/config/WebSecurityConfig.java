package com.Oxog.Beta.config;

import com.Oxog.Beta.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;


@EnableWebSecurity
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class WebSecurityConfig   {
    @Autowired
    private DataSource dataSource;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http   .sessionManagement()
                .maximumSessions(1) // 최대 접속수를 1개로 제한한다.
                .expiredUrl("/account/login/") ;//
        http
                .authorizeRequests()
                    .antMatchers("/image/**"
                            , "/css/**",
                            "/",
                            "/board/list",
                            "/account/register"
                            ,"/board/detail"
                    ).permitAll()
                .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/account/login")
                    .defaultSuccessUrl("/")
                    .loginProcessingUrl("/account/login")
                .successHandler(
                        new AuthenticationSuccessHandler() {
                            @Override
                            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                System.out.println("authentication : " + authentication.getName());
                                response.sendRedirect("/"); // 인증이 성공한 후에는 root로 이동
                            }
                        }
                )
                    .permitAll()
                .and()
                  .logout()
                  .logoutUrl("/logout") // 로그아웃 처리 URL
                  .logoutSuccessUrl("/")
                  .permitAll();
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.jdbcAuthentication() // 인증 처리
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery(
                        "select username,password,enabled "
                        + "from user "
                        + "where username = ?")
                .authoritiesByUsernameQuery(
                        "select u.username,r.name "
                        +" from user_role ur inner join user u on ur.user_id = u.id"
                        +" inner join role r on ur.role_id = r.id"
                        +" where username = ?");
    }


    public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

        @Override
        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                    Authentication authentication) throws IOException, ServletException {
            if (authentication != null && authentication.getDetails() != null) {
                try {
                    request.getSession().invalidate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect("/");
        }
    }



}