import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NodeConnection {
    Socket socket;
    BufferedReader in;
    PrintWriter out;

    void println(String string){
        out.println(string);
    }

    String receive() throws IOException {
        return in.readLine();
    }

    public PrintWriter getOut() {
        return out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public Socket getSocket() {
        return socket;
    }

    String ask(String string) throws IOException {
        println(string);
        return receive();
    }

    public NodeConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }
}
