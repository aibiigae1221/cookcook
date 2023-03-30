package com.aibiigae1221.cookcook.web.domain;

import jakarta.validation.constraints.Min;

public class RecipeSearchParameters {

	
	private String keyword;
	
	@Min(value=1, message = "페이지 번호는 최소 1로 지정해야 합니다.")
	private int pageNo;
	
	public RecipeSearchParameters() {
		pageNo = 1;
	}
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	
}
