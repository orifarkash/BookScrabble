package Model.Logic;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class MessageHandler implements ClientHandler {

    public HostServer hostServer;

    @Override
    public void handleClient(InputStream in, OutputStream out) throws IOException {
        Scanner fGuest = new Scanner(in); //read from the guest query.
        //PrintWriter sendToGuest = new PrintWriter(out); //the answer to the guest will sent it in.
        String message = fGuest.next(); //read the first line from the guest.
        String[] msg = message.split(";"); // id;method;args...
        String id = msg[0];
        String method = msg[1];
        String args = msg[2];
        String[] argsArr = args.split(",");


        //check here witch methodName
        if (method.equals("challenge")) {
            System.out.println("Send to Server --->CHALLENGE");
            Socket myServer = hostServer.sendToMyServer("C", argsArr[0]); //send to the server the query from the guest to the server in format: ("C" , word)
            Scanner scan = new Scanner(myServer.getOutputStream().toString());
            Boolean answerFromServer = Boolean.getBoolean(scan.next()); //after get the message, the server will send the answer.
            hostServer.notifyObservers(id + ":" + "challenge :" + args + "," + answerFromServer);

        } if (method.equals("tryPlaceWord")) {
            System.out.println("Send to Server --->TRYPLACEWORD");
            Socket myServer = hostServer.sendToMyServer("Q", argsArr[0]); //send to the server the query from the guest to the server in format: ("Q" , word)
            Scanner scan = new Scanner(myServer.getOutputStream().toString());
            Boolean answerFromServer = Boolean.getBoolean(scan.next()); //after get the message, the server will send the answer.
            hostServer.notifyObservers(id + ":" + "tryPlaceWord :" + args + "," + answerFromServer);
        }
        /**
         * Notify the host model about the
         */
        else {
            hostServer.updateObservers(message);
        }

    }


    @Override
    public void close() {
        if (hostServer != null) {
            hostServer.close(); // Close the host server if it is not null
        }
    }

}