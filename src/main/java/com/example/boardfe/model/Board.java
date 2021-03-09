package com.example.boardfe.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Board
 */
@Setter
@Getter
@NoArgsConstructor
public class Board {

	private int num;
	private String title;
	private String contents;
	private String writeName;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime writeDate;
	private String modifyName;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime modifyDate;
	private int cnt;
	private String writeId;
	private String modifyId;

}