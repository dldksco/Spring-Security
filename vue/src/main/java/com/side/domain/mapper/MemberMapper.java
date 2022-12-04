package com.side.domain.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.side.domain.dto.LoginDto;
import com.side.domain.dto.MemberDto;
import com.side.domain.dto.TokenDto;

@Mapper
public interface MemberMapper {

	public String login(LoginDto loginDto) throws SQLException;

	public void saveRefreshToken(Map<String, String> map) throws SQLException;

	public Object getRefreshToken(String userid) throws SQLException;

	public void deleteRefreshToken(Map<String, String> map) throws SQLException;

	int idCheck(String userId) throws SQLException;

	void joinMember(MemberDto memberDto) throws SQLException;

	MemberDto getMember(String userId) throws SQLException;

	void updateMember(MemberDto memberDto) throws SQLException;

	List<MemberDto> listMember(Map<String, Object> map) throws SQLException;

	void deleteMember(String userId) throws SQLException;

	public MemberDto memberInfo(String userid) throws SQLException;

}
