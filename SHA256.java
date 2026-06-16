package com.example.quiz;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;
import java.net.*;
import org.json.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SHA256 {
    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCVT175";
        String qCode = "Y3Fk7eRI";
        String url = "http://36.50.135.242:2230/api/rest/header?studentCode="+studentCode+"&qCode="+qCode;

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);

        JSONObject response = new JSONObject(sb.toString());
        String requestId = response.getString("requestId");
        JSONObject data = response.getJSONObject("data");

        System.out.println(data);
        String nonce = data.getString("nonce");
        String signingKey = data.getString("signingKey");
        JSONArray events = data.getJSONArray("events");

        List<String> list = new ArrayList<>();
        for (int i = 0; i < events.length(); i++)
            list.add(events.getString(i));

        String payload = nonce + ":" + String.join("|", list) + ":" + studentCode.toUpperCase();

        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec key = new SecretKeySpec(
                signingKey.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
        );
        mac.init(key);

        byte[] bytes = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));

        StringBuilder hex = new StringBuilder();
        for (byte b : bytes)
            hex.append(String.format("%02x", b));

        String result = hex.toString();

        JSONObject submit = new JSONObject();
        submit.put("requestId", requestId);
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);
        submit.put("answer", result);

        HttpURLConnection conn2 = (HttpURLConnection) new
                URL("http://36.50.135.242:2230/api/rest/header/submit").openConnection();
        conn2.setRequestMethod("POST");
        conn2.setDoOutput(true);
        conn2.setRequestProperty("Content-Type", "application/json");

        OutputStream os=conn2.getOutputStream();
        os.write(submit.toString().getBytes());

        BufferedReader resultReader=new BufferedReader(new InputStreamReader(conn2.getInputStream()));
        while((line=resultReader.readLine())!=null) System.out.println(line);


    }
}
