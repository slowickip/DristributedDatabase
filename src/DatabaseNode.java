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
    }

    private void record(String string){
        this.key = Integer.parseInt(string.split(":")[0]);
        this.value = Integer.parseInt(string.split(":")[1]);
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
