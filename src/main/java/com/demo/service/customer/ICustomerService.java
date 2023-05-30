package com.demo.service.customer;

import com.demo.model.Customer;
import com.demo.model.Deposit;
import com.demo.service.IGeneralService;

import java.math.BigDecimal;
import java.util.List;

public interface ICustomerService extends IGeneralService<Customer> {
    List<Customer> findAllByDeleteIsFalse();

    List<Customer> findAllByIdNot(Long id);
    List<Customer> findAllByIdNotAndDeleteIsFalse(Long id);
    void incrementBalance(Long customerId, BigDecimal transactionAmount);
    Deposit deposit(Deposit deposit);
    Customer save(Customer customer);
    Customer delete(Customer customer);
}
