package com.aibiigae1221.cookcook.web.domain;


import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class SignUpParameters implements Serializable{

	private static final long serialVersionUID = 1L;

	@NotNull(message="이메일을 입력해주세요.")
	@NotBlank(message = "이메일을 입력해주세요.")
	@Pattern(regexp = "^[a-zA-Z0-9_]+@[0-9a-zA-Z]+\\.[0-9a-z]{2,3}", message = "이메일 형식이 유효하지 않습니다.")
	private String email;

	@NotNull(message="비밀번호를 입력해주세요.")
	@NotBlank(message = "비밀번호를 입력해주세요.")
	private String password;
	
	@NotNull(message="이름을 입력해주세요.")
	@NotBlank(message = "이름을 입력해주세요.")
	private String nickname;
	
	public SignUpParameters() {}

	public SignUpParameters(String email, String password, String nickname) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Override
	public String toString() {
		return "SignUpParameters [email=" + email + ", password=" + password + ", nickname=" + nickname + "]";
	}
}
