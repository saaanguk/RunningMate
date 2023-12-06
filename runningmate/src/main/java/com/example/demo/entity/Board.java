package com.example.demo.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.example.demo.DTO.BoardDTO;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idx;
	
	@Column(nullable = false, length = 100)
	private String title;
	
	@Lob
	private String content;
	
	@ColumnDefault("0")
	private int hit;
	
	@GeneratedValue
	private String username;
	
	@GeneratedValue
	private String useremail;
	
	@ColumnDefault("1")
	private int invisable;
	
	private LocalDateTime regdate;
	
	@Builder
	public Board(Long idx, int hit, int invisable, String content, String title, String username, String regdate, String useremail) {
		this.idx = idx;
		this.hit=hit;
		this.invisable=invisable;
		this.content=content;
		this.title=title;
		this.username = username;
		this.useremail = useremail;
		this.regdate=LocalDateTime.now();
	}
	
}
