package com.demo.service.deposit;

import com.demo.model.Customer;
import com.demo.model.Deposit;
import com.demo.model.Withdraw;

import java.util.List;
import java.util.Optional;

public class DepositServiceImpl implements IDepositService {

    @Override
    public List<Deposit> findAll() {
        return null;
    }

    @Override
    public Optional<Deposit> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Deposit save(Deposit deposit) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Customer delete(Deposit deposit) {
        return null;
    }

    @Override
    public Withdraw withdraw(Withdraw withdraw) {
        return null;
    }
}
