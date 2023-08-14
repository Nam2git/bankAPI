package com.ws.tla.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import com.ws.tla.enums.CardType;
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
@Table(name = "CARD")
public class Card implements Serializable {

    @Id
    @Column(name = "CARD_ID")
    @GenericGenerator(strategy = "increment", name = "CARD_GENERATOR")
    @GeneratedValue(generator = "CARD_GENERATOR")
    private Long cardId;

    @Column(name = "CARD_TYPE")
    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @Column(name = "CARD_NUMBER")
    private String cardNumber;

    @NotNull
    @Column(name = "CVV_NUMBER")
    private Long cvvNumber;

    @Column(name = "TOTAL_LIMIT")
    private Long totalLimit;

    @Column(name = "CREATED_DATE")
    @CreationTimestamp
    private Timestamp createdDate;

    @Column(name = "LAST_MODIFIED_DATE")
    @CreationTimestamp
    private Timestamp lastModifiedDate;

    @OneToOne(mappedBy = "card")
    @JsonIgnore
    private Account account;
}
