
public class ConfigurationML {
	
	private String file_path; // dataset
	private String target; // targeted feature (column name)
	private float train_size;
	
	// TODO
	private DataSet data;
	
	public ConfigurationML(String file_path, String target, float train_size) {
		this.file_path = file_path;
		this.target = target;
		this.train_size = train_size;
	}
	
	
	public String getFilePath() {
		return file_path;
	}
	
	public String getTarget() {
		return target;
	}

	public float getTrainSize() {
		return train_size;
	}
}
