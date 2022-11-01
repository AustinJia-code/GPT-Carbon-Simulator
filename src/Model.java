public class Model{

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
        return averageRuntime+ Math.floor(Math.floor(Math.random() * runtimeError * 200f) / 100 - runtimeError);
    }
    public double getEstimatedCarbonOutput(){
        return averageCarbonOutput + Math.floor(Math.floor(Math.random() * carbonError * 200f) / 100 - carbonError);
    }
}