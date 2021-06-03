package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class CommunicationThread extends Thread {

    private final Socket socket;
    private final Map<String, String> cache;

    public CommunicationThread(Socket socket, Map<String, String> cache) {
        this.socket = socket;
        this.cache = cache;
    }

    private String retrieveData(String s) {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            String url = Constants.INTERNET_ADDRESS + Constants.SEARCH ;
            HttpGet httpGet = new HttpGet(url);
            Log.d(Constants.TAG_SERVER, "URL to be processed is: " + url);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            return httpClient.execute(httpGet, responseHandler);
        } catch (IOException clientProtocolException) {
            Log.e(Constants.TAG_SERVER, clientProtocolException.getMessage());
            if (Constants.DEBUG) {
                clientProtocolException.printStackTrace();
            }
        }

        return "";
    }


    private String retrieve(String hour, String minute) {
        //return cache.computeIfAbsent(hour + minute, this::retrieveData);
        return "";
    }

    @Override
    public void run() {
        try {
            Log.v(Constants.TAG_SERVER, "Connection opened to " + socket.getLocalAddress() + ":" + socket.getLocalPort() + " from " + socket.getInetAddress());

            BufferedReader bufferedReader = Utilities.getReader(socket);
            String hour = bufferedReader.readLine();
            String minute = bufferedReader.readLine();

            try {
                String content = retrieve(hour, minute);

                PrintWriter writer = Utilities.getWriter(socket);
                writer.println(content);
                writer.flush();
            } catch (IOException clientProtocolException) {
                Log.e(Constants.TAG_SERVER, clientProtocolException.getMessage());
                if (Constants.DEBUG) {
                    clientProtocolException.printStackTrace();
                }
            }

            socket.close();
            Log.v(Constants.TAG_SERVER, "Connection closed");
        } catch (IOException ioException) {
            Log.e(Constants.TAG_SERVER, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

}

