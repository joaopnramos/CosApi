package com.example.citizensonscience;

public class LoginResponse {

    private boolean error;
    private String token;


    public LoginResponse(boolean error, String token) {
        this.error = error;
        this.token = token;

    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return token;
    }


}
