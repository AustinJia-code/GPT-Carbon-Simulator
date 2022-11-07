import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class StoredRequestsReader {
    HashMap<Timestamp, ArrayList<Request>> data;
    public StoredRequestsReader(String fileName) throws FileNotFoundException {
        Scanner scan = new Scanner(new File(fileName));
        data = new HashMap<>();

        scan.nextLine();
        while(scan.hasNext()){
            String[] csvLine = scan.nextLine().split(",");
            Timestamp timestamp = new Timestamp(csvLine[0]);
            //data.put();
        }
    }
}
