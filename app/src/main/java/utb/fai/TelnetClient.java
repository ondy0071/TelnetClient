package utb.fai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TelnetClient {
    private final String remoteHost;
    private final int remotePort;

    public TelnetClient(String remoteHost, int remotePort) {
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    public void run() throws IOException, InterruptedException {
        try (Socket clientSocket = new Socket(remoteHost, remotePort)) {
            Thread incomingListener = new Thread(() -> listenSocket(clientSocket));
            Thread outgoingSender = new Thread(() -> sendStandardInput(clientSocket));

            incomingListener.start();
            outgoingSender.start();

            outgoingSender.join();
        }
    }

    private static void listenSocket(Socket socket) {
        try (BufferedReader inputReader = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {
            char[] dataChunk = new char[1024];
            int length;
            while ((length = inputReader.read(dataChunk)) != -1) {
                System.out.print(new String(dataChunk, 0, length));
                System.out.flush();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void sendStandardInput(Socket socket) {
        try {
            BufferedReader consoleInput = new BufferedReader(
                    new InputStreamReader(System.in, StandardCharsets.UTF_8));
            PrintWriter networkWriter = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

            String userInput;
            while ((userInput = consoleInput.readLine()) != null) {
                networkWriter.print(userInput + "\r\n");
                networkWriter.flush();
                if ("/QUIT".equals(userInput)) {
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}