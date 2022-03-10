package br.com.compass.pb.shop.resource;

import br.com.compass.pb.shop.filter.Secured;
import br.com.compass.pb.shop.model.Product;
import br.com.compass.pb.shop.service.ProductService;
import com.google.gson.Gson;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Secured
@Path("search")
public class ProductResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts() {

        try (ProductService ps = new ProductService()) {
            List<Product> productList = ps.findAll();

            return Response.ok(new Gson().toJson(productList)).build();
        }
        catch (Exception e) {
            return Response.serverError().build();
        }
    }


    @GET
    @Path("{query}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts(@PathParam("query") String query) {

        try (ProductService ps = new ProductService()) {

            String[] terms = query.strip().split("-");
            List<Product> productList = ps.search(terms);

            return Response.ok(new Gson().toJson(productList)).build();
        }
        catch (Exception e) {
            return Response.serverError().build();
        }
    }

}
