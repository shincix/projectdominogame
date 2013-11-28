package game;

import java.util.ArrayList;
import java.util.List;

import system.Parameters;

import model.Domino;
import model.Invite;
import model.Player;
import model.Room;
import communicator.Translator;

public class Manager {

	private Translator translator;

	private Room room;

	private List<String> userMessages;

	private List<String> onlineUsers;

	private String myUsername;

	private List<String> chatMessages;

	private List<Invite> invites;

	private List<Invite> receivedInvites;

	private boolean acceptedInvite;

	public Manager() throws Exception, Exception {
		translator = new Translator(this);
		room = new Room(-1);
		userMessages = new ArrayList<String>();
		myUsername = "";
		onlineUsers = new ArrayList<String>();
		chatMessages = new ArrayList<String>();
		invites = new ArrayList<Invite>();
		receivedInvites = new ArrayList<Invite>();
		acceptedInvite = false;
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
			userMessages.add("Informe um login v�lido!");
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
		if (!getRoom().pushAvaliableDominos(getMyPlayer())) {
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

		if (getRoom().isFinishedGame()) {
			Player won = getRoom().whoWon();
			if (won == null) {
				addUserMessage("Game Over!!! There are no winners.");
			} else {
				addUserMessage(won.getUsername() + " won the game!");
			}
		}

		return result;
	}

	public List<String> getOnlineUsers() {
		translator.getOnlineUsers();
		return this.onlineUsers;
	}

	public void setOnlineUsers(List<String> onlineUsers) {
		this.onlineUsers = onlineUsers;
	}

	public String getMyUsername() {
		return myUsername;
	}

	public List<String> getChatMsg() {
		List<String> result = new ArrayList<String>();
		result.addAll(chatMessages);
		chatMessages.clear();
		return result;
	}

	public void addChatMessages(String message) {
		chatMessages.add(message);
	}

	public void sendChatMsg(String msg) {
		translator.sendChatMsg(msg);

	}

	public void invite(String username) {
		int amountInvite = 0;
		for (Invite invite : getInvites()) {
			if (invite.getStatus().equals(Invite.ACCPETED)
					|| invite.getStatus().equals(Invite.PENDENT)) {
				amountInvite++;
			}
		}
		if (amountInvite < Parameters.MAX_NUMBER_OF_PLAYERS) {

			if (getInvite(username) == null) {
				translator.invite(username);
			} else {
				addUserMessage("The user has been invited");
			}
		} else {
			addUserMessage("Limit reached invitations");
		}

	}

	public List<Invite> getInvites() {
		return invites;
	}

	public void setInvites(List<Invite> invites) {
		this.invites = invites;
	}

	private Invite getInvite(String receptor) {
		Invite result = null;

		for (Invite invite : invites) {
			if (invite.getReceptor().equals(receptor)) {
				result = invite;
			}
		}

		return result;
	}

	private Invite getReceivedInvites(String issuing) {
		Invite result = null;

		for (Invite invite : receivedInvites) {
			if (invite.getIssuing().equals(issuing)) {
				result = invite;
			}
		}

		return result;
	}

	public List<Invite> getReceivedInvites() {
		return receivedInvites;
	}

	public void setReceivedInvites(List<Invite> receivedInvites) {
		this.receivedInvites = receivedInvites;
	}

	public void addReceivedInvites(Invite invite) {
		receivedInvites.add(invite);
	}

	public void refuseInvite(String user) {
		Invite invite = getReceivedInvites(user);
		invite.refuse();
		translator.responseInvite(invite);
	}

	public void acceptInvite(String user) {
		Invite invite = getReceivedInvites(user);
		invite.accept();
		this.acceptedInvite = true;
		translator.responseInvite(invite);
		addUserMessage("Wait for the master player!");
	}

	public boolean isAcceptedInvite() {
		return acceptedInvite;
	}

	public void startGame() {

		List<String> usersPlayers = new ArrayList<String>();
		usersPlayers.add(myUsername);
		for (Invite invite : invites) {
			if (invite.getStatus().equals(Invite.ACCPETED)) {
				usersPlayers.add(invite.getReceptor());
			}
		}

		if (usersPlayers.size() > 1) {
			translator.startGame(usersPlayers);
		}

	}

}
