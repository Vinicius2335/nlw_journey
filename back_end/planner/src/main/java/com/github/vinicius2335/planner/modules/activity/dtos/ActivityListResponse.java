package com.github.vinicius2335.planner.modules.activity.dtos;

import java.util.List;

public record ActivityListResponse(
        List<ActivitiesDTO> activities
) {
}
