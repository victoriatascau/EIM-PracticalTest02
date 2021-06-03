package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class RetrieveTask extends android.os.AsyncTask<String, Void, Information> {

    private final Button button;
    private final TextView dataTextView;

    public RetrieveTask(Button button, TextView dataTextView) {
        this.button = button;
        this.dataTextView = dataTextView;
    }

    @Override
    public Information doInBackground(String... params) {
        String hour = params[1];
        String minute = params[2];
        Socket socket = null;
        StringBuilder content = new StringBuilder();
        try {
            String serverAddress = params[0];
            int serverPort = Integer.parseInt(params[0]);
            socket = new Socket(serverAddress, serverPort);
            if (socket == null) {
                return null;
            }
            Log.v(Constants.TAG_CLIENT, "Connection opened with " + socket.getInetAddress() + ":" + socket.getLocalPort());

            PrintWriter writer = Utilities.getWriter(socket);
            writer.println(hour);
            writer.println(minute);
            writer.flush();

            BufferedReader bufferedReader = Utilities.getReader(socket);
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                content.append(currentLine);
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG_CLIENT, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
                Log.v(Constants.TAG_CLIENT, "Connection closed");
            } catch (IOException ioException) {
                Log.e(Constants.TAG_CLIENT, "An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }

        try {
            JSONObject result = new JSONObject(content.toString());
        } catch (JSONException jsonException) {
            Log.e(Constants.TAG_CLIENT, jsonException.getMessage());
            if (Constants.DEBUG) {
                jsonException.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Information information) {
        if (information == null) {
            return;
        }

        dataTextView.setText(information.getData());
    }
}