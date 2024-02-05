package com.oa.homework.controller;

import com.oa.homework.model.RewardsSummary;
import com.oa.homework.service.RewardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/rewards")
public class RewardsController {

    private final RewardsService rewardsService;

    @Autowired
    public RewardsController(RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getRewardsForCustomer(@PathVariable Long customerId) {
        RewardsSummary rewards = rewardsService.calculateRewardsForCustomer(customerId);
        return ResponseEntity.ok(rewards);
    }

    @GetMapping("/period/{customerId}")
    public ResponseEntity<?> getRewardsForCustomer(@PathVariable Long customerId, @RequestParam String startMonth, @RequestParam String endMonth) {
        try {
            YearMonth start = YearMonth.parse(startMonth);
            YearMonth end = YearMonth.parse(endMonth);
            RewardsSummary rewards = rewardsService.calculateRewardsForCustomer(customerId, start, end);
            return ResponseEntity.ok(rewards);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format");
        }
    }
}

