package com.example.quiz;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
public class QueryValidation {
    public static void main(String[] args) throws Exception {
        String studentCode="B22DCVT175";
        String qCode="67SadlMQ";
        String url="http://36.50.135.242:2230/api/rest/path?studentCode="+studentCode+"&qCode="+qCode;
        HttpURLConnection conn=(HttpURLConnection)new URL(url).openConnection();
        conn.setRequestMethod("GET");
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

        String submitURL = "http://36.50.135.242:2230/api/rest/path/1?studentCode="
                +studentCode+"&qCode="+qCode+"&requestId="+requestId+"&currency=USD";
        HttpURLConnection conn2=(HttpURLConnection)new URL(submitURL).openConnection();
        conn2.setRequestMethod("GET");

        BufferedReader br2=new BufferedReader(new InputStreamReader(conn2.getInputStream()));
        while((line=br2.readLine())!=null) sb.append(line);



    }
}
