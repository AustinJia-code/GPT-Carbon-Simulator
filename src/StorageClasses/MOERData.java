package StorageClasses;

public class MOERData {
    private Timestamp timestamp;
    private float MOER, MOERVersion, frequency;
    public MOERData(Timestamp timestamp, float MOER, float MOERVersion, float frequency){
        this.timestamp = timestamp;
        this.MOER = MOER;
        this.MOERVersion = MOERVersion;
        this.frequency = frequency;
    }

    public float getMOER(){
        return MOER;
    }

    public String toString(){
        return timestamp + " " + MOER + " " + MOERVersion + " " + frequency;
    }

    public String toCSV(){
        return timestamp + "," + MOER + "," + MOERVersion + "," + frequency;
    }
}
