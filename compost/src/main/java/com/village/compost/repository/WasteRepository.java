package com.village.compost.repository;

import com.village.compost.entity.User;
import com.village.compost.entity.WasteRequest;
import com.village.compost.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WasteRepository extends JpaRepository<WasteRequest, Long> {

    List<WasteRequest> findByHousehold(User user);

    List<WasteRequest> findByStatus(RequestStatus status);

}