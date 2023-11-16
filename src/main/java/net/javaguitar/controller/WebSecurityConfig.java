package net.javaguitar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserAuthenticationProvider authenticationProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/").permitAll();
/*
			http.authorizeRequests().antMatchers("/css/**", "/js/**", "/images/**").permitAll()
					.antMatchers("/auth/admin/**")
					.hasRole("ADMIN")
					.antMatchers("/auth/**").hasAnyRole("ADMIN", "USER").anyRequest().authenticated();

			http.formLogin().loginPage("/login").loginProcessingUrl("/authenticate").failureUrl("/login?error")
					.defaultSuccessUrl("/index").usernameParameter("user_id").passwordParameter("user_pwd").permitAll();

			http.logout();*/

	}


	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(authenticationProvider);
	}


}
