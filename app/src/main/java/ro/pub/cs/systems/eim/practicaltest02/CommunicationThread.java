package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class CommunicationThread extends Thread {

    private final Socket socket;

    public CommunicationThread(Socket socket) {
        this.socket = socket;
    }

    public Pair<Integer, Integer> getTime() throws IOException {
        try (Socket socket = new Socket("utcnist.colorado.edu", 13)) {
            BufferedReader bufferedReader = Utilities.getReader(socket);

            String empty = bufferedReader.readLine();

            String line = bufferedReader.readLine();
            Log.v(Constants.TAG_SERVER, "Time " + line);
            String[] parts = line.split(" ")[2].split(":");

            return new Pair<>(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }
    }


    @Override
    public void run() {
        try {
            Log.v(Constants.TAG_SERVER, "Connection opened to " + socket.getLocalAddress() + ":" + socket.getLocalPort() + " from " + socket.getInetAddress());

            try {
                PrintWriter writer = Utilities.getWriter(socket);
                BufferedReader bufferedReader = Utilities.getReader(socket);

                boolean set = false;
                int hour = 0;
                int minute = 0;

                while (true) {

                    String request = bufferedReader.readLine();
                    String[] parts = request.split(",");

                    if (parts.length == 3 && parts[0].equals("set")) {

                        set = true;
                        hour = Integer.parseInt(parts[1]);
                        minute = Integer.parseInt(parts[2]);

                        Log.v(Constants.TAG_SERVER, "Set to " + hour + ":" + minute);

                        continue;
                    }
                    if (parts[0].equals("reset")) {
                        set = false;

                        continue;
                    }
                    if (parts[0].equals("poll")) {
                        Pair<Integer, Integer> currentTime = getTime();
                        int hourNow = currentTime.first;
                        int minuteNow = currentTime.second;

                        if (!set) {
                            writer.println("none");
                        } else {
                            if (hour > hourNow) {
                                writer.println("active");
                            } else if (hour == hourNow) {
                                if (minute >= minuteNow) {
                                    writer.println("active");
                                } else {
                                    writer.println("inactive");
                                }
                            } else {
                                writer.println("inactive");
                            }
                        }
                        writer.flush();

                        continue;
                    }
                }
            } catch (Exception ex) {
                Log.e(Constants.TAG_SERVER, ex.getMessage());
                if (Constants.DEBUG) {
                    ex.printStackTrace();
                }
            }

            socket.close();
            Log.v(Constants.TAG_SERVER, "Connection closed");
        } catch (Exception ex) {
            Log.e(Constants.TAG_SERVER, "An exception has occurred: " + ex.getMessage());
            if (Constants.DEBUG) {
                ex.printStackTrace();
            }
        }
    }

}

