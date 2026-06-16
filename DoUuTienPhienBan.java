package com.example.quiz;
import java.util.*;
import java.io.*;
import java.net.*;
import org.json.*;
public class DoUuTienPhienBan {
    public static void main(String[] args) throws Exception {
        String qCode = "6SgtZW8b";
        String studentCode = "B22DCVT175";
        String url = "http://36.50.135.242:2230/api/rest/method?studentCode="+studentCode+"&qCode="+qCode;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) sb.append(line);

        JSONObject response = new JSONObject(sb.toString());
        String requestId = response.getString("requestId");
        JSONObject data = response.getJSONObject("data");
        System.out.println(data);
        JSONArray modules = data.getJSONArray("modules");
        JSONArray deps = data.getJSONArray("deps");

        Map<String, Integer> ver = new HashMap<>();
        Map<String, Integer> indeg = new HashMap<>();
        Map<String, List<String>> g = new HashMap<>();

        for (int i = 0; i < modules.length(); i++) {
            JSONObject m = modules.getJSONObject(i);

            String id = m.getString("id");

            ver.put(id, m.getInt("version"));
            indeg.put(id, 0);
            g.put(id, new ArrayList<>());
        }

        for (int i = 0; i < deps.length(); i++) {
            JSONObject d = deps.getJSONObject(i);

            String u = d.getString("before");
            String v = d.getString("after");

            g.get(u).add(v);
            indeg.put(v, indeg.get(v) + 1);
        }

        PriorityQueue<String> pq =
                new PriorityQueue<>((a, b) -> ver.get(b) - ver.get(a));

        for (String id : indeg.keySet())
            if (indeg.get(id) == 0)
                pq.add(id);

        List<String> ans = new ArrayList<>();

        while (!pq.isEmpty()) {
            String u = pq.poll();
            ans.add(u);

            for (String v : g.get(u)) {
                indeg.put(v, indeg.get(v) - 1);

                if (indeg.get(v) == 0)
                    pq.add(v);
            }
        }

        String result = String.join(",", ans);

        JSONObject submit = new JSONObject();
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);
        submit.put("answer", result);

        HttpURLConnection conn2 = (HttpURLConnection) new URL("http://36.50.135.242:2230/api/rest/method/" + requestId).openConnection();
        conn2.setRequestMethod("PUT");
        conn2.setDoOutput(true);
        conn2.setRequestProperty("Content-Type", "application/json");

        OutputStream os = conn2.getOutputStream();
        os.write(submit.toString().getBytes());
        os.flush();

        BufferedReader resultReader2 = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
        while((line=resultReader2.readLine())!=null) System.out.println(line);
    }
}
