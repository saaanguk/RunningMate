package com.example.demo.DTO;

import java.time.LocalDateTime;

import com.example.demo.entity.Inquire;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InquireDTO {
	private Long idx;
	private String content;
	private int finish;
	private String regdate;
	private String title;
	private String useremail;
	private String username;
	int count;
	
	
	public Inquire toEntity() {
		Inquire build = Inquire.builder()
				.idx(idx)
				.content(content)
				.title(title)
				.useremail(useremail)
				.username(username)
				.finish(1)
				.build();
		return build;
	}
	
	@Builder
	public InquireDTO(Long idx, String content, String title, String useremail, int count, String regdate, String username) {
		this.idx = idx;
		this.content = content;
		this.title = title;
		this.useremail = useremail;
		this.username = username;
		this.count = count;
		this.regdate = regdate;
	}
}
