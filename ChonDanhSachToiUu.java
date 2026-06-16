package com.example.quiz;
import java.util.*;
import java.io.*;
import java.net.*;
import org.json.*;
public class ChonDanhSachToiUu {
    public static void main(String[] args) throws Exception {
        String qCode = "1EALsgYZ";
        String studentCode = "B22DCVT175";
        String url = "http://36.50.135.242:2230/api/rest/object?studentCode="+studentCode+"&qCode="+qCode;

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);

        JSONObject obj = new JSONObject(sb.toString());
        String requestId = obj.getString("requestId");
        JSONObject data = obj.getJSONObject("data");
        System.out.println(data);
        int capacity = data.getInt("capacity");
        JSONArray items = data.getJSONArray("items");

        int n = items.length();
        int bestValue = 0;
        String result = "";

        for (int mask = 0; mask < (1 << n); mask++) {
            int weight = 0, value = 0;
            List<String> ids = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    JSONObject item = items.getJSONObject(i);
                    weight += item.getInt("weight");
                    value += item.getInt("value");
                    ids.add(item.getString("id"));
                }
            }

            if (weight <= capacity && value > bestValue) {
                bestValue = value;
                result = String.join(",", ids) + "|" + value;
            }
        }

        JSONObject submit = new JSONObject();
        submit.put("requestId", requestId);
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);
        submit.put("answer", result);

        HttpURLConnection conn2 = (HttpURLConnection)
                new URL("http://36.50.135.242:2230/api/rest/object/submit").openConnection();
        conn2.setRequestMethod("POST");
        conn2.setRequestProperty("Content-Type","application/json");
        conn2.setDoOutput(true);

        OutputStream os = conn2.getOutputStream();
        os.write(submit.toString().getBytes());

        BufferedReader resultReader = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
        while ((line = resultReader.readLine()) != null) System.out.println(line);


    }
}
