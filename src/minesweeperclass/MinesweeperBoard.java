package minesweeperclass;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperBoard {
	private final char[][] Board;
	private final char BOMB = 'B';
	private final char UNTOUCHED = '-';
	private final char DUG = 'D';
	private final char FLAGGED = 'F';
	private final char FLAGGED_BOMB = 'C';
	private final int size;

	public MinesweeperBoard(int size) throws IllegalArgumentException {
		if (size < 1) {
			throw new IllegalArgumentException(
					"Invalid size input! We need at least a 1x1 grid to play Minesweeper");
		}
		this.size = size;
		Board = new char[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (Math.random() < .25) {
					Board[i][j] = BOMB;
				} else {
					Board[i][j] = UNTOUCHED;
				}
			}
		}
	}

	public MinesweeperBoard(char[][] board) throws IllegalArgumentException {
		if (board.length != board[0].length) {
			throw new IllegalArgumentException(
					"You input a grid that was not a square!");
		} else if (board.length < 1) {
			throw new IllegalArgumentException(
					"We need at least a 1x1 grid to play Minesweeper");
		}
		size = board.length;
		Board = board;
		if (!checkRep()) {
			throw new IllegalArgumentException("Input an invalid grid");
		}
	}

	public MinesweeperBoard() {
		this(10);
	}

	public int getSize() {
		return size;
	}

	public boolean checkRep() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (!(isType(i, j, BOMB, UNTOUCHED)
						|| isType(i, j, DUG, FLAGGED) || isType(i, j,
							FLAGGED_BOMB))) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean isType(int x, int y, char type) {
		if ((x < size && x >= 0) && (y < size && y >= 0)) {
			return Board[y][x] == type;
		}
		return false;
	}

	private boolean isType(int x, int y, char type1, char type2) {
		return isType(x, y, type1) || isType(x, y, type2);
	}

	private String getBombNeighbors(int x, int y) {
		int bombCount = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (!(i == 0 && j == 0)
						&& isType(x + i, y + j, BOMB, FLAGGED_BOMB)) {
					bombCount++;
				}
			}
		}
		if (bombCount == 0) {
			return " ";
		}
		return "" + bombCount;
	}

	private void setDug(int x, int y) {
		Board[y][x] = DUG;
		if (getBombNeighbors(x, y).equals(" ")) {
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					if (!(i == 0 && j == 0) && isType(x + i, y + j, UNTOUCHED)) {
						setDug(x + i, y + j);
					}
				}
			}
		}
	}

	private List<String> getBoard() {
		List<String> boardStrings = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			boardStrings.add("");
			for (int j = 0; j < size; j++) {
				if (isType(j, i, DUG)) {
					boardStrings.set(i,
							boardStrings.get(i) + getBombNeighbors(j, i) + " ");
				} else if (isType(j, i, FLAGGED, FLAGGED_BOMB)) {
					boardStrings.set(i, boardStrings.get(i) + "F" + " ");
				} else if (isType(j, i, UNTOUCHED, BOMB)) {
					boardStrings.set(i, boardStrings.get(i) + "-" + " ");
				} else {
					throw new RuntimeException(
							"There was an undefined type in the MinesweeperBoard");
				}

			}
		}
		return boardStrings;
	}

	public synchronized List<String> flag(int x, int y) {
		if (isType(x, y, BOMB, UNTOUCHED)) {
			Board[y][x] = FLAGGED;
		}
		return getBoard();
	}

	public synchronized List<String> deFlag(int x, int y) {
		if (isType(x, y, FLAGGED)) {
			Board[y][x] = UNTOUCHED;
		}
		return getBoard();
	}

	public synchronized List<String> dig(int x, int y) {
		if (isType(x, y, UNTOUCHED)) {
			setDug(x, y);
		} else if (isType(x, y, BOMB)) {
			Board[y][x] = DUG;
			// indicates socket should be closed
			return null;
		}
		return getBoard();
	}

	public synchronized List<String> look() {
		return getBoard();
	}

}
