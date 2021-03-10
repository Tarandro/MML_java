import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;

public class MMLReadJsonParametersTest {
	
	@Test
	public void testReadJsonParameters1() throws Exception {
		String string_json = Files.readString(Paths.get("./mml_test/LanguageTest/mml_LanguageTest1.json")); // no error
		
		try {
			ReadJsonParameters parameters = new ReadJsonParameters(string_json);
			parameters.read();
		}
		catch(Exception e){
			System.out.println("JSON Paramètres mal rempli");
		}
		
	}
	
	@Test
	public void testReadJsonParameters2() throws Exception {
		String string_json = Files.readString(Paths.get("./mml_test/ReadJsonParametersTest/mml_ReadJsonTest2.json")); // train_size > 1
		
		try {
			ReadJsonParameters parameters = new ReadJsonParameters(string_json);
			parameters.read();
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
		}
		catch(IllegalArgumentException e){
			System.out.println("Language non disponible");
		}
		
	}
}
