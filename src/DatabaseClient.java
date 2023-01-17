import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class DatabaseClient {

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
