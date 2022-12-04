package com.side.domain.service;

import java.util.List;


import com.side.global.util.PageNavigation;
import com.side.domain.dto.MemoDto;
import com.side.domain.dto.MemoParameterDto;

public interface MemoService {
	public boolean writeMemo(MemoDto memoDto) throws Exception;
	public List<MemoDto> listMemo(MemoParameterDto memoParameterDto, String articleno) throws Exception;
//	public PageNavigation makeMemoNavigation(MemoParameterDto memoParameterDto) throws Exception;
	public boolean modifyMemo(MemoDto memoDto) throws Exception;
	public boolean deleteMemo(int memoNo) throws Exception;
}
 