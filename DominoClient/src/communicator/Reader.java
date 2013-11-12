package communicator;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class Reader {
	
	private ObjectInputStream ois;
	
	public Reader(InputStream inputStream) throws IOException {
		ois = new ObjectInputStream(inputStream);
	}
	
	public Object read() throws ClassNotFoundException, IOException {
		Object object = ois.readObject();
		System.out.println(">> Read: " + object);
		return object;
	}
	
	public void close() throws IOException {
		ois.close();
	}
	
}
