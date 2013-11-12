package communicator;

import java.io.IOException;

public class ReaderListener extends Thread {

	private Translator translator;

	public ReaderListener(Translator translator) {
		this.translator = translator;
	}

	@Override
	public void run() {

		while (true) {
			try {
				translator.processMensage(translator.remoteRead());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
