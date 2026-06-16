package com.example.quiz;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
public class GomTuAnagram {
    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCVT175";
        String qCode = "5o8Exf58";
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

        JSONArray words = data.getJSONArray("words");
        // logic
        Map<String,List<String>> mp = new TreeMap<>();
        for(int i=0;i<words.length();i++){
            String w = words.getString(i);
            char[] c = w.toCharArray();
            Arrays.sort(c);
            mp.computeIfAbsent(new String(c),k->new ArrayList<>()).add(w);
        }
        ArrayList<String> res = new ArrayList<>();
        for(List<String> g : mp.values()){
            Collections.sort(g);
            res.add(String.join(",",g));
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
