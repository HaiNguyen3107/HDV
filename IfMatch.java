package com.example.quiz;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.io.*;
import java.net.*;
import org.json.*;
public class IfMatch {
    public static void main(String[] args) throws Exception {
        String qCode = "BOuYE6kx";
        String studentCode = "B22DCVT175";
        String url = "http://36.50.135.242:2230/api/rest/method?studentCode="+studentCode+"&qCode="+qCode;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);

        JSONObject response = new JSONObject(sb.toString());
        String requestId =  response.getString("requestId");
        JSONObject data = response.getJSONObject("data");
        System.out.println(data);

        String etag = data.getString("etag");
        JSONObject answer = new JSONObject();
        answer.put("status", "RESOLVED");

        JSONObject submit = new JSONObject();
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);
        submit.put("answer", answer);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(
                                "http://36.50.135.242:2230/api/rest/method/"
                                        + requestId))
                        .header("Content-Type",
                                "application/json")
                        .header("If-Match", etag)
                        .method("PATCH",
                                HttpRequest.BodyPublishers.ofString(
                                        submit.toString()))
                        .build();

        HttpResponse<String> response2 =
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString());

        System.out.println(response2.statusCode());
        System.out.println(response2.body());

    }
}
