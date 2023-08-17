package com.ws.tla.repository;

import com.ws.tla.entity.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("Select custPrfl, accts from CustomerProfile custPrfl inner join Account accts on custPrfl.custProfileId = accts.customerProfile.custProfileId")
    List<Object[]> findAllCustsPrflsAcctsAndCardInfo();

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("Select custPrfl, accts from CustomerProfile custPrfl inner join Account accts on custPrfl.custProfileId = accts.customerProfile.custProfileId where custPrfl.custProfileId=?1")
    List<Object[]> findCustPrflAcctAndCardInfo(Long custPrflId);
}
