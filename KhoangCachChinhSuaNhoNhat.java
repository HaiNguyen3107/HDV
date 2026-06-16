package com.example.quiz;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
public class KhoangCachChinhSuaNhoNhat {
    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCVT175";
        String qCode = "LvcjAvzs";
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
        String s = data.getString("source");
        String t = data.getString("target");
        //logic
        int[][] dp = new int[s.length()+1][t.length()+1];

        for(int i=0;i<=s.length();i++) dp[i][0]=i;
        for(int j=0;j<=t.length();j++) dp[0][j]=j;

        for(int i=1;i<=s.length();i++)
            for(int j=1;j<=t.length();j++)
                if(s.charAt(i-1)==t.charAt(j-1))
                    dp[i][j]=dp[i-1][j-1];
                else
                    dp[i][j]=1+Math.min(
                            dp[i-1][j-1],
                            Math.min(dp[i-1][j],dp[i][j-1])
                    );

        String answer = String.valueOf(dp[s.length()][t.length()]);
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
