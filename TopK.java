package com.example.quiz;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
public class TopK {
    public static void main(String[] args) throws Exception {
        String studentCode="B22DCVT175";
        String qCode="y68s9m7x";
        //Get
        String url="http://36.50.135.242:2230/api/rest/data?studentCode="+studentCode+"&qCode="+qCode;
        HttpURLConnection conn=(HttpURLConnection)new URL(url).openConnection();
        conn.setRequestMethod("GET");

        BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb=new StringBuilder();
        String line;
        while((line=br.readLine())!=null) sb.append(line);

        JSONObject response=new JSONObject(sb.toString());
        String requestId=response.getString("requestId");
        JSONObject data=response.getJSONObject("data");

        // Logic
        JSONArray codes=data.getJSONArray("codes");
        int k=data.getInt("k");

        Map<String,Integer> map=new HashMap<>();
        for(int i=0;i<codes.length();i++) {
            map.put(codes.getString(i), map.getOrDefault(codes.getString(i), 0) + 1);
        }

        List<Map.Entry<String,Integer>> list=new ArrayList<>(map.entrySet());

        list.sort((a, b) -> {
            if (!a.getValue().equals(b.getValue())) {
                return b.getValue() - a.getValue();

            } else {
                return a.getKey().compareTo(b.getKey());
            }
        });

        StringBuilder answer=new StringBuilder();

        for(int i=0;i<k&&i<list.size();i++){
            if(i>0) answer.append("|");
            answer.append(list.get(i).getKey());
            answer.append("=");
            answer.append(list.get(i).getValue());
        }
        String result=answer.toString();
        System.out.println(result);

        //Post
        JSONObject submit=new JSONObject();
        submit.put("studentCode",studentCode);
        submit.put("qCode",qCode);
        submit.put("requestId",requestId);
        submit.put("answer",result);

        HttpURLConnection postConn=(HttpURLConnection)new
                URL("http://36.50.135.242:2230/api/rest/data/submit").openConnection();
        postConn.setRequestMethod("POST");
        postConn.setRequestProperty("Content-Type","application/json");
        postConn.setDoOutput(true);
        OutputStream os=postConn.getOutputStream();
        os.write(submit.toString().getBytes());

        BufferedReader resultReader=new BufferedReader(new InputStreamReader(postConn.getInputStream()));
        while((line=resultReader.readLine())!=null) System.out.println(line);
    }

}
