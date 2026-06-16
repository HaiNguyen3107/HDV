package com.example.quiz;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChuKyHMAC {
    public static void main(String[] args) throws Exception {
        String studentCode="B22DCVT175";
        String qCode="kVqlp8fC";
        String url="http://36.50.135.242:2230/api/rest/header?studentCode="+studentCode+"&qCode="+qCode;
        HttpURLConnection conn=(HttpURLConnection)new URL(url).openConnection();
        conn.setRequestMethod("GET");

        BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb=new StringBuilder();
        String line;
        while((line=br.readLine())!=null) sb.append(line);
        JSONObject response=new JSONObject(sb.toString());
        String requestId=response.getString("requestId");
        JSONObject data=response.getJSONObject("data");
        System.out.println(data);

        String nonce = data.getString("nonce");
        JSONArray events = data.getJSONArray("events");
        String signingKey = data.getString("signingKey");

        StringBuilder payload = new StringBuilder();
        payload.append(nonce).append(":");
        for (int i = 0; i < events.length(); i++) {
            if (i > 0) payload.append("|");
            payload.append(events.getString(i));
        }

        payload.append(":").append(studentCode.toUpperCase());

        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(signingKey.getBytes(),"HmacSHA256");
        mac.init(secretKey);
        byte[] hash = mac.doFinal(payload.toString().getBytes());
        StringBuilder signature = new StringBuilder();
        for (byte b : hash) {
            signature.append(String.format("%02x", b));
        }
        String xSignature = signature.toString();

        JSONObject submit = new JSONObject();
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);
        submit.put("requestId", requestId);

        HttpURLConnection  conn2=(HttpURLConnection)new URL
                ("http://36.50.135.242:2230/api/rest/header/submit").openConnection();
        conn2.setRequestMethod("POST");
        conn2.setDoOutput(true);
        conn2.setRequestProperty("Content-Type","application/json");
        conn2.setRequestProperty("X-Signature",xSignature);

        OutputStream os = conn2.getOutputStream();
        os.write(submit.toString().getBytes());

        BufferedReader br2=new BufferedReader(new InputStreamReader(conn2.getInputStream()));
        while ((line=br2.readLine())!=null) sb.append(line);


    }
}
