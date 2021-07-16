package org.example.springboot.config.auth;

import lombok.RequiredArgsConstructor;
import org.example.springboot.domain.user.Role;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity //PURPOSE: Spring Security 설정들을 활성화시켜 준다.
/*
 * WebSecurityConfigurerAdapter 를 상속받은 config 클래스에 @EnableWebSecurity 어노테이션을 달면
 * SpringSecurityFilterChain 이 자동으로 포함된다.
    */
  public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

  /*
   * 스프링 시큐리티 규칙 (접근 권한 작성)
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().ignoringAntMatchers("/h2-console/**") //to use h2-console
      .and()
        .headers().frameOptions().disable() // to use h2-console
      .and()
        .authorizeRequests()
        .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
        .antMatchers("/api/v1/**").hasRole(Role.USER.name())
        .anyRequest().authenticated()
      .and()
        .logout()
          .logoutSuccessUrl("/")
      .and()
        .oauth2Login()
          //.loginPage("/")
            .userInfoEndpoint()
              .userService(customOAuth2UserService);
  }
}
