package com.village.compost.repository;

import com.village.compost.entity.CompostAllocation;
import com.village.compost.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompostAllocationRepository
        extends JpaRepository<CompostAllocation, Long> {

    List<CompostAllocation> findByFarmer(User farmer);

}