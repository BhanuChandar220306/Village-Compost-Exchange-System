package com.village.compost.service.impl;

import com.village.compost.dto.response.RewardResponse;
import com.village.compost.entity.User;
import com.village.compost.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {

    @Override
    public RewardResponse getMyRewards() {

        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return RewardResponse.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .rewardPoints(user.getRewardPoints())
                .build();
    }
}