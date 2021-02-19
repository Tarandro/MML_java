import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.junit.Test;

public class MMLJsonTest {
	
	@Test
	public void testJson1() throws Exception {
		String str = Files.readString(Paths.get("mml_test1.json")); //le json n'a pas de champ filename
		JSONObject obj = new JSONObject(str);
		JsonTest test = new JsonTest(obj);
		try {
			test.fileName();
			fail("Il y a bien un champ filename");
		}
		catch (org.json.JSONException e) {
			assertTrue(true);
		}
		
	}
	
	@Test
	public void testJson2() throws Exception {
		String str = Files.readString(Paths.get("mml_test2.json")); //le json a un champ filename vide
		JSONObject obj = new JSONObject(str);
		JsonTest test = new JsonTest(obj);
		try {
			test.fileName();
			fail("Le champ filename n'est pas vide");
		}
		catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
	}
	
	@Test
	public void testJson3() throws Exception {
		String str = Files.readString(Paths.get("mml_test3.json")); //le json a une taille d'entraînement supérieure à 1
		JSONObject obj = new JSONObject(str);
		JsonTest test = new JsonTest(obj);
		try {
			test.trainSize();
			fail("train_size est bien entre 0 et 1");
		}
		catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
	}
	
	@Test
	public void testJson4() throws Exception {
		String str = Files.readString(Paths.get("mml_test4.json")); //le json désigne un langage non supporté
		JSONObject obj = new JSONObject(str);
		JsonTest test = new JsonTest(obj);
		try {
			test.language();
			fail("Le langage est bien supporté");
		}
		catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
	}
	
	
	
	

}
