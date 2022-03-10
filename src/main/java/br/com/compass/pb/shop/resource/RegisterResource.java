package br.com.compass.pb.shop.resource;

import br.com.compass.pb.shop.model.Address;
import br.com.compass.pb.shop.model.Client;
import br.com.compass.pb.shop.service.ClientService;
import br.com.compass.pb.shop.util.JWEUtil;
import com.google.gson.Gson;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("register")
public class RegisterResource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response register(@FormParam("name") String name,
                             @FormParam("email") String email,
                             @FormParam("cpf") String cpf,
                             @FormParam("addressLine") String addressLine,
                             @FormParam("city") String city,
                             @FormParam("state") String state,
                             @FormParam("postalCode") String postalCode,
                             @FormParam("password") String password) {

        try (ClientService cs = new ClientService()) {
            Address address = new Address(state, city, addressLine, postalCode);
            Client client = new Client(name, email, cpf, password, address);
            cs.save(client);

            String token = JWEUtil.generateToken(client);

            return Response.ok(new Gson().toJson(token)).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }
}
