package com.github.vinicius2335.planner.modules.activity;

import com.github.vinicius2335.planner.modules.activity.dtos.ActivityCreateRequest;
import com.github.vinicius2335.planner.modules.trip.Trip;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(name = "occurs_at", nullable = false)
    private LocalDateTime occursAt;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    public Activity(ActivityCreateRequest request, Trip trip) {
        this.trip = trip;
        this.title = request.title();
        this.occursAt = LocalDateTime.parse(request.occursAt(), DateTimeFormatter.ISO_DATE_TIME);
    }
}
