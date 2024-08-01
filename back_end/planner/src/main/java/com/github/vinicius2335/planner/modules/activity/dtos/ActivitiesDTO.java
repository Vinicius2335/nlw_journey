package com.github.vinicius2335.planner.modules.activity.dtos;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ActivitiesDTO{
    private LocalDateTime date;
    private List<ActivityDetailsDTO> activities;
}
