import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;

//Save randomly generated requests into file, delayed requests (queue?), location redirection, determine algorithm effectiveness


public class SimulationRunner {
    public static void main(String[] args) throws IOException {
        //carbon output in tons e-6
        Model[] models = {new Model("GPT13B", 17, 1.5f, 3, 0.5f),
                          new Model("GENGPT2", 10, 0.5f, 3, 0.5f)};

        CSVReader austinData = new CSVReader("ERCOT_AUSTIN_2022-03_MOER.txt");

        TreeMap<Timestamp, ArrayList<Request>> simulatedMonth = generateRequests(1000, models, austinData);
        //TreeMap<Timestamp, ArrayList<Request>> simulatedMonth = readRequests(1000, models);

        float optimized = 0f;
        float notOptimized = 0f;

        for(Timestamp t : simulatedMonth.keySet()){
            System.out.println(t);
            if(simulatedMonth.get(t).size() > 0) {
                for (Request request : simulatedMonth.get(t)) {
                    if(request.getMOER() > austinData.getAverageMOER()){
                        optimized += models[1].getEstimatedCarbonOutput();
                    }else{
                        optimized += models[0].getEstimatedCarbonOutput();
                    }
                    notOptimized += models[0].getEstimatedCarbonOutput();
                }
            }
            System.out.println("Optimized: " + (Math.round(optimized*100)/100f));
            System.out.println("Not Optimized: " + (Math.round(notOptimized*100)/100f));
        }
    }

    /*
    private static TreeMap<Timestamp, ArrayList<Request>> readRequests(String fileName) throws FileNotFoundException {
        StoredRequestsReader readData = new StoredRequestsReader("Requests.txt");

    }
    */

    private static TreeMap<Timestamp, ArrayList<Request>> generateRequests(int requestCount, Model[] models, CSVReader data) throws IOException {
        TreeMap<Timestamp, ArrayList<Request>> simulatedMonth = new TreeMap<>();

        for(Timestamp t : data.getTimestamps()){
            simulatedMonth.put(t, new ArrayList<Request>());
        }

        Random r = new Random();
        FileWriter storeRequests = new FileWriter("Requests.txt");

        for(int i = 0; i < requestCount; i++){
            Timestamp t = data.randTimestamp();
            char c = (char)(r.nextInt(26) + 'a');
            Request request = new Request(t, Character.toString(c), models[r.nextInt(models.length)], data.getMOERData(t));
            storeRequests.write(request.toCSV() + "\n");
            simulatedMonth.get(t).add(request);
        }

        return simulatedMonth;
    }
}
