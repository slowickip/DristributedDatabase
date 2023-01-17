import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class DatabaseNode {
    public static void main(String[] args) {
        List<String> tmpLista = new ArrayList<>(args.length);
        for (String s :
                args) {
            tmpLista.add(s);
        }
        List<List<String>> argumenty = new ArrayList<>();
//        for (String s : args) {
//            System.out.println(s);
//        }
        while(!tmpLista.isEmpty()){
            if (Pattern.compile("^-.").matcher(tmpLista.get(0)).find()){
                argumenty.add(new ArrayList<>());
            }
            argumenty.get(argumenty.size()-1).add(tmpLista.get(0));
            tmpLista.remove(0);
        }
        for (List<String> l: argumenty){
            for (String s : l) {
                System.out.print(s+" ");
            }
            System.out.println();
        }
    }
}
