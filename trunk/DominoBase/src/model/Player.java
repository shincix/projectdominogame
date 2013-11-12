package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represent who is playing
 */
public class Player implements Serializable {
	
	private static final long serialVersionUID = -7414266462219657287L;
	
	// Determines a sequence of the game. 
	private int id;
	
	// Dominos in the hands of the player.
	private List<Domino> dominos;
	
	// Identifies the player. Unique identifier in the whole game.
	private String username;
	
	// Determines if the player can play.
	private boolean token;
	
	
	public Player() {
		dominos = new ArrayList<Domino>();
		token = false;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Domino> getDominos() {
		return dominos;
	}

	public void setDominos(List<Domino> dominos) {
		this.dominos = dominos;
	}

	public boolean isToken() {
		return token;
	}

	public void setToken(boolean token) {
		this.token = token;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void addDomino(Domino domino) {
		dominos.add(domino);
	}

	public void removeDomino(Domino domino) {
		dominos.remove(domino);
	}
	
}
