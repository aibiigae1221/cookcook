package com.aibiigae1221.cookcook.web.domain;

import java.io.Serializable;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class AddRecipeCookStepParameters implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	private String imageFileName;

	@NotNull(message = "조리 과정에 대한 부연 설명을 입력해주세요.")
	@NotBlank(message = "조리 과정에 대한 부연 설명을 입력해주세요.")
	private String detail;
	
	@Min(value= 0, message = "조리과정 순서의 최소값은 0입니다.") // 이거 적용안됨. 왜인지 모르겠음
	@NotNull
	private int order;
	
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getImageFileName() {
		return imageFileName;
	}
	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}
	
	@Override
	public String toString() {
		return "AddRecipeCookStepParameters [imageFileName=" + imageFileName + ", detail=" + detail + ", order=" + order
				+ "]";
	}
	
}
