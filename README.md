
# 기술 스택

Spring-Boot, Spring-Security, MyBatis

---

# 주요 구현 기능 

### 로그인 및 회원가입
회원가입 CRUD 구현  

보안을 위해 JWT와 Spring-Security 사용  
DB에서 직접 password 조회를 할 수 없도록 Sping-Security BCryptPasswordEncoder 사용  
### Token 
  Access Token 및 Refresh Token 만료시 재로그인 
  Access Token의 만료일자를 짧게하여 보안성을 높임  
  Access Token 만료시 Refresh Token을 통해 재발급

Access Token : Local Storage 저장  

Refresh Token : Http Secure only Cookie 저장(XSS 공격 방지)
___

### 게시판 및 댓글
게시판 CRUD 구현, 댓글 CRD 구현

___
