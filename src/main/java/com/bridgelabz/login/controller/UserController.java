package com.bridgelabz.login.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.bridgelabz.login.dto.UserDto;
import com.bridgelabz.login.exception.UserBookRegistrationException;
import com.bridgelabz.login.dto.ForgotPwdDto;
import com.bridgelabz.login.dto.LoginDto;
import com.bridgelabz.login.dto.UpdatePwdDto;
import com.bridgelabz.login.dto.UpdateUserDto;
import com.bridgelabz.login.model.User;
import com.bridgelabz.login.response.Response;
import com.bridgelabz.login.service.IUserService;

@RestController
@RequestMapping("/user")
public class UserController {

//    @Qualifier("IUserService")
    @Autowired
    private IUserService userService;

    @GetMapping("/verifyemail/{token}")
    public ResponseEntity<Response> verifyemail(@PathVariable("token") String token)
    {
        return new ResponseEntity<Response>(new Response("email verified", userService.verify(token),201,"true"),HttpStatus.ACCEPTED);
    }

    @PostMapping("/loginuser")
    public ResponseEntity<Response> loginUser(@RequestBody LoginDto dto,BindingResult result)
    {
        Response respDTO = userService.loginUser(dto);
        return new ResponseEntity<Response>(respDTO, HttpStatus.OK);
    }

    @GetMapping("/getAllusers")
    public ResponseEntity<Response>getAllUsers(){
        Response respDTO = userService.getAllUser();
        return new ResponseEntity<Response>(respDTO, HttpStatus.OK);
    }

    @GetMapping("/getuserbyid/{id}")
    public ResponseEntity<Response> getUserById(@PathVariable Long id) {
        User user= userService.getUserById(id);
        return new ResponseEntity<Response>(new Response("welcome", userService.getUserById(id),200,"true"),HttpStatus.OK);
    }

    @PutMapping("/update/{id}/{token}")
    public ResponseEntity<Response> updateUserById(@PathVariable Long id,@PathVariable  String token, @RequestBody  UpdateUserDto dto) {
        Response respDTO = userService.updateUserById(token,id, dto);
        return new ResponseEntity<Response>(respDTO,HttpStatus.OK);
    }
    @PostMapping("/forgotpassword")
    public ResponseEntity<Response> forgotPwd(@RequestBody  ForgotPwdDto forgotdto) {
        Response respDTO = userService.forgotPwd(forgotdto);
        return new ResponseEntity<Response>(respDTO, HttpStatus.OK);
    }
    @PostMapping("/updatepassword")
    public ResponseEntity<Response> updatePassword(@RequestBody UpdatePwdDto pwddto,BindingResult result)
    {
        return new ResponseEntity<Response>(new Response("password updated successfully", userService.updatepwd(pwddto),200,"true"),HttpStatus.OK);
    }

    @GetMapping("/sendotp/{token}")
    public Response sendotp(@PathVariable String token) {
        return userService.sendotp(token);
    }


    @GetMapping("/verifyotp/{token}")
    public Response verifyotp(@PathVariable String token, @RequestParam int otp) throws UserBookRegistrationException {
        return userService.verifyOtp(token, otp);
    }

    @GetMapping("/checkuser/{token}")
    public User check(@PathVariable String token) {
        return userService.check(token);
    }

    @DeleteMapping("/deleteuser/{token}/{id}")
    public ResponseEntity<Response> deleteuser(@PathVariable String token, @PathVariable Long id) {
        Response respDTO = userService.delete(token, id);
        return new ResponseEntity<Response>(respDTO, HttpStatus.OK);
    }
}
