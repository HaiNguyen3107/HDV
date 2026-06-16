package com.example.quiz;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
public class DoiSoatThanhToan {
    public static void main(String[] args) throws Exception{
        String qCode = "yb63GtYz";
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
        int failedCount = 0;
        double capturedTotal = 0.0;
        double refundedTotal = 0.0;
        for (int i = 0; i < data.length(); i++) {
            String status = data.getJSONObject(i).getString("status");
            if (status.equals("CAPTURED")) {
                capturedTotal += data.getJSONObject(i).getDouble("amount");
            } else if (status.equals("REFUNDED")) {
                refundedTotal += data.getJSONObject(i).getDouble("amount");
            } else if (status.equals("FAILED")) {
                failedCount++;
            }
        }
        double netTotal = (double) capturedTotal -  refundedTotal;
        JSONObject answer = new JSONObject();
        answer.put("capturedTotal", String.format("%.2f", capturedTotal));
        answer.put("refundedTotal", String.format("%.2f", refundedTotal));
        answer.put("failedCount", failedCount);
        answer.put("netTotal", String.format("%.2f", netTotal));

        JSONObject submit = new JSONObject();
        submit.put("requestId", requestId);
        submit.put("answer", answer);
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);

        HttpURLConnection conn2 = (HttpURLConnection) new URL("http://36.50.135.242:2230/api/rest/data/submit").openConnection();
        conn2.setRequestMethod("POST");
        conn2.setDoOutput(true);
        conn2.setRequestProperty("Content-Type", "application/json");

        OutputStream os = conn2.getOutputStream();
        os.write(submit.toString().getBytes());

        BufferedReader bf2 = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
        while ((line = bf2.readLine()) != null) System.out.println(line);

    }
}
