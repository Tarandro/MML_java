import java.io.IOException;

public abstract class MLExecutor {
	
	public abstract void generateCode(String file_path, String target) throws IOException;
	public abstract void run() throws IOException;

}
