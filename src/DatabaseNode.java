import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class DatabaseNode {

    private static int key, value;

    public DatabaseNode() {
    }

    public void showArguments(List<List<String>> arguments){
        for (List<String> l: arguments){
            for (String s : l)
                System.out.print(s+" ");
            System.out.println();
        }
    }

    private void connect(String string){

    }

    private void tcpport(String string){
        int localPort = Integer.parseInt(string);

        ServerSocket server = null;
        Socket client = null;
        try {
            server = new ServerSocket(localPort);
        }
        catch (IOException e) {
            System.out.println("Could not listen");
            System.exit(-1);
        }

        System.out.println("Server listens on port: " + server.getLocalPort());

        while(true) {
            try {
                client = server.accept();
            }
            catch (IOException e) {
                System.out.println("Accept failed");
                System.exit(-1);
            }

            (new ServerThread(client)).start();
        }

    }

    private void record(String string){
        this.key = Integer.parseInt(string.split(":")[0]);
        this.value = Integer.parseInt(string.split(":")[1]);
    }

    public static class ServerThread extends Thread {
        private final Socket socket;

        public ServerThread(Socket socket) {
            super();
            this.socket = socket;
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                //tutaj kod
                String in1 = in.readLine();
                System.out.println(in1);
            } catch (IOException e1) {
                // do nothing
                System.out.println('1');
            }
            try {
                socket.close();
            } catch (IOException e) {
                // do nothing
                System.out.println('2');
            }
        }
    }

    public static void main(String[] args) {
        DatabaseNode databaseNode = new DatabaseNode();
        List<String> tmpList = new ArrayList<>(args.length);
        List<List<String>> arguments = new ArrayList<>();
        for (String s : args)
            tmpList.add(s);

        while(!tmpList.isEmpty()){
            // If argument starts with "-" create new node inside "arguments"
            if (Pattern.compile("^-.").matcher(tmpList.get(0)).find())
                arguments.add(new ArrayList<>());
            arguments.get(arguments.size()-1).add(tmpList.get(0));
            tmpList.remove(0);
        }

        for (List<String> l : arguments) {
            switch (l.get(0)){
                case "-connect": {
                    databaseNode.connect(l.get(1));
                    break;
                }
                case "-tcpport": {
                    databaseNode.tcpport(l.get(1));
                    break;
                }
                case "-record": {
                    databaseNode.record(l.get(1));
                    break;
                }
                default:{
                    throw new IllegalStateException("Unexpected value: " + l.get(0));
                }
            }
        }
    }
}
