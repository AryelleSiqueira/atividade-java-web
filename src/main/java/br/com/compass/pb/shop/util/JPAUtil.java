package br.com.compass.pb.shop.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {

    private static final EntityManagerFactory FACTORY =
            Persistence.createEntityManagerFactory("my_shop");

    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }
}
