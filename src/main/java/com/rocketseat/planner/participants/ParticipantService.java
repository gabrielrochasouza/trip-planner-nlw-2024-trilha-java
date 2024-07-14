package com.rocketseat.planner.participants;

import com.rocketseat.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository repository;

    public void registerParticipantsToEvent(List<String> participantsEventToInvite, Trip trip) {
        System.out.println("Participants Register");
        List<Participant> participants = participantsEventToInvite.stream().map(email -> new Participant(email, trip)).toList();

        this.repository.saveAll(participants);

        System.out.println(participants.get(0).getId());
    }

    public ParticipantCreateResponse registerParticipantToEvent(String email, Trip trip) {
        System.out.println("Participant Register");
        Participant newParticipant = new Participant(email, trip);

        this.repository.save(newParticipant);

        return new ParticipantCreateResponse(newParticipant.getId());
    }

    public void triggerConfirmationEmailToParticipants(UUID tripId) {
        System.out.println("Email was send to participants");
    }

    public void triggerConfirmationEmailToParticipant(String email) {
        System.out.println("Email was send to participant");
    }

    public List<ParticipantData> getAllParticipants (UUID tripId) {
        return this.repository.findByTripId(tripId).stream().map(participant -> new ParticipantData(
                participant.getId(),
                participant.getName(),
                participant.getEmail(),
                participant.getConfirmed()
            )
        ).toList();
    }

}
