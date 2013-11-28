package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import system.Actions;
import system.Parameters;
import system.Util;

import model.Invite;
import model.Player;
import model.Room;

public class Server {

	private List<Connection> clients;

	// private Room room;

	private List<Room> rooms;

	private ServerSocket server;

	private List<Invite> invites;

	/**
	 * Default constructor
	 */
	public Server() {
		clients = new ArrayList<Connection>();
		// room = new Room();
		rooms = new ArrayList<Room>();
		invites = new ArrayList<Invite>();
	}

	/**
	 * Run listener server socket.
	 */
	public void execute() {
		try {

			server = new ServerSocket(7000);
			System.out.println("Servidor rodando na porta 7000...");

			while (true) {
				Socket socket = server.accept();
				Connection connection = new Connection(socket, this);
				clients.add(connection);
				validClients();
			}

		} catch (IOException e) {
			System.out.println("IOException: " + e);
		}
	}

	/**
	 * Verify if the socket connection clients is active. If not, the
	 * connections is removed.
	 */
	private void validClients() {

		Iterator<Connection> i = clients.iterator();
		while (i.hasNext()) {
			Connection connection = i.next();
			if (!connection.isAlive()) {
				clients.remove(connection);
				sendMsgToAll(Util.prepareMsg(Actions.MESSAGE,
						connection.getUsername() + " saiu."));
			}
		}

	}

	/**
	 * Verify in all connection, if the login is used in server.
	 * 
	 * @param login
	 * @return
	 */
	public boolean existsLogin(String login) {

		boolean result = false;

		for (Connection connection : clients) {
			if (connection.isLogged()
					&& connection.getUsername().equalsIgnoreCase(login)) {
				result = true;
			}
		}

		return result;
	}

	/**
	 * send a menssage broadcast. To all clients.
	 * 
	 * @param msg
	 */
	public void sendMsgToAll(Object msg) {
		for (Connection connection : clients) {
			connection.sendMsg(msg);
		}
	}

	/**
	 * Get all clients connections.
	 * 
	 * @return List of connections
	 */
	public List<Connection> getClients() {
		return clients;
	}

	/**
	 * Start game...
	 */
	/*
	 * public void startGame() { // The game can only be started once. if
	 * (!room.isGameStarted()) {
	 * 
	 * validClients();
	 * 
	 * int id = 0;
	 * 
	 * for (Connection connection : clients) { Player player = new Player();
	 * player.setId(++id); player.setUsername(connection.getUsername());
	 * room.addPlayers(player); }
	 * 
	 * room.start();
	 * 
	 * // Send a message all clients to update the room status.
	 * sendMsgToAll(Util.prepareMsg(Actions.UPDATE_ROOM, room));
	 * 
	 * }
	 * 
	 * }
	 */

	/**
	 * Start game...
	 */
	public boolean startGame(List<String> usernamePlayers) {

		boolean canStart = true;
		boolean result = false;

		validClients();

		for (String username : usernamePlayers) {
			if (isUserInRoom(username)) {
				canStart = false;
				break;
			}
		}

		if (canStart) {

			Room room = new Room(getNextIdRoom());

			int id = 0;

			for (String s : usernamePlayers) {
				Connection connection = getConnection(s);
				if (connection != null) {
					Player player = new Player();
					player.setId(++id);
					player.setUsername(connection.getUsername());
					room.addPlayers(player);
				}
			}

			room.start();
			rooms.add(room);
			result = true;

			// Send a message all players from room.
			sendMsgToPlayers(Util.prepareMsg(Actions.UPDATE_ROOM, room), room);

		}

		return result;

	}

	private int getNextIdRoom() {

		int lastIdRoom = -1;

		for (Room r : rooms) {
			if (r.getId() > lastIdRoom) {
				lastIdRoom = r.getId();
			}
		}

		return ++lastIdRoom;
	}

	public void sendMsgToPlayers(Map<String, Object> msg, Room room) {

		for (Player p : room.getPlayers()) {
			Connection connection = getConnection(p.getUsername());
			connection.sendMsg(msg);
		}

	}

	private boolean isUserInRoom(String username) {
		boolean result = false;

		for (Room r : rooms) {
			for (Player p : r.getPlayers()) {
				if (p.getUsername().equals(username)) {
					result = true;
					break;
				}
			}
		}

		return result;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public Room getRoomPlayer(String username) {
		Room result = null;

		for (Room r : rooms) {
			for (Player p : r.getPlayers()) {
				if (p.getUsername().equals(username)) {
					result = r;
					break;
				}
			}
		}

		return result;
	}

	public List<Map<String, Object>> getAllUsersOnline() {
		List<Map<String, Object>> users = new ArrayList<Map<String, Object>>();
		List<String> usersInRoom = getUsersInRoom();
		List<String> usersOnline = getUsersOnline();

		for (String user : usersOnline) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Util.KEY_USERNAME, user);
			map.put(Util.KEY_INROOM, usersInRoom.contains(user));
			users.add(map);
		}

		return users;
	}

	public List<String> getUsersInRoom() {
		List<String> users = new ArrayList<String>();

		for (Room r : rooms) {
			for (Player p : r.getPlayers()) {
				users.add(p.getUsername());
			}
		}

		return users;
	}

	public List<String> getUsersOnline() {
		List<String> users = new ArrayList<String>();

		for (Connection c : clients) {
			users.add(c.getUsername());
		}

		return users;
	}

	public void addInvite(Invite invite) {
		invites.add(invite);
	}

	public Invite getInvite(String issuing, String receptor) {
		Invite result = null;

		for (Invite invite : invites) {
			if (invite.getIssuing().equals(issuing)
					&& invite.getReceptor().equals(receptor)) {
				result = invite;
				break;
			}
		}

		return result;

	}

	public Connection getConnection(String username) {

		Connection result = null;

		for (Connection connection : clients) {
			if (connection.getUsername() != null
					&& connection.getUsername().equals(username)) {
				result = connection;
			}
		}

		return result;
	}

	public List<Invite> getInvitesIssuing(String username) {
		List<Invite> result = new ArrayList<Invite>();

		for (Invite invite : invites) {
			if (invite.getIssuing().equals(username)) {
				result.add(invite);
			}
		}

		return result;
	}

	public void setRoom(Room room) {
		
		
		for (int i = 0; i < rooms.size(); i++) {
			if (room.getId() == rooms.get(i).getId()) {
				rooms.set(i, room);
				break;
			}
		}

	}
}
