package com.example.quiz;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.*;

public class Merkle {
    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCVT175";
        String qCode = "Z2jTihRp";
        String url = "http://36.50.135.242:2230/api/rest/header?studentCode="+studentCode+"&qCode="+qCode;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null)  sb.append(line);

        JSONObject response = new JSONObject(sb.toString());
        String requestId = response.getString("requestId");
        JSONObject data = response.getJSONObject("data");
        System.out.println(data);
        JSONArray leaves = data.getJSONArray("leaves");
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        List<byte[]> cur = new ArrayList<>();

// Hash từng leaf
        for (int i = 0; i < leaves.length(); i++) {
            cur.add(md.digest(leaves.getString(i).getBytes()));
        }

// Xây Merkle Tree
        while (cur.size() > 1) {
            List<byte[]> next = new ArrayList<>();

            for (int i = 0; i < cur.size(); i += 2) {
                byte[] left = cur.get(i);
                byte[] right = (i + 1 < cur.size()) ? cur.get(i + 1) : left;

                byte[] merged = new byte[left.length + right.length];

                System.arraycopy(left, 0, merged, 0, left.length);
                System.arraycopy(right, 0, merged, left.length, right.length);

                next.add(md.digest(merged));
            }

            cur = next;
        }

// byte[] -> hex
        StringBuilder sb2 = new StringBuilder();
        for (byte b : cur.get(0))
            sb2.append(String.format("%02x", b));

        String result = sb2.toString();


        //
        JSONObject submit =  new JSONObject();
        submit.put("requestId", requestId);
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);
        submit.put("answer", result);

        HttpURLConnection connection2 = (HttpURLConnection) new URL
                ("http://36.50.135.242:2230/api/rest/header/submit").openConnection();
        connection2.setRequestMethod("POST");
        connection2.setDoOutput(true);
        connection2.setRequestProperty("Content-Type", "application/json");

        OutputStream os = connection2.getOutputStream();
        os.write(submit.toString().getBytes());
        os.flush();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
        while ((line = reader.readLine()) != null) System.out.println(line);
    }

}
