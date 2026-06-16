package com.example.quiz;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
public class KhopNhieuMauTrongLog {
    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCVT175";
        String qCode = "CtuOeGkl";
        String url = "http://36.50.135.242:2230/api/rest/character?studentCode="+studentCode+"&qCode="+qCode;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);

        JSONObject response = new JSONObject(sb.toString());
        String requestId = response.getString("requestId");
        JSONObject data = response.getJSONObject("data");
        System.out.println(data);
        String text = data.getString("text");
        JSONArray patterns = data.getJSONArray("patterns");
        // logic
        ArrayList<String> res = new ArrayList<>();
        for(int i=0;i<patterns.length();i++){
            String p = patterns.getString(i);
            int cnt = 0;
            for(int j=0;j+p.length()<=text.length();j++)
                if(text.substring(j,j+p.length()).equals(p))
                    cnt++;
            res.add(p + "=" + cnt);
        }
        String answer = String.join("|",res);
        //
        JSONObject submit =  new JSONObject();
        submit.put("requestId",requestId);
        submit.put("answer",answer);
        submit.put("studentCode",studentCode);
        submit.put("qCode",qCode);

        HttpURLConnection conn = (HttpURLConnection) new URL("http://36.50.135.242:2230/api/rest/character/submit").openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type","application/json");

        OutputStream os = conn.getOutputStream();
        os.write(submit.toString().getBytes());

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        while ((line = reader.readLine()) != null) System.out.println(line);

    }
}
