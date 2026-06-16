package com.example.quiz;


import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;

public class ChonPhanTuThuK {
    public static void main(String[] args) throws Exception {
        String studentCode="B22DCVT175";
        String qCode="oP6ykPKa";
        String url="http://36.50.135.242:2230/api/rest/path?studentCode="+studentCode+"&qCode="+qCode;

        HttpURLConnection conn=(HttpURLConnection)new URL(url).openConnection();
        conn.setRequestMethod("GET");

        BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb=new StringBuilder();
        String line;
        while((line=br.readLine())!=null) sb.append(line);

        JSONObject response=new JSONObject(sb.toString());
        String requestId=response.getString("requestId");
        JSONObject data=response.getJSONObject("data");
        System.out.println(data);
        int k = data.getInt("k");
        String type =  data.getString("type");
        JSONArray records=data.getJSONArray("records");

        ArrayList<JSONObject> list = new ArrayList<>();
        for(int i = 0; i < records.length(); i++){
            JSONObject o = records.getJSONObject(i);
            if(o.getString("type").equals(type))
                list.add(o);
        }
        list.sort((a,b) -> b.getInt("value") - a.getInt("value"));
        JSONObject ans = list.get(k - 1);
        String answer = ans.getString("id") + "|" + ans.getInt("value");

        JSONObject submit =  new JSONObject();
        submit.put("requestId", requestId);
        submit.put("answer", answer);
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);

        String submitURL = "http://36.50.135.242:2230/api/rest/path/submit?studentCode="+
                studentCode+"&qCode="+qCode + "&requestId="+requestId + "&answer="+ URLEncoder.encode(answer, "UTF-8");;
        HttpURLConnection conn2=(HttpURLConnection)new URL(submitURL).openConnection();
        conn2.setRequestMethod("GET");
        BufferedReader br2=new BufferedReader(new InputStreamReader(conn2.getInputStream()));
        while((line=br2.readLine())!=null) System.out.println(line);

    }
}
