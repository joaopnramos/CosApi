package com.example.citizensonscience;

public class LoginResponse {

    private boolean error;
    private String token, code, id;

    public LoginResponse(boolean error, String token, String code, String id) {
        this.error = error;
        this.token = token;
        this.code = code;
        this.id = id;

    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return token;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getCode() {
        return code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
