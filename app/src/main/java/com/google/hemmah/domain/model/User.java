package com.google.hemmah.domain.model;

import com.google.hemmah.domain.model.enums.UserType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class User {
	private String userName;
	private String email;
	private String password;
	private String phoneNumber;
	private String firstName;
	private String lastName;
	private UserType userType;


}
