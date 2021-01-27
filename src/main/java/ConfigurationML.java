
public class ConfigurationML {
	
	private String file_path; // dataset
	private String target; // targeted feature (column name)
	
	// TODO
	private DataSet data;
	
	public ConfigurationML(String file_path, String target) {
		this.file_path = file_path;
		this.target = target;
	}
	
	
	public String getFilePath() {
		return file_path;
	}
	
	public String getTarget() {
		return target;
	}

}
