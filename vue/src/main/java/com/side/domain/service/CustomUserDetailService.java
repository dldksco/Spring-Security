package com.side.domain.service;

import java.sql.SQLException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.side.domain.dto.MemberDto;
import com.side.domain.mapper.MemberMapper;
import com.side.global.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
	private final MemberMapper memberMapper;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		MemberDto memberInfo=null;
		try {
			memberInfo = memberMapper.memberInfo(userId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(memberInfo==null) {
			throw new UserNotFoundException("not found loingid : " + userId);
		}else
			return memberInfo;
		
	}

}
