package com.example.quiz;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
public class ChonBanGhiBangBinarySearch {
    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCVT175";
        String qCode = "sYyNho5Y";
        String url = "http://36.50.135.242:2230/api/rest/path?studentCode="+studentCode+"&qCode="+qCode;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);

        JSONObject response = new JSONObject(sb.toString());
        String requestId = response.getString("requestId");
        JSONObject data = response.getJSONObject("data");
        System.out.println(data);

        JSONArray records = data.getJSONArray("records");
        int target = data.getInt("target");
        String answer = "";
        for (int i = 0; i < records.length(); i++) {
            JSONObject record = records.getJSONObject(i);
            int k = record.getInt("threshold");
            String id = record.getString("id");
            if(k >= target) {
                answer = id;
                break;
            }
        }
        JSONObject submit = new JSONObject();
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);
        submit.put("requestId", requestId);
        submit.put("answer", answer);

        String submitUrl =
                "http://36.50.135.242:2230/api/rest/path/submit"
                        + "?studentCode=" + studentCode
                        + "&qCode=" + qCode
                        + "&requestId=" + requestId
                        + "&answer=" + answer;

        HttpURLConnection submitConn =  (HttpURLConnection) new URL(submitUrl).openConnection();
        submitConn.setRequestMethod("GET");

        BufferedReader resultReader=new BufferedReader(new InputStreamReader(submitConn.getInputStream()));
        while((line=resultReader.readLine())!=null) System.out.println(line);

    }
}
