import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonTest {
	
	private JSONObject obj;

	public JsonTest(JSONObject obj) {
		this.obj = obj;
	}
	
	public void fileName() {
		JSONObject d = (JSONObject) obj.get("dataset");
		String f = "";
		try {
			f = d.getString("filename");
			}
		catch (org.json.JSONException errFileName) {
			System.out.println("Entrer un champ filename \n");
			throw errFileName;
			}
		if (f.isBlank()) {
				throw new IllegalArgumentException("Donner un chemin de fichier");
			}
	}
	
	public void target() {
		String t = "";
		try {
			t = obj.getString("target_variable");
		}
		catch (org.json.JSONException errTarget) {
			System.out.println("Entrer un champ target_variable \n");
			throw errTarget;
		}
		if (t.isBlank()) {
			throw new IllegalArgumentException("Donner un nom de variable cible");
		}
	}
	
	public void trainSizes() {
		JSONArray train_sizes = obj.getJSONArray("training");
		Set<Float> set_train_sizes = new HashSet<Float>();
		for(int i=0; i<train_sizes.length(); i++)
		{
			float t = (float)0.7;
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
	}
	
	public void languages() {
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
	}
}
