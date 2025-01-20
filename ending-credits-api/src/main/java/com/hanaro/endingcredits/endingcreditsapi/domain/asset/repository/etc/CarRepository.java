package com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.etc;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, UUID> {
}
