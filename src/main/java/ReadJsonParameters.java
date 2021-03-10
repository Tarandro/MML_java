import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReadJsonParameters {
	
	private String string_json;
	private String filename;
	private String target_variable;
	private Set<Float> set_train_sizes;
	private Set<String> set_metrics;
	private Set<Integer> set_max_depth_values;
	private Set<String> set_languages;
	private int repetition;
	
	public ReadJsonParameters(String string_json) {
		this.string_json = string_json;
	}
	
	public void read() {
		JSONObject obj = new JSONObject(string_json);
		
		//on teste le json avant de lancer le code
		JsonTest test = new JsonTest(obj);
		test.fileName();
		test.target();
		test.trainSizes();
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

		
		JSONArray train_sizes = obj.getJSONArray("training");
		Set<Float> set_train_sizes = new HashSet<Float>();
		for(int i=0; i<train_sizes.length(); i++)
		{
			Float t = (float) 0.7;
			try {
				BigDecimal bg = train_sizes.getBigDecimal(i);
				t = bg.floatValue();
				set_train_sizes.add(t);
			}
			catch (NullPointerException errTrainSize) {
				System.out.println("Donner une taille d'ensemble d'entraînement");
			}
			if (t > 1 || t<0) {
				throw new IllegalArgumentException("Choisir une taille d'entraînement entre 0 et 1");
			}
		}
		this.set_train_sizes = set_train_sizes;
		
		
		JSONArray metrics = obj.getJSONArray("metrics");
		Set<String> set_metrics = new HashSet<String>();
		for(int i=0; i<metrics.length(); i++)
		{
			set_metrics.add(metrics.getString(i));
		}
		this.set_metrics = set_metrics;
		
		
		JSONArray max_depth_values = obj.getJSONArray("max_depth");
		Set<Integer> set_max_depth_values = new HashSet<Integer>();
		for(int i=0; i<max_depth_values.length(); i++)
		{
			set_max_depth_values.add(max_depth_values.getInt(i));
		}
		this.set_max_depth_values = set_max_depth_values;
		
		
		JSONArray languages = obj.getJSONArray("languages");
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
		
		this.repetition = 1;
		try {
			this.repetition = obj.getInt("repetition");
		}
		catch (NullPointerException errRepetition) {
			System.out.println("Donner un nombre entier pour repetition");
		}

		
	}
	
	public String getFilemame() {
		return filename;
	}
	
	public String getTargetVariable() {
		return target_variable;
	}
	
	public Set<Float> getTrainSizes() {
		return set_train_sizes;
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
	
	public int getRepetition() {
		return repetition;
	}
	
}
