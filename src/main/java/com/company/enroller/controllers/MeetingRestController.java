package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.*;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;
    @Autowired
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings()	 {
        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable("id") Long id) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {
        Meeting fMeeting = meetingService.findById(meeting.getId());
        if (fMeeting == null) {
            meetingService.save(meeting);

            Map<String, Long> body = new HashMap<>();
            body.put("id", meeting.getId());

            return new ResponseEntity<Map>(body,HttpStatus.CREATED);
        } else  {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeMeeting(@PathVariable("id") Long id) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        meetingService.delete(meeting);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateMeeting(@PathVariable("id") Long id, @RequestBody Meeting meeting) {
        Meeting fMeeting = meetingService.findById(id);

        if (fMeeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        if(fMeeting.getId() != meeting.getId()) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }

        meetingService.update(meeting);
        return new ResponseEntity(HttpStatus.OK);

    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.POST)
    public ResponseEntity<?> registerParticipants(@PathVariable("id") Long id, @RequestBody Participant participant) {
        Participant fParticipant = participantService.findByLogin(participant.getLogin());
        if(fParticipant == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Meeting meeting = meetingService.findById(id);
        if(meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Participant f2Participant = meetingService.getParticipant(meeting, participant);
        if(f2Participant != null) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }

        meetingService.addParticipant(meeting, participant);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipants(@PathVariable("id") Long id) {
        Meeting meeting = meetingService.findById(id);
        if(meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Collection<Participant> participants = meetingService.getParticipants(meeting);
        return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants/{login}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeParticipants(@PathVariable("id") Long id, @PathVariable("login") String login) {
        Meeting meeting = meetingService.findById(id);
        if(meeting == null) {
            System.out.println("aaa");
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        }

        Participant participant = participantService.findByLogin(login);
        if(participant == null) {
            System.out.println("bbbb");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Participant fParticipant = meetingService.getParticipant(meeting, participant);
        if(fParticipant == null) {
            System.out.println("ccc");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        meetingService.removeParticipant(meeting, participant);
        return new ResponseEntity(HttpStatus.OK);
    }


}
