package au.com.confess.confess;

import android.util.Log;

import org.json.JSONException;
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
public class GetAllConfession extends Thread{
    private static final String TAG = "mytag";

    private static String URI;

    private static String result = null;

    GetAllConfession(String URI)
    {
        this.URI = URI;

        start();
    }

    public void run()
    {
        makeRequest();
    }

    public String getResult()
    {
        return result;
    }

    public static String makeRequest()
    {
        HttpURLConnection urlConnection;
        try {
            //Connect
            urlConnection = (HttpURLConnection) ((new URL(URI).openConnection()));
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            urlConnection.setRequestProperty("api_key", "27a309cb-4559-40d6-8052-6b7d1b672972");
            int responseCode = urlConnection.getResponseCode();
            Log.i(TAG, "Log Reponse code "+responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            result = response.toString();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
