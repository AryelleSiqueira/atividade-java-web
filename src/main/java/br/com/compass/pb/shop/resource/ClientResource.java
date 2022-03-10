package br.com.compass.pb.shop.resource;

import br.com.compass.pb.shop.filter.Secured;
import br.com.compass.pb.shop.model.Client;
import br.com.compass.pb.shop.service.ClientService;
import br.com.compass.pb.shop.util.GsonUtil;
import br.com.compass.pb.shop.util.JWEUtil;
import com.google.gson.Gson;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Secured
@Path("client")
public class ClientResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProfileInfo(@HeaderParam("Authorization") String authHeader) {

        try (ClientService clientService = new ClientService()) {
            Client client = clientService
                    .find(JWEUtil.getClientIdFromAuthHeader(authHeader));

            Gson gson = GsonUtil.getGsonWithExclusionStrategy();
            return Response.ok(gson.toJson(client)).build();
        }
        catch (Exception e) {
            return Response.serverError().build();
        }
    }
}
