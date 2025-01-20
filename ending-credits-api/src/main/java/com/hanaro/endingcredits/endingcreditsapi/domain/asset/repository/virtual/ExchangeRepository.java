package com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.virtual;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.virtual.ExchangeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExchangeRepository extends JpaRepository<ExchangeEntity, UUID> {
}
