package com.example.AIGen.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.AIGen.security.jwt.AuthEntryPointJwt;
import com.example.AIGen.security.jwt.AuthTokenFilter;
import com.example.AIGen.services.UserDetailsServiceImpl;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
//(securedEnabled = true,
//jsr250Enabled = true,
//prePostEnabled = true) // by default
public class WebSecurityConfig { //extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

		@Bean
		public AuthTokenFilter authenticationJwtTokenFilter() {
		  return new AuthTokenFilter();
		}
		
		
		
		@Bean
		public DaoAuthenticationProvider authenticationProvider() {
		  DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		
		  authProvider.setUserDetailsService(userDetailsService);
		  authProvider.setPasswordEncoder(passwordEncoder());
		
		  return authProvider;
		}
		
		
		
		@Bean
		public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		  return authConfig.getAuthenticationManager();
		}
		
		@Bean
		public PasswordEncoder passwordEncoder() {
		  return new BCryptPasswordEncoder();
		}
		
		
		
//		@Bean
//		public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		  http.csrf(csrf -> csrf.disable())
//		      .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
//		      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//		      .authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**").permitAll().requestMatchers("/api/test/**")
//		          .permitAll()
//		          .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll().anyRequest().authenticated());
//		
//		  http.authenticationProvider(authenticationProvider());
//		
//		  http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//		
//		  return http.build();
//		}
		@Bean
		public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		    http.csrf(csrf -> csrf.disable())
		        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
		        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		        .authorizeHttpRequests(auth -> auth
		            .requestMatchers("/api/auth/**","/api/otp/**","/api/resource/**","/boondmanager/**","/api/emails/**","/api/files/**","/api/test/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
		            .requestMatchers("/api/resource/admin/**").hasRole("ADMIN")
		            .requestMatchers("/api/resource/vp/**").hasAnyRole("ADMIN", "VP")
		            .requestMatchers("/api/resource/manager/**").hasAnyRole("ADMIN", "VP", "MANAGER")
		            .anyRequest().authenticated());

		    http.authenticationProvider(authenticationProvider());

		    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

		    return http.build();
		}	
		
}


