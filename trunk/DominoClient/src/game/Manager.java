package game;

import java.util.ArrayList;
import java.util.List;

import model.Domino;
import model.Player;
import model.Room;
import communicator.Translator;

public class Manager {

	private Translator translator;

	private Room room;

	private List<String> userMessages;
	
	private String myUsername;

	public Manager() throws Exception, Exception {
		translator = new Translator(this);
		room = new Room();
		userMessages = new ArrayList<String>();
		myUsername  = "";
	}

	public void addUserMessage(String message) {
		userMessages.add(message);
	}

	public List<String> getMessages() {
		List<String> result = new ArrayList<String>();

		result.addAll(userMessages);
		userMessages.clear();

		return result;
	}

	public boolean login(String username) {
		boolean result = false;

		if (username == null || username.isEmpty()) {
			userMessages.add("Informe um login válido!");
		} else {
			myUsername = username;
			result = translator.login(username);
		}

		return result;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public void exit() {
		translator.exit();
	}
	
	public boolean isMyTurn() {
		return getMyPlayer().isToken();
	}
	
	public Player getMyPlayer() {
		return room.getPlayer(this.myUsername);
	}
	
	public void next() {
		translator.next();
	}

	public void pushDominoBoard() {
		if( !getRoom().pushAvaliableDominos(getMyPlayer())) {
			addUserMessage("There is not dominos available");
		}
		
	}

	public void putOnBoard(Domino x, String side, String me) {
		if (getRoom().putOnBoard(x, side, me)) {
			next();
		} else {
			addUserMessage("Move not permmited");
		}
	}
	
	public boolean finishedGame() {
		boolean result = false;
		
		if(getRoom().isFinishedGame()) {
			Player won = getRoom().whoWon();
			if(won == null) {
				addUserMessage("Game Over!!! There are no winners.");
			} else {
				addUserMessage(won.getUsername() + " won the game!");
			}
		}
		
		return result;
	}
}
