package model;

import java.io.Serializable;

/**
 * Part of the Domino that is manipulated by the player.
 */
public class Domino implements Serializable {

	private static final long serialVersionUID = -7534721277825768082L;

	private int side1;

	private int side2;

	public Domino(int side1, int side2) {
		setSide1(side1);
		setSide2(side2);
	}

	public int getSide1() {
		return side1;
	}

	public void setSide1(int side1) {
		this.side1 = side1;
	}

	public int getSide2() {
		return side2;
	}

	public void setSide2(int side2) {
		this.side2 = side2;
	}

	/**
	 * Method must used by client. Define client path with image of the domino
	 * to be used by GUI.
	 * 
	 * @return complete path of the image file.
	 */
	public String getImagePath() {
		String rootPath = "/images/";
		return rootPath + side1 + "x" + side2 + ".png";
	}

	/**
	 * Method must used by client. When the object is placed in a string,
	 * formats the display to the user.
	 * Can be modified if necessary.
	 */
	@Override
	public String toString() {
		return getSide1() + "x" + getSide2();
	}
	
}
