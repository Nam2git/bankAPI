package com.ws.tla.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ACCOUNT")
public class Account implements Serializable {

    @Id
    @Column(name = "ACCT_ID")
    @GenericGenerator(strategy = "increment", name = "ACCOUNT_GENERATOR")
    @GeneratedValue(generator = "ACCOUNT_GENERATOR")
    private Long acctId;

    @Column(name = "ACCT_NUM")
    private String acctNum;

    @Column(name = "BALANCE")
    private Long balance;

    @Column(name = "ACCT_STAT")
    private String acctStat;

    @Column(name = "CREATED_DATE")
    @CreationTimestamp
    private Timestamp createdDate;

    @Column(name = "LAST_MODIFIED_DATE")
    @CreationTimestamp
    private Timestamp lastModifiedDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_CARD_ID", referencedColumnName = "CARD_ID")
    private Card card;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_CUST_PROFILE_ID", referencedColumnName = "CUST_PROFILE_ID")
    private CustomerProfile customerProfile;
}
