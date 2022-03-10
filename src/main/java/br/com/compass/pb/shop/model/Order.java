package br.com.compass.pb.shop.model;

import br.com.compass.pb.shop.util.GsonHidden;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal totalAmount;
    private LocalDate date;
    private BigDecimal shippingFee;

    @GsonHidden
    @ManyToOne
    private Client client;

    @ManyToMany
    @JoinTable(name = "tb_order_products")
    private List<Product> productList;

    @Enumerated(value = EnumType.STRING)
    private Status status;


    public Order() {
        this.productList = new ArrayList<>();
        this.totalAmount = BigDecimal.ZERO;
        this.status = Status.NEW;
    }

    public Order(Client client) {
        this();
        this.client = client;
    }

    public void addProduct(Product product) {
        this.productList.add(product);
        this.totalAmount = this.totalAmount.add(product.getPrice());
    }

    public List<Product> getProductList() {
        return productList;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getShippingFee() {
        return this.shippingFee;
    }

    public void setShippingFee(BigDecimal shippingFee) {
        this.shippingFee = shippingFee;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder order =
                new StringBuilder("ID: ").append(this.id).append("\n");

        order.append("Preço total: R$ ").append(this.totalAmount).append("\n\n");

        order.append("Produtos:\n");
        this.productList.forEach(product ->
                order.append("\t- ").append(product.getName()).append(";\n"));

        order.append("\nEndereço de entrega:\n").append(this.client.getAddress()).append("\n\n");

        if (this.shippingFee != null) {
            order.append("Taxa de entrega: R$").append(this.shippingFee);
        }

        return order.toString();
    }
}
