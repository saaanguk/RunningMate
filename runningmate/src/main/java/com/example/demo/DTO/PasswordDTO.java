package com.example.demo.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordDTO {
	
	@NotBlank(message = "현재 비밀번호를 입력해주세요.")
	private String currentPassword;
	
	@NotBlank(message = "새로운 비밀번호를 입력해주세요.")
	@Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    private String newPassword;
	
	@NotBlank(message = "새로운 비밀번호를 다시 입력해주세요.")
    private String confirmPassword;
}
