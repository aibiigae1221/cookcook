package com.aibiigae1221.cookcook.web.domain;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class LoginParameters implements Serializable{

	private static final long serialVersionUID = 1L;

	@NotNull(message = "이메일을 입력해주세요.")
	@NotBlank(message = "이메일을 입력해주세요.")
	@Pattern(regexp = "^[a-zA-Z0-9_]+@[0-9a-zA-Z]+\\.[0-9a-z]{2,3}", message = "이메일 형식이 유효하지 않습니다.")
	private String email;
	
	@NotNull(message = "비밀번호를 입력해주세요.")
	@NotBlank(message = "비밀번호를 입력해주세요.")
	private String password;

	public LoginParameters() {}
	
	public LoginParameters(
			@NotNull(message = "이메일을 입력해주세요.") @NotBlank(message = "이메일을 입력해주세요.") @Pattern(regexp = "^[a-zA-Z0-9_]+@[0-9a-zA-Z]+\\.[0-9a-z]{2,3}", message = "이메일 형식이 유효하지 않습니다.") String email,
			@NotNull(message = "비밀번호를 입력해주세요.") @NotBlank(message = "비밀번호를 입력해주세요.") String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginParameters [email=" + email + ", password=" + password + "]";
	}
}
