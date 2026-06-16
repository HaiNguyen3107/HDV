package com.example.quiz;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
public class MoPhongMay {
    public static void main(String[] args) throws Exception{
        String qCode = "GNLDnmQg";
        String studentCode = "B22DCVT175";
        String url = "http://36.50.135.242:2230/api/rest/method?studentCode="+studentCode+"&qCode="+qCode;

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);

        JSONObject response = new JSONObject(sb.toString());
        String requestId = response.getString("requestId");
        JSONObject data = response.getJSONObject("data");
        System.out.println(data);

        String state =  data.getString("initialState");
        JSONArray events = data.getJSONArray("events");
        JSONArray transitions = data.getJSONArray("transitions");
        Map<String, String> mp = new HashMap<>();

        for (int i = 0; i < transitions.length(); i++) {
            JSONObject t = transitions.getJSONObject(i);

            String from = t.getString("from");
            String event = t.getString("event");
            String to = t.getString("to");

            mp.put(from + "|" + event, to);
        }

        for (int i = 0; i < events.length(); i++) {
            String event = events.getString(i);
            String key = state + "|" + event;

            if (mp.containsKey(key)) {
                state = mp.get(key);
            }
        }

        String result = state;
        //
        JSONObject submit = new JSONObject();
        submit.put("answer", result);
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);

        HttpURLConnection conn2 = (HttpURLConnection) new
                URL("http://36.50.135.242:2230/api/rest/method/" + requestId).openConnection();
        conn2.setRequestMethod("PUT");
        conn2.setDoOutput(true);
        conn2.setRequestProperty("Content-Type", "application/json");
        OutputStream os=conn2.getOutputStream();
        os.write(submit.toString().getBytes());

        BufferedReader resultReader=new BufferedReader(new InputStreamReader(conn2.getInputStream()));
        while((line=resultReader.readLine())!=null) System.out.println(line);
    }
}
