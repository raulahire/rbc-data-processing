package com.example.demo.repository;

import com.example.demo.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository  extends JpaRepository<Stock, Integer> {

    List<Stock> findAllByStock(String stock);
}
