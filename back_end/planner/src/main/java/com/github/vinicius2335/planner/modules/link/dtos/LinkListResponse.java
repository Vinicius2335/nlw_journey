package com.github.vinicius2335.planner.modules.link.dtos;

import java.util.List;

public record LinkListResponse(
        List<LinkDetailsDTO> links
) {
}
