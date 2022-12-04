package com.side.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel(value = "MemoDto : 댓글정보", description = "댓글의 상세 정보를 나타낸다.")
public class MemoDto {
	@ApiModelProperty(value = "댓글번호")
	private int memoNo;
	@ApiModelProperty(value = "작성자 아이디")
	private String userId;
	@ApiModelProperty(value = "글내용")
	private String content;
	@ApiModelProperty(value = "글번호")
	private int articleNo;
	@ApiModelProperty(value = "작성일")
	private String registerTime;
	public MemoDto(int memoNo, String userId, String content, int articleNo, String registerTime) {
		super();
		this.memoNo = memoNo;
		this.userId = userId;
		this.content = content;
		this.articleNo = articleNo;
		this.registerTime = registerTime;
	}
	public int getMemoNo() {
		return memoNo;
	}
	public void setMemoNo(int memoNo) {
		this.memoNo = memoNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getArticleNo() {
		return articleNo;
	}
	public void setArticleNo(int articleNo) {
		this.articleNo = articleNo;
	}
	public String getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}
	public MemoDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
