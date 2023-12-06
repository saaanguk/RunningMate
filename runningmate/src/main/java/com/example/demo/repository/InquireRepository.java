package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Inquire;

public interface InquireRepository extends JpaRepository<Inquire, Long> {

	Page<Inquire> findByUseremailOrderByIdxDesc(PageRequest of, String currentUsername);

	Long countByUseremail(String myname);

}
