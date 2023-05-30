package com.demo.controller;

import com.demo.model.Customer;
import com.demo.model.Deposit;
import com.demo.model.Withdraw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.demo.service.customer.ICustomerService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;
//

    //    @Autowired
//    private IWithdrawService withdrawService;
    //
//    @Autowired
//    private ITransferService transferService;


    @GetMapping
    public String showListPage(Model model) {

        List<Customer> customers = customerService.findAllByDeleteIsFalse();

        model.addAttribute("customers", customers);

        return "/customers/list";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        model.addAttribute("customer", new Customer());

        return "/customers/create";
    }

    @GetMapping("/update/{id}")
    public String showUpdatePage(@PathVariable Long id, Model model) {

        Optional<Customer> customerOptional = customerService.findById(id);

        if (!customerOptional.isPresent()) {
            model.addAttribute("error", true);
        }
        else {
            Customer customer = customerOptional.get();
            model.addAttribute("customer", customer);
        }

        return "/customers/update";
    }

    @GetMapping("/deposit/{id}")
    public String showDepositPage(@PathVariable Long id, Model model) {

        Optional<Customer> customerOptional = customerService.findById(id);

        if (!customerOptional.isPresent()) {
            model.addAttribute("error", true);
            model.addAttribute("messages", "Customer not found");
        }
        else {
            Customer customer = customerOptional.get();

            Deposit deposit = new Deposit();
            deposit.setCustomer(customer);

            model.addAttribute("deposit", deposit);
        }

        return "/customers/deposit";
    }


    @GetMapping("/withdraw/{id}")
    public String showWithdrawPage(@PathVariable Long id, Model model) {

        Optional<Customer> customerOptional = customerService.findById(id);

        if (!customerOptional.isPresent()) {
            model.addAttribute("error", true);
            model.addAttribute("messages", "Customer not found");
        }
        else {
            Customer customer = customerOptional.get();

            Withdraw withdraw = new Withdraw();
            withdraw.setCustomer(customer);

            model.addAttribute("withdraw", withdraw);
        }

        return "/customers/withdraw";
    }


    @PostMapping("/create")
    public String doCreate(@ModelAttribute Customer customer, BindingResult bindingResult, Model model) {

        new Customer().validate(customer, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("customer", customer);
            model.addAttribute("error", true);
            return "/customers/create";
        }

        customer.setBalance(BigDecimal.ZERO);
        customerService.save(customer);
        model.addAttribute("success", true);
        model.addAttribute("messages", "Create customer successful");

        return "/customers/create";
    }

    @PostMapping("/update/{id}")
    public String doUpdate(@PathVariable Long id, @ModelAttribute Customer customer, Model model) {

        Optional<Customer> customerOptional = customerService.findById(id);

        if (!customerOptional.isPresent()) {
            model.addAttribute("error", true);
        }
        else {
            customer.setId(id);
            BigDecimal balance = customerOptional.get().getBalance();
            customer.setBalance(balance);
            customerService.save(customer);
            model.addAttribute("customer", customer);
        }
        model.addAttribute("success", true);
        model.addAttribute("messages", "Update customer successful");
        return "/customers/update";
    }

    @PostMapping("/deposit/{customerId}")
    public String doDeposit(@PathVariable Long customerId, @Validated @ModelAttribute Deposit deposit, BindingResult bindingResult, Model model) {

        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("error", true);
            model.addAttribute("deposit", deposit);
            return "/customers/deposit";
        }

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()) {
            model.addAttribute("error", true);
        }
        else {
            Customer customer = customerOptional.get();

            deposit.setCustomer(customer);
            deposit = customerService.deposit(deposit);

            deposit.setTransactionAmount(BigDecimal.ZERO);

            model.addAttribute("deposit", deposit);
        }

        model.addAttribute("success", true);
        model.addAttribute("messages", "Deposit successful");

        return "/customers/deposit";
    }

    @PostMapping("/withdraw/{customerId}")
    public String doWithdraw(@PathVariable Long customerId, @Validated @ModelAttribute Withdraw withdraw, BindingResult bindingResult, Model model) {

        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("error", true);
            model.addAttribute("withdraw", withdraw);
            return "/customers/withdraw";
        }

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()) {
            model.addAttribute("error", true);
        }
        else {
            Customer customer = customerOptional.get();

            withdraw.setCustomer(customer);
            withdraw = customerService.withdraw(withdraw);

            withdraw.setTransactionAmount(BigDecimal.ZERO);

            model.addAttribute("deposit", withdraw);
        }

        model.addAttribute("success", true);
        model.addAttribute("messages", "Withdraw successful");

        return "/customers/withdraw";
    }




    @GetMapping("/delete/{id}")
    public String deletePage(Model model, @PathVariable Long id) {
        Optional<Customer> customerOptional = customerService.findById(id);
        model.addAttribute("customer", customerOptional.get());
        return "/customers/delete";
    }

    @PostMapping("/delete/{id}")
    public String deletePage(Model model, @PathVariable Long id, @ModelAttribute Customer customer) {
        Optional<Customer> customerOptional = customerService.findById(id);

        if (!customerOptional.isPresent()) {
            model.addAttribute("error", true);
        }
        else {
            customer.setId(id);
            customer.setDeleted(true);
            customerService.save(customer);
            model.addAttribute("customer", customer);
        }
        model.addAttribute("success", true);
        model.addAttribute("messages", "Delete customer successful");
        return "redirect:/customers";
    }

}