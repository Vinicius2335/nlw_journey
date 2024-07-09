package com.github.vinicius2335.planner.modules.trip;

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
@Data
@Builder
@Entity
@Table(name = "trips")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String destination;

    @Column(name = "starts_at", nullable = false)
    private LocalDateTime startsAt;

    @Column(name = "ends_at", nullable = false)
    private LocalDateTime endsAt;

    @Column(name = "is_confirmed", nullable = false)
    private boolean isConfirmed;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(name = "owner_email", nullable = false)
    private String ownerEmail;

    public Trip(TripPayloadRequest payload){
        this.destination = payload.destination();
        this.startsAt = LocalDateTime.parse(payload.startsAt(), DateTimeFormatter.ISO_DATE_TIME);
        this.endsAt = LocalDateTime.parse(payload.endsAt(), DateTimeFormatter.ISO_DATE_TIME);
        this.ownerName = payload.ownerName();
        this.ownerEmail = payload.ownerEmail();
        this.isConfirmed = false;
    }

    public void updateTrip(TripPayloadRequest payload){
        this.destination = payload.destination();
        this.startsAt = LocalDateTime.parse(payload.startsAt(), DateTimeFormatter.ISO_DATE_TIME);
        this.endsAt = LocalDateTime.parse(payload.endsAt(), DateTimeFormatter.ISO_DATE_TIME);
    }
}
