package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import system.Parameters;

/**
 * Represents the room game. All Actions must be do by a object of this class.
 * The manipulation of object (engine game) be diferents implements in the
 * server and client. Each have yours.
 * 
 */
public class Room implements Serializable {

	private static final long serialVersionUID = -3183120280903804020L;
	
	private int id;
	
	// Players in room
	private List<Player> players;

	// Dominos available for players
	private List<Domino> dominosAvailable;

	// The central domino on board
	private DominoOnBoard dominoCenter;

	// Determines if the game started
	private boolean gameStarted;

	// Determines it the game finished
	private boolean finishedGame;

	public Room(int id) {
		setPlayers(new ArrayList<Player>());
		setDominosAvailable(new ArrayList<Domino>());
		setGameStarted(false);
		generateDominos();
		this.id = id;
	}

	/**
	 * Start the game
	 */
	public void start() {
		giveDominos();
		setGameStarted(true);
	}

	/**
	 * Add a player in room. However the room does not exceed the limit of
	 * players.
	 * 
	 * @param player
	 */
	public void addPlayers(Player player) {
		if (getPlayers().size() < Parameters.MAX_NUMBER_OF_PLAYERS) {
			getPlayers().add(player);
		}
	}

	/**
	 * Defines the next player, according to the sequence.
	 */
	public void next() {

		Player currentPlayer = whoIsPlaying();
		Player nextPlayer = getNextPlayerById(currentPlayer.getId());

		currentPlayer.setToken(false);
		nextPlayer.setToken(true);

	}

	/**
	 * Get next player in the sequence by current id.
	 * 
	 * @param id
	 * @return next player
	 */
	public Player getNextPlayerById(int id) {
		return getPlayerById(nextId(id));
	}

	/**
	 * Get a player by username
	 * 
	 * @param username
	 * @return player
	 */
	public Player getPlayer(String username) {

		Player result = null;

		for (Player p : getPlayers()) {
			if (p.getUsername().equals(username)) {
				result = p;
				break;
			}
		}

		return result;
	}

	/**
	 * Get a player by id
	 * 
	 * @param id
	 * @return player
	 */
	public Player getPlayerById(int id) {
		Player result = null;

		for (Player p : getPlayers()) {
			if (p.getId() == id) {
				result = p;
			}
		}

		return result;
	}

	/**
	 * Put a domino in the board of room.
	 * 
	 * @param domino
	 * @param side
	 * @return true if the domino is on the board.
	 */
	private boolean putOnBoard(Domino domino, String side) {

		boolean result = false;
		DominoOnBoard newBoardNode = new DominoOnBoard(domino);

		// Checks whether there are any domino on the board.
		if (getDominoCenter() != null) {

			DominoOnBoard lastBoardNode = getLastDominoBoard(side);

			// Checks if the moves is possible between two nodes
			if (newBoardNode.defineSides(lastBoardNode, side)) {

				// Execute moviment
				if (side.equals(Parameters.LEFT)
						&& lastBoardNode.putOnLeft(newBoardNode)) {
					result = true;
				} else if (side.equals(Parameters.RIGHT)
						&& lastBoardNode.putOnRight(newBoardNode)) {
					result = true;
				}

			}

		} else {

			/*
			 * If not exists put the central Domino on the board. In this case,
			 * the sides of domino is not important.
			 */
			newBoardNode.setLeftSide(Parameters.SIDE1);
			newBoardNode.setRightSide(Parameters.SIDE2);
			setDominoCenter(newBoardNode);
			result = true;
		}

		return result;
	}

	/**
	 * From the central node gets the last node in the specified side.
	 * 
	 * @param side
	 * @return the last node of the side
	 */
	private DominoOnBoard getLastDominoBoard(String side) {

		// Get central node
		DominoOnBoard lastBoard = getDominoCenter();

		switch (side) {
		case Parameters.LEFT:
			while (lastBoard.getLeft() != null) {
				lastBoard = lastBoard.getLeft();
			}
			break;

		case Parameters.RIGHT:
			while (lastBoard.getRight() != null) {
				lastBoard = lastBoard.getRight();
			}
			break;
		}

		return lastBoard;
	}

	/**
	 * Makes the initial distribution of dominos for players. Each player must
	 * have the same number of domino. The remainings should be available at the
	 * table.
	 */
	private void giveDominos() {

		/*
		 * Calculates the quantity of dominos by player. The value must be
		 * truncated and integer to not miss dominos
		 */
		int numberDominosByPlayer = (int) (getDominosAvailable().size() / players
				.size());

		/*
		 * Control variables
		 */
		int biggestDomino = 0;
		int biggestBushwacker = 0;

		/*
		 * The player that will start game
		 */
		Player last = null;

		/*
		 * Random sort dominos for each player.
		 */
		for (Player p : getPlayers()) {
			Random random = new Random();
			for (int i = 0; i < numberDominosByPlayer; i++) {

				// Sort a available domino
				int index = random.nextInt(getDominosAvailable().size());
				Domino domino = getDominosAvailable().get(index);

				// move available domino to palyer
				p.addDomino(domino);
				getDominosAvailable().remove(index);

				/*
				 * Determines who will start the game. Starts who has the
				 * largest Bushwacker.
				 */
				if (domino.getSide1() == domino.getSide2()
						&& domino.getSide1() + domino.getSide2() > biggestBushwacker) {
					biggestBushwacker = domino.getSide1() + domino.getSide2();
					last = p;
				}

				/*
				 * If no there Bushwacker raffled, start that has the domino
				 * with the largest sum of sides.
				 */
				if (biggestBushwacker == 0
						&& domino.getSide1() + domino.getSide2() > biggestDomino) {
					biggestDomino = domino.getSide1() + domino.getSide2();
					last = p;
				}

			}
		}
		
		last.setToken(true);

	}

	/**
	 * Generate all dominos.
	 */
	private void generateDominos() {
		for (int i = 0; i <= 6; i++) {
			for (int j = i; j <= 6; j++) {
				getDominosAvailable().add(new Domino(i, j));
			}
		}
	}

	/**
	 * Get the next id of the sequence room based on an id
	 * 
	 * @param id
	 * @return next id of the sequence room
	 */
	private int nextId(int id) {

		// Next id
		int result = ++id;

		// Check quantity players in room.
		if (result < 1 || result > getPlayers().size()) {
			result = 1;
		}

		return result;
	}

	/**
	 * Player action of put a domino on board. This method moves the domino from
	 * the player's hands to the table.
	 * 
	 * @param domino
	 * @param side
	 * @param username
	 * @return true if the move carreid.
	 */
	public boolean putOnBoard(Domino domino, String side, String username) {

		boolean result = false;
		
		//Execute moviment
		if (putOnBoard(domino, side)) {
			getPlayer(username).removeDomino(domino);

			/*
			 * Check if game is finished. There are two possibilities: a) The
			 * player has no domino. b) No more moves.
			 */
			if (getPlayer(username).getDominos().size() == 0
					|| !hasPossibleMoves()) {
				setFinishedGame(true);
			}

			result = true;
		}

		return result;
	}
	
	/**
	 * 
	 * @return The player who is current playing.
	 */
	public Player whoIsPlaying() {
		
		Player result = null;

		for (Player p : getPlayers()) {
			if (p.isToken()) {
				result = p;
				break;
			}
		}

		return result;
	}
	
	/**
	 * Get to player the first domino in the stack available
	 *  
	 * @param player
	 * @return domino
	 */
	public boolean pushAvaliableDominos(Player player) {

		boolean result = false;
		
		//Check if there domino in the stacks
		if (getDominosAvailable().size() > 0) {
			Domino domino = getDominosAvailable().get(0);
			player.addDomino(domino);
			getDominosAvailable().remove(0);
			result = true;
		}

		return result;
	}
	
	/**
	 * Check there are still moves.
	 * 
	 * @return true if exists.
	 */
	private boolean hasPossibleMoves() {

		boolean result = false;
		int leftNumberAvailable = -1;
		int rightNumberAvailable = -1;

		// Get number available each side of the board.
		if (getDominoCenter() != null) {
			leftNumberAvailable = getLastDominoBoard(Parameters.LEFT)
					.getLeftSide();
			rightNumberAvailable = getLastDominoBoard(Parameters.RIGHT)
					.getRightSide();
		}

		// Checks if any player has a available number
		for (Player player : getPlayers()) {
			for (Domino domino : player.getDominos()) {
				if (domino.getSide1() == leftNumberAvailable
						|| domino.getSide1() == rightNumberAvailable
						|| domino.getSide2() == leftNumberAvailable
						|| domino.getSide2() == rightNumberAvailable) {

					result = true;
					break;
				}
			}

		}

		return result;

	}

	public Player whoWon() {

		Player result = null;

		if (isFinishedGame()) {
			for (Player player : getPlayers()) {
				if (player.getDominos().size() == 0) {
					result = player;
					break;
				}
			}
		}

		return result;
	}

	// Getters and Setters

	public int getId() {
		return id;
	}

	public boolean isGameStarted() {
		return gameStarted;
	}

	public void setGameStarted(boolean gameStarted) {
		this.gameStarted = gameStarted;
	}

	public DominoOnBoard getDominoCenter() {
		return dominoCenter;
	}

	public void setDominoCenter(DominoOnBoard boardNode) {
		this.dominoCenter = boardNode;
	}

	public List<Domino> getDominosAvailable() {
		return dominosAvailable;
	}

	public List<Player> getPlayers() {
		return players;
	}

	private void setPlayers(List<Player> players) {
		this.players = players;
	}

	private void setDominosAvailable(List<Domino> dominosAvailable) {
		this.dominosAvailable = dominosAvailable;
	}

	public boolean isFinishedGame() {
		return finishedGame;
	}

	public void setFinishedGame(boolean finishedGame) {
		this.finishedGame = finishedGame;
	}

}
