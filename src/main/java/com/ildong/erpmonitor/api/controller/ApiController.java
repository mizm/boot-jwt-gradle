package com.example.demo.board.controller;

import java.util.HashMap;
import javax.servlet.http.HttpServletResponse;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.demo.board.service.JwtService;

@RestController
//@CrossOrigin("*")
public class ApiController {
	
	@Autowired
    private JwtService jwtService;
	
	//login
	@RequestMapping(value = "/api/login", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public void Login(@RequestBody HashMap params, HttpServletResponse response) throws Exception {

		String id = "123";
		String token = jwtService.create("id",id,"hi");
		response.setHeader("Authorization", token);

	}
	

}
