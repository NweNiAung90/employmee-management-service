package com.app.employee.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "status",
        "secret_key"
})
@Data
public class AuthResponse {
    @JsonProperty("status")
    private String status;

    @JsonProperty("secret_key")
    private String secretKey;

    public AuthResponse(String status, String secretKey) {
        super();
        this.status = status;
        this.secretKey = secretKey;
    }

    public AuthResponse() {
        super();
    }
}