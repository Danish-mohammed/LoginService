package com.bridgelabz.login.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name="User")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String FirstName;
	private String LastName;
	private String DataOfBirth;
	private String Kyc;
	private int Otp;
	private String email;
	private String Password;
	private LocalDateTime RegisteredDate;
	private LocalDateTime UpdatedDate;
	private LocalDateTime PurchaseDate;
	private LocalDateTime ExpiryDate;
	@Column(name ="is_verify_email ", columnDefinition = "boolean default false")
	private boolean verifyEmail;
	
}

