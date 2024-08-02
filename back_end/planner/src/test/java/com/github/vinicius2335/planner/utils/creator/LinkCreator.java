package com.github.vinicius2335.planner.utils.creator;

import com.github.vinicius2335.planner.modules.link.Link;
import com.github.vinicius2335.planner.modules.link.dtos.LinkCreateRequest;
import com.github.vinicius2335.planner.modules.link.dtos.LinkDetailsDTO;
import com.github.vinicius2335.planner.modules.link.dtos.LinkIdResponse;
import com.github.vinicius2335.planner.modules.link.dtos.LinkListResponse;
import com.github.vinicius2335.planner.modules.trip.Trip;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.UUID;

@UtilityClass
public final class LinkCreator {

    public static Link mockLink(Trip trip){
        return Link.builder()
                .title("Google")
                .url("https://www.google.com.br")
                .trip(trip)
                .build();
    }

    public static LinkCreateRequest mockLinkCreateRequest(String title, String url){
        return new LinkCreateRequest(title, url);
    }

    public static LinkCreateRequest mockInvalidLinkCreateRequest(){
        return new LinkCreateRequest(null, null);
    }

    public static LinkIdResponse mockLinkIdResponse(UUID linkId){
        return new LinkIdResponse(linkId);
    }

    public static LinkDetailsDTO mockLinkDetailsDTO(Link link){
        return new LinkDetailsDTO(
                link.getId(),
                link.getTitle(),
                link.getUrl()
        );
    }

    public static LinkListResponse mockLinkListResponse(Link link){
        return new LinkListResponse(
                List.of(mockLinkDetailsDTO(link))
        );
    }
}
