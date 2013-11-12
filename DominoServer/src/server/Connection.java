package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

import model.Room;

import system.Actions;
import system.Parameters;
import system.Util;

public class Connection extends Thread {

	private Socket connection;

	private String username;

	private ObjectInputStream in;

	private ObjectOutputStream out;
	
	private Server server;
	
	public Connection(Socket socket, Server server) throws IOException {
		super();

		connection = socket;
		this.server = server; 
		username = "";

		out = new ObjectOutputStream(connection.getOutputStream());
		out.flush();

		in = new ObjectInputStream(connection.getInputStream());
		out.flush();

		start();
	}

	@Override
	public void run() {

		try {

			while (true) {

				try {
					Object msg = in.readObject();
					System.out.println(">> Read " + this.getUsername() + ": " + msg);
					processMessage(msg);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// O tratamento dos parametros vai ser feito conforme o tipo da ação
	private void processMessage(Object message) {

		if (message instanceof Map) {

			@SuppressWarnings("unchecked")
			Map<String, Object> msg = (Map<String, Object>) message;

			String action = Util.getAction(msg);
			Object parameter = Util.getParameter(msg);

			switch (action) {
			case Actions.LOGON:

				String login = (String) parameter;

				if (server.existsLogin(login)) {
					msg = Util.prepareMsg(Actions.LOGON_FAILURE, "login " + login + " in use.");
					sendMsg(msg);
				} else {
					this.username = login;
					msg = Util.prepareMsg(Actions.LOGON_SUCESSFUL, null);
					sendMsg(msg);

					msg = Util.prepareMsg(Actions.MESSAGE, login + " connected.");
					server.sendMsgToAll(msg);

					msg = Util.prepareMsg(Actions.MESSAGE, "Wating for more "
							+ (Parameters.MAX_NUMBER_OF_PLAYERS - server.getClients().size())
							+ " player(s).");
					server.sendMsgToAll(msg);

					if (server.getClients().size() == Parameters.MAX_NUMBER_OF_PLAYERS) {
						msg = Util.prepareMsg(Actions.MESSAGE,
								"Let's Go! The game will be started...");
						server.sendMsgToAll(msg);
						server.startGame();
					}
				}
				break;

			case Actions.GIVEME_ROOM:

				// The client can only request the room, if the game is started
				// and is in its turn.
				if (server.getRoom().isGameStarted()
						&& server.getRoom().getPlayer(this.username).isToken()) {
					msg = Util.prepareMsg(Actions.UPDATE_ROOM, server.getRoom());
					sendMsg(msg);
				}

				break;

			case Actions.UPDATE_ROOM:

				// The client can only update the room, if the game is started
				// and is in its turn.
				if (parameter instanceof Room) {
					if (server.getRoom().isGameStarted()
							&& server.getRoom().getPlayer(this.username).isToken()) {

						server.setRoom((Room) parameter);
						server.getRoom().next();
						msg = Util.prepareMsg(Actions.UPDATE_ROOM, server.getRoom());
						server.sendMsgToAll(msg);
					}

				}

				break;

			case Actions.DISCONNECT:
				try {
					msg = Util.prepareMsg(Actions.MESSAGE, this.username + " saiu.");
					disconnect();
					server.sendMsgToAll(msg);					
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			
			case Actions.ONLINE_USERS:
				msg = Util.prepareMsg(Actions.ONLINE_USERS, server.getAllUsersOnline());
				sendMsg(msg);
				break;
			
			case Actions.INVITE:
				String user = (String) parameter;
				if(server.getUsersOnline().contains(user) && !server.getUsersInRoom().contains(user)) {
					//Convidar
				} else {
					msg = Util.prepareMsg(Actions.MESSAGE, user + " is already playing.");
					sendMsg(msg);
				}
				break;
				
			default:
				break;
			}
		}
	}

	/**
	 * Send a message to client.
	 * 
	 * @param msg
	 */
	public void sendMsg(Object msg) {
		try {
			System.out.println(">> Write " + this.getUsername() + ": " + msg);
			out.writeObject(msg);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getConnection() {
		return connection;
	}

	public void disconnect() throws IOException {
		in.close();
		out.close();
		connection.close();
		interrupt();
	}

	public String getUsername() {
		return username;
	}

	/**
	 * Check if the client already logged.
	 * 
	 * @return true if client is logged.
	 */
	public boolean isLogged() {
		boolean result = false;

		if (this.username != null && !this.username.trim().equals("")) {
			result = true;
		}

		return result;
	}

}
