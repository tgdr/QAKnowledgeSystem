package main.java.DataBase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class GetRealData {

    public static String getrealdata(){
        String rs = "";
        try {
            URL url = new URL("http://192.168.0.253/getsensor");
            System.out.println("okok");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");

            BufferedReader buf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result="";
            String temp;
            while((temp = buf.readLine())!=null ){
                result+=temp;
            }
            //    System.out.println(result);
            rs = getBody(result);
            System.out.println();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static String getBody(String val) {
        String start = "<body>";
        String end = "</body>";
        int s = val.indexOf(start) + start.length();
        int e = val.indexOf(end);
        return val.substring(s, e);
    }


}
