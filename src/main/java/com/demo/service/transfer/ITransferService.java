package com.demo.service.transfer;

import com.demo.model.Customer;
import com.demo.model.Transfer;
import com.demo.service.IGeneralService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ITransferService extends IGeneralService<Transfer> {
    List<Transfer> findAll();

    List<Transfer> findAllBySender(Customer sender);

    BigDecimal getAllFeesAmount();

    Optional<Transfer> findOne(Long id);

    Transfer save(Transfer transfer);
}
