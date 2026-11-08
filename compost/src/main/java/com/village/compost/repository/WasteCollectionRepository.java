package com.village.compost.repository;

import com.village.compost.entity.WasteCollection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WasteCollectionRepository extends JpaRepository<WasteCollection, Long> {
}