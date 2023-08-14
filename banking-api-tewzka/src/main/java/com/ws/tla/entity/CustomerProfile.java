package com.ws.tla.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "CUST_PROFILE")
public class CustomerProfile implements Serializable {

    @Id
    @Column(name = "CUST_PROFILE_ID")
    @GenericGenerator(strategy = "increment", name = "CUSTOMER_GENERATOR")
    @GeneratedValue(generator = "CUSTOMER_GENERATOR")
    private Long custProfileId;

    @Column(name = "FIRSTNAME")
    private String firstname;

    @Column(name = "LASTNAME")
    private String lastname;

    @Column(name = "CITY")
    private String city;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "CREATED_DATE")
    @CreationTimestamp
    private Timestamp createdDate;

    @Column(name = "LAST_MODIFIED_DATE")
    @CreationTimestamp
    private Timestamp lastModifiedDate;

    @OneToMany(mappedBy = "customerProfile", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Account> accounts;
}
