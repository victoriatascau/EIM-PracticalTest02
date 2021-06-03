package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerThread extends Thread {
    private final Map<String, String> cache = new ConcurrentHashMap<>();

    @Override
    public void run() {
        // List all IPs where the server is visible
        Enumeration interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (IOException e){
            Log.e(Constants.TAG_SERVER, "Could not query interface list: " + e.getMessage());
            if (Constants.DEBUG) {
                e.printStackTrace();
            }
        }
        String IPs = "";
        while(interfaces.hasMoreElements())
        {
            NetworkInterface n = (NetworkInterface) interfaces.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements())
            {
                InetAddress i = (InetAddress) ee.nextElement();
                if (i instanceof Inet4Address) {
                    if(IPs.length() > 0)
                        IPs += ", ";
                    IPs += i.getHostAddress().toString();
                }
            }
        }
        Log.v(Constants.TAG_SERVER, IPs);

        try {
            ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT, 50, InetAddress.getByName("0.0.0.0"));
            while (true) {
                Socket socket = serverSocket.accept();
                Log.v(Constants.TAG_SERVER, "accept()-ed: " + socket.getInetAddress());
                new CommunicationThread(socket, cache).start();
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG_SERVER, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }
}

