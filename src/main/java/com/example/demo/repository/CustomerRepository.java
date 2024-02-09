package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	/**
	 * メールアドレス検索
	 * SELECT * FROM customers WHERE email = ?
	 */
	Customer findByEmail(String email);

}
