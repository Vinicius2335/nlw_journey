package com.github.vinicius2335.planner.modules.activity.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivitiesDTO{
    private LocalDateTime date;
    private List<ActivityDetailsDTO> activities;
}
