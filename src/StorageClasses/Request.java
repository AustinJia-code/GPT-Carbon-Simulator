package StorageClasses;

public class Request {
    private String prompt;
    private Model model;
    private float MOER;
    private double runtime, carbonOutput;
    private Timestamp timestamp;
    private MOERData data;

    public Request(Timestamp timestamp, String prompt, Model model, MOERData data){
        this.timestamp = timestamp;
        this.prompt = prompt;
        this.model = model;
        this.data = data;

        this.MOER = data.getMOER();
        this.runtime = model.getEstimatedRuntime();
        this.carbonOutput = model.getEstimatedCarbonOutput();
    }

    public String getPrompt() {
        return prompt;
    }

    public Model getModel() {
        return model;
    }

    public MOERData getData() {
        return data;
    }

    public float getMOER(){
        return MOER;
    }

    public String toCSV(){
        return timestamp.toCSV() + "," + prompt + "," + model.toCSV() + "," + data.toCSV();
    }

}
