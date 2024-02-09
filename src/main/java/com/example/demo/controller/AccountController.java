package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Customer;
import com.example.demo.repository.CustomerRepository;



@Controller
public class AccountController {
	
	@Autowired
	CustomerRepository customerRepository;
	
	// 会員登録画面表示
	@GetMapping("/account")
	public String create() {
		// 画面遷移
		return "accountForm";
	}
	
	// 会員登録処理
	@PostMapping("/account")
	public String store(
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "address", defaultValue = "") String address,
			@RequestParam(name = "tel", defaultValue = "") String tel,
			@RequestParam(name = "email", defaultValue = "") String email,
			@RequestParam(name = "password", defaultValue = "") String password,
			Model model) {
		// リクエストパラメータの入力値チェック
		List<String> errors = new ArrayList<>();
		if (name.isEmpty()) {
			// 名前の入力値チェック：必須入力エラー
			errors.add("名前は必須です。");
		}
		if (address.isEmpty()) {
			// 住所の入力値チェック：必須入力エラー
			errors.add("住所は必須です。");
		}
		if (tel.isEmpty()) {
			// 電話番号の入力値チェック：必須入力エラー
			errors.add("電話番号は必須です。");
		}
		if (email.isEmpty()) {
			// メールアドレスの入力値チェック：必須入力エラー
		} else {
			if (customerRepository.findByEmail(email) instanceof Customer) {
				// 重複エラー
				errors.add("登録済のメールアドレスです。");
			}
		}
		if (password.isEmpty()) {
			// パスワードの入力値チェック：必須入力エラー
			errors.add("パスワードは必須です。");
		}
		
		// 入力値チェックのエラーの有無で処理を分岐
		if (errors.size() > 0) {
			// 入力値エラーがある場合：エラーリストとリクエストパラメータをスコープに登録
			model.addAttribute("errorList", errors);
			model.addAttribute("name", name);
			model.addAttribute("address", address);
			model.addAttribute("tel", tel);
			model.addAttribute("email", email);
			// 画面遷移
			return "accountForm";
		}
		
		// リクエストパラメータをもとに登録対象の顧客をインスタンス化
		Customer customer = new Customer(name, address, tel, email, password);
		// 登録対象顧客のインスタンスを永続化
		customerRepository.save(customer);
		// 画面遷移
		return "redirect:/";
	}
	
}
