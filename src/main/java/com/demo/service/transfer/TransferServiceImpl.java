package com.demo.service.transfer;


import com.demo.model.Customer;
import com.demo.model.Transfer;
import com.demo.model.Withdraw;
import com.demo.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransferServiceImpl implements ITransferService {

    @Autowired
    private TransferRepository transferRepository;

    @Override
    public Optional<Transfer> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Customer delete(Transfer transfer) {
        return null;
    }

    @Override
    public Withdraw withdraw(Withdraw withdraw) {
        return null;
    }

    @Override
    public List<Transfer> findAll() {
        return null;
    }

    @Override
    public List<Transfer> findAllBySender(Customer sender) {
        return null;
    }

    @Override
    public BigDecimal getAllFeesAmount() {
        return null;
    }

    @Override
    public Optional<Transfer> findOne(Long id) {
        return Optional.empty();
    }

    @Override
    public Transfer save(Transfer transfer) {
        return null;
    }
}
