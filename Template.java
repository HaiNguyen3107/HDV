package com.example.quiz;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
public class Template {
    public static void main(String[] args) throws Exception {
        String studentCode="B22DCVT175";
        String qCode="???";
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

        // Logic
        JSONArray codes=data.getJSONArray("codes");
        int k=data.getInt("k");
        String result = "";


        //Post
        JSONObject submit=new JSONObject();
        submit.put("studentCode",studentCode);
        submit.put("qCode",qCode);
        submit.put("requestId",requestId);
        submit.put("answer",result);

        HttpURLConnection postConn=(HttpURLConnection)new
                URL("http://36.50.135.242:2230/api/rest/data/submit").openConnection();
        postConn.setRequestMethod("POST");
        postConn.setRequestProperty("Content-Type","application/json");
        postConn.setDoOutput(true);
        OutputStream os=postConn.getOutputStream();
        os.write(submit.toString().getBytes());

        BufferedReader resultReader=new BufferedReader(new InputStreamReader(postConn.getInputStream()));
        while((line=resultReader.readLine())!=null) System.out.println(line);

    }
}
