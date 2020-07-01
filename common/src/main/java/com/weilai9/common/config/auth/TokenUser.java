package com.weilai9.common.config.auth;

import com.weilai9.dao.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenUser {
    private Long customerId;
    private String customerName;
    private List<String> authorities;
    private Integer status;


    public TokenUser(Long customerId, String customerName) {
        this.customerId = customerId;
        this.customerName = customerName;
    }
    public TokenUser(Customer customer) {
        this.customerId = customer.getCustomerId();
        this.customerName = customer.getCustomerName();
        this.status = customer.getCustomerStatus();
    }
}
