package com.company.enroller.persistence;

import java.util.Collection;

import com.company.enroller.model.Participant;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;

@Component("meetingService")
public class MeetingService {

	DatabaseConnector connector;

	public MeetingService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

    public Meeting findById(Long id) {
        String hql = "FROM Meeting WHERE id = :id";
        Query query = connector.getSession().createQuery(hql);
        query.setParameter("id", id);
        return (Meeting) query.uniqueResult();
    }

    public void save(Meeting meeting) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().save(meeting);
        transaction.commit();
    }

    public void delete(Meeting meeting) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().delete(meeting);
        transaction.commit();
    }

    public void update(Meeting meeting) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().merge(meeting);
        transaction.commit();
    }

    public void addParticipant(Meeting meeting, Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        meeting.addParticipant(participant);
        connector.getSession().merge(meeting);
        transaction.commit();
    }

    public Collection<Participant> getParticipants(Meeting meeting) {
        return meeting.getParticipants();
    }

    public Participant getParticipant(Meeting meeting, Participant participant) {
        return meeting.getParticipants()
                .stream()
                .filter(p -> p.getLogin().equals(participant.getLogin()))
                .findFirst()
                .orElse(null);
    }

    public void removeParticipant(Meeting meeting, Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        meeting.getParticipants().remove(participant);
        transaction.commit();
    }
}
