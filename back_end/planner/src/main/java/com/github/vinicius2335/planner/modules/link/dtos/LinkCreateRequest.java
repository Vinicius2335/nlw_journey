package com.github.vinicius2335.planner.modules.link.dtos;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record LinkCreateRequest(
        @NotBlank(message = "Link Title: cannot be null or empty")
        String title,

        @URL(message = "Link URL: must be a valid URL. Example valid url: https://www.youtube.com")
        @NotBlank(message = "Link URL: cannot be null or empty")
        String url
) {
}
