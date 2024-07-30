package com.github.vinicius2335.planner.utils.creator;

import com.github.vinicius2335.planner.modules.link.Link;
import com.github.vinicius2335.planner.modules.trip.Trip;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class LinkCreator {

    public static Link mockLink(Trip trip){
        return Link.builder()
                .title("Google")
                .url("https://www.google.com.br")
                .trip(trip)
                .build();
    }
}
