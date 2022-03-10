package br.com.compass.pb.shop.dao;

import br.com.compass.pb.shop.model.Client;
import br.com.compass.pb.shop.model.Order;
import br.com.compass.pb.shop.util.JPAUtil;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class OrderDao {

    private final EntityManager em;

    public OrderDao() {
        this.em = JPAUtil.getEntityManager();
    }

    public void save(Order order) {
        this.em.getTransaction().begin();
        this.em.persist(order);
        this.em.getTransaction().commit();
    }

    public void update(Order order) {
        this.em.getTransaction().begin();
        this.em.merge(order);
        this.em.getTransaction().commit();
    }

    public Order find(Long id) {
        String jpql = "SELECT o FROM Order o WHERE o.id = " + id;

        return this.em.createQuery(jpql, Order.class).getSingleResult();
    }

    public List<Order> findByClient(Client client) {
        Long client_id = client.getId();

        String jpql = "SELECT o FROM Order o WHERE o.client = " + client_id;

        return this.em.createQuery(jpql, Order.class).getResultList();
    }

    public Order findNewOrderFromClient(Client client) {
        Long client_id = client.getId();

        String jpql = "SELECT o FROM Order o WHERE o.client = " + client_id + " AND o.status = 'NEW'";

        return this.em.createQuery(jpql, Order.class).getSingleResult();
    }

    public List<Order> findPendingOrdersByDate(LocalDate date) {
        String jpql = "SELECT o FROM Order o WHERE o.status = 'WAITING_PAYMENT' AND o.date = '" + date + "'";
        return em.createQuery(jpql, Order.class).getResultList();
    }

    public void remove(Order order) {
        this.em.getTransaction().begin();
        this.em.remove(order);
        this.em.getTransaction().commit();
    }

    public void close() {
        this.em.close();
    }
}
