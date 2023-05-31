package com.demo.controller;

import com.demo.model.Customer;
import com.demo.model.Deposit;
import com.demo.model.Transfer;
import com.demo.model.Withdraw;
import com.demo.service.transfer.ITransferService;
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
    @Autowired
    private ITransferService transferService;


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

    @GetMapping("/transfer/{senderId}")
    public String showTransferPage(@PathVariable Long senderId, Model model) {

        Optional<Customer> senderOptional = customerService.findById(senderId);

        if (!senderOptional.isPresent()) {
            model.addAttribute("error", true);
            model.addAttribute("messages", "Sender not found");
        }
        else {
            Customer sender = senderOptional.get();

            Transfer transfer = new Transfer();
            transfer.setSender(sender);
            model.addAttribute("transfer", transfer);
            List<Customer> recipients =customerService.findAllByIdNotAndDeleteIsFalse(senderId);
            model.addAttribute("recipients", recipients);
        }

        return "/customers/transfer";
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

    @PostMapping("/transfer/{senderId}")
    public String doTransfer(@PathVariable Long senderId, @Validated @ModelAttribute Transfer transfer, BindingResult bindingResult, Model model) {


        new Transfer().validate(transfer, bindingResult);

        Optional<Customer> senderOptional = customerService.findById(senderId);
        List<Customer> recipients = customerService.findAllByIdNotAndDeleteIsFalse(senderId);

        model.addAttribute("recipients", recipients);
        model.addAttribute("transfer", transfer);

        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("error", true);

            return "/customers/transfer";
        }

        if (!senderOptional.isPresent()) {
            model.addAttribute("error", true);
            model.addAttribute("messages", "Sender not valid");

            return "/customers/transfer";
        }

        Long recipientId = transfer.getRecipient().getId();

        Optional<Customer> recipientOptional = customerService.findById(recipientId);

        if (!recipientOptional.isPresent()) {
            model.addAttribute("error", true);
            model.addAttribute("messages", "Recipient not valid");

            return "/customers/transfer";
        }

        if (senderId.equals(recipientId)) {
            model.addAttribute("error", true);
            model.addAttribute("messages", "Sender ID must be different from Recipient ID");

            return "/customers/transfer";
        }

        BigDecimal senderCurrentBalance = senderOptional.get().getBalance();

        BigDecimal transferAmountStr = transfer.getTransferAmount();

        BigDecimal transferAmount = BigDecimal.valueOf(Long.parseLong(String.valueOf(transferAmountStr)));
        long fees = 10L;
        BigDecimal feesAmount = transferAmount.multiply(BigDecimal.valueOf(fees)).divide(BigDecimal.valueOf(100));
        BigDecimal transactionAmount = transferAmount.add(feesAmount);

        if (senderCurrentBalance.compareTo(transactionAmount) < 0) {
            model.addAttribute("error", true);
            model.addAttribute("messages", "Sender balance not enough to transfer");

            return "/customers/transfer";
        }

//        Transfer transfer = new Transfer();
        transfer.setSender(senderOptional.get());
        transfer.setRecipient(recipientOptional.get());
        transfer.setTransferAmount(transferAmount);
        transfer.setFees(fees);
        transfer.setFeesAmount(feesAmount);
        transfer.setTransactionAmount(transactionAmount);

        customerService.transfer(transfer);

        transfer.setSender(transfer.getSender());
        transfer.setTransferAmount(transferAmount);
        transfer.setTransactionAmount(transferAmount);

        model.addAttribute("transferDTO", transfer);

        model.addAttribute("success", true);
        model.addAttribute("messages", "Transfer success");

        return "/customers/transfer";
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