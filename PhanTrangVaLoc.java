package com.example.quiz;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
public class PhanTrangVaLoc {
    public static void main(String[] args) throws Exception {
        String studentCode="B22DCVT175";
        String qCode="jaitQ5Xe";
        String url="http://36.50.135.242:2230/api/rest/path?studentCode="+studentCode+"&qCode="+qCode;
        HttpURLConnection conn=(HttpURLConnection)new URL(url).openConnection();
        conn.setRequestMethod("GET");
        BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb=new StringBuilder();
        String line;
        while((line=br.readLine())!=null) sb.append(line);
        JSONObject response=new JSONObject(sb.toString());
        String requestId=response.getString("requestId");
        JSONArray data=response.getJSONArray("data");
        System.out.println(data);

        int maxO = Integer.MIN_VALUE;
        int page = 0;
        String customerId = "";
        for (int i = 0; i < data.length(); i++) {
            JSONObject o=data.getJSONObject(i);
            int overdueAmount =  o.getInt("overdueAmount");
            String status = o.getString("status");
            if (status.equals("OVERDUE") && overdueAmount > maxO) {
                maxO = overdueAmount;
                customerId = o.getString("customerId");
                page = o.getInt("page");
            }
        }

        JSONObject submit = new JSONObject();
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);
        submit.put("requestId", requestId);

        String submitURL = "http://36.50.135.242:2230/api/rest/path/" + customerId +
                "?studentCode="+studentCode+"&qCode="+qCode + "&requestId=" + requestId + "&status=OVERDUE"+ "&page=" + page;
        HttpURLConnection conn2=(HttpURLConnection)new URL(submitURL).openConnection();
        conn2.setRequestMethod("GET");

        BufferedReader br2=new BufferedReader(new InputStreamReader(conn2.getInputStream()));
        while ((line=br2.readLine())!=null) sb.append(line);

    }
}
