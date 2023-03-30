package com.aibiigae1221.cookcook.web.domain;

import jakarta.validation.constraints.Min;

public class RecentRecipeParameters {

	@Min(value = 3, message = "보여주고자 하는 레시피 갯수는 최소 3개입니다.")
	private int amount;
	
	public RecentRecipeParameters() {}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	
}
