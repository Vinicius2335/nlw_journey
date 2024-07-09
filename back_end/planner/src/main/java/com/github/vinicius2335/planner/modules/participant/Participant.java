package com.github.vinicius2335.planner.modules.participant;

import com.github.vinicius2335.planner.modules.trip.Trip;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "participants")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(name = "is_confirmed", nullable = false)
    private boolean isConfirmed;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    public Participant (String email, Trip trip){
        this.email = email;
        this.name = "";
        this.isConfirmed = false;
        this.trip = trip;
    }
}
