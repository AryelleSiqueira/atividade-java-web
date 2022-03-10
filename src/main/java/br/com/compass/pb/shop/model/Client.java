package br.com.compass.pb.shop.model;

import br.com.compass.pb.shop.util.GsonHidden;

import javax.persistence.*;

@Entity
@Table(name = "tb_client", uniqueConstraints = {@UniqueConstraint(columnNames = {"email", "cpf"})})
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String cpf;

    @GsonHidden
    private String password;

    @OneToOne
    private Address address;


    public Client() {}

    public Client(String name, String email, String cpf, String password, Address address) {
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.password = password;
        this.address = address;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
}
