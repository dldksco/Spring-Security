package com.side.domain.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.side.domain.dto.MemoDto;
import com.side.domain.dto.MemoParameterDto;
import com.side.domain.service.MemoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

//http://localhost:9999/vue/swagger-ui.html
@CrossOrigin(origins = { "*" }, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.POST} , maxAge = 6000)
@RestController
@RequestMapping("/board/memo")
@RequiredArgsConstructor
@Api("메모 컨트롤러")
public class MemoController {

	private static final Logger logger = LoggerFactory.getLogger(MemoController.class);
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	private final MemoService memoService;

	@ApiOperation(value = "댓글작성", notes = "새로운 답변 정보를 입력한다. 그리고 DB입력 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@PostMapping
	public ResponseEntity<String> writeMemo(
			@RequestBody @ApiParam(value = "메모 정보.", required = true) MemoDto memoDto) throws Exception {
		logger.info("writeArticle - 호출");
		if (memoService.writeMemo(memoDto)) {
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "댓글", notes = "모든 댓글의 정보를 반환한다.", response = List.class)
	@GetMapping("/{articleNo}")
	public ResponseEntity<List<MemoDto>> listMemo(
			@ApiParam(value = "메모를 얻기위한 부가정보.", required = true) MemoParameterDto memoParameterDto, @PathVariable("articleNo") String articleNo)
			throws Exception {
		logger.info("listMemo - 호출");
		List<MemoDto> a = memoService.listMemo(memoParameterDto,articleNo);
		memoParameterDto.setArticleNo(Integer.parseInt(articleNo));
		return new ResponseEntity<List<MemoDto>>(memoService.listMemo(memoParameterDto,articleNo), HttpStatus.OK);
	}

	@ApiOperation(value = "댓글수정", notes = "수정할 댓글 정보를 입력한다. 그리고 DB수정 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@PutMapping
	public ResponseEntity<String> modifyMemo(
			@RequestBody @ApiParam(value = "수정할 댓글정보.", required = true) MemoDto memoDto) throws Exception {
		logger.info("modifyArticle - 호출 {}", memoDto);

		if (memoService.modifyMemo(memoDto)) {
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.OK);
	}

	@ApiOperation(value = "댓글삭제", notes = "댓글번호에 해당하는 게시글의 정보를 삭제한다. 그리고 DB삭제 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@DeleteMapping("/{memono}")
	public ResponseEntity<String> deleteMemo(
			@PathVariable("memono") @ApiParam(value = "삭제 댓글번호.", required = true) int memoNo)
			throws Exception {
		logger.info("deleteMemo - 호출");
		if (memoService.deleteMemo(memoNo)) {
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}
}