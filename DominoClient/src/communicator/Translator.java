package communicator;

import game.Manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Invite;
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
					/*
					 * Player p = (Player) parameter; Player p1 = new Player();
					 * p1.setLogin(p.getLogin()); p1.setDominos(p.getDominos());
					 * p1.setToken(p.isToken()); p1.setTeste("cliente");
					 * manager.getRoom().addPlayers(p1);
					 */
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

			case Actions.ONLINE_USERS:
				List<String> temp = new ArrayList<String>();
				List<Map<String, Object>> users = (List<Map<String, Object>>) parameter;
				for (int i = 0; i < users.size(); i++) {
					temp.add((String) users.get(i).get(Util.KEY_USERNAME));
				}
				manager.setOnlineUsers(temp);
				break;

			case Actions.CHAT_MESSAGE:
				String temp1 = (String) parameter;
				manager.addChatMessages(temp1);
				break;

			case Actions.STATUS_INVITE:
				
				if(parameter instanceof List) {
					List<Invite> i = new ArrayList<Invite>();
					List<Invite> remote = (List<Invite>) parameter;
					i.addAll(remote);
					manager.setInvites(i);
				}
				break;

			case Actions.INVITE:
				Invite invite = (Invite) parameter;
				if (invite != null) {
					Invite r = new Invite(invite.getIssuing(), invite.getReceptor());
					r.setStatus(invite.getStatus());
					manager.addReceivedInvites(r);
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
	public Map<String, Object> remoteRead() throws ClassNotFoundException,
			IOException {

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

	public void getOnlineUsers() {
		try {
			remoteWrite(Util.prepareMsg(Actions.ONLINE_USERS, null));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendChatMsg(String msg) {
		try {
			remoteWrite(Util.prepareMsg(Actions.CHAT_MESSAGE, msg));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void invite(String userSelected) {
		try {
			remoteWrite(Util.prepareMsg(Actions.INVITE, userSelected));
			manager.addUserMessage("Convite enviado a " + userSelected);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void responseInvite(Invite invite) {
		
		try {
			remoteWrite(Util.prepareMsg(Actions.RESPONSE_INVITE, invite));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void startGame(List<String> usersPlayers) {
		
		try {
			remoteWrite(Util.prepareMsg(Actions.START_GAME, usersPlayers));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
