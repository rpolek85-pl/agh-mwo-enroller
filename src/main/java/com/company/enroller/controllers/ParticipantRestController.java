package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipants(
            @RequestParam(value = "sortBy", defaultValue = "login") String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = "DESC") String sortOrder,
            @RequestParam(value = "key", defaultValue ="") String key)	 {

        if(!(sortOrder.equals("ASC") || sortOrder.equals("DESC"))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(!(sortBy.equals("login") || sortBy.equals("password"))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Collection<Participant> participants = participantService.getAll(sortBy, sortOrder, key);
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
        Participant participant = participantService.findByLogin(login);
        if (participant == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Participant>(participant, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
        Participant fParticipant = participantService.findByLogin(participant.getLogin());
        if (fParticipant == null) {
            participantService.save(participant);
            return new ResponseEntity(HttpStatus.CREATED);
        } else  {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeParticipant(@PathVariable("id") String login) {
        Participant participant = participantService.findByLogin(login);
        if (participant == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        participantService.delete(participant);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateParticipant(@PathVariable("id") String login, @RequestBody Participant participant) {
        Participant fParticipant = participantService.findByLogin(login);

        if (fParticipant == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        if(fParticipant.getLogin().equals(participant.getLogin())) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }

        participantService.update(participant);
        return new ResponseEntity(HttpStatus.OK);

    }



}
