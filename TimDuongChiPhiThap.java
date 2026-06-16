package com.example.quiz;
import java.util.*;
import java.io.*;
import java.net.*;
import org.json.*;
public class TimDuongChiPhiThap {
    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCVT175";
        String qCode = "4aanImPV";
        String url = "http://36.50.135.242:2230/api/rest/object?studentCode="+studentCode+"&qCode="+qCode;

        HttpURLConnection conn=(HttpURLConnection)new URL(url).openConnection();
        conn.setRequestMethod("GET");

        BufferedReader br =new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);

        JSONObject response = new JSONObject(sb.toString());
        String requestId = response.getString("requestId");
        JSONObject data = response.getJSONObject("data");

        System.out.println(data);
        JSONArray edges = data.getJSONArray("edges");
        String start = data.getString("start");
        String end = data.getString("end");

        Map<String,List<String[]>> g = new HashMap<>();

        for(int i=0;i<edges.length();i++){
            JSONObject e=edges.getJSONObject(i);
            g.computeIfAbsent(e.getString("from"),k->new ArrayList<>())
                    .add(new String[]{
                            e.getString("to"),
                            e.getInt("weight")+""
                    });
        }

        Map<String,Integer> dist=new HashMap<>();
        Map<String,String> par=new HashMap<>();

        PriorityQueue<String[]> pq=
                new PriorityQueue<>(Comparator.comparingInt(a->Integer.parseInt(a[1])));

        dist.put(start,0);
        pq.add(new String[]{start,"0"});

        while(!pq.isEmpty()){
            String[] cur=pq.poll();

            for(String[] nxt:g.getOrDefault(cur[0],new ArrayList<>())){
                int nd=Integer.parseInt(cur[1])+Integer.parseInt(nxt[1]);

                if(nd<dist.getOrDefault(nxt[0],Integer.MAX_VALUE)){
                    dist.put(nxt[0],nd);
                    par.put(nxt[0],cur[0]);
                    pq.add(new String[]{nxt[0],nd+""});
                }
            }
        }

        List<String> path=new ArrayList<>();
        for(String x=end;x!=null;x=par.get(x))
            path.add(x);

        Collections.reverse(path);

        String result=dist.get(end)+"|"+String.join("->",path);

        JSONObject submit = new JSONObject();
        submit.put("answer",result);
        submit.put("requestId",requestId);
        submit.put("studentCode",studentCode);
        submit.put("qCode",qCode);

        HttpURLConnection postConn=(HttpURLConnection)new
                URL("http://36.50.135.242:2230/api/rest/object/submit").openConnection();
        postConn.setRequestMethod("POST");
        postConn.setDoOutput(true);
        postConn.setRequestProperty("Content-Type","application/json");
        OutputStream os=postConn.getOutputStream();
        os.write(submit.toString().getBytes());

        BufferedReader resultReader=new BufferedReader(new InputStreamReader(postConn.getInputStream()));
        while ((line = resultReader.readLine()) != null) System.out.println(line);



    }
}
