package au.com.confess.confess;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by admin on 17/07/15.
 */
public class HttpCreateNewLog extends Thread{

    private static String URI;
    private static JSONObject json;
    private static String result = null;
    HttpCreateNewLog(String URI, JSONObject json)
    {
        this.URI = URI;
        this.json= json;
        start();
    }

    public void run() {
        makeRequest();
    }

    public String getResult() {
        return result;
    }

    public static String makeRequest() {
        HttpURLConnection urlConnection;

        String data = json.toString();

        try {
            //Connect
            urlConnection = (HttpURLConnection) ((new URL(URI).openConnection()));
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("api_key", "27a309cb-4559-40d6-8052-6b7d1b672972");
            urlConnection.connect();

            //Write
            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(data);
            writer.close();
            outputStream.close();

            //Read
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            bufferedReader.close();
            result = sb.toString();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
