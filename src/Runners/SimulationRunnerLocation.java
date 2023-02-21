package Runners;

import StorageClasses.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;

//Save randomly generated requests into file, delayed requests (queue?), location redirection, determine algorithm effectiveness


public class SimulationRunnerLocation {
    public static void main(String[] args) throws IOException {
        double cpu_util, carbon, energy, power;
        double exe_time;
        String location = "";
        cpu_util = 0.5;
        power = Math.round((1.37*cpu_util + 41.32)*100)/100;
        //carbon output in tons e-6
        Model[] models = {new Model("GPT13B", 17, 1.5f, 3, 0.5f),
                new Model("GENGPT2", 10, 0.5f, 3, 0.5f)};

        CSVReader austinData = new CSVReader("ERCOT_AUSTIN_2022-08_MOER.txt");
        CSVReader calData = new CSVReader("CAISO_NORTH_2022-08_MOER.txt");

        //TreeMap<Timestamp, ArrayList<Request>> simulatedMonth = generateRequests(1000, models, austinData);
        TreeMap<StorageClasses.Timestamp, ArrayList<StorageClasses.Request>> simulatedMonth = readRequests("Requests.txt", austinData);

        float optimized = 0f;
        float notOptimized = 0f;

        for(Timestamp t : simulatedMonth.keySet()){
            if(simulatedMonth.get(t).size() > 0) {
                for (Request request : simulatedMonth.get(t)) {
                    if(austinData.getMOER(t) < calData.getMOER(t)) {
                        location = "Texas";
                        exe_time = austinData.getMOER(t) > austinData.getAverageMOER() ? models[1].getEstimatedRuntime() : models[0].getEstimatedRuntime();
                        energy = power*exe_time / 1000 / 3600; //kilowatt hours
                        carbon = energy/1000 * austinData.getMOER(t) * 453.592;
                    }else {
                        location = "California";
                        exe_time = calData.getMOER(t) > calData.getAverageMOER() ? models[1].getEstimatedRuntime() : models[0].getEstimatedRuntime();
                        energy = power*exe_time / 1000 / 3600; //kilowatt hours
                        carbon = energy/1000 * calData.getMOER(t) * 453.592;
                    }

                    optimized += carbon;

                    exe_time = models[0].getEstimatedRuntime();
                    energy = power*exe_time / 1000 / 3600; //kilowatt hours
                    carbon = energy/1000 * austinData.getMOER(t) * 453.592;
                    notOptimized += carbon;
                }
            }
            System.out.println("Optimized: " + (Math.round(optimized*100)/100f));
            System.out.println("Not Optimized: " + (Math.round(notOptimized*100)/100f));
        }
        //System.out.println(t + " " + location);
        System.out.println("Optimized (g): " + (Math.round(optimized*100)/100f));
        System.out.println("Not Optimized (g): " + (Math.round(notOptimized*100)/100f));
    }

    private static TreeMap<Timestamp, ArrayList<Request>> readRequests(String fileName, CSVReader data) throws FileNotFoundException {
        Timestamp timestamp;
        String prompt;
        Model model;
        MOERData moerData;
        Request request;
        TreeMap<Timestamp, ArrayList<Request>> simulatedMonth = new TreeMap<>();

        Scanner scan = new Scanner(new File(fileName));

        for(Timestamp t : data.getTimestamps()){
            simulatedMonth.put(t, new ArrayList<Request>());
        }

        scan.nextLine();
        while(scan.hasNext()){
            String[] csvLine = scan.nextLine().split(",");
            if(csvLine.length == 11) {
                timestamp = new Timestamp(csvLine[0]);
                prompt = csvLine[1];
                model = new Model(csvLine[2], Float.parseFloat(csvLine[3]), Float.parseFloat(csvLine[4]), Float.parseFloat(csvLine[5]), Float.parseFloat(csvLine[6]));
                moerData = new MOERData(timestamp, Float.parseFloat(csvLine[8]), Float.parseFloat(csvLine[9]), Float.parseFloat(csvLine[10]));
                request = new Request(timestamp, prompt, model, moerData);
                simulatedMonth.get(timestamp).add(request);
            }
        }

        return simulatedMonth;
    }

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
