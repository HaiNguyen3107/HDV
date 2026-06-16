package com.example.quiz;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
public class LapLichTheoDeadLine {
    public static void main(String[] args) throws Exception{
        String studentCode="B22DCVT175";
        String qCode = "hlGgan40";
        String url = "http://36.50.135.242:2230/api/rest/object?studentCode="+studentCode+"&qCode="+qCode;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);

        JSONObject response = new JSONObject(sb.toString());
        String requestId = response.getString("requestId");
        JSONObject data = response.getJSONObject("data");

        JSONArray jobs = data.getJSONArray("jobs");
        System.out.println("jobs: " + jobs);
        //
        List<JSONObject> list = new ArrayList<>();
        for (int i = 0; i < jobs.length(); i++)
            list.add(jobs.getJSONObject(i));

        list.sort(Comparator.comparingInt(o -> o.getInt("end")));

        List<String> ans = new ArrayList<>();
        int lastEnd = -1;

        for (JSONObject job : list)
            if (job.getInt("start") >= lastEnd) {
                ans.add(job.getString("id"));
                lastEnd = job.getInt("end");
            }

        String result = String.join(",", ans);
        //
        JSONObject submit = new JSONObject();
        submit.put("requestId", requestId);
        submit.put("answer", result);
        submit.put("qCode", qCode);
        submit.put("studentCode", studentCode);

        HttpURLConnection postConn =  (HttpURLConnection)
                new URL("http://36.50.135.242:2230/api/rest/object/submit").openConnection();
        postConn.setRequestMethod("POST");
        postConn.setRequestProperty("Content-Type","application/json");
        postConn.setDoOutput(true);
        OutputStream os = postConn.getOutputStream();
        os.write(submit.toString().getBytes());
        BufferedReader resultReader = new BufferedReader(new InputStreamReader(postConn.getInputStream()));
        while((line=resultReader.readLine())!=null) System.out.println(line);

    }
}
