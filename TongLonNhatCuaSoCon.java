package com.example.quiz;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class TongLonNhatCuaSoCon {
    public static void main(String[] args) throws Exception {
        String qCode = "MTDxG6Zt";
        String studentCode = "B22DCVT175";

        String url
                = "http://36.50.135.242:2230/api/rest/data?studentCode=" + studentCode + "&qCode=" + qCode;
        HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
        connection.setRequestMethod("GET");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) sb.append(line);
        JSONObject response =  new JSONObject(sb.toString());
        String requestId = response.getString("requestId");
        JSONObject data = response.getJSONObject("data");

        JSONArray values = data.getJSONArray("values");
        int k = data.getInt("k");
        int n = values.length();

        long[] prefix=new long[n+1];

        for(int i=0;i<n;i++) prefix[i+1]=prefix[i]+values.getInt(i);

        Deque<Integer> deque=new ArrayDeque<>();

        long maxSum=Long.MIN_VALUE;

        deque.addLast(0);

        for(int i=1;i<=n;i++){

            while(!deque.isEmpty()&&deque.peekFirst()<i-k) deque.pollFirst();

            maxSum=Math.max(maxSum,prefix[i]-prefix[deque.peekFirst()]);

            while(!deque.isEmpty()&&prefix[deque.peekLast()]>=prefix[i]) deque.pollLast();

            deque.addLast(i);
        }

        String result=String.valueOf(maxSum);
        System.out.println(result);


        JSONObject submit =  new JSONObject();
        submit.put("studentCode",studentCode);
        submit.put("qCode",qCode);
        submit.put("requestId",requestId);
        submit.put("answer",result);
        HttpURLConnection postConn =
                (HttpURLConnection)new URL("http://36.50.135.242:2230/api/rest/data/submit").openConnection();
        postConn.setRequestMethod("POST");
        postConn.setDoOutput(true);
        postConn.setRequestProperty("Content-Type","application/json");
        OutputStream os=postConn.getOutputStream();
        os.write(submit.toString().getBytes());
        BufferedReader resultReader=new BufferedReader(new InputStreamReader(postConn.getInputStream()));
        while((line=resultReader.readLine())!=null) System.out.println(line);


    }
}