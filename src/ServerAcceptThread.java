import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerAcceptThread extends Thread {
    static ServerSocket server = null;
    static int localPort;
    boolean exit = false;

    public static String getAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress() +":"+server.getLocalPort();
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public ServerAcceptThread(int localPort) {
        this.localPort = localPort;
    }

    public void run(){
        Socket client = null;
        try {
            server = new ServerSocket(localPort);
            server.setReuseAddress(true);
        }
        catch (IOException e) {
            System.out.println("Could not listen");
            //System.exit(-1);
        }

        System.out.println("Server listens on port: " + server.getLocalPort());

        while(true) {
            try {
                client = server.accept();
            }
            catch (IOException e) {
                System.out.println("Accept failed");
            }
            (new ServerThread(client)).start();
        }
    }
}
