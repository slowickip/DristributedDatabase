import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class DatabaseClient {

    Socket socket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    InetSocketAddress address = null;

    public DatabaseClient() {
    }

    public void showArguments(List<List<String>> arguments){
        for (List<String> l: arguments){
            for (String s : l)
                System.out.print(s+" ");
            System.out.println();
        }
    }

    private void gateway(String string){
        address = new InetSocketAddress(string.split(":")[0], Integer.parseInt(string.split(":")[1]));

        try {
            socket = new Socket();
            socket.connect(address, 500);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (UnknownHostException e) {
            System.out.println("Unknown host");
            System.exit(-1);
        }
        catch  (IOException e) {
            System.out.println("No I/O");
            System.exit(-1);
        }
    }

    private void operation(String operation, String argument){

    }

    public static void main(String[] args) {
        DatabaseClient databaseClient = new DatabaseClient();
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
                case "-gateway": {
                    databaseClient.gateway(l.get(1));
                    break;
                }
                case "-operation": {
                    databaseClient.operation(l.get(1),l.get(2));
                    break;
                }
                default:{
                    throw new IllegalStateException("Unexpected value: " + l.get(0));
                }
            }
        }
    }
}
