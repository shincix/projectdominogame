package communicator;

import game.Manager;

import java.io.IOException;
import java.util.Map;

import model.Player;
import model.Room;

import system.Actions;
import system.Util;

public class Translator {
	
	
	private ClientSocket client;
	private ReaderListener readerListner;
	private Manager manager;

	public Translator(Manager manager) throws Exception {
		this.manager = manager;
		client = new ClientSocket();
		readerListner = new ReaderListener(this);
		readerListner.start();
	}
	
	public boolean login(String username) {
		boolean result = false;
		try {
			remoteWrite(Util.prepareMsg(Actions.LOGON, username));
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void next() {
		try {
			remoteWrite(Util.prepareMsg(Actions.UPDATE_ROOM, manager.getRoom()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void processMensage(Map<String, Object> message) {

		if (message != null) {
			String action = Util.getAction(message);
			Object parameter = Util.getParameter(message);

			// TODO implementar execução para todas as ações possiveis
			// (system.Actions)
			switch (action) {
			case Actions.LOGON_FAILURE:
				if (parameter instanceof String) {
					manager.addUserMessage((String) parameter);
				}
				break;

			case Actions.LOGON_SUCESSFUL:
				if (parameter instanceof Player) {
					/*Player p = (Player) parameter;
					Player p1 = new Player();
					p1.setLogin(p.getLogin());
					p1.setDominos(p.getDominos());
					p1.setToken(p.isToken());
					p1.setTeste("cliente");
					manager.getRoom().addPlayers(p1);*/
				}
				break;

			case Actions.MESSAGE:
				if (parameter instanceof String) {
					manager.addUserMessage((String) parameter);
				}
				break;

			case Actions.UPDATE_ROOM:
				if (parameter instanceof Room) {
					manager.setRoom((Room) parameter);
				}
				break;

			default:
				System.out.println("Action not found: " + action);
				break;
			}
		}
	}

	public void exit() {
		// readerListner.interrupt();
		readerListner.stop();

		try {
			remoteWrite(Util.prepareMsg(Actions.DISCONNECT, null));
			client.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> remoteRead() throws ClassNotFoundException, IOException {

		Object remoteObject = client.getReader().read();
		Map<String, Object> result = null;

		if (remoteObject != null && remoteObject instanceof Map) {
			result = (Map<String, Object>) remoteObject;
		}

		return result;
	}

	private void remoteWrite(Map<String, Object> message) throws IOException {
		client.getWriter().write(message);
	}

	public ClientSocket getClient() {
		return client;
	}

}
