package com.bank.customer.domain;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Generated
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CustomerDom {
    String name;
    String gender;
    String identification;
    String address;
    String phone;
    String password;
    Boolean isActive;
    String customerId;
}
