import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;



public class MMLReadJsonParametersTest {
	
	@Test
	public void testReadJsonParameters1() throws Exception {
		String string_json = Files.readString(Paths.get("./mml_test/LanguageTest/mml_LanguageTest1.json")); // no error
		
		try {
			ReadJsonParameters parameters = new ReadJsonParameters(string_json);
			parameters.read();
		}
		catch(Exception e){
			fail("JSON Paramètres mal rempli");
		}
		
	}
	
	@Test
	public void testReadJsonParameters2() throws Exception {
		String string_json = Files.readString(Paths.get("./mml_test/ReadJsonParametersTest/mml_ReadJsonTest2.json")); // train_size > 1
		
		try {
			ReadJsonParameters parameters = new ReadJsonParameters(string_json);
			parameters.read();
			fail("la taille d'entraînement est correcte");
		}
		catch(IllegalArgumentException e){
			System.out.println("Taille d'entraînement non compris entre 0 et 1");
		}
		
	}
	
	@Test
	public void testReadJsonParameters3() throws Exception {
		String string_json = Files.readString(Paths.get("./mml_test/ReadJsonParametersTest/mml_ReadJsonTest3.json")); // language pas disponible
		
		try {
			ReadJsonParameters parameters = new ReadJsonParameters(string_json);
			parameters.read();
			fail("le langage est bien disponible");
		}
		catch(IllegalArgumentException e){
			System.out.println("Language non disponible");
		}
		
	}
}
