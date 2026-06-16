package com.example.quiz;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
public class DayTangDaiNhat {
    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCVT175";
        String qCode = "jAtXPoq1";
        String url = "http://36.50.135.242:2230/api/rest/path?studentCode="+studentCode+"&qCode="+qCode;

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

        JSONArray values =  data.getJSONArray("values");
        // logic
        ArrayList<Integer> lis = new ArrayList<>();

        for(int i=0;i<values.length();i++){
            int x = values.getInt(i);
            int pos = Collections.binarySearch(lis,x);
            if(pos < 0) pos = -pos - 1;
            if(pos == lis.size())
                lis.add(x);
            else
                lis.set(pos,x);
        }
        String answer = String.valueOf(lis.size());

        //
        JSONObject submit = new JSONObject();
        submit.put("requestId", requestId);
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);
        submit.put("answer", answer);

        HttpURLConnection conn2 = (HttpURLConnection) new
                URL("http://36.50.135.242:2230/api/rest/data/submit").openConnection();
        conn2.setRequestMethod("POST");
        conn2.setDoOutput(true);
        conn2.setRequestProperty("Content-Type", "application/json");

        OutputStream os = conn2.getOutputStream();
        os.write(submit.toString().getBytes());

        BufferedReader br2 = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
        while ((line = br2.readLine()) != null) System.out.println(line);


    }
}
