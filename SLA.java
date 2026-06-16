package com.example.quiz;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;


public class SLA {
    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCVT175";
        String qCode = "Tx94JTmQ";

        String url = "http://36.50.135.242:2230/api/rest/object?studentCode=" + studentCode + "&qCode=" + qCode;
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);

        JSONObject obj = new JSONObject(sb.toString());
        String requestId = obj.getString("requestId");
        JSONObject data = obj.getJSONObject("data");

        int maxEtaDays = data.getInt("maxEtaDays");
        JSONArray quotes =  data.getJSONArray("quotes");
        double weightKg = data.getDouble("weightKg");
        System.out.println("maxEtaDays: " + maxEtaDays);
        System.out.println("quotes: " + quotes);

        String bestCarrier = "";
        double bestFee = Double.MAX_VALUE;
        int bestEta = 0;
        double bestReliability = -1;

        for (int i = 0; i < quotes.length(); i++) {
            JSONObject q = quotes.getJSONObject(i);

            String carrier = q.getString("carrier");
            double baseFee = q.getDouble("baseFee");
            double perKgFee = q.getDouble("perKgFee");
            int etaDays = q.getInt("etaDays");
            double reliability = q.getDouble("reliability");

            double fee = baseFee + perKgFee * weightKg;

            if (etaDays <= maxEtaDays) {
                if (fee < bestFee || (Math.abs(fee - bestFee) < 1e-9 && reliability > bestReliability)) {
                    bestFee = fee;
                    bestEta = etaDays;
                    bestReliability = reliability;
                    bestCarrier = carrier;
                }
            }
        }

        String result = bestCarrier + "|" + String.format(java.util.Locale.US, "%.2f", bestFee) + "|" + bestEta;
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
