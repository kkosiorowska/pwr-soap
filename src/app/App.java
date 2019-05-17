package app;

import javax.xml.soap.SOAPException;
import java.io.IOException;
import java.util.Scanner;

public class App {
    String address;
    String nextAddress;

    Server server;
    Thread serverThread;

    public App(String address, int port, String nextAddress, int nextPort) {

        this.address = address + ":" + port;
        this.nextAddress = nextAddress+ ":" + nextPort;
    }

    public void creatMessage() throws IOException, SOAPException {
        Scanner scaner = new Scanner(System.in);
        String choice;
        String message = null;
        String destination = null;
        String port;

        consoleLog("-------------------Send message-------------------");
        consoleLog("1. - Unicast");
        consoleLog("2. - Breadcast");
        consoleLog("Choice:");
        choice = scaner.nextLine();

        switch (choice) {
            case "1":
                consoleLog("Unicast");
                consoleLog("Message: ");
                message = scaner.nextLine();
                consoleLog("Send to: ");
                port = scaner.nextLine();
                destination = "localhost:"+port;
                break;
            case "2":
                consoleLog("Breadcast");
                consoleLog("Message: ");
                message = scaner.nextLine();
                destination = "All";
                break;
            default:
                consoleLog("Error!");
        }

        Message mewMessage = new Message(address);
        mewMessage.setDestination(destination);
        mewMessage.setMessage(message);
        Server.sendMessage(mewMessage);
    }

    public void readMessages() throws SOAPException {
        consoleLog("-------------------Read mesages-------------------");
        for (int i = 0; i < server.getMassagesBox().size(); i++) {
            consoleLog(i + ". From: " + server.getMassagesBox().get(i).getSource());
            consoleLog("Message: " + server.getMassagesBox().get(i).getMessage());
        }
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
            //consoleLog("4. - Połącz do serwera");
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
                case "4":
//                    if(!connected){
//                        startServer();
//                        connected = true;
//                        writeToConsole("Połączono");
//                    }
//                    writeToConsole("Aktualnie połączony");
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
