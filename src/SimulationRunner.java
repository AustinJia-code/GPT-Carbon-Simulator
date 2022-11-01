import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;

public class SimulationRunner {
    public static void main(String[] args) throws FileNotFoundException {
        int requestCount = 1000;
        Model[] models = {new Model("GPT13B", 20, 10, 2, 2),
                          new Model("GPT2", 10, 5, 2, 2)};

        CSVReader austinData = new CSVReader("ERCOT_AUSTIN_2022-03_MOER.txt");

        TreeMap<Timestamp, ArrayList<Request>> simulatedMonth = new TreeMap<>();

        for(Timestamp t : austinData.getTimestamps()){
            simulatedMonth.put(t, new ArrayList<Request>());
        }

        Random r = new Random();

        for(int i = 0; i < requestCount; i++){
            Timestamp t = austinData.randTimestamp();
            char c = (char)(r.nextInt(26) + 'a');
            simulatedMonth.get(t).add(new Request(t, Character.toString(c), models[r.nextInt(models.length)], austinData.getMOERData(t)));
        }

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
            System.out.println("Optimized: " + optimized);
            System.out.println("Not Optimized: " + notOptimized);
        }
    }
}
