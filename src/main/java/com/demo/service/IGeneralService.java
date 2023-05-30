package com.demo.service;

import com.demo.model.Customer;
import com.demo.model.Withdraw;

import java.util.List;
import java.util.Optional;

public interface IGeneralService<T> {
    List<T> findAll();
    Optional<T> findById(Long id);
    T save(T t);
    void deleteById(Long id);
    Customer delete(T t);
    Withdraw withdraw(Withdraw withdraw);
}
