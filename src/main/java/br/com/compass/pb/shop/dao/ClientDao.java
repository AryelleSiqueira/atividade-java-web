package br.com.compass.pb.shop.dao;

import br.com.compass.pb.shop.model.Client;
import br.com.compass.pb.shop.util.JPAUtil;

import javax.persistence.EntityManager;
import java.io.Closeable;

public class ClientDao implements Closeable {

    private final EntityManager em;

    public ClientDao() {
        this.em = JPAUtil.getEntityManager();
    }

    public void save(Client client) {
        this.em.getTransaction().begin();
        this.em.persist(client);
        this.em.getTransaction().commit();
    }

    public Client find(Long id) {
        String jpql = "SELECT c FROM Client c WHERE c.id = " + id;

        return this.em.createQuery(jpql, Client.class).getSingleResult();
    }

    public Client findByEmail(String email) {
        String jpql = "SELECT c FROM Client c WHERE c.email = '" + email + "'";

        return this.em.createQuery(jpql, Client.class).getSingleResult();
    }

    public void close() {
        this.em.close();
    }
}
