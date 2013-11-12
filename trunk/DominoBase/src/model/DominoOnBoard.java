package model;

import java.io.Serializable;

import system.Parameters;

/**
 * Data structure for assembling the game with dominos placed by players.
 * 
 * When put on the table is necessary to identify which side of the domino is
 * left and what is right. Moreover, one should link the pieces to be able
 * scroll through on the structure.
 */
public class DominoOnBoard implements Serializable {

	private static final long serialVersionUID = 5927176236567547194L;

	private Domino domino;

	// Binds to object that is positioned on the left side
	private DominoOnBoard left;

	// Determines which side of the domino is left
	private int leftSide;

	// Binds to object that is positioned on the right side
	private DominoOnBoard right;

	// Determines which side of the domino is right
	private int rightSide;

	/**
	 * Default constructor. The leftSide and rightSide must be initialized with
	 * value that not exists in the game. Range values of domino: 1-6
	 * 
	 * @param domino
	 */
	public DominoOnBoard(Domino domino) {
		setDomino(domino);
		setLeft(null);
		setLeftSide(-1);
		setRight(null);
		setRightSide(-1);
	}

	/**
	 * Put a domino in the left side of domino of the this object.
	 * 
	 * A Domino only can be placed on the left side of the other, if not exist a
	 * domino and your right side equals of the left side of the other.
	 * 
	 * @param boardNode
	 *            node to be placed on the left side.
	 * @return true if the domino was placed on the left side.
	 */
	public boolean putOnLeft(DominoOnBoard boardNode) {

		boolean result = false;

		// Verify if left side is available and if the domino is compatible with
		// the side.
		if (getLeft() == null && getLeftSide() == boardNode.getRightSide()) {
			setLeft(boardNode);
			result = true;
		}

		return result;

	}

	/**
	 * Put a domino in the right side of domino of the this object.
	 * 
	 * A Domino only can be placed on the right side of the other, if not exist
	 * a domino and your left side equals of the right side of the other.
	 * 
	 * @param boardNode
	 *            node to be placed on the right side.
	 * @return true if the domino was placed on the right side.
	 */
	public boolean putOnRight(DominoOnBoard boardNode) {

		boolean result = false;

		// Verify if left side is available and if the domino is compatible with
		// the side.
		if (getRight() == null && getRightSide() == boardNode.getLeftSide()) {
			setRight(boardNode);
			result = true;
		}

		return result;
	}

	// TODO Mudar metodo para Room... Não cabe ao nó decidir o seu lado e sim
	// caracteristicas da mesa.
	// TODO Comentar metodo.
	/**
	 * 
	 * @param boardNode
	 * @param side
	 * @return
	 */
	public boolean defineSides(DominoOnBoard boardNode, String side) {

		boolean result = false;

		// Initialized with value not exits in the domino game.
		int valueSide = -1;

		if (side.equals(Parameters.LEFT)) {
			valueSide = boardNode.getLeftSide();
			if (getDomino().getSide1() == valueSide) {
				setRightSide(Parameters.SIDE1);
				setLeftSide(Parameters.SIDE2);
			} else if (getDomino().getSide2() == valueSide) {
				setRightSide(Parameters.SIDE2);
				setLeftSide(Parameters.SIDE1);
			}

			// se isso ocorrer é por que as pecas se encaixam
			if (getLeftSide() >= 0 && getRightSide() >= 0) {
				// TODO Lembrar desse detalhe ao mudar o metodo para Room
				setRight(boardNode);
				result = true;
			}
		} else if (side.equals(Parameters.RIGHT)) {
			valueSide = boardNode.getRightSide();
			if (getDomino().getSide1() == valueSide) {
				setRightSide(Parameters.SIDE2);
				setLeftSide(Parameters.SIDE1);
			} else if (getDomino().getSide2() == valueSide) {
				setRightSide(Parameters.SIDE1);
				setLeftSide(Parameters.SIDE2);
			}

			if (getLeftSide() >= 0 && getRightSide() >= 0) {
				// TODO Lembrar desse detalhe ao mudar o metodo para Room
				setLeft(boardNode);
				result = true;
			}
		}

		return result;
	}

	/*
	 * Getters and Setters
	 */

	public Domino getDomino() {
		return domino;
	}

	public void setDomino(Domino domino) {
		this.domino = domino;
	}

	public DominoOnBoard getLeft() {
		return left;
	}

	public int getLeftSide() {
		return leftSide;
	}

	private void setLeftSide(int leftside) {
		this.leftSide = leftside;
	}

	public void setLeftSide(String side) {

		int leftSide = -1;

		switch (side) {
		case Parameters.SIDE1:
			leftSide = getDomino().getSide1();
			break;

		case Parameters.SIDE2:
			leftSide = getDomino().getSide2();
			break;
		}

		setLeftSide(leftSide);
	}

	public DominoOnBoard getRight() {
		return right;
	}

	public int getRightSide() {
		return rightSide;
	}

	private void setRightSide(int rightSide) {
		this.rightSide = rightSide;
	}

	public void setRightSide(String side) {

		int rightSide = -1;

		switch (side) {
		case Parameters.SIDE1:
			rightSide = getDomino().getSide1();
			break;

		case Parameters.SIDE2:
			rightSide = getDomino().getSide2();
			break;
		}

		setRightSide(rightSide);

	}

	private void setLeft(DominoOnBoard boardNode) {
		this.left = boardNode;
	}

	private void setRight(DominoOnBoard boardNode) {
		this.right = boardNode;
	}

}
