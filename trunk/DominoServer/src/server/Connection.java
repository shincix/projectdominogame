package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import model.Invite;
import model.Room;

import system.Actions;
import system.Util;

public class Connection extends Thread {

	private Socket connection;

	private String username;

	private boolean acceptedInvite;

	private ObjectInputStream in;

	private ObjectOutputStream out;

	private Server server;

	public Connection(Socket socket, Server server) throws IOException {
		super();

		connection = socket;
		this.server = server;
		username = "";
		acceptedInvite = false;

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
					System.out.println(">> Read " + this.getUsername() + ": "
							+ msg);
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
					msg = Util.prepareMsg(Actions.LOGON_FAILURE, "login "
							+ login + " in use.");
					sendMsg(msg);
				} else {
					this.username = login;
					msg = Util.prepareMsg(Actions.LOGON_SUCESSFUL, null);
					sendMsg(msg);

					msg = Util.prepareMsg(Actions.MESSAGE, login
							+ " connected.");
					server.sendMsgToAll(msg);

				}
				break;

			case Actions.GIVEME_ROOM:

				// The client can only request the room, if the game is started
				// and is in its turn.
				if (server.getRoomPlayer(username).isGameStarted()
						&& server.getRoomPlayer(username)
								.getPlayer(this.username).isToken()) {
					msg = Util.prepareMsg(Actions.UPDATE_ROOM,
							server.getRoomPlayer(username));
					sendMsg(msg);
				}

				break;

			case Actions.UPDATE_ROOM:

				// The client can only update the room, if the game is started
				// and is in its turn.
				if (parameter instanceof Room) {
					Room room = server.getRoomPlayer(username);
					if (room.isGameStarted()
							&& room.getPlayer(this.username).isToken()) {

						room = (Room) parameter;
						room.next();
						server.setRoom(room);
						msg = Util.prepareMsg(Actions.UPDATE_ROOM, room);
						server.sendMsgToPlayers(msg, room);
					}

				}

				break;

			case Actions.DISCONNECT:
				try {
					msg = Util.prepareMsg(Actions.MESSAGE, this.username
							+ " saiu.");
					disconnect();
					server.sendMsgToAll(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;

			case Actions.ONLINE_USERS:
				msg = Util.prepareMsg(Actions.ONLINE_USERS,
						server.getAllUsersOnline());
				sendMsg(msg);
				break;

			case Actions.INVITE:

				String user = (String) parameter;

				// Verificar se o receptor não está em uma sala.
				if (server.getUsersOnline().contains(user)
						&& !server.getUsersInRoom().contains(user)) {

					Connection recptorConnection = server.getConnection(user);
					if (recptorConnection != null) {

						if (!recptorConnection.isAcceptedInvite()) {
							Invite invite = server.getInvite(username, user);

							// validar se já existe um convite com emissor e
							// recptor
							if (invite == null) {

								invite = new Invite(username, user);

								server.addInvite(invite);
								recptorConnection.sendMsg(Util.prepareMsg(
										Actions.INVITE, invite));

								sendMsg(Util.prepareMsg(Actions.STATUS_INVITE,
										server.getInvitesIssuing(username)));
							} else {
								sendMsg(Util.prepareMsg(Actions.MESSAGE,
										"There is already an invitation to the user "
												+ user));
							}
						} else {
							sendMsg(Util.prepareMsg(Actions.MESSAGE,
									"The user already accepted an invitation"));

						}
					} else {
						sendMsg(Util.prepareMsg(Actions.MESSAGE,
								"Connecting recptor not found"));
					}

				} else {
					msg = Util.prepareMsg(Actions.MESSAGE, user
							+ " is already playing.");
					sendMsg(msg);
				}
				break;

			case Actions.RESPONSE_INVITE:

				Invite responseInvite = (Invite) parameter;
				Invite serverInvite = server.getInvite(
						responseInvite.getIssuing(),
						responseInvite.getReceptor());
				if (serverInvite != null) {
					serverInvite.setStatus(responseInvite.getStatus());
					if (serverInvite.getStatus().equals(Invite.ACCPETED)
							&& !isAcceptedInvite()) {
						setAcceptedInvite(true);
					}
				}

				Connection issuingConnection = server
						.getConnection(responseInvite.getIssuing());
				List<Invite> invites = server.getInvitesIssuing(responseInvite
						.getIssuing());
				issuingConnection.sendMsg(Util.prepareMsg(
						Actions.STATUS_INVITE, invites));

				break;

			case Actions.CHAT_MESSAGE:
				String chatMsg = (String) parameter;
				server.sendMsgToAll(Util.prepareMsg(Actions.CHAT_MESSAGE,
						username + "> " + chatMsg));
				break;

			case Actions.START_GAME:
				List<String> usernamePlayers = (List<String>) parameter;

				if (server.startGame(usernamePlayers)) {
					msg = Util.prepareMsg(Actions.MESSAGE,
							"Let's Go! The game will be started...");
					server.sendMsgToPlayers(msg, server.getRoomPlayer(username));
				}
				/*
				 * msg = Util.prepareMsg(Actions.MESSAGE, "Wating for more " +
				 * (Parameters.MAX_NUMBER_OF_PLAYERS - server
				 * .getClients().size()) + " player(s).");
				 * server.sendMsgToAll(msg);
				 * 
				 * if (server.getClients().size() ==
				 * Parameters.MAX_NUMBER_OF_PLAYERS) { msg =
				 * Util.prepareMsg(Actions.MESSAGE,
				 * "Let's Go! The game will be started...");
				 * server.sendMsgToAll(msg); server.startGame(); }
				 */

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
			out.reset();
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

	private boolean isAcceptedInvite() {
		return acceptedInvite;
	}

	private void setAcceptedInvite(boolean acceptedInvite) {
		this.acceptedInvite = acceptedInvite;
	}

}
