package com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.securities;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.securities.SecuritiesCompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SecuritiesCompanyRepository extends JpaRepository<SecuritiesCompanyEntity, UUID> {
}
