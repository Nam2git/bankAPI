package com.ws.tla.repository;

import com.ws.tla.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Account set balance = balance+?2 where acctId=?1")
    public void saveAmountByAcctID(long acctID, int balance);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Account set balance = balance-?2 where acctId=?1")
    public void withdrawAmountByAcctID(long acctID, int balance);
}
