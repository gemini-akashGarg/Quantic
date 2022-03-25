package com.gemini.automation;
import com.gemini.automation.ApiTest.ApiClientConnect;
import com.gemini.automation.ApiTest.ApiClientConnect_Interface;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AppTest {
    public static void main(String args[]) {
        ApiClientConnect apiClientConnect = new ApiClientConnect();

        String token = "Bearer e44ac095d53abb1da69ff4cdc9c0bc24ea741dbc7cbe6f2ba2dfdbec9cb1ecd4";

        // Headers
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", token);
        headers.put("Content-Type", "application/json");

        // Payload
        JsonObject req = new JsonObject();
        req.addProperty("name", "User_updated_2");
        req.addProperty("email", "Updated_User2@gmail.com");
        req.addProperty("gender", "male");
        req.addProperty("status", "inactive");

        // Mock PUT Request
        String url = "https://gorest.co.in/public/v1/users/3533";


        JsonObject response = apiClientConnect.httpCreateRequest("patch", url, req.toString(), headers);
        System.out.println(response.toString());


    }
}
