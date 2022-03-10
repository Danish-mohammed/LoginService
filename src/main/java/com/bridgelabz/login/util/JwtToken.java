package com.bridgelabz.login.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class JwtToken {
	private static final String SECRET="Mohammed";
	public String jwtToken(Long userId)
	{
		String token=null;
		token=JWT.create().withClaim("id",userId).sign(Algorithm.HMAC256(SECRET));
		return token;
	}
	public Long parseJWT(String jwt)
	{
		Long id=(long)0;
		if(jwt!=null)
		{
			
			id=JWT.require(Algorithm.HMAC256(SECRET)).build().verify(jwt).getClaim("id").asLong();
		}
		return id;
	}
}

