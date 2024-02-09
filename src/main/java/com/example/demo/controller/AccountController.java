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
	public String create(Model model) {
		// 会員のインスタンスをスコープに登録
		model.addAttribute("customer", new Customer());
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
		// リクエストパラメータをもとに登録対象の顧客をインスタンス化
		Customer customer = new Customer(name, address, tel, email, password);
		// リクエストパラメータの妥当性検査
		List<String> errors = this.validate(customer);
		
		// 入力値チェックのエラーの有無で処理を分岐
		String nextURL = "redirect:/";
		if (errors.size() > 0) {
			// 入力値エラーがある場合：エラーリストとリクエストパラメータをスコープに登録
			model.addAttribute("errorList", errors);
			model.addAttribute("customer", customer);
			// 遷移先の設定
			nextURL = "accountForm";
		} else {
			// 入力値エラーがない場合：登録対象顧客のインスタンスを永続化
			customerRepository.save(customer);
		}
		
		// 画面遷移
		return nextURL;

	}
	
	/**
	 * 入力値の妥当性を検査する
	 * @param customer 検査対象の入力値が設定された顧客インスタンス 
	 * @return エラーリスト
	 */
	private List<String> validate(Customer customer) {
		final String ERR_MESSAGE_REQUIRED = "@fieldは必須です。";
		final String ERR_MESSAGE_DUPLICATED = "登録済みのメールアドレスです。";
		
		List<String> errors = new ArrayList<String>();
		if (!isRequired(customer.getName())) {
			errors.add(ERR_MESSAGE_REQUIRED.replace("@field", "名前"));
		}
		if (!isRequired(customer.getAddress())) {
			errors.add(ERR_MESSAGE_REQUIRED.replace("@field", "住所"));
		}
		if (!isRequired(customer.getTel())) {
			errors.add(ERR_MESSAGE_REQUIRED.replace("@field", "電話番号"));
		}
		if (!isRequired(customer.getPassword())) {
			errors.add(ERR_MESSAGE_REQUIRED.replace("@field", "パスワード"));
		}
		if (!isRequired(customer.getEmail())) {
			errors.add(ERR_MESSAGE_REQUIRED.replace("@field", "メールアドレス"));
		} else if (isDuplicated(customer.getEmail())) {
			errors.add(ERR_MESSAGE_DUPLICATED);
		}
		return errors;
	}
	
	/**
	 * 必須入力チェック
	 * @param target 検査対象文字列
	 * @return 空文字列またはnullの場合はfalse、それ以外はtrue
	 */
	private boolean isRequired(String target) {
		return !(target.isEmpty());
	}
	
	/**
	 * メールアドレスの重複チェック
	 * @param target 検査対象メールアドレス
	 * @return すでに登録されたメールアドレスである場合はtrue、それ以外はfalse
	 */
	private boolean isDuplicated(String target) {
		return (customerRepository.findByEmail(target) instanceof Customer);
	}
	
}
