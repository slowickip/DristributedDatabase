import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.net.UnknownHostException;

public class DatabaseNode {

    private static int key, value;
    private static HashMap<String,NodeConnection> map;

    public DatabaseNode() {
        map = new HashMap<>();
    }

    public static int getKey() {
        return key;
    }

    public static int getValue() {
        return value;
    }

    public static void setKey(int key) {
        DatabaseNode.key = key;
    }

    public static void setValue(int value) {
        DatabaseNode.value = value;
    }

    public static HashMap<String, NodeConnection> getMap() {
        return map;
    }

    public void showArguments(List<List<String>> arguments){
        for (List<String> l: arguments){
            for (String s : l)
                System.out.print(s+" ");
            System.out.println();
        }
    }

    private void connect(String string) throws IOException {
        String ip = string.split(":")[0];
        int port = Integer.parseInt(string.split(":")[1]);
        if (ip.equals("localhost"))
            ip = InetAddress.getLocalHost().getHostAddress();
        String stringAddress = ip+":"+port;
        InetSocketAddress address = new InetSocketAddress(ip, port);
        Socket tmpSocket = new Socket();
        try {
            tmpSocket.connect(address,1000);
        } catch (UnknownHostException e) {
            System.out.println("Could not connect to "+string);
        }
        catch  (IOException e) {
            System.out.println("Could not connect to "+string);
        }
        if (tmpSocket.isConnected()) {
            getMap().put(stringAddress, new NodeConnection(tmpSocket));
            getMap().get(stringAddress).println("add-to-map " + ServerAcceptThread.getAddress());
            System.out.println("Connected to " + stringAddress);
        }
    }

    private void tcpport(String string){
        ServerAcceptThread sat = new ServerAcceptThread(Integer.parseInt(string));
        sat.start();
    }

    private void record(String string){
        this.key = Integer.parseInt(string.split(":")[0]);
        this.value = Integer.parseInt(string.split(":")[1]);
        System.out.println("Key and value set to "+getKey()+":"+getValue());
    }

    public static void main(String[] args) throws IOException {
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
