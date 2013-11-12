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

import model.Player;
import model.Room;

public class Server {

	private List<Connection> clients;
	
	private Room room;
	
	private List<Room> rooms;
	
	private ServerSocket server;
	
	/**
	 * Default constructor
	 */
	public Server() {
		clients = new ArrayList<Connection>();
		room = new Room();
		rooms = new ArrayList<Room>();
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
				
				//Valid the limit connections to game play.
				if (this.clients.size() == Parameters.MAX_NUMBER_OF_PLAYERS) {

					connection.sendMsg(Util.prepareMsg(Actions.LOGON_FAILURE,
							"The room limit reached!"));
					connection.disconnect();

				} else {
					clients.add(connection);
				}

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
				sendMsgToAll(Util.prepareMsg(Actions.MESSAGE, connection.getUsername() + " saiu."));
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
			if (connection.isLogged() && connection.getUsername().equalsIgnoreCase(login)) {
				result = true;
			}
		}

		return result;
	}
	
	/**
	 * send a menssage broadcast. To all clients.
	 * @param msg
	 */
	public void sendMsgToAll(Object msg) {
		for (Connection connection : clients) {
			connection.sendMsg(msg);
		}
	}
	
	/**
	 * Get all clients connections.
	 * @return List of connections
	 */
	public List<Connection> getClients() {
		return clients;
	}

	/**
	 * Start game...
	 */
	public void startGame() {
		// The game can only be started once.
		if (!room.isGameStarted()) {
			
			validClients();
			
			int id = 0;
			
			for (Connection connection : clients) {
				Player player = new Player();
				player.setId(++id);
				player.setUsername(connection.getUsername());
				room.addPlayers(player);
			}

			room.start();
			
			//Send a message all clients to update the room status.
			sendMsgToAll(Util.prepareMsg(Actions.UPDATE_ROOM, room));

		}

	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
	
	public List<Map<String,Object>> getAllUsersOnline() {
		List<Map<String,Object>> users = new ArrayList<Map<String,Object>>();
		List<String> usersInRoom = getUsersInRoom();
		List<String> usersOnline = getUsersOnline();
		
		for(String user : usersOnline) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put(Util.KEY_USERNAME, user);
			map.put(Util.KEY_INROOM, usersInRoom.contains(user));
			users.add(map);
		}
		
		return users;
	}
	
	public List<String> getUsersInRoom() {
		List<String> users = new ArrayList<String>();
		
		for(Room r : rooms) {
			for(Player p : r.getPlayers()) {
				users.add(p.getUsername());
			}
		}
		
		return users;
	}
	
	public List<String> getUsersOnline() {
		List<String> users = new ArrayList<String>();
		
		for(Connection c : clients) {
			users.add(c.getUsername());
		}
		
		return users;
	}
}
