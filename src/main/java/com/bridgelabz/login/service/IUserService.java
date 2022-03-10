package com.bridgelabz.login.service;

import org.springframework.stereotype.Service;
import com.bridgelabz.login.dto.ForgotPwdDto;
import com.bridgelabz.login.dto.LoginDto;
import com.bridgelabz.login.dto.UpdatePwdDto;
import com.bridgelabz.login.dto.UpdateUserDto;
import com.bridgelabz.login.dto.UserDto;
import com.bridgelabz.login.exception.UserBookRegistrationException;
import com.bridgelabz.login.model.User;
import com.bridgelabz.login.response.Response;


@Service
public interface IUserService {
	public User verify(String token);
	public Response loginUser(LoginDto dto)throws UserBookRegistrationException ;
	public Response getAllUser();
	public User getUserById(Long id) throws UserBookRegistrationException;
    public Response updateUserById(String token,Long id,UpdateUserDto dto)throws UserBookRegistrationException;
    public Response forgotPwd(ForgotPwdDto forgotdto);
    public  User updatepwd(UpdatePwdDto pwddto);
    public Response sendotp(String token );
    public Response verifyOtp(String token, int otp) throws UserBookRegistrationException;
    public Response delete(String token, Long id);
    public User check(String token);
    public Response purchaseBook(String token);
    public Response expiry(String token);

   

 

}

