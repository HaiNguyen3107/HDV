package com.example.quiz;
import java.util.*;
import java.io.*;
import java.net.*;
import org.json.*;
public class IdempotencyKey {
    public static void main(String[] args) throws Exception {
        String qCode = "xthsolYs";
        String studentCode = "B22DCVT175";
        String url = "http://36.50.135.242:2230/api/rest/method?studentCode="+studentCode+"&qCode="+qCode;

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);

        JSONObject response =  new JSONObject(sb.toString());
        String requestId =  response.getString("requestId");
        JSONObject data =  response.getJSONObject("data");
        System.out.println(data);

        int capacity =  data.getInt("capacity");
        JSONArray requests = data.getJSONArray("requests");
        List<String> cache = new ArrayList<>();
        List<String> ans = new ArrayList<>();
        for (int i = 0; i < requests.length(); i++) {
            JSONObject r = requests.getJSONObject(i);

            String id = r.getString("id");
            String key = r.getString("key");
            if (!cache.contains(key)) {
                ans.add(id);
                if (cache.size() == capacity)
                    cache.remove(0);   // xóa key cũ nhất
                cache.add(key);
            }
        }
        String result = String.join(",", ans);

        JSONObject submit = new JSONObject();
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);
        submit.put("answer", result);

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
