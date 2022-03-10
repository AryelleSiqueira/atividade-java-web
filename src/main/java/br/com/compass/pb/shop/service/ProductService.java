package br.com.compass.pb.shop.service;

import br.com.compass.pb.shop.dao.ProductDao;
import br.com.compass.pb.shop.model.Product;

import java.util.List;

public class ProductService implements AutoCloseable {

    private final ProductDao dao;

    public ProductService() {
        this.dao = new ProductDao();
    }

    public void save(Product product) {
        this.dao.save(product);
    }

    public Product find(Long id) {
        return this.dao.find(id);
    }

    public List<Product> search(String[] terms) {
        return this.dao.search(terms);
    }

    public List<Product> findAll() {
        return this.dao.findAll();
    }

    @Override
    public void close() {
        this.dao.close();
    }
}
