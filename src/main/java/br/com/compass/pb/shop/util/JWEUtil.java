package br.com.compass.pb.shop.util;

import br.com.compass.pb.shop.model.Client;
import com.google.gson.Gson;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.JoseException;

import java.nio.charset.StandardCharsets;
import java.security.Key;

public class JWEUtil {

    private static final Key KEY = new AesKey("hgr9os5w3jewk0q7".getBytes(StandardCharsets.UTF_8));


    public static String encrypt(String jsonObj) throws JoseException {
        JsonWebEncryption jwe = new JsonWebEncryption();
        jwe.setPayload(jsonObj);
        jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A128KW);
        jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
        jwe.setKey(KEY);

        return jwe.getCompactSerialization();
    }


    public static String decryptPayload(String serializedJwe) throws JoseException {
        JsonWebEncryption jwe = new JsonWebEncryption();
        jwe.setAlgorithmConstraints(new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT,
                KeyManagementAlgorithmIdentifiers.A128KW));
        jwe.setContentEncryptionAlgorithmConstraints(new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT,
                ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256));
        jwe.setKey(KEY);
        jwe.setCompactSerialization(serializedJwe);

        return jwe.getPayload();
    }


    public static String generateToken(Client client) throws JoseException {
        TokenPayloadObj tpo = new TokenPayloadObj(client.getId(), client.getEmail());

        return encrypt(new Gson().toJson(tpo));
    }


    public static Long getClientIdFromAuthHeader(String authHeader) throws JoseException {
        String token = authHeader.split(" ")[1].strip();
        String payload = decryptPayload(token);

        TokenPayloadObj tp = new Gson().fromJson(payload, TokenPayloadObj.class);

        return tp.getClientId();
    }
}
