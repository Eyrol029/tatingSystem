package com.backend.backend.controller.Billing;

import com.backend.backend.model.Billing.StatementOfAccount;
import com.backend.backend.service.Billing.StatementOfAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/billing/soa")
public class StatementOfAccountController {

    @Autowired
    private StatementOfAccountService service;

    @PostMapping
    public StatementOfAccount add(@RequestBody StatementOfAccount soa) {
        return service.addSoa(soa);
    }

    @GetMapping("/{id}")
    public StatementOfAccount get(@PathVariable Integer id) {
        return service.getSoaById(id);
    }

    @GetMapping
    public List<StatementOfAccount> getAll() {
        return service.getAllSoa();
    }

    @PutMapping
    public StatementOfAccount update(@RequestBody StatementOfAccount soa) {
        return service.updateSoa(soa);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteSoa(id);
        return "Statement of Account deleted successfully.";
    }
}
