package com.side.global.jwt;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.lookup.SingleDataSourceLookup;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.side.global.exception.UnAuthorizedException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

// import 생략

@RequiredArgsConstructor
@Component
public class JwtTokenProvider implements InitializingBean { // JWT 토큰을 생성 및 검증 모듈

	@Value("${spring.jwt.secret}")
	private String secretKey;

	private long accessTokenValidityInMilliseconds = 1000*60*60; // 1시간만 토큰 유효
	private long refreshTokenValidityInMilliseconds =  1000*60*60;
	private final UserDetailsService userDetailsService;
	private Key key;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	@Override
	public void afterPropertiesSet() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	// Jwt 토큰 생성
	public String createAcessToken(String userId, String date) {
		Claims claims = Jwts.claims();
		claims.put(userId, date);
		// 토큰 만료기간
		Date now = new Date();
		Date validity = new Date(now.getTime() + this.accessTokenValidityInMilliseconds);
		return Jwts.builder().setSubject("access-token").setClaims(claims).setIssuedAt(now) // 토큰 발행 시간정보
				.setExpiration(validity) // 토큰 만료일 설정
				.signWith(key, SignatureAlgorithm.HS512) // 암호화
				.compact();
	}

	public String createRefreshToken(String userId, String date) {// , List<String> roles) {
		Claims claims = Jwts.claims();
		String roles = "ROLE_USER";
		claims.put(userId, date);
		// 토큰 만료기간
		Date now = new Date();
		Date validity = new Date(now.getTime() + this.refreshTokenValidityInMilliseconds);
		
		return Jwts.builder().setSubject("refresh-token").setClaims(claims).setIssuedAt(now) // 토큰 발행 시간정보
				.setExpiration(validity) // 토큰 만료일 설정
				.signWith(key, SignatureAlgorithm.HS512) // 암호화 
				.compact();
	}
		
	// Jwt 토큰으로 인증 정보를 조회
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserId(token));

		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	// 유저 이름 추출
	public String getUserId(String token) {
		return String.valueOf(Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("userid"));
	}

//	@SuppressWarnings("deprecation")
//	public Map<String, Object> get(String token) {
//		Jws<Claims> claims = null;
//		try { 
//			claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
//			System.out.println("무조건여기일듯");
//		} catch (Exception e) {
//			throw new UnAuthorizedException();
////			개발환경
////			Map<String,Object> testMap = new HashMap<>();
////			testMap.put("userid", userid);
////			return testMap;
//		}
//		Map<String, Object> value = claims.getBody();
//		return value;
//	}
//
//	public String getUserId(String token) {
//		return (String) this.get(token).get("userid");
//	}

	public String resolveToken(String str) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		String token = request.getHeader("refresh-token");
		return token;
	}

	// Jwt 토큰의 유효성 + 만료일자 확인
	public boolean validateToken(String token) {

		Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
		return !claims.getBody().getExpiration().before(new Date());

	}

	private Claims parseClaims(String accessToken) {
		try {
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}
}