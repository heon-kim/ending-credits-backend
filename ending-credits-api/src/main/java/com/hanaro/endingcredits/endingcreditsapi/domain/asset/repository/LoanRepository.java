package com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository;


import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, UUID> {
}
