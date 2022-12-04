package com.side.global.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.side.global.jwt.JwtTokenProvider;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
//RestAPI => UsernamePasswordAuthenticationFilter앞
public class JwtFilter extends GenericFilterBean {

	private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

	// public static final String AUTHORIZATION_HEADER = "Authorization";

	private JwtTokenProvider tokenProvider;
	public JwtFilter(JwtTokenProvider tokenProvider){
		this.tokenProvider = tokenProvider; 
	}
	/**
	 * jwt 토큰의 인증정보를 SecurityContext에 저장하는 역할 수행
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		String requestURI = httpServletRequest.getRequestURI();
		if(true) {
			filterChain.doFilter(request, response);
		}
		//프론트단에서 처리했음
		
		
		
		
//		// 아래 uri로 접근시 토큰이 필요하지 않으므로 바로 다음필터실행
//        if (requestURI.equals("/vue/user/") 
//        || requestURI.equals("/vue/board") 
//        ){
//        	filterChain.doFilter(request, response);
//            return;
//        }

//		Cookie[] cookies = httpServletRequest.getCookies();
//		String refreshToken = null;
//		String useToken = null;
//		for (Cookie cookie : cookies) {
//			if (cookie.getName().equals("refreshToken")) {
//				refreshToken = cookie.getValue();
//			}
//		}
//		if (requestURI.equals("/vue/**")) {
//			useToken = tokenProvider.resolveToken(refreshToken);
//			try {
//				if (StringUtils.hasText(refreshToken) && tokenProvider.validateToken(useToken)) {
//					filterChain.doFilter(request, response);
//					return;
//				}
//			} catch (Exception e) {
//				log.error(e.getMessage(), e);
//			}
//		} else {// if (requestURI != "/api/v1/token/getAccessToken") {
//			useToken = tokenProvider.resolveToken(httpServletRequest.getHeader("Authorization"));
//		}
//
//		try {
//			if (StringUtils.hasText(useToken) && tokenProvider.validateToken(useToken)) {
//				Authentication authentication = tokenProvider.getAuthentication(useToken);
//
//				SecurityContextHolder.getContext().setAuthentication(authentication);
//				logger.debug("Security Contextx에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
//
//			} else {
//				logger.debug("유효한 JWT 토큰이 없습니다, uri:{}", requestURI);
//			}
//			filterChain.doFilter(request, response);
//			// 잘못된 서명인 토큰일 경우
//		} catch (SecurityException | MalformedJwtException e) {
//			logger.info("잘못된 JWT 서명입니다.");
//			setErrorResponse(HttpStatus.UNAUTHORIZED, httpServletResponse, e);
//
//			// 만료된 토큰일 경우
//		} catch (ExpiredJwtException e) {
//			logger.info("만료된 JWT 토큰입니다.");
//			setErrorResponse(HttpStatus.UNAUTHORIZED, httpServletResponse, e);
//		}
//		// 지원되지 않는 토큰일 경우
//		catch (UnsupportedJwtException e) {
//			logger.info("지원되지 않는 JWT 토큰입니다.");
//			setErrorResponse(HttpStatus.UNAUTHORIZED, httpServletResponse, e);
//
//			// 잘못된 토큰일 경우
//		} catch (IllegalArgumentException e) {
//			logger.info("JWT 토큰이 잘못되었습니다.");
//			setErrorResponse(HttpStatus.UNAUTHORIZED, httpServletResponse, e);
//		}
	}

	public void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable ex) throws IOException {
		response.setStatus(status.value());
		response.setContentType("application/json; charset=UTF-8");
		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put("HttpStatus", HttpStatus.UNAUTHORIZED);
			responseJson.put("message", ex.getMessage());
			responseJson.put("status", false);
			responseJson.put("statusCode", 401);
			responseJson.put("code", "Tk401");
			response.getWriter().print(responseJson);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}