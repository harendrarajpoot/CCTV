package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.modal.Bill;

public interface BillRepository extends JpaRepository<Bill, Integer> {

}
