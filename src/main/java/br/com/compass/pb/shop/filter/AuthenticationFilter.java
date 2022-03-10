package br.com.compass.pb.shop.filter;

import br.com.compass.pb.shop.util.TokenPayloadObj;
import br.com.compass.pb.shop.util.JWEUtil;
import com.google.gson.Gson;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final String AUTHENTICATION_SCHEME = "Bearer";

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {

        String authorizationHeader =
                containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        boolean isTokenBasedAuth = authorizationHeader != null
                && authorizationHeader.toLowerCase().startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");

        if (!isTokenBasedAuth) {
            containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        String token = authorizationHeader.split(" ")[1].trim();

        try {
            validateToken(token);

        } catch (Exception e) {
            containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    private static void validateToken(String token) throws Exception {
        TokenPayloadObj tp = new Gson().
                fromJson(JWEUtil.decryptPayload(token), TokenPayloadObj.class);

        if (tp == null) {
            throw new Exception();
        }
    }

}
