package com.example.quiz;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {

    public static void main(String[] args) {
        try {

            String studentCode = "B22DCVT175";
            String qCode = "sYyNho5Y";

            // GET
            String url = "http://36.50.135.242:2230/api/rest/path"
                    + "?studentCode=" + studentCode
                    + "&qCode=" + qCode;

            HttpURLConnection conn =
                    (HttpURLConnection) new URL(url).openConnection();

            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            br.close();

            System.out.println(response);

            // =====================
            // Ở đây parse JSON
            // lấy requestId
            // lấy records
            // lấy target
            // binary search
            // =====================

            String answer = "R3"; // ví dụ

            String body =
                    "{"
                            + "\"studentCode\":\"" + studentCode + "\","
                            + "\"qCode\":\"" + qCode + "\","
                            + "\"requestId\":\"REQUEST_ID\","
                            + "\"answer\":\"" + answer + "\""
                            + "}";

            HttpURLConnection postConn =
                    (HttpURLConnection) new URL(
                            "http://36.50.135.242:2230/api/rest/path/submit"
                    ).openConnection();

            postConn.setRequestMethod("POST");
            postConn.setRequestProperty(
                    "Content-Type",
                    "application/json"
            );

            postConn.setDoOutput(true);

            OutputStream os = postConn.getOutputStream();
            os.write(body.getBytes());
            os.flush();
            os.close();

            BufferedReader postBr = new BufferedReader(
                    new InputStreamReader(postConn.getInputStream()));

            StringBuilder postResponse = new StringBuilder();

            while ((line = postBr.readLine()) != null) {
                postResponse.append(line);
            }

            postBr.close();

            System.out.println(postResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}