package utb.fai;

public class App {

    public static void main(String[] arguments) {
        String serverAddress = "127.0.0.1";
        int serverPort = 9999;

        if (arguments.length > 1) {
            serverAddress = arguments[0];
            try {
                serverPort = Integer.parseInt(arguments[1]);
            } catch (NumberFormatException exception) {
                System.err.println("Chybny port: " + arguments[1]);
                System.exit(1);
            }
        } else if (arguments.length == 1) {
            serverAddress = arguments[0];
        }

        try {
            TelnetClient clientInstance = new TelnetClient(serverAddress, serverPort);
            clientInstance.run();
        } catch (Exception exception) {
            System.err.println("Nepodarilo se pripojit na " + serverAddress + ":" + serverPort);
            exception.printStackTrace();
        }
    }
}