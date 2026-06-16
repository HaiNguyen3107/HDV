package com.example.quiz;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
public class XChecksum {
    public static void main(String[] args) throws Exception {
        String studentCode="B22DCVT175";
        //String qCode="S5EoXgH5";
        String qCode = "QWXruq4B";
        String url="http://36.50.135.242:2230/api/rest/header?studentCode="+studentCode+"&qCode="+qCode;
        HttpURLConnection conn=(HttpURLConnection)new URL(url).openConnection();
        conn.setRequestMethod("GET");
        String checksum = conn.getHeaderField("X-Checksum");

        BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb=new StringBuilder();
        String line;
        while((line=br.readLine())!=null) sb.append(line);
        JSONObject response=new JSONObject(sb.toString());
        String requestId=response.getString("requestId");
        JSONArray data=response.getJSONArray("data");
        System.out.println(data);

        JSONObject submit = new JSONObject();
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);
        submit.put("requestId", requestId);

        HttpURLConnection conn2=(HttpURLConnection)new URL
                ("http://36.50.135.242:2230/api/rest/header/submit").openConnection();
        conn2.setRequestMethod("POST");
        conn2.setDoOutput(true);
        conn2.setRequestProperty("Content-Type", "application/json");
        conn2.setRequestProperty("X-Checksum", checksum);

        OutputStream os = conn2.getOutputStream();
        os.write(submit.toString().getBytes());

        BufferedReader br2=new BufferedReader(new InputStreamReader(conn2.getInputStream()));
        while((line=br2.readLine())!=null) sb.append(line);

    }
}
