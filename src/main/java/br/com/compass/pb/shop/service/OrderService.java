package br.com.compass.pb.shop.service;

import br.com.compass.pb.shop.dao.OrderDao;
import br.com.compass.pb.shop.model.Client;
import br.com.compass.pb.shop.model.Order;

import java.time.LocalDate;
import java.util.List;

public class OrderService implements AutoCloseable {

    private final OrderDao dao;

    public OrderService() {
        this.dao = new OrderDao();
    }

    public void save(Order order) {
        this.dao.save(order);
    }

    public void update(Order order) {
        this.dao.update(order);
    }

    public void remove(Order order) {
        this.dao.remove(order);
    }

    public Order find(Long id) {
        return this.dao.find(id);
    }

    public List<Order> findPendingOrdersByDate(LocalDate date) {
        return this.dao.findPendingOrdersByDate(date);
    }

    public List<Order> findByClient(Client client) {
        return this.dao.findByClient(client);
    }

    public Order getShoppingCart(Client client) {
        Order order;

        try {
            order = this.dao.findNewOrderFromClient(client);
        }
        catch (Exception e) {
            order = new Order(client);
            this.dao.save(order);
        }
        return order;
    }

    @Override
    public void close() {
        this.dao.close();
    }
}
