package com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.bank;


import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.bank.FundEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FundRepository extends JpaRepository<FundEntity, UUID> {
}
