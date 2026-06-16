package com.example.quiz;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
public class KMP {
    public static void main(String[] args) throws Exception {

        String studentCode="B22DCVT175";
        String qCode="nBu1jfVq";

        /*
         * GET
         */

        String url="http://36.50.135.242:2230/api/rest/character?studentCode="+studentCode+"&qCode="+qCode;

        HttpURLConnection conn=(HttpURLConnection)new URL(url).openConnection();

        conn.setRequestMethod("GET");

        BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));

        StringBuilder sb=new StringBuilder();

        String line;

        while((line=br.readLine())!=null) sb.append(line);

        JSONObject response=new JSONObject(sb.toString());

        String requestId=response.getString("requestId");

        JSONObject data=response.getJSONObject("data");



        /*
         * LOGIC
         */

        String text=data.getString("text");

        String pattern=data.getString("pattern");
        int x = text.indexOf(pattern);
        String result = String.valueOf(x);
        System.out.println(result);



        /*
         * POST
         */

        JSONObject submit=new JSONObject();

        submit.put("studentCode",studentCode);

        submit.put("qCode",qCode);

        submit.put("requestId",requestId);

        submit.put("answer",result);

        HttpURLConnection postConn=(HttpURLConnection)new URL("http://36.50.135.242:2230/api/rest/character/submit").openConnection();

        postConn.setRequestMethod("POST");

        postConn.setRequestProperty("Content-Type","application/json");

        postConn.setDoOutput(true);

        OutputStream os=postConn.getOutputStream();

        os.write(submit.toString().getBytes());

        BufferedReader resultReader=new BufferedReader(new InputStreamReader(postConn.getInputStream()));

        while((line=resultReader.readLine())!=null) System.out.println(line);
    }





}
