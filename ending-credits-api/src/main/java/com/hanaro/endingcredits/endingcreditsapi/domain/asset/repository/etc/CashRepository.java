package com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.etc;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc.CashEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.enums.CurrencyCodeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashRepository extends JpaRepository<CashEntity, CurrencyCodeType> {
}
