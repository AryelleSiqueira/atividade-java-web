package br.com.compass.pb.shop.resource;

import br.com.compass.pb.shop.model.Client;
import br.com.compass.pb.shop.service.ClientService;
import br.com.compass.pb.shop.util.JWEUtil;
import com.google.gson.Gson;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("login")
public class LoginResource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(@FormParam("email") String email,
                          @FormParam("password") String password) {

        try (ClientService clientService = new ClientService()) {
            Client client = clientService.findByEmail(email);

            if (client.authenticate(password)) {
                String token = JWEUtil.generateToken(client);
                return Response.ok(new Gson().toJson(token)).build();
            }
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        catch (Exception e) {
            return Response.serverError().build();
        }
    }
}
