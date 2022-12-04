package com.side.domain.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.side.domain.dto.LoginDto;
import com.side.domain.dto.MemberDto;
import com.side.domain.dto.TokenDto;
import com.side.domain.service.MemberService;
import com.side.global.jwt.JwtTokenProvider;

import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("/user") 
@RequiredArgsConstructor
@Api("사용자  V1")
public class MemberController {

	public static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	private final JwtTokenProvider jwtService;
	private final MemberService memberService;
	private final BCryptPasswordEncoder passwordEncoder;

	@ApiOperation(value = "회원가입", notes = "회원가입", response = Map.class)
	@PostMapping("/regist")
	public ResponseEntity<Map<String, Object>> join(@RequestBody MemberDto memberDto) {
		logger.info("memberDto info : {}", memberDto);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null; 
		memberDto.setUserPwd(passwordEncoder.encode(memberDto.getUserPwd()));
		try {
			if (memberService.idCheck(memberDto.getUsername()) < 1) {
				memberService.joinMember(memberDto);
				resultMap.put("message", SUCCESS);
				status = HttpStatus.OK;

			} else {
				resultMap.put("message", "DUPLICATED");
				status = HttpStatus.ACCEPTED;
			}

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("message", "FAIL");
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "회원정보 확인", notes = "회원정보를 확인합니다.")
	@GetMapping(value = "/userinfo/{userId}")
	public ResponseEntity<?> userInfo(@PathVariable("userId") String userId) {
		logger.debug("userInfo userid : {}", userId);
		try {
			MemberDto memberDto = memberService.getMember(userId);
			if (memberDto != null) 
				return new ResponseEntity<MemberDto>(memberDto, HttpStatus.OK);
			else
				return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@ApiOperation(value = "회원정보수정", notes = "회원정보를 수정합니다.")
	@PutMapping(value = "/modify")
	public ResponseEntity<Map<String, Object>> userModify(@RequestBody MemberDto memberDto) {
		logger.debug("userModify memberDto : {}", memberDto);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = null;
		memberDto.setUserPwd(passwordEncoder.encode(memberDto.getUserPwd()));
		try {
			memberService.updateMember(memberDto);
			resultMap.put("message", SUCCESS);
			status = HttpStatus.OK;
		} catch (Exception e) {
//			return exceptionHandling(e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "로그인", notes = "Access-token과 로그인 결과 메세지를 반환한다.", response = Map.class)
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDto loginDto,HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
//		loginDto.setUserPwd(passwordEncoder.encode(loginDto.getUserPwd())); 평문으로집어넣어야비교가능
		try {
			String userid = memberService.login(loginDto);
			TokenDto token = memberService.tokenGenerator(userid);
			System.out.println(userid);
			if (userid != null) {
				String accessToken = jwtService.createAcessToken("userid", userid);// key, data
				String refreshToken = jwtService.createRefreshToken("userid", userid);// key, data
				ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken)/// new
																											/// Cookie("refreshToken",
																											/// token.getRefreshToken());
						.path("/").maxAge(14 * 24 * 60 * 60) // 14일
						.httpOnly(true).secure(true).sameSite("None").build();
				response.addHeader("Set-Cookie", responseCookie.toString());
				System.out.println(responseCookie.toString());
				logger.debug("로그인 accessToken 정보 : {}", accessToken);
				logger.debug("로그인 refreshToken 정보 : {}", refreshToken);
				resultMap.put("access-token", accessToken);
				resultMap.put("message", SUCCESS);
				status = HttpStatus.ACCEPTED; 
				System.out.println("다온거자너");
			} else {
				resultMap.put("message", FAIL);
				status = HttpStatus.ACCEPTED;
			}
		} catch (Exception e) {
			logger.error("로그인 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
//		return null;
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

//	@ApiOperation(value = "로그아웃", notes = "회원 정보를 담은 Token을 제거한다.", response = Map.class)
//	@GetMapping("/logout/{userid}")
	// 프론트단에서처리
//	public ResponseEntity<?> removeToken(@PathVariable("userid") String userid) {
//		Map<String, Object> resultMap = new HashMap<>();
//		HttpStatus status = HttpStatus.ACCEPTED;
//		try {
//			memberService.deleRefreshToken(userid);
//			resultMap.put("message", SUCCESS);
//			status = HttpStatus.ACCEPTED;
//		} catch (Exception e) {
//			logger.error("로그아웃 실패 : {}", e);
//			resultMap.put("message", e.getMessage());
//			status = HttpStatus.INTERNAL_SERVER_ERROR;
//		}
//		return new ResponseEntity<Map<String, Object>>(resultMap, status);
//
//	}

	@PostMapping(value = "/refresh")
	public ResponseEntity<?> issueToken(@CookieValue("refreshToken") String refreshTokenRequest) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
//			String refreshToken = jwtService.resolveToken(refreshCookie.getValue());

			// 유저 권한 저장 들어있는 컬렉션
			String refreshToken = jwtService.resolveToken(refreshTokenRequest);
			String accessTokenUserId = jwtService.getUserId(refreshToken);
			String newAccessToken = null;
			if (accessTokenUserId != null) {
				newAccessToken = jwtService.createAcessToken("userid", accessTokenUserId);
				log.debug("토큰 재발급 성공");
				resultMap.put("access-token", newAccessToken);
				resultMap.put("message", SUCCESS);
				status = HttpStatus.ACCEPTED;

			} else {
				resultMap.put("message", FAIL);
				status = HttpStatus.UNAUTHORIZED;
			}
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		} catch (Exception e) {
			resultMap.put("message", FAIL);
			status = HttpStatus.UNAUTHORIZED;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
	}

	@ApiOperation(value = "회원인증", notes = "회원 정보를 담은 Token을 반환한다.", response = Map.class)
	@GetMapping("/info/{userid}")
	public ResponseEntity<Map<String, Object>> getInfo(
			@PathVariable("userid") @ApiParam(value = "인증할 회원의 아이디.", required = true) String userid,
			HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		try {
			if (jwtService.validateToken(request.getHeader("access-token"))) {

				logger.info("사용 가능한 토큰!!!");

//				로그인 사용자 정보.
				MemberDto memberDto = memberService.memberInfo(userid);
				resultMap.put("memberInfo", memberDto);
				resultMap.put("message", SUCCESS);
				status = HttpStatus.ACCEPTED;
			} else {
				logger.error("사용 불가능 토큰!!!");
				resultMap.put("message", FAIL);
				status = HttpStatus.UNAUTHORIZED;
			}
		} catch (ExpiredJwtException e) {
			logger.error("사용 불가능 토큰!!!");
			resultMap.put("message", FAIL);
			status = HttpStatus.UNAUTHORIZED;
		} catch (Exception e) {
			logger.error("정보조회 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ApiOperation(value = "회원정보삭제", notes = "회원정보를 삭제합니다.")
	@DeleteMapping(value = "/deleteUser/{userId}")
	public ResponseEntity<?> userDelete(@PathVariable("userId") String userId) {
		logger.debug("userDelete userid : {}", userId);
		try {
			memberService.deleteMember(userId);
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("message", SUCCESS);
			return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

}
