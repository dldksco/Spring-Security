package com.side.domain.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.side.domain.dto.LoginDto;
import com.side.domain.dto.MemberDto;
import com.side.domain.dto.TokenDto;
import com.side.domain.mapper.MemberMapper;
import com.side.global.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final SqlSession sqlSession;
	private final MemberMapper memberMapper;
	private final JwtTokenProvider jwtTokenProvider;
	private final BCryptPasswordEncoder passwordEncoder;

	@Override
	public MemberDto getMember(String userId) throws Exception {
		return sqlSession.getMapper(MemberMapper.class).getMember(userId);
	}

	@Override
	public String login(LoginDto loginDto) throws Exception {
		MemberDto memberDto = sqlSession.getMapper(MemberMapper.class).getMember(loginDto.getUserId());// indUserByUsername(loginDto.getUsername())
		if (memberDto == null) {
			return null;
		}
		if (!passwordEncoder.matches(loginDto.getUserPwd(), memberDto.getPassword())) {
			System.out.println("오긴해?");
			return null;
		}
		
		return memberDto.getUsername();
	}

	@Override
	public MemberDto memberInfo(String userId) throws Exception {
		return sqlSession.getMapper(MemberMapper.class).memberInfo(userId);
	}

	@Override
	public int idCheck(String userId) throws Exception {
		return sqlSession.getMapper(MemberMapper.class).idCheck(userId);
	}

	@Override
	public void joinMember(MemberDto memberDto) throws Exception {
		sqlSession.getMapper(MemberMapper.class).joinMember(memberDto);
	}

	@Override
	public void updateMember(MemberDto memberDto) throws Exception {
		sqlSession.getMapper(MemberMapper.class).updateMember(memberDto);
	}

	@Override
	public List<MemberDto> listMember(Map<String, Object> map) throws Exception {
		return sqlSession.getMapper(MemberMapper.class).listMember(map);

	}

	@Override
	public void deleteMember(String userId) throws Exception {
		sqlSession.getMapper(MemberMapper.class).deleteMember(userId);
	}

	@Override
	public TokenDto tokenGenerator(String userid) throws Exception {

		MemberDto memberDto = memberMapper.getMember(userid);// indUserByUsername(loginDto.getUsername())

		if (memberDto == null) {
			return null;
		}

		return TokenDto.builder().accessToken(jwtTokenProvider.createAcessToken(memberDto.getUsername(), ("ROLE_USER")))
				.refreshToken(jwtTokenProvider.createRefreshToken(memberDto.getUsername(), ("ROLE_USER"))).build();
	}

}
