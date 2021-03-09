package com.example.boardfe.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * ResultMsg
 */
@Getter
@ToString
@Setter
public class ResultMsg {
	private String successYn;
	private String statusCode;
	private String message;
	private String devMessage;
	
	public ResultMsg(){
		
	}
	
	public ResultMsg(String successYn) {
		this.successYn = successYn;
		this.statusCode = null;
		this.message = null;
		this.devMessage = null;
	}
	
}