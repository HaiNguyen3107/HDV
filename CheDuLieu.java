package com.example.quiz;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
public class CheDuLieu {
    public static void main(String[] args) throws Exception {
        String qCode = "8ZksP6q4";
        String studentCode = "B22DCVT175";
        String url = "http://36.50.135.242:2230/api/rest/character?studentCode="+studentCode+"&qCode="+qCode;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);

        JSONObject response = new JSONObject(sb.toString());
        String requestId =  response.getString("requestId");
        String logs =  response.getString("data");

        logs = logs.replaceAll(
                "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}",
                "[EMAIL]"
        );
        logs = logs.replaceAll(
                "\\b0\\d{9}\\b",
                "[PHONE]"
        );
        logs = logs.replaceAll(
                "token=[^\\s|]+",
                "token=[TOKEN]"
        );
        String result = logs;

        JSONObject submit = new JSONObject();
        submit.put("requestId", requestId);
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);
        submit.put("answer", result);

        HttpURLConnection connection2 = (HttpURLConnection) new URL("http://36.50.135.242:2230/api/rest/character/submit").openConnection();
        connection2.setRequestMethod("POST");
        connection2.setDoOutput(true);
        connection2.setRequestProperty("Content-Type", "application/json");

        OutputStream os = connection2.getOutputStream();
        os.write(submit.toString().getBytes());

        BufferedReader br2=new BufferedReader(new InputStreamReader(connection2.getInputStream()));
        while((line=br2.readLine())!=null) System.out.println(line);

    }
}
