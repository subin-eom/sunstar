package com.sunstar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

	@RequestMapping("/RegistrationBuyer")
	public String RegistrationBuyer(Model model) {
		model.addAttribute("contentpage", "registercustomer.jsp");
		return "Registration/register";
	}
	
	@RequestMapping("/registercustomer/info")
	public String registercustomer(Model model) {
		model.addAttribute("contentpage", "registercustomerform.jsp");
		return "Registration/register";
	}
}
