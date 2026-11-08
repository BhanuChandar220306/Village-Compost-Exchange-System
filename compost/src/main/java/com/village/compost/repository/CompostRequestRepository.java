package com.village.compost.repository;

import com.village.compost.entity.CompostRequest;
import com.village.compost.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompostRequestRepository
        extends JpaRepository<CompostRequest, Long> {

    List<CompostRequest> findByFarmer(User farmer);

    List<CompostRequest> findByStatus(
            com.village.compost.enums.RequestStatus status
    );
}