package net.javaguitar.controller;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import net.javaguitar.model.UsersModel;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	SqlSession ss;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String user_id = authentication.getName();
		String user_pwd = (String) authentication.getCredentials();
		UsersModel usersModel = new UsersModel();
		usersModel.setUser_id(user_id);
		usersModel.setUser_pwd(user_pwd);
		int retVal = ss.selectOne("net.javaguitar.mapper.UsersMapper.selectUsers", usersModel);
		if (retVal == 0)
			throw new BadCredentialsException("Login Error !!");
		usersModel.setUser_pwd(null);

		ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		return new UsernamePasswordAuthenticationToken(usersModel, null, authorities);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
