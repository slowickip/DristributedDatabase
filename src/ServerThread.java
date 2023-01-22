import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerThread extends Thread {
    private Socket socket;
    BufferedReader in;
    PrintWriter out;

    public ServerThread(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("IOException");
        }
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }

    public void run() {
        try {
            while (true){
                String inputString = in.readLine();
                switch (inputString.split("\\s")[0]){
                    case "terminate":{
                        if (!DatabaseNode.getMap().isEmpty()){
                            DatabaseNode.getMap().forEach((k,v)-> {
                                if (v.socket.isConnected()){
                                    try {
                                        v.println("terminating-server "+ServerAcceptThread.getAddress());
                                    } catch (UnknownHostException e) {
                                        System.out.println("RuntimeException");
                                    }
                                }
                            });
                        }
                        out.println("OK");
                        sleep(1000);
                        System.exit(-1);
                        break;
                    }
                    case "new-record":{
                        int key = Integer.parseInt(inputString.split("\\s")[1].split(":")[0]);
                        int value = Integer.parseInt(inputString.split("\\s")[1].split(":")[1]);
                        DatabaseNode.setKey(key);
                        DatabaseNode.setValue(value);
                        System.out.println("Key and value set to "+DatabaseNode.getKey()+":"+DatabaseNode.getValue());
                        out.println("OK");
                        break;
                    }
                    case "set-value":{
                        int key = Integer.parseInt(inputString.split("\\s")[1].split(":")[0]);
                        int value = Integer.parseInt(inputString.split("\\s")[1].split(":")[1]);
                        if (key == DatabaseNode.getKey()){
                            DatabaseNode.setValue(value);
                            out.println("OK");
                        }else {
                            if (!DatabaseNode.getMap().isEmpty()){
                                DatabaseNode.getMap().forEach((k,v)-> {
                                    if (v.socket.isConnected()){
                                        String result = "ERROR";
                                        try {
                                            Socket tmpSocket = new Socket();
                                            tmpSocket.connect(new InetSocketAddress(k.split(":")[0],
                                                    Integer.parseInt(k.split(":")[1])));
                                            NodeConnection tmpConnection = new NodeConnection(tmpSocket);
                                            result = tmpConnection.ask(inputString);
                                        } catch (IOException e) {
                                            //throw new RuntimeException(e);
                                        }
                                        out.println(result);
                                    }
                                });
                            }else{
                                out.println("ERROR");
                            }
                        }
                        break;
                    }
                    case "get-value":{
                        int key = Integer.parseInt(inputString.split("\\s")[1]);
                        if (key == DatabaseNode.getKey()){
                            out.println(DatabaseNode.getKey()+":"+DatabaseNode.getValue());
                        }else {
                            if (!DatabaseNode.getMap().isEmpty()){
                                DatabaseNode.getMap().forEach((k,v)-> {
                                    if (v.socket.isConnected()){
                                        String result = "ERROR";
                                        try {
                                            Socket tmpSocket = new Socket();
                                            tmpSocket.connect(new InetSocketAddress(k.split(":")[0],
                                                    Integer.parseInt(k.split(":")[1])));
                                            NodeConnection tmpConnection = new NodeConnection(tmpSocket);
                                            result = tmpConnection.ask(inputString);
                                        } catch (IOException e) {
                                            //throw new RuntimeException(e);
                                        }
                                        out.println(result);
                                    }
                                });
                            }else {
                                out.println("ERROR");
                            }
                        }
                        break;
                    }
                    case "find-key":{
                        int key = Integer.parseInt(inputString.split("\\s")[1]);
                        if (key == DatabaseNode.getKey()){
                            out.println(ServerAcceptThread.getAddress());
                        }else {
                            if (!DatabaseNode.getMap().isEmpty()){
                                DatabaseNode.getMap().forEach((k,v)-> {
                                    if (v.socket.isConnected()){
                                        String result = "ERROR";
                                        try {
                                            Socket tmpSocket = new Socket();
                                            tmpSocket.connect(new InetSocketAddress(k.split(":")[0],
                                                    Integer.parseInt(k.split(":")[1])));
                                            NodeConnection tmpConnection = new NodeConnection(tmpSocket);
                                            result = tmpConnection.ask(inputString);
                                        } catch (IOException e) {
                                            //throw new RuntimeException(e);
                                        }
                                        out.println(result);
                                    }
                                });
                            }
                        }
                        break;
                    }
                    case "get-max":{
                        if (!DatabaseNode.getMap().isEmpty()){
                            final String[] result = {DatabaseNode.getKey() + ":" + DatabaseNode.getValue()};
                                DatabaseNode.getMap().forEach((k,v)-> {
                                    if (v.socket.isConnected()){
                                        try {
                                            Socket tmpSocket = new Socket();
                                            tmpSocket.connect(new InetSocketAddress(k.split(":")[0],
                                                    Integer.parseInt(k.split(":")[1])));
                                            NodeConnection tmpConnection = new NodeConnection(tmpSocket);
                                            String ask = tmpConnection.ask(inputString);
                                            if(Integer.parseInt(ask.split(":")[1])>Integer.parseInt(result[0].split(":")[1]))
                                                result[0] = ask;
                                        } catch (IOException e) {
                                            //throw new RuntimeException(e);
                                        }
                                        out.println(result[0]);
                                    }
                                });
                            }else {
                                out.println(DatabaseNode.getValue());
                            }
                        break;
                    }
                    case "get-min": {
                        if (!DatabaseNode.getMap().isEmpty()) {
                            final String[] result = {DatabaseNode.getKey() + ":" + DatabaseNode.getValue()};
                            DatabaseNode.getMap().forEach((k, v) -> {
                                if (v.socket.isConnected()) {
                                    try {
                                        Socket tmpSocket = new Socket();
                                        tmpSocket.connect(new InetSocketAddress(k.split(":")[0],
                                                Integer.parseInt(k.split(":")[1])));
                                        NodeConnection tmpConnection = new NodeConnection(tmpSocket);
                                        String ask = tmpConnection.ask(inputString);
                                        if (Integer.parseInt(ask.split(":")[1]) < Integer.parseInt(result[0].split(":")[1]))
                                            result[0] = ask;
                                    } catch (IOException e) {
                                        //throw new RuntimeException(e);
                                    }
                                    out.println(result[0]);
                                }
                            });
                        } else {
                            out.println(DatabaseNode.getValue());
                            break;
                        }
                    }
                    case "terminating-server":{
                        DatabaseNode.getMap().remove(inputString.split("\\s")[1]);
                        break;
                    }
                    case "add-to-map":{
                        Socket tmpSocket = new Socket();
                        String stringAddress = inputString.split("\\s")[1];
                        String ip = stringAddress.split(":")[0];
                        int port = Integer.parseInt(stringAddress.split(":")[1]);
                        InetSocketAddress address = new InetSocketAddress(ip, port);
                        tmpSocket.connect(address,1000);
                        if (tmpSocket.isConnected())
                            DatabaseNode.getMap().put(stringAddress,new NodeConnection(tmpSocket));
                        break;
                    }
                    default:
                        throw new IllegalStateException("Unexpected value: " + inputString.split("\\s")[0]);
                }
                in.close();
                socket.close();
            }

        } catch (IOException e1) {
            // do nothing
            System.out.println("IOException");
        } catch (InterruptedException e) {
            System.out.println("InterruptedException");
        }
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.out.println(2);
        }
    }
}
