package com.example.quiz;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
public class CalculateFinalPrice {
    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCVT175";
        String qCode = "jUqlIWz8";

        String url = "http://36.50.135.242:2230/api/rest/object?studentCode=" + studentCode + "&qCode=" + qCode;
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);

        JSONObject obj = new JSONObject(sb.toString());
        String requestId = obj.getString("requestId");
        JSONObject data = obj.getJSONObject("data");

        double price = data.getDouble("price");
        int taxRate = data.getInt("taxRate");
        double discount = data.getDouble("discount");
        double taxRate2 = (double) taxRate / 100;
        double discount2 = (double) discount / 100;

        //finalPrice = price * (1 + taxRate / 100) * (1 - discount / 100)
        double finalPrice = (double) price * (1 + taxRate2) * (1 - discount2);
        String answer2 = String.format("%.2f", finalPrice);

        JSONObject answer = new JSONObject();
        answer.put("finalPrice", answer2);

        JSONObject submit = new JSONObject();
        submit.put("requestId", requestId);
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);
        submit.put("answer", answer);

        HttpURLConnection conn2 = (HttpURLConnection) new URL("http://36.50.135.242:2230/api/rest/object/submit").openConnection();
        conn2.setRequestMethod("POST");
        conn2.setDoOutput(true);
        conn2.setRequestProperty("Content-Type", "application/json");

        OutputStream os=conn2.getOutputStream();
        os.write(submit.toString().getBytes());

        BufferedReader br2=new BufferedReader(new InputStreamReader(conn2.getInputStream()));
        while ((line=br2.readLine()) != null) sb.append(line);

    }
}
