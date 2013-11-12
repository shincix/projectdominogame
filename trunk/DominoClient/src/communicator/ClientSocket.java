package communicator;

import java.io.IOException;

import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSocket {

	private Socket socket;
	private Reader reader;
	private Writer writer;

	public ClientSocket() throws UnknownHostException, IOException {
		socket = new Socket("127.0.0.1", 7000);
		writer = new Writer(socket.getOutputStream());
		reader = new Reader(socket.getInputStream());
		writer.flush();
	}

	public Reader getReader() {
		return reader;
	}

	public Writer getWriter() {
		return writer;
	}

	public void disconnect() throws IOException {
		reader.close();
		writer.close();
		socket.close();
	}

}
