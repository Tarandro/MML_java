import org.json.JSONObject;

// results of an execution (in Python, R or Julia...)
public class MLResult {
	
	private String result;
	private JSONObject json_result;

	public MLResult(String result, JSONObject json_result) {
		this.result = result;
		this.json_result = json_result;
	}
	
	public String getStringResult() {
		return result;
	}
	
	public JSONObject getJSONResult() {
		return json_result;
	}

}
