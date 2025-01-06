package org.springframe.backend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class GithubAuthService {
    @Value("${spring.security.oauth2.client.provider.github.token-uri}$")
    private  String GITHUB_TOKEN_URL ;
    @Value("${spring.security.oauth2.client.provider.github.user-info-uri}$")
    private  String GITHUB_USER_INFO_URL ;
    @Value("${spring.security.oauth2.client.registration.github.client-id}$")
    private String  clientId;
    @Value("${spring.security.oauth2.client.registration.github.client-secret}$")
    private String clientSecret;

    public String getAccessToken(String code, String red_uri) throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("client_id",clientId)
                .add("client_secret",clientSecret)
                .add("code",code)
                .add("redirect_uri",red_uri)
                .build();

        Request request = new Request.Builder()
                .url(GITHUB_TOKEN_URL)
                .header("Accept","application/json")
                .post(formBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String body = response.body().string();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String,Object> jsonMap = objectMapper.readValue(body,Map.class);
            return (String) jsonMap.get("access_token");
        }
    }

    public Map<String, Object> getUserInfo(String accessToken) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(GITHUB_USER_INFO_URL)
                .header("Accept","application/json")
                .header("Authorization","Bearer "+accessToken)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String body = response.body().string();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(body, Map.class);

        }
    }
}
