package com.side.domain.service;

import java.util.List;

import com.side.global.util.PageNavigation;
import com.side.domain.dto.BoardDto;
import com.side.domain.dto.BoardParameterDto;

public interface BoardService {
	public boolean writeArticle(BoardDto boardDto) throws Exception;
	public List<BoardDto> listArticle(BoardParameterDto boardParameterDto) throws Exception;
//	public PageNavigation makePageNavigation(BoardParameterDto boardParameterDto) throws Exception;
	
	public BoardDto getArticle(int articleNo) throws Exception;
	public void updateHit(int articleNo) throws Exception;
	public boolean modifyArticle(BoardDto boardDto) throws Exception;
	public boolean deleteArticle(int articleNo) throws Exception;
}
