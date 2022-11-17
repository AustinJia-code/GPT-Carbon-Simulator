package StorageClasses;

public class Model{

    public String toCSV;
    private String modelName;
    private float averageRuntime, averageCarbonOutput, runtimeError, carbonError;

    public Model(String modelName, float averageRuntime, float averageCarbonOutput, float runtimeError, float carbonError){
        this.modelName = modelName;
        this.averageRuntime = averageRuntime;
        this.averageCarbonOutput = averageCarbonOutput;
        this.runtimeError = runtimeError;
        this.carbonError = carbonError;
    }

    public double getEstimatedRuntime(){
        return averageRuntime + Math.random() * runtimeError * 2 - runtimeError;
    }
    public double getEstimatedCarbonOutput(){
        return averageCarbonOutput + Math.random() * carbonError * 2 - carbonError;
    }
    public String toCSV(){
        return modelName + "," + averageRuntime + "," + averageCarbonOutput + "," + runtimeError + "," + carbonError;
    }
}