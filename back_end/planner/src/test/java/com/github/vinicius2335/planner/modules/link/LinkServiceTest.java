package com.github.vinicius2335.planner.modules.link;

import com.github.vinicius2335.planner.modules.link.dtos.LinkCreateRequest;
import com.github.vinicius2335.planner.modules.link.dtos.LinkIdResponse;
import com.github.vinicius2335.planner.modules.link.dtos.LinkListResponse;
import com.github.vinicius2335.planner.modules.trip.Trip;
import com.github.vinicius2335.planner.utils.creator.LinkCreator;
import com.github.vinicius2335.planner.utils.creator.TripCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Teste Unitário para LinkService")
class LinkServiceTest {

    @InjectMocks
    private LinkService underTest;

    @Mock
    private LinkRepository linkRepository;

    private Link link;
    private Trip trip;

    @BeforeEach
    void setUp() {
        trip = TripCreator.mockTrip();
        trip.setId(UUID.randomUUID());
        link = LinkCreator.mockLink(trip);
        link.setId(UUID.randomUUID());

        when(linkRepository.save(any(Link.class)))
                .thenReturn(link);

        when(linkRepository.findAllByTripId(any(UUID.class)))
                .thenReturn(List.of(link));
    }

    @Test
    @DisplayName("Resgistra um link")
    void registerLink_whenSuccessfully() {
        // given
        LinkCreateRequest request = LinkCreator.mockLinkCreateRequest(link.getTitle(), link.getUrl());
        // when
        assertThatCode(() -> underTest.registerLink(request, trip))
                .doesNotThrowAnyException();
        // then
        verify(linkRepository, times(1)).save(any(Link.class));
    }

    @Test
    @DisplayName("Recupera um lista de links relacionados a uma viagem")
    void getAllLinksByTripId_returnLinkList_whenSuccessfully() {
        // when
        LinkListResponse actual = underTest.getAllLinksByTripId(trip.getId());
        // then
        verify(linkRepository, times(1)).findAllByTripId(any(UUID.class));
        LinkListResponse expected = LinkCreator.mockLinkListResponse(link);

        assertThat(actual)
                .isNotNull()
                .isEqualTo(expected);
    }
}