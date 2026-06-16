package com.example.quiz;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
public class AuditField {
    public static void main(String[] args) throws Exception {
        String studentCode="B22DCVT175";
        String qCode="dPze1JIt";
        String url="http://36.50.135.242:2230/api/rest/data?studentCode="+studentCode+"&qCode="+qCode;
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

        JSONObject answer = new JSONObject();
        answer.put("status", "ACTIVE");
        answer.put("activatedBy", "B22DCVT175");

        JSONObject submit = new JSONObject();
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);
        submit.put("answer", answer);

        HttpURLConnection conn2=(HttpURLConnection)new URL
                ("http://36.50.135.242:2230/api/rest/method/" + requestId).openConnection();
        conn2.setRequestMethod("PUT");
        conn2.setDoOutput(true);
        conn2.setRequestProperty("Content-Type", "application/json");

        OutputStream os = conn2.getOutputStream();
        os.write(submit.toString().getBytes());

        BufferedReader br2=new BufferedReader(new InputStreamReader(conn2.getInputStream()));
        while((line=br2.readLine())!=null) sb.append(line);

    }
}
