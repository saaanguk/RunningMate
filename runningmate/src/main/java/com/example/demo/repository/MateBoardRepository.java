package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.MateBoard;


public interface MateBoardRepository extends JpaRepository<MateBoard, Long>{

	Page<MateBoard> findByInvisable(PageRequest of, int i);

	Page<MateBoard> findByInvisableAndLocal(PageRequest of, int i, String local);

	Long countByLocal(String local);

	
}
