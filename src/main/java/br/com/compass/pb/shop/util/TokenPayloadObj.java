package br.com.compass.pb.shop.util;

import java.time.LocalDateTime;

public class TokenPayloadObj {

    private Long clientId;
    private String email;


    public TokenPayloadObj() {}

    public TokenPayloadObj(Long clientId, String email) {
        this.clientId = clientId;
        this.email = email;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
