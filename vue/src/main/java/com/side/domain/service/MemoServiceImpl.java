package com.side.domain.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.side.global.util.PageNavigation;

import lombok.RequiredArgsConstructor;

import com.side.domain.dto.MemoDto;
import com.side.domain.dto.MemoParameterDto;
import com.side.domain.mapper.MemoMapper;
@Service
@RequiredArgsConstructor
public class MemoServiceImpl implements MemoService {

	private final SqlSession sqlSession;

	@Override
	public boolean writeMemo(MemoDto memoDto) throws Exception {
		if (memoDto.getContent() == null) {
			throw new Exception();
		}
		return sqlSession.getMapper(MemoMapper.class).writeMemo(memoDto) == 1;
	}

	@Override
	public List<MemoDto> listMemo(MemoParameterDto memoParameterDto, String articleno) throws Exception {
		int start = memoParameterDto.getPg() == 0 ? 0 : (memoParameterDto.getPg() - 1) * memoParameterDto.getSpp();
		memoParameterDto.setStart(start);
		return sqlSession.getMapper(MemoMapper.class).listMemo(memoParameterDto);
	}

//	@Override
//	public PageNavigation makeMemoNavigation(MemoParameterDto memoParameterDto) throws Exception {
//		int naviSize = 5;
//		PageNavigation memoNavigation = new PageNavigation();
//		memoNavigation.setCurrentPage(memoParameterDto.getPg());
//		memoNavigation.setNaviSize(naviSize);
//		int totalCount = sqlSession.getMapper(MemoMapper.class).getTotalCount(memoParameterDto);// 총글갯수 269
//		memoNavigation.setTotalCount(totalCount);
//		int totalPageCount = (totalCount - 1) / memoParameterDto.getSpp() + 1;// 27
//		memoNavigation.setTotalPageCount(totalPageCount);
//		boolean startRange = memoParameterDto.getPg() <= naviSize;
//		memoNavigation.setStartRange(startRange);
//		boolean endRange = (totalPageCount - 1) / naviSize * naviSize < memoParameterDto.getPg();
//		memoNavigation.setEndRange(endRange);
//		memoNavigation.makeNavigator();
//		return memoNavigation;
//	}


	@Override
	@Transactional
	public boolean modifyMemo(MemoDto memoDto) throws Exception {
		return sqlSession.getMapper(MemoMapper.class).modifyMemo(memoDto) == 1;
	}

	@Override
	@Transactional
	public boolean deleteMemo(int memoNo) throws Exception {
		return sqlSession.getMapper(MemoMapper.class).deleteMemo(memoNo) == 1;
	}
}
