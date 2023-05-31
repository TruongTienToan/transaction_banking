package com.demo.repository;

import com.demo.model.Customer;
import com.demo.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    @Query("SELECT SUM(tr.feesAmount) FROM Transfer as tr")
    BigDecimal getAllFeesAmount();

    List<Transfer> findAllBySender(Customer sender);
}
