package com.side.domain.service;

import java.util.List;
import java.util.Map;

import com.side.domain.dto.LoginDto;
import com.side.domain.dto.MemberDto;
import com.side.domain.dto.TokenDto;

public interface MemberService {

	public String login(LoginDto loginDto) throws Exception;



	int idCheck(String userId) throws Exception;

	void joinMember(MemberDto memberDto) throws Exception;

	MemberDto getMember(String userId) throws Exception;

	void updateMember(MemberDto memberDto) throws Exception;

	List<MemberDto> listMember(Map<String, Object> map) throws Exception;

	void deleteMember(String userid) throws Exception;

	public MemberDto memberInfo(String userid) throws Exception;

	public TokenDto tokenGenerator(String userid) throws Exception;

}
