package com.oa.homework.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
@AllArgsConstructor
public class RewardsSummary {
    private Map<LocalDate, Integer> monthlyPoints;
    private int totalPoints;
}
