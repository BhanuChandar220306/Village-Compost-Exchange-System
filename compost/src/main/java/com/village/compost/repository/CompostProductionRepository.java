package com.village.compost.repository;

import com.village.compost.entity.CompostProduction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompostProductionRepository extends JpaRepository<CompostProduction, Long> {
}