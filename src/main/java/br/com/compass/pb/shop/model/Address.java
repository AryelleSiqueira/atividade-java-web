package br.com.compass.pb.shop.model;

import javax.persistence.*;

@Entity
@Table(name = "tb_addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2)
    private String state;

    private String city;
    private String addressLine;

    @Column(length = 10)
    private String postalCode;


    public Address() {}

    public Address(String state, String city, String addressLine, String postalCode) {
        this.state = state;
        this.city = city;
        this.addressLine = addressLine;
        this.postalCode = postalCode;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddressLine() {
        return this.addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return this.addressLine + ",\n" +
                this.city + "/" + this.state + "\n" +
                "CEP: " + this.postalCode;
    }
}
