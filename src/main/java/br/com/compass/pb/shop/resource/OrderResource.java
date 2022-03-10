package br.com.compass.pb.shop.resource;

import br.com.compass.pb.shop.filter.Secured;
import br.com.compass.pb.shop.model.Client;
import br.com.compass.pb.shop.model.Order;
import br.com.compass.pb.shop.model.Product;
import br.com.compass.pb.shop.model.Status;
import br.com.compass.pb.shop.service.ClientService;
import br.com.compass.pb.shop.service.OrderService;
import br.com.compass.pb.shop.service.ProductService;
import br.com.compass.pb.shop.util.CorreiosApiUtil;
import br.com.compass.pb.shop.util.GsonUtil;
import br.com.compass.pb.shop.util.JWEUtil;
import br.com.compass.pb.shop.util.SendGridAPIUtil;
import com.google.gson.Gson;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Secured
@Path("order")
public class OrderResource {

    private static final String CONTEXT = "/shop/app";

    @POST
    @Path("new/add/{productId}")
    public Response addProductToOrder(@PathParam("productId") Long productId,
                                      @HeaderParam("Authorization") String authHeader) {

        try (ClientService cs = new ClientService()) {
            Client client = cs.find(JWEUtil.getClientIdFromAuthHeader(authHeader));

            try (OrderService os = new OrderService()) {
                Order order = os.getShoppingCart(client);

                try (ProductService ps = new ProductService()) {
                    Product product = ps.find(productId);
                    order.addProduct(product);
                    os.update(order);
                }
                catch (NoResultException e) {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
                return Response.created(URI.create(CONTEXT + "/order/new")).build();
            }
        }
        catch (Exception e) {
            return Response.serverError().build();
        }
    }


    @GET
    @Path("new")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getShoppingCart(@HeaderParam("Authorization") String authHeader) {

        try (ClientService cs = new ClientService()) {
            Client client = cs.find(JWEUtil.getClientIdFromAuthHeader(authHeader));

            try (OrderService os = new OrderService()) {
                Order order = os.getShoppingCart(client);

                Gson gson = GsonUtil.getGsonWithExclusionStrategy();
                return Response.ok(gson.toJson(order)).build();
            }
        }
        catch (Exception e) {
            return Response.serverError().build();
        }
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllOrdersFromClient(@HeaderParam("Authorization") String authHeader) {

        try (ClientService cs = new ClientService()) {
            Client client = cs.find(JWEUtil.getClientIdFromAuthHeader(authHeader));

            try (OrderService os = new OrderService()) {
                List<Order> orders = os.findByClient(client);

                Gson gson = GsonUtil.getGsonWithExclusionStrategy();
                return Response.ok(gson.toJson(orders)).build();
            }
        }
        catch (Exception e) {
            return Response.serverError().build();
        }
    }


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderById(@PathParam("id") Long id,
                                 @HeaderParam("Authorization") String authHeader) {

        try (OrderService os = new OrderService()) {

            Order order;
            try {
                order = os.find(id);
            }
            catch (NoResultException e) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            Long clientId = JWEUtil.getClientIdFromAuthHeader(authHeader);

            if (!order.getClient().getId().equals(clientId)) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            Gson gson = GsonUtil.getGsonWithExclusionStrategy();
            return Response.ok(gson.toJson(order)).build();
        }
        catch (Exception e) {
            return Response.serverError().build();
        }
    }


    @POST
    @Path("new/payment")
    public Response finishOrder(@HeaderParam("Authorization") String authHeader) {

        try (ClientService cs = new ClientService()) {
            Client client = cs.find(JWEUtil.getClientIdFromAuthHeader(authHeader));

            try (OrderService os = new OrderService()) {
                Order order = os.getShoppingCart(client);

                if (order.getProductList().isEmpty()) {
                    return Response.noContent().build();
                }
                BigDecimal fee = CorreiosApiUtil.getShippingFee(client.getAddress().getPostalCode());
                order.setShippingFee(fee);
                order.setStatus(Status.WAITING_PAYMENT);
                order.setDate(LocalDate.now());
                os.update(order);

                String message = "Olá, " + client.getName()
                        + "\n\nRecebemos seu pedido!\n\n" + order
                        + "\n\nEfetue o pagamento até "
                        + order.getDate().plusDays(7L).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                SendGridAPIUtil.sendEmail(client.getEmail(), "Pedido efetuado", message);

                return Response.created(URI.create(CONTEXT + "/order/" + order.getId())).build();
            }
        }
        catch (Exception e) {
            return Response.serverError().build();
        }
    }

}
