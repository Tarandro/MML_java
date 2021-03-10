import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReadJsonParameters {
	
	private String string_json;
	private String filename;
	private String target_variable;
	private float train_size;
	private Set<String> set_metrics;
	private Set<Integer> set_max_depth_values;
	private Set<String> set_languages;
	
	public ReadJsonParameters(String string_json) {
		this.string_json = string_json;
	}
	
	public void read() {
		JSONObject obj = new JSONObject(string_json);
		
		//on teste le json avant de lancer le code
		JsonTest test = new JsonTest(obj);
		test.fileName();
		test.target();
		test.trainSize();
		test.languages();
		
		JSONObject d = (JSONObject) obj.get("dataset");
		
		this.filename = "";
		try {
			this.filename = d.getString("filename");
			}
		catch (NullPointerException errFaileName) {
			System.out.println("Donner un chemin de fichier");
			}


		this.target_variable = "";
		try {
			this.target_variable = obj.getString("target_variable");
		}
		catch (NullPointerException errTarget) {
			System.out.println("Donner un nom de variable cible");
		}
		
		
		this.train_size = (float)0.7;
		try {
			this.train_size = obj.getFloat("training");
		}
		catch (NullPointerException errTrainSize) {
			System.out.println("Donner une taille d'ensemble d'entraînement");
		}
		if (train_size > 1 || train_size<0) {
			throw new IllegalArgumentException("Choisir une taille d'entraînement entre 0 et 1");
		}
		
		
		JSONArray metrics = obj.getJSONArray("metrics");
		// add one by one name_metric from metrics to list_metrics
		Set<String> set_metrics = new HashSet<String>();
		for(int i=0; i<metrics.length(); i++)
		{
			set_metrics.add(metrics.getString(i));
		}
		this.set_metrics = set_metrics;
		
		
		JSONArray max_depth_values = obj.getJSONArray("max_depth");
		// add one by one name_metric from metrics to list_metrics
		Set<Integer> set_max_depth_values = new HashSet<Integer>();
		for(int i=0; i<max_depth_values.length(); i++)
		{
			set_max_depth_values.add(max_depth_values.getInt(i));
		}
		this.set_max_depth_values = set_max_depth_values;
		
		
		JSONArray languages = obj.getJSONArray("languages");
		// add one by one name_metric from metrics to list_metrics
		Set<String> set_languages = new HashSet<String>();
		for(int i=0; i<languages.length(); i++)
		{
			String l = "";
			try {
				l = languages.getString(i).toLowerCase();
				set_languages.add(l);
			}
			catch (NullPointerException errLanguage) {
				System.out.println("Donner un nom de langage");
			}
			if (!(l.matches("python") || l.matches("r") || l.matches("julia"))) {
				throw new IllegalArgumentException("Choisir Python, R ou Julia");
			}
		}
		this.set_languages = set_languages;
		
	}
	
	public String getFilemame() {
		return filename;
	}
	
	public String getTargetVariable() {
		return target_variable;
	}
	
	public float getTrainSize() {
		return train_size;
	}
	
	public Set<String> getMetrics() {
		return set_metrics;
	}
	
	public Set<Integer> getMaxDepthValues() {
		return set_max_depth_values;
	}
	
	public Set<String> getLanguages() {
		return set_languages;
	}
	
}
