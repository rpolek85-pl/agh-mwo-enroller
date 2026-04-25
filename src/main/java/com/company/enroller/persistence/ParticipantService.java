package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.company.enroller.model.Participant;

@Component("participantService")
public class ParticipantService {

	DatabaseConnector connector;

    @Autowired
    PasswordEncoder passwordEncoder;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Participant> getAll(String  sortBy, String sortOrder, String key) {
        String hql = "FROM Participant WHERE login LIKE :key ORDER BY " + sortBy + " " + sortOrder;
        Query query = connector.getSession().createQuery(hql);
        query.setParameter("key", "%" + key + "%");
		return query.list();
	}

    public Participant findByLogin(String login) {
        String hql = "FROM Participant WHERE login = :login";
        Query query = connector.getSession().createQuery(hql);
        query.setParameter("login", login);
        return (Participant) query.uniqueResult();
    }

    public void save(Participant participant) {
        String hashedPassword = passwordEncoder.encode(participant.getPassword());
        participant.setPassword(hashedPassword);

        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().save(participant);
        transaction.commit();
    }

    public void delete(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().delete(participant);
        transaction.commit();
    }

    public void update(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().merge(participant);
        transaction.commit();
    }




}
