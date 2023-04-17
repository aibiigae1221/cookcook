package com.aibiigae1221.cookcook.web.domain;

import java.io.Serializable;

public class EditRecipeCookStepParameters implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String imageFileName;

	private String detail;
	
	private int order;
	
	public EditRecipeCookStepParameters() {}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

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

	@Override
	public String toString() {
		return "EditRecipeCookStepParameters [imageFileName=" + imageFileName + ", detail=" + detail + ", order="
				+ order + "]";
	}
}
