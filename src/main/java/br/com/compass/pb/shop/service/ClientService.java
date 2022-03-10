package br.com.compass.pb.shop.service;

import br.com.compass.pb.shop.dao.AddressDao;
import br.com.compass.pb.shop.dao.ClientDao;
import br.com.compass.pb.shop.model.Client;

public class ClientService implements AutoCloseable {

    private final ClientDao dao;

    public ClientService() {
        this.dao = new ClientDao();
    }

    public void save(Client client) {
        try (AddressDao addressDao = new AddressDao()) {
            addressDao.save(client.getAddress());
        }
        this.dao.save(client);
    }

    public Client find(Long id) {
        return this.dao.find(id);
    }

    public Client findByEmail(String email) {
        return this.dao.findByEmail(email);
    }

    @Override
    public void close() {
        this.dao.close();
    }
}
