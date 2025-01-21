package com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.AssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AssetRepository extends JpaRepository<AssetEntity, UUID> {
}
