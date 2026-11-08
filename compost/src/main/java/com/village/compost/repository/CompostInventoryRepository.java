package com.village.compost.repository;

import com.village.compost.entity.CompostInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompostInventoryRepository
        extends JpaRepository<CompostInventory, Long> {
}