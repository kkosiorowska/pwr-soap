package app;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private int port;
    private String address = "localhost";

    private ServerSocket server;

    private static List<Message> massagesBox = new ArrayList<>();
    private static List<Message> massageBoxToSend = new ArrayList<>();

    Server(int port) throws IOException {
        this.port = port;
        this.address = address + ":" + port;

        this.server = new ServerSocket(port, 0, InetAddress.getLocalHost());
    }

    public static void main(String[] args) {

    }
}
