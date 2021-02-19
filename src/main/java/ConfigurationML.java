import java.util.HashSet;
import java.util.Set;

public class ConfigurationML {

	private static final float DEFAULT_TRAIN_SIZE = 0.8f;
	private static final int DEFAULT_MAX_DEPTH = 5;
	private static final Set<String> DEFAULT_METRICS = new HashSet<String>();
	{DEFAULT_METRICS.add("accuracy");}


	private String file_path; // dataset
	private String target; // targeted feature (column name)
	private float train_size;
	private Set<String> metrics;
	private int max_depth;

	// TODO
	private DataSet data;

	public ConfigurationML() {
		this.file_path = null;
		this.target = null;
		this.train_size = DEFAULT_TRAIN_SIZE;
		this.metrics = DEFAULT_METRICS;
		this.max_depth = DEFAULT_MAX_DEPTH;

	}


	public void setFilePath(String file_path) {
		this.file_path = file_path;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public void setTrainSize(float train_size) {
		this.train_size = train_size;
	}

	public void setMetrics(Set<String> metrics) {
		this.metrics = metrics;
	}

	public void setMaxDepth(int max_depth) {
		this.max_depth = max_depth;
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

	public Set<String> getMetrics() {
		return metrics;
	}
	public int getMaxDepth( ) {
		return max_depth;
	}
}
