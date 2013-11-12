package communicator;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class Writer {
	
	private ObjectOutputStream oos;
	
	public Writer(OutputStream outputStream) throws IOException {
		oos = new ObjectOutputStream(outputStream);
		flush();
	}
	
	public void flush() throws IOException {
		oos.flush();
	}
	
	public void write(Object object) throws IOException {
		System.out.println(">> Write: " + object);
		oos.writeObject(object);
		flush();
	}
	
	public void close() throws IOException {
		oos.close();
	}
}
