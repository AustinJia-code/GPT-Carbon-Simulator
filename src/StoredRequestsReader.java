import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class StoredRequestsReader {
    public StoredRequestsReader(String fileName, CSVReader data) throws FileNotFoundException {
        Timestamp timestamp;
        String prompt;
        Model model;
        MOERData moerData;
        Request request;
        HashMap<Timestamp, ArrayList<Request>> simulatedMonth = new HashMap<>();

        Scanner scan = new Scanner(new File(fileName));

        for(Timestamp t : data.getTimestamps()){
            simulatedMonth.put(t, new ArrayList<Request>());
        }

        scan.nextLine();
        while(scan.hasNext()){
            String[] csvLine = scan.nextLine().split(",");
            timestamp = new Timestamp(csvLine[0]);
            prompt = csvLine[1];
            model = new Model(csvLine[2], Float.parseFloat(csvLine[3]), Float.parseFloat(csvLine[4]), Float.parseFloat(csvLine[5]), Float.parseFloat(csvLine[6]));
            moerData = new MOERData(timestamp, Float.parseFloat(csvLine[8]), Float.parseFloat(csvLine[6]), Float.parseFloat(csvLine[6]));
            request = new Request(timestamp, prompt, model, moerData);

            simulatedMonth.get(timestamp).add(request);
        }
    }
}
