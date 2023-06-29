package com.shopmax.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration //Bean 객체를 싱글톤으로 공유할 수 있게 해준다.
@EnableWebSecurity //spring security filterChain이 자동으로 포함되게 한다
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		   //람다로 변경됨
		   //login에 crsf 추가
		   //memberFormloginForm.html 오타 MemberFormDto -> memberFormDto로 변경
		   
	       http
           .authorizeHttpRequests(authorize -> authorize
               .requestMatchers("/css/**", "/js/**", "/img/**", "/fonts/**").permitAll()
               .requestMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
               .requestMatchers("/admin/**").hasRole("ADMIN")
               .anyRequest().authenticated()
           )
           .formLogin(formLogin -> formLogin
        	   .loginPage("/members/login") 
        	   .defaultSuccessUrl("/")
        	   .usernameParameter("email")
        	   .failureUrl("/members/login/error")
               .permitAll()
           )
           .logout(logout -> logout.
        		   logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
        		   .logoutSuccessUrl("/")
        		   .permitAll()
        	)
           .exceptionHandling(handling -> handling
        		   .authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
           .rememberMe(Customizer.withDefaults());
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
