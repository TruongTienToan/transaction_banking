package com.demo.service.withdraw;

import com.demo.model.Customer;
import com.demo.model.Withdraw;

import java.util.List;
import java.util.Optional;

public class WithdrawServiceImpl implements IWithdrawService{
    @Override
    public List<Withdraw> findAll() {
        return null;
    }

    @Override
    public Optional<Withdraw> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Withdraw save(Withdraw withdraw) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Customer delete(Withdraw withdraw) {
        return null;
    }

    @Override
    public Withdraw withdraw(Withdraw withdraw) {
        return null;
    }
}
