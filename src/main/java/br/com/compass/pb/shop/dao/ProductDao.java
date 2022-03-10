package br.com.compass.pb.shop.dao;

import br.com.compass.pb.shop.model.Product;
import br.com.compass.pb.shop.util.JPAUtil;

import javax.persistence.EntityManager;
import java.io.Closeable;
import java.util.List;

public class ProductDao implements Closeable {

    private final EntityManager em;


    public ProductDao() {
        this.em = JPAUtil.getEntityManager();
    }


    public void save(Product product) {
        this.em.persist(product);
    }


    public Product find(Long id) {
        String jpql = "SELECT p FROM Product p WHERE p.id = " + id;

        return this.em.createQuery(jpql, Product.class).getSingleResult();
    }


    public List<Product> search(String[] terms) {
        StringBuilder jpql =
                new StringBuilder("SELECT p FROM Product p WHERE p.name LIKE '%").append(terms[0]).append("%'");

        for (int i = 1; i < terms.length; i++) {
            jpql.append(" OR p.name LIKE '%").append(terms[i]).append("%'");
        }

        return em.createQuery(jpql.toString(), Product.class).getResultList();
    }


    public List<Product> findAll() {
        String jpql = "SELECT p FROM Product p";
        return em.createQuery(jpql, Product.class).getResultList();
    }

    @Override
    public void close() {
        this.em.close();
    }
}
