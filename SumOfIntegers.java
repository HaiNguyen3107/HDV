package com.example.quiz;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
public class SumOfIntegers {
    public static void main(String[] args) throws Exception {
        String qCode = "FskRUegm";
        String studentCode = "B22DCVT175";
        String url = "http://36.50.135.242:2230/api/rest/data?studentCode="+studentCode+"&qCode="+qCode;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);

        JSONObject response = new JSONObject(sb.toString());
        String requestId =  response.getString("requestId");
        JSONArray data = response.getJSONArray("data");
        System.out.println(data);

        int answer = 0;
        for(int i = 0; i < data.length(); i++){
            answer += data.getInt(i);
        }

        JSONObject submit = new JSONObject();
        submit.put("requestId", requestId);
        submit.put("answer", answer);
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);

        HttpURLConnection connection2 = (HttpURLConnection) new URL("http://36.50.135.242:2230/api/rest/data/submit").openConnection();
        connection2.setRequestMethod("POST");
        connection2.setDoOutput(true);
        connection2.setRequestProperty("Content-Type", "application/json");

        OutputStream os = connection2.getOutputStream();
        os.write(submit.toString().getBytes());

        BufferedReader br2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
        while ((line = br2.readLine()) != null) System.out.println(line);

    }
}
