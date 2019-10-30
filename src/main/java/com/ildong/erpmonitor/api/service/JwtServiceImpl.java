package com.ildong.erpmonitor.api.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ildong.erpmonitor.api.error.UnauthorizedException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("jwtService")
public class JwtServiceImpl implements JwtService{

	private static final String SALT =  "ydhSecret";
	private long tokenValidMilisecond = 1000L * 60 * 120;
	@Override
	public <T> String create(String key, T data, String subject){
		Date now = new Date();
		String jwt = Jwts.builder()
						 .setHeaderParam("typ", "JWT")
						 .setHeaderParam("regDate", System.currentTimeMillis())
						 .setSubject(subject)
						 .claim(key, data)
						 .setExpiration(new Date(now.getTime() + tokenValidMilisecond))
						 .signWith(SignatureAlgorithm.HS256, this.generateKey())
						 .compact();
		return jwt;
	}
	
	private byte[] generateKey(){
		byte[] key = null;
		try {
			key = SALT.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
//			if(log.isInfoEnabled()){
//				e.printStackTrace();
//			}else{
//				log.error("Making JWT Key Error ::: {}", e.getMessage());
//			}
		}
		
		return key;
	}

	@Override
	public boolean isUsable(String jwt) {
		try{
			Jws<Claims> claims = Jwts.parser()
					  .setSigningKey(this.generateKey())
					  .parseClaimsJws(jwt);
			Claims cl = claims.getBody();
			Date expiration = cl.get("exp", Date.class);
			System.out.println(expiration);
			return true;
			
		}catch (Exception e) {
			
//			if(log.isInfoEnabled()){
//				e.printStackTrace();
//			}else{
//				log.error(e.getMessage());
//			}
			throw new UnauthorizedException();

			/*개발환경!!!
			 * return false;*/
			 
		}
//		try {
//            Jws<Claims> claims = Jwts.parser().setSigningKey(this.generateKey()).parseClaimsJws(jwt);
//            return !claims.getBody().getExpiration().before(new Date());
//        } catch (Exception e) {
//            return false;
//        }
	}
	
	@Override
	public Map<String, Object> get(String key) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		String jwt = request.getHeader("Authorization");
		Jws<Claims> claims = null;
		try {
			claims = Jwts.parser()
						 .setSigningKey(SALT.getBytes("UTF-8"))
						 .parseClaimsJws(jwt);
		} catch (Exception e) {
//			if(log.isInfoEnabled()){
//				e.printStackTrace();
//			}else{
//				log.error(e.getMessage());
//			}
			throw new UnauthorizedException();
			
			/*개발환경
			Map<String,Object> testMap = new HashMap<>();
			testMap.put("memberId", 2);
			return testMap;*/
		}
//		System.out.println(claims.getBody().get(key));
		@SuppressWarnings("unchecked")
		Map<String, Object> value = (Map<String, Object>)claims.getBody().get(key);
		return value;
	}
	
	public String getId()
	{
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		String jwt = request.getHeader("Authorization");
		Jws<Claims> claims = null;
		try {
			claims = Jwts.parser()
						 .setSigningKey(SALT.getBytes("UTF-8"))
						 .parseClaimsJws(jwt);
		} catch (Exception e) {
//			if(log.isInfoEnabled()){
//				e.printStackTrace();
//			}else{
//				log.error(e.getMessage());
//			}
			throw new UnauthorizedException();
			
			/*개발환경
			Map<String,Object> testMap = new HashMap<>();
			testMap.put("memberId", 2);
			return testMap;*/
		}
//		System.out.println(claims.getBody().get(key));
		@SuppressWarnings("unchecked")
		String value = (String) claims.getBody().get("userId");
		return value;
	}
	
	@Override
	public int getMemberId() {
		return (int)this.get("member").get("memberId");
	}
}
