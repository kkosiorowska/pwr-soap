package app;

import javax.xml.soap.SOAPException;
import java.io.IOException;
import java.util.Scanner;

public class App {
    String address;
    String nextAddress;

    Server server;
    Thread serverThread;

    public App(String address, int port, String nextAddress, int nextPort) throws IOException {

        this.address = address + ":" + port;
        this.nextAddress = nextAddress+ ":" + nextPort;

        server = new Server(address, port, nextAddress, nextPort);
    }

    public void creatMessage() throws IOException, SOAPException {
        Scanner scaner = new Scanner(System.in);
        String choice;
        String message = null;
        String destination = null;
        String port;
        String name;

        consoleLog("-------------------Send message-------------------");
        consoleLog("1. - Unicast");
        consoleLog("2. - Broadcast");
        consoleLog("Choice:");
        choice = scaner.nextLine();

        switch (choice) {
            case "1":
                consoleLog("Unicast");
                consoleLog("Message: ");
                message = scaner.nextLine();
                consoleLog("Send to : ");
                name = scaner.nextLine();
                consoleLog("Port : ");
                port = scaner.nextLine();
                destination = name + ":" + port;
                consoleLog("Send to " + destination);
                break;
            case "2":
                consoleLog("Broadcast");
                consoleLog("Message: ");
                message = scaner.nextLine();
                destination = "All";
                consoleLog("Send to " + destination);
                break;
            default:
                consoleLog("Error!");
        }

        Message mewMessage = new Message();
        mewMessage.setSource(address);
        mewMessage.setDestination(destination);
        mewMessage.setMessage(message);
        server.sendMessage(mewMessage);
    }

    public void readMessages() throws SOAPException {
            consoleLog("-------------------Read mesages-------------------");

            if(server.getMassagesBox().size()==0) consoleLog("No message.");
            else{
                for (int i = 1; i < server.getMassagesBox().size()+1; i++) {
                    consoleLog(i + ". From: " + server.getMassagesBox().get(i-1).getSource());
                    consoleLog("Text: " + server.getMassagesBox().get(i-1).getMessage());
                }
            }
            Scanner scaner = new Scanner(System.in);
            consoleLog("");
            scaner.nextLine();
    }

    public void showLogs(){
        consoleLog("-------------------Show logs-------------------");
        server.showLogs();
    }

    public void menu() throws IOException, SOAPException {
        serverThread = new Thread(server);
        serverThread.start();

        Scanner scaner = new Scanner(System.in);
        String choice;

        while (true) {
            consoleLog("--------------------------------------------------");
            consoleLog("-----------------------MENU-----------------------");
            consoleLog("--------------------------------------------------");
            consoleLog("1. - Send message");
            consoleLog("2. - Read message");
            consoleLog("3. - Show logs");
            consoleLog("0. - Exit");
            consoleLog("Choice:");
            choice = scaner.nextLine();
            switch (choice) {
                case "1":
                    creatMessage();
                    break;
                case "2":
                    readMessages();
                    break;
                case "3":
                    showLogs();
                    break;
                case "0":
                    System.exit(0);;
                default:
                    consoleLog("Error!");
            }
        }
    }

    private void consoleLog(String string) {
        synchronized (System.out) {
            System.out.println(string);
        }
    }

    public static void main(String[] args) throws IOException, SOAPException {
        if(args.length == 4 ){
            App app = new App (args[0], Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]));
            app.menu();
        }
        else{
            System.out.println("Incorrect amount of args");
            return;
        }
    }
}
