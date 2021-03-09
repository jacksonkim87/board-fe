package com.example.boardfe.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * AppUser
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class AppUser {
	
	private String id; 	
	private String idNo;
	private String password;
	private String memberName;	 
	private String email;
	private String sex; 
	private Integer age;
	private String role;

}
