package com.google.hemmah.model;

import com.google.hemmah.model.enums.UserType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class User {
	private String userName;
	private String email;
	private String password;
	private String phoneNumber;
	private String firstName;
	private String lastName;
	private UserType userType;
}
