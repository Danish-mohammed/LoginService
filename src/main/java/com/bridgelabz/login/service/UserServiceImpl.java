package com.bridgelabz.login.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.bridgelabz.login.dto.ForgotPwdDto;
import com.bridgelabz.login.dto.LoginDto;
import com.bridgelabz.login.dto.UpdatePwdDto;
import com.bridgelabz.login.dto.UpdateUserDto;
import com.bridgelabz.login.dto.UserDto;
import com.bridgelabz.login.exception.UserBookRegistrationException;
import com.bridgelabz.login.model.User;
import com.bridgelabz.login.repository.UserRepository;
import com.bridgelabz.login.response.Response;
import com.bridgelabz.login.util.Jms;
import com.bridgelabz.login.util.JwtToken;

@Service
public class UserServiceImpl implements IUserService{
	
	@Autowired
	private UserRepository userrepo;
	
	@Autowired
	private ModelMapper modelmapper;
	
	@Autowired
	private BCryptPasswordEncoder pwdencoder;
	
	@Autowired
	private JwtToken jwt;

	@Autowired
	private Jms jms;

	@Override
	public User verify(String token) {
		long id=jwt.parseJWT(token);
		User user=userrepo.isIdExists(id).orElseThrow(() -> new UserBookRegistrationException("user not exists",HttpStatus.OK,id,"false"));
		user.setVerifyEmail(true);
		userrepo.save(user);
		return user;
	}

	@Override
	public Response loginUser(LoginDto dto) {
		User  user=userrepo.findByEmail(dto.getEmail()).orElseThrow(() -> new UserBookRegistrationException("login failed",HttpStatus.OK,null,"false"));
		boolean ispwd=pwdencoder.matches(dto.getPassword(),user.getPassword());
		if(ispwd==false) {
			throw new UserBookRegistrationException("login failed",HttpStatus.OK,null,"false");
		} else {
			String token=jwt.jwtToken(user.getId());
			return new Response(" Successfully login user ", user, 200, token);
		}
	}

	@Override
	public Response getAllUser() {
		List<User> user = userrepo.findAll();
		System.out.println(user);
		return new Response("users are",user,200,"true");
	}

	@Override
	public User getUserById(Long id) throws UserBookRegistrationException {
		return userrepo.getUserById(id).orElseThrow(() -> new UserBookRegistrationException("user not exists",HttpStatus.OK,id,"false"));
	}

	@Override
	public Response updateUserById(String token,Long id, UpdateUserDto dto) throws UserBookRegistrationException {
		Optional<User> isUserPresent = userrepo.findById(id);
		if (isUserPresent.isPresent()) {
			isUserPresent.get().setFirstName(dto.getFirstName());
			isUserPresent.get().setLastName(dto.getLastName());
			isUserPresent.get().setDataOfBirth(dto.getDataOfBirth());
			isUserPresent.get().setEmail(dto.getEmail());
			isUserPresent.get().setPassword(dto.getPassword());
			isUserPresent.get().setUpdatedDate(LocalDateTime.now());
			userrepo.save(isUserPresent.get());
			return new Response("Update user sucesssfully",isUserPresent,200,"true");
		} else {
			throw new UserBookRegistrationException("invalid bank details", null, 400, "true");
		}
	}

	@Override
	public Response forgotPwd(ForgotPwdDto forgotdto) {
//		User user=getUserByEmail(forgotdto.getEmail());
		Optional<User> isUserPresent = userrepo.findByEmail(forgotdto.getEmail());
		String body="http://localhost:4200/resetpassword/"+jwt.jwtToken(isUserPresent.get().getId());
		jms.sendEmail(isUserPresent.get().getEmail(),"Reset Password",body);
		return new Response("Rest password ",body,200,"true");
	}

	@Override
	public User updatepwd(UpdatePwdDto pwddto) {
		long id=jwt.parseJWT(pwddto.getToken());
	    User user=getUserById(id);
		user.setPassword(pwdencoder.encode(pwddto.getNewpassword()));
		userrepo.save(user);
		return user;
	}

	@Override
	public Response sendotp(String token) {
		long id=jwt.parseJWT(token);
		Optional<User>isUserPresent = userrepo.findById(id);
		if (isUserPresent.isPresent()) {
			
			String body = "OTP = " + isUserPresent.get().getOtp();
			jms.sendEmail(isUserPresent.get().getEmail(),"verification email",body);
			return new Response("Sucesssfully sent otp in mail",isUserPresent,200,"true");
		} else {
			throw new UserBookRegistrationException("User id is not present", HttpStatus.OK, null, "false");
		}
	}

	@Override
	public Response verifyOtp(String token, int otp) throws UserBookRegistrationException {
		long id=jwt.parseJWT(token);
		Optional<User> isUserPresent = userrepo.findById(id);
		if (isUserPresent.isPresent()) {
			if (isUserPresent.get().getOtp() == otp) {
				return new Response("verify otp ",isUserPresent,200,"true");
			}
				else {
					throw new UserBookRegistrationException("User id is not present", HttpStatus.OK, null, "false");
				}
		} else {
			throw new UserBookRegistrationException("User id is not present", HttpStatus.OK, null, "false");
		}
	}

	@Override
	public Response delete(String token, Long id) {
		long Id=jwt.parseJWT(token);
		Optional<User> isUserPresent = userrepo.findById(id);
		if (isUserPresent.isPresent()) {
			userrepo.delete(isUserPresent.get());
			return new Response("User Data Deleted successful  ",isUserPresent,200,"true");
		} else {
			throw new UserBookRegistrationException("User id is not present", HttpStatus.OK, isUserPresent.get(), "false");
		}
	}

	@Override
	public User check(String token) {
		long id=jwt.parseJWT(token);
		User user=userrepo.isIdExists(id).orElseThrow(() -> new UserBookRegistrationException("user not exists",HttpStatus.OK,id,"false"));
		user.setVerifyEmail(true);
		userrepo.save(user);
		return user;
	}

	@Override
	public Response purchaseBook(String token) {
		long Id=jwt.parseJWT(token);
		Optional<User>isUserPresent=userrepo.findById(Id);
		if(isUserPresent.isPresent()) {
			LocalDateTime todayDate=LocalDateTime.now();
			isUserPresent.get().setPurchaseDate(LocalDateTime.now());
			isUserPresent.get().setExpiryDate(todayDate.plusYears(1));
			String body="Dear User You have succesfully purchased";
			jms.sendEmail(isUserPresent.get().getEmail(),"succesfullypurchaes ",body);
			userrepo.save(isUserPresent.get());
			return new Response("User was purchase is succesfully","ExpiryDate:"+isUserPresent.get().getExpiryDate(),200,"true");
		}
		 else {
				throw new UserBookRegistrationException("User id is not present", HttpStatus.OK, isUserPresent.get(), "false");
			}
		}

	@Override
	public Response expiry(String token) {
		long Id=jwt.parseJWT(token);
		Optional<User>isUserPresent=userrepo.findById(Id);
		if(isUserPresent.isPresent()) {
			LocalDateTime today=LocalDateTime.now();
			if(today.equals(isUserPresent.get().getExpiryDate())) {
				String body="Dear User your purchase of the book is gettin gexpired,Please Subscribe to keep reading book";
				System.out.println(body);
				jms.sendEmail(isUserPresent.get().getEmail(),"Remainder of purchase is gettingexpired ",body);
				return new Response("Sending  mail to Remainder  ",isUserPresent.get().getFirstName(),200,"true");
			}
			return new Response("Sending  mail to Remainder  ",isUserPresent.get().getFirstName(),200,"true");
		}else {
					throw new UserBookRegistrationException("User id is not present", HttpStatus.OK, isUserPresent.get(), "false");
		}
	}
}	
	
	
	


	
	
	

	