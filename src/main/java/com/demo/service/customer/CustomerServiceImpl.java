package com.demo.service.customer;

import com.demo.model.Customer;
import com.demo.model.Deposit;
import com.demo.model.Withdraw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demo.repository.CustomerRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private CustomerRepository customerRepository;




    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Withdraw withdraw(Withdraw withdraw) {
        Customer customer = withdraw.getCustomer();
        BigDecimal transactionAmount = withdraw.getTransactionAmount();
        BigDecimal balance = customer.getBalance();
        customerRepository.decrementBalance(customer.getId(), transactionAmount);

        withdraw.setId(null);
        withdraw.setCreatedAt(new Date());

        customer = customerRepository.findById(customer.getId()).get();
        withdraw.setCustomer(customer);

        return withdraw;
    }

    @Override
    public List<Customer> findAllByDeleteIsFalse() {
        return customerRepository.findAllByDeletedIsFalse();
    }

    @Override
    public List<Customer> findAllByIdNot(Long id) {
        return customerRepository.findAllByIdNot(id);
    }

    @Override
    public List<Customer> findAllByIdNotAndDeleteIsFalse(Long id) {
        return customerRepository.findAllByIdNotAndDeletedIsFalse(id);
    }

    @Override
    public void incrementBalance(Long customerId, BigDecimal transactionAmount) {
        customerRepository.incrementBalance(customerId, transactionAmount);
    }

    @Override
    public Deposit deposit(Deposit deposit) {
        Customer customer = deposit.getCustomer();
        BigDecimal transactionAmount = deposit.getTransactionAmount();
        customerRepository.incrementBalance(customer.getId(), transactionAmount);

        deposit.setTransactionAmount(null);
        deposit.setCreatedAt(new Date());

        customer = customerRepository.findById(customer.getId()).get();
        deposit.setCustomer(customer);

        return deposit;
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer delete(Customer customer) {
        customerRepository.delete(customer);
        return customer;
    }
}

























