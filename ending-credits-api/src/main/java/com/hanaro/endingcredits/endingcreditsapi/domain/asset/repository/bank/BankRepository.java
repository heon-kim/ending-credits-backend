package com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.bank;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.bank.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BankRepository extends JpaRepository<BankEntity, UUID> {
    Optional<BankEntity> findByBankName(String bankName);
}
