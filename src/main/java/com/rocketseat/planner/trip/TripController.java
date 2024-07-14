package com.rocketseat.planner.trip;

import com.rocketseat.planner.activity.ActivityData;
import com.rocketseat.planner.activity.ActivityRequestPayload;
import com.rocketseat.planner.activity.ActivityResponse;
import com.rocketseat.planner.activity.ActivityService;
import com.rocketseat.planner.link.LinkData;
import com.rocketseat.planner.link.LinkRequestPayload;
import com.rocketseat.planner.link.LinkResponse;
import com.rocketseat.planner.link.LinkService;
import com.rocketseat.planner.participants.ParticipantCreateResponse;
import com.rocketseat.planner.participants.ParticipantData;
import com.rocketseat.planner.participants.ParticipantRequestPayload;
import com.rocketseat.planner.participants.ParticipantService;
import com.rocketseat.planner.participants.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private LinkService linkService;

    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload) {
        Trip newTrip = new Trip(payload);
        this.tripRepository.save(newTrip);
        this.participantService.registerParticipantsToEvent(payload.emails_to_invite(), newTrip);

        return ResponseEntity.ok(new TripCreateResponse(newTrip.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails (@PathVariable("id") UUID id ) {
        Optional<Trip> trip =this.tripRepository.findById(id);

        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTripDetails (@PathVariable("id") UUID id, @RequestBody TripRequestPayload payload) {
        Optional<Trip> trip =this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setDestination(payload.destination());

            this.tripRepository.save(rawTrip);

            return ResponseEntity.ok(rawTrip);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/confirmed")
    public ResponseEntity<Trip> confirmTrip (@PathVariable("id") UUID id ) {
        Optional<Trip> trip =this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setConfirmed(true);

            this.participantService.triggerConfirmationEmailToParticipants(rawTrip.getId());
            this.tripRepository.save(rawTrip);

            return ResponseEntity.ok(rawTrip);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipantToTrip (@PathVariable("id") UUID id, @RequestBody ParticipantRequestPayload payload) {
        Optional<Trip> trip =this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            ParticipantCreateResponse participantCreateResponse = this.participantService.registerParticipantToEvent(payload.email(), rawTrip);

            if (rawTrip.getConfirmed()) {
                this.participantService.triggerConfirmationEmailToParticipant(payload.email());
            }

            return ResponseEntity.ok(participantCreateResponse);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{tripId}/participants")
    public ResponseEntity<List<ParticipantData>> getAllParticipantOfTrip (@PathVariable("tripId") UUID tripId) {
        Optional<Trip> trip =this.tripRepository.findById(tripId);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            List<ParticipantData> allParticipants = this.participantService.getAllParticipants(tripId);

            return ResponseEntity.ok(allParticipants);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{tripId}/activities")
    public ResponseEntity<ActivityResponse> registerActity (@PathVariable("tripId") UUID id, @RequestBody ActivityRequestPayload payload) {
        Optional<Trip> trip =this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            ActivityResponse activityResponse = this.activityService.registerActivity(payload, rawTrip);

            return ResponseEntity.ok(activityResponse);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityData>> getActivities (@PathVariable("id") UUID id ) {
        Optional<Trip> trip =this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            List<ActivityData> activityResponse = this.activityService.getAllActivies(rawTrip.getId());

            return ResponseEntity.ok(activityResponse);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{tripId}/links")
    public ResponseEntity<LinkResponse> registerLink (@PathVariable("tripId") UUID id, @RequestBody LinkRequestPayload payload) {
        Optional<Trip> trip =this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            LinkResponse linkResponse = this.linkService.registerLink(payload, rawTrip);

            return ResponseEntity.ok(linkResponse);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{tripId}/links")
    public ResponseEntity<List<LinkData>> registerLink (@PathVariable("tripId") UUID id) {
        Optional<Trip> trip =this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            List<LinkData> linkResponse = this.linkService.getAllLinksFromTripId(rawTrip.getId());

            return ResponseEntity.ok(linkResponse);
        }

        return ResponseEntity.notFound().build();
    }

}
