import java.io.IOException;

public abstract class MLExecutor {
	
	protected ConfigurationML configuration;
	
	public abstract void generateCode() throws IOException;
	public abstract MLResult run() throws IOException;

}
