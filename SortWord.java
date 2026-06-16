package com.example.quiz;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
public class SortWord {
    public static void main(String[] args) throws Exception {
        String qCode = "OPJbLNhD";
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
        String data =  response.getString("data");
        System.out.println(data);

        String[] words = data.split("\\s+");
        Arrays.sort(words);
        String result = String.join(" ", words);

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
