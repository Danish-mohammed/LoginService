package com.bridgelabz.login.appconfiguration;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.bridgelabz.login.model.User;
import com.bridgelabz.login.util.JwtToken;

@Configuration
public class AppConfig {
	@Bean
	public BCryptPasswordEncoder bcryptpasswordencoder()
	{
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JwtToken jwttoken()
	{
		return new JwtToken();
	}

	@Bean
	public User user()
	{
		return new User();
	}

	@Bean
	public ModelMapper modelMapper() {
	ModelMapper modelMapper = new ModelMapper();
	modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	return modelMapper;
	}
}
