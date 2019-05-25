package app;


import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server implements Runnable {

    private boolean showLogs = false;
    private String source;
    private String nextSource;
    private static int nextPort;

    private ServerSocket server;

    private static List<Message> massagesBox = new ArrayList<>();
    private static List<Message> massageBoxToSend = new ArrayList<>();

    Server(String address, int port, String nextAddress, int nextPort) throws IOException {
        this.source = address + ":" + port;
        this.nextPort = nextPort;
        this.nextSource = nextAddress + ":" + nextPort;
        this.server = new ServerSocket(port, 0, InetAddress.getByName(address));
    }

    public void sendMessage(Message message) {
        massageBoxToSend.add(message);
        //tworzymy gniazdo
        try (Socket client = new Socket(InetAddress.getByName("localhost"), nextPort)) {
            // Uzyskanie strumieni do komunikacji
            OutputStream sockOut = client.getOutputStream();
            // Komunikacja (zależna od protokołu)
            //zwracamy Obiekt SOAPMessage zadeklarowany dla naszej klasy
            //przesyla ten obiekt SOAPMessage w podanym strumieniu wyjściowym.
            message.getSoapMessage().writeTo(sockOut);
            sockOut.flush();
            // Po zakończeniu komunikacji - zamkniecie strumieni i gniazda
            sockOut.close();
        } catch (UnknownHostException exc) {
            // nieznany host
            exc.printStackTrace();
        } catch (SocketException exc) {
            // wyjątki związane z komunikacją przez gniazda
            exc.printStackTrace();

        } catch (IOException exc) {
            // inne wyjątki we/wy
        } catch (SOAPException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        while(true) {

            try {
                Message message = new Message();

                consoleLog("[MassageBox] Waiting for message.");
                Socket client = this.server.accept();
                consoleLog("[MassageBox] New message.");
                InputStream in = client.getInputStream();
                message.setSoapMessage(MessageFactory.newInstance().createMessage(null, in));
                consoleLog("[MassageBox] " + message.toString());

                if(!message.getSource().equals(source) && message.getDestination().equals("All") && !message.getSource().equals(nextSource)){
                    consoleLog("[Send] Send message to " + nextSource);
                    sendMessage(message);
                }else if(!message.getSource().equals(source) && !message.getDestination().equals(source) && !message.getSource().equals(nextSource)){
                    consoleLog("[Send] Send message to " + nextSource);
                    sendMessage(message);
                }

                if(message.getDestination().equals(source) || message.getDestination().equals("All")) {
                    consoleLog("[Info] One new massage. Check massagesBox.");
                    massagesBox.add(message);
                }

                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SOAPException e) {
                e.printStackTrace();
            }
        }
    }

    private void consoleLog(String string) {
        synchronized (System.out) {
            if(this.showLogs){
                System.out.println(string);
            }
        }
    }

    public void showLogs(){
        this.showLogs = true;
        consoleLog("[MassageBox] Waiting for message.");
        Scanner scaner = new Scanner(System.in);
        scaner.nextLine();
        this.showLogs = false;
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

    public boolean isShowLogs() {
        return showLogs;
    }

    public void setShowLogs(boolean showLogs) {
        this.showLogs = showLogs;
    }
}
