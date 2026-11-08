package com.village.compost.repository;

import com.village.compost.entity.PickupSchedule;
import com.village.compost.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PickupScheduleRepository
        extends JpaRepository<PickupSchedule, Long> {

    List<PickupSchedule> findByCollector(User collector);

    List<PickupSchedule> findByPickupDate(LocalDate pickupDate);
}