package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AccountController {
	
	// 会員登録画面表示
	@GetMapping("/account")
	public String create() {
		// 画面遷移
		return "accountForm";
	}
	
}
