package br.com.compass.pb.shop.dao;

import br.com.compass.pb.shop.model.Address;
import br.com.compass.pb.shop.util.JPAUtil;

import javax.persistence.EntityManager;
import java.io.Closeable;

public class AddressDao implements Closeable {

    private final EntityManager em;

    public AddressDao() {
        this.em = JPAUtil.getEntityManager();
    }

    public void save(Address address) {
        this.em.getTransaction().begin();
        this.em.persist(address);
        this.em.getTransaction().commit();
    }

    @Override
    public void close() {
        this.em.close();
    }
}
