package app;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {
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

    public static List<Message> getMassagesBox() {
        return massagesBox;
    }

    public static void setMassagesBox(List<Message> massagesBox) {
        Server.massagesBox = massagesBox;
    }

    public static List<Message> getMassageBoxToSend() {
        return massageBoxToSend;
    }

    public static void setMassageBoxToSend(List<Message> massageBoxToSend) {
        Server.massageBoxToSend = massageBoxToSend;
    }

    public static void sendMessage(Message message){

    }

    public static void showLogs(){

    }

    @Override
    public void run() {

    }

    public static void main(String[] args) {

    }
}
