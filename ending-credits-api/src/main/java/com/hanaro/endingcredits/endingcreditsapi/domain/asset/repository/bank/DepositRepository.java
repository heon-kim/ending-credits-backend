package com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.bank;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.bank.DepositEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositRepository extends JpaRepository<DepositEntity, Long> {
    
}
