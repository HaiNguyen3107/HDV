package com.example.quiz;
import java.util.*;
import java.net.*;
import java.io.*;
import org.json.*;
public class RollingHash {
    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCVT175";
        String qCode = "cnLVuzUl";
        String url = "http://36.50.135.242:2230/api/rest/header?studentCode="+studentCode+"&qCode="+qCode;

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");

        BufferedReader bf =new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while((line=bf.readLine())!=null) sb.append(line);

        JSONObject response =  new JSONObject(sb.toString());
        String requestId =  response.getString("requestId");
        JSONObject data = response.getJSONObject("data");

        System.out.println(data);
        String text = data.getString("text");
        int windowSize = data.getInt("windowSize");

        //HashSet<String> seen = new HashSet<>();
        String result = "NONE";
//        for (int i = 0; i <= text.length() - windowSize; i++) {
//            String sub = text.substring(i, i + windowSize);
//            if (seen.contains(sub)) {
//                result = sub;
//                break;
//            }
//
//            seen.add(sub);
//        }
//        System.out.println(result);

        JSONObject submit = new JSONObject();
        submit.put("requestId", requestId);
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);
        submit.put("answer", result);

        HttpURLConnection conn2 = (HttpURLConnection) new URL("http://36.50.135.242:2230/api/rest/header/submit").openConnection();
        conn2.setRequestMethod("POST");
        conn2.setDoOutput(true);
        conn2.setRequestProperty("Content-Type", "application/json");

        OutputStream os=conn2.getOutputStream();
        os.write(submit.toString().getBytes());
        os.flush();

        BufferedReader resultReader2=new BufferedReader(new InputStreamReader(conn2.getInputStream()));
        while((line=resultReader2.readLine())!=null) System.out.println(line);

    }
}
