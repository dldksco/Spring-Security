package com.side.domain.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.side.domain.dto.MemoDto;
import com.side.domain.dto.MemoParameterDto;


@Mapper
public interface MemoMapper {
	public int writeMemo(MemoDto memoDto) throws SQLException;
	public List<MemoDto> listMemo(MemoParameterDto memoParameterDto) throws SQLException;
	public int getTotalCount(MemoParameterDto memoParameterDto) throws SQLException;
	public int modifyMemo(MemoDto memoDto) throws SQLException;
	public int deleteMemo(int memoNo) throws SQLException;
}
