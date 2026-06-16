package com.example.quiz;
import java.util.*;
import java.util.zip.*;
import java.io.*;
import java.net.*;
import org.json.*;
public class ChecksumCRC32 {
    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCVT175";
        String qCode = "XuuHdhjM";
        String url = "http://36.50.135.242:2230/api/rest/header?studentCode="+studentCode+"&qCode="+qCode;

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

        String payload = data.getString("payload");
        CRC32 crc = new CRC32();
        crc.update(payload.getBytes());

        String result = Long.toHexString(crc.getValue());

        JSONObject submit = new JSONObject();
        submit.put("qCode", qCode);
        submit.put("studentCode", studentCode);
        submit.put("requestId", requestId);
        submit.put("answer", result);

        HttpURLConnection conn2 = (HttpURLConnection) new URL("http://36.50.135.242:2230/api/rest/header/submit").openConnection();
        conn2.setRequestMethod("POST");
        conn2.setDoOutput(true);
        conn2.setRequestProperty("Content-Type", "application/json");

        OutputStream os=conn2.getOutputStream();
        os.write(submit.toString().getBytes());
        os.flush();
        BufferedReader resultReader=new BufferedReader(new InputStreamReader(conn2.getInputStream()));
        while((line=resultReader.readLine())!=null) System.out.println(line);
    }
}
