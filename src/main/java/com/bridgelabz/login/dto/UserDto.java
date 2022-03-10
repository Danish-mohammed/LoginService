package com.bridgelabz.login.dto;

import java.time.LocalDateTime;

import lombok.Data;
@Data
public class UserDto {


private String FirstName;
private String LastName;
private String Kyc;
private String dataOfBirth;
private int otp;
private String Email;
private String Password;


}
