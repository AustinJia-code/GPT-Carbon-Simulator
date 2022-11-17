package StorageClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CSVReader {
    TreeMap<Timestamp, MOERData> data;

    public CSVReader(String fileName) throws FileNotFoundException {
        Scanner scan = new Scanner(new File(fileName));
        data = new TreeMap<>();

        scan.nextLine();
        while(scan.hasNext()){
            String[] csvLine = scan.nextLine().split(",");
            Timestamp timestamp = new Timestamp(csvLine[0]);
            data.put(timestamp, new MOERData(timestamp, Float.parseFloat(csvLine[1]), Float.parseFloat(csvLine[2]), Float.parseFloat(csvLine[3])));
        }
    }

    public Set<Timestamp> getTimestamps(){
        return data.keySet();
    }

    public Timestamp randTimestamp(){
        int size = data.keySet().size();
        Timestamp[] allTimestamps = data.keySet().toArray(new Timestamp[size]);

        Random r = new Random();
        int rIndex = r.nextInt(size);

        return allTimestamps[rIndex];
    }

    public MOERData getMOERData(Timestamp t){
        return data.get(t);
    }

    public float getMOER(Timestamp t){
       if(data.keySet().contains(t)) return data.get(t).getMOER();
       return -1f;
    }

    public float getAverageMOER(){
        float total = 0;
        for(Timestamp t : data.keySet()){
            total += data.get(t).getMOER();
        }
        return total / data.keySet().size();
    }

    public TreeMap<Timestamp, MOERData> getData() {
        return data;
    }

    public String toString(){
        String output = "";
        for (Timestamp t : data.keySet()) {
            output += data.get(t) + "\n";
        }
        return output;
    }
}
