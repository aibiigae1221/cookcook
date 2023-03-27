package com.aibiigae1221.cookcook.web.domain;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;


public class AddRecipeCookStepParameters implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	private String uploadUrl;

	@NotNull(message = "조리 과정에 대한 부연 설명을 입력해주세요.")
	private String detail;
	
	@NotNull
	private Long order;
	
	public String getUploadUrl() {
		return uploadUrl;
	}
	public void setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public Long getOrder() {
		return order;
	}
	public void setOrder(Long order) {
		this.order = order;
	}
	
	@Override
	public String toString() {
		return "AddRecipeCookStepParameters [uploadUrl=" + uploadUrl + ", detail=" + detail + ", order=" + order + "]";
	}
}
