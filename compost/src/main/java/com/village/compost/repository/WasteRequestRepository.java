package com.village.compost.repository;

import com.village.compost.entity.WasteRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WasteRequestRepository extends JpaRepository<WasteRequest, Long> {
}