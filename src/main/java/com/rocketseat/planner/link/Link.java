package com.rocketseat.planner.link;


import com.rocketseat.planner.trip.Trip;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Table(name="links")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "url", nullable = false)
    private String url;

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    @ManyToOne
    @JoinColumn(name="trip_id", nullable = false)
    private Trip trip;

    public Link (String title, String url, Trip trip) {
        this.title = title;
        this.url = url;
        this.trip = trip;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
