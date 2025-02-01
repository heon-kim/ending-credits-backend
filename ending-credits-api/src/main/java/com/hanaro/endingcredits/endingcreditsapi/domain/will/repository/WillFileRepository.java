package com.hanaro.endingcredits.endingcreditsapi.domain.will.repository;

import com.hanaro.endingcredits.endingcreditsapi.domain.will.entities.WillFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WillFileRepository extends JpaRepository<WillFileEntity, UUID> {}
