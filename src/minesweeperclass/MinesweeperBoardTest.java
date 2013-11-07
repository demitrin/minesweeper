package minesweeperclass;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class MinesweeperBoardTest {

	/*
	 * Test each public method, testing valid grid inputs and invalid grid
	 * inputs
	 * 
	 * Testing space: constructor(), constructor(int size), constructor(char[][]
	 * board), flag(x,y), deFlag(x,y), dig(x,y), recursive chaining from
	 * dig(x,y), look(), checkRep()
	 */

	/*
	 * The following tests will test the constructors with valid and invalid
	 * inputs. If the input is invalid, test for IllegalArgumentException
	 */
	@Test
	public void testDefaultConstructor() {
		// default constructor test
		MinesweeperBoard board = new MinesweeperBoard();
		assertTrue(board.look().size() == 10);
		assertTrue(board.checkRep());
	}

	@Test
	public void testSizeConstructor() {
		// constructor with size input test
		MinesweeperBoard board = new MinesweeperBoard(20);
		assertTrue(board.look().size() == 20);
		assertTrue(board.checkRep());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSizeConstructorException() {
		// test invalid size arguments for exception
		new MinesweeperBoard(-1);
		new MinesweeperBoard(0);
	}

	@Test
	public void testBoardConstructor() {
		// input char[][] constructor test
		char[][] testBoard = new char[5][5];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (j % 2 == 1) {
					testBoard[i][j] = 'B';
				} else
					testBoard[i][j] = '-';
			}
		}
		// construct expected List<String>
		MinesweeperBoard board = new MinesweeperBoard(testBoard);
		List<String> expectedStringArray = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			expectedStringArray.add("");
			for (int j = 0; j < 5; j++) {
				expectedStringArray.set(i, expectedStringArray.get(i) + "- ");
			}
		}
		// compare look() to the expected List<String>
		assertTrue(board.look().equals(expectedStringArray));
		assertTrue(board.checkRep());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBoardConstructorException() {
		// test invalid char[][] inputs for exception
		new MinesweeperBoard(new char[2][3]);
		new MinesweeperBoard(new char[3][2]);
		new MinesweeperBoard(new char[0][0]);
	}

	/*
	 * The following tests will focus on flag(x,y) and deFlag(x,y) testing for
	 * actions if given valid x,y grid coordinates and handling for invalid x,y
	 * coordinates (taking no action)
	 */

	@Test
	public void testFlagValidInputs() {
		// test valid inputs for board.flag(x,y)
		char[][] testBoard = new char[5][5];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (j % 2 == 1) {
					testBoard[i][j] = 'B';
				} else
					testBoard[i][j] = '-';
			}
		}
		// construct expected List<String>
		MinesweeperBoard board = new MinesweeperBoard(testBoard);
		List<String> expectedStringArray = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			expectedStringArray.add("");
			for (int j = 0; j < 5; j++) {
				expectedStringArray.set(i, expectedStringArray.get(i) + "- ");
			}
		}
		// flag 2,2
		List<String> boardRep = board.flag(2, 2);
		expectedStringArray.set(2, "- - F - - ");
		assertTrue(boardRep.equals(expectedStringArray));
		assertTrue(board.checkRep());

		// now with the same board, flag 3,2
		boardRep = board.flag(3, 2);
		expectedStringArray.set(2, "- - F F - ");
		assertTrue(boardRep.equals(expectedStringArray));
		assertTrue(board.checkRep());

		// dig at 0,0 then flag it
		board.dig(0, 0);
		boardRep = board.flag(0, 0);
		for (int i = 0; i < 5; i++) {
			if (i == 0) {
				expectedStringArray.set(i, "2 - - - - ");
			} else if (i == 2) {
				expectedStringArray.set(i, "- - F F - ");

			} else {
				expectedStringArray.set(i, "- - - - - ");
			}
		}
		// check that it did not mark the exposed spot as FLAGGED
		assertTrue(boardRep.equals(expectedStringArray));
		assertTrue(board.checkRep());
	}

	@Test
	public void testFlagInvalidInputs() {
		// test for inputs that are not on the grid for board.flag(x,y)

		// construct testBoard
		char[][] testBoard = new char[5][5];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (j % 2 == 1) {
					testBoard[i][j] = 'B';
				} else
					testBoard[i][j] = '-';
			}
		}
		// construct expected List<String>
		MinesweeperBoard board = new MinesweeperBoard(testBoard);
		List<String> expectedStringArray = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			expectedStringArray.add("");
			for (int j = 0; j < 5; j++) {
				expectedStringArray.set(i, expectedStringArray.get(i) + "- ");
			}
		}
		// incorrect grid positions
		board.flag(-1, 0);
		board.flag(0, -1);
		board.flag(6, 4);
		board.flag(4, 6);
		// check that nothing was set to FLAGGED
		assertTrue(board.look().equals(expectedStringArray));
		assertTrue(board.checkRep());
	}

	@Test
	public void testDeFlagValidInputs() {
		// test deFlag(x,y) for (x,y) that are on the grid

		// construct testBoard
		char[][] testBoard = new char[5][5];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (j % 2 == 1) {
					testBoard[i][j] = 'B';
				} else
					testBoard[i][j] = '-';
			}
		}
		// construct expected List<String>
		MinesweeperBoard board = new MinesweeperBoard(testBoard);
		List<String> expectedStringArray = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			expectedStringArray.add("");
			for (int j = 0; j < 5; j++) {
				expectedStringArray.set(i, expectedStringArray.get(i) + "- ");
			}
		}
		// insert a flag and deflag it. check that there are no flags on the
		// board
		board.flag(0, 0);
		List<String> boardRep = board.deFlag(0, 0);
		assertTrue(boardRep.equals(expectedStringArray));
		assertTrue(board.checkRep());

		// insert lots of flags and deflag them. check that there are no flags
		// on the board
		board.flag(0, 0);
		board.flag(1, 2);
		board.flag(3, 2);
		board.flag(4, 3);
		board.deFlag(3, 2);
		board.deFlag(1, 2);
		board.deFlag(4, 3);
		boardRep = board.deFlag(0, 0);
		assertTrue(boardRep.equals(expectedStringArray));
		assertTrue(board.checkRep());

	}

	@Test
	public void testDeFlagOtherInputs() {
		// test all other possible deFlag(x,y) situations

		// construct testBoard
		char[][] testBoard = new char[5][5];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (j % 2 == 1) {
					testBoard[i][j] = 'B';
				} else
					testBoard[i][j] = '-';
			}
		}
		MinesweeperBoard board = new MinesweeperBoard(testBoard);
		List<String> expectedStringArray = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			expectedStringArray.add("");
			for (int j = 0; j < 5; j++) {
				expectedStringArray.set(i, expectedStringArray.get(i) + "- ");
			}
		}
		// test on untouched (x,y)
		List<String> boardRep = board.deFlag(2, 2);
		assertTrue(boardRep.equals(expectedStringArray));
		assertTrue(board.checkRep());

		// test outside of bounds
		board.deFlag(-1, 0);
		board.deFlag(0, -1);
		board.deFlag(6, 3);
		boardRep = board.deFlag(3, 6);
		assertTrue(boardRep.equals(expectedStringArray));
		assertTrue(board.checkRep());

		// test on exposed (x,y)
		board.dig(0, 0);
		expectedStringArray.set(0, "2 - - - - ");
		boardRep = board.deFlag(0, 0);
		assertTrue(boardRep.equals(expectedStringArray));
		assertTrue(board.checkRep());
	}

	/*
	 * the following tests will test dig(x,y) inputs: (x,y) on grid that are
	 * either safe, bombs, flagged, or already explored, and (x,y) not on grid
	 */

	@Test
	public void testDigUntouched() {
		// test dig(x,y) on untouched spots, both bombs and empty
		// not checking for proliferation of untouched neighbors

		// construct testBoard
		char[][] testBoard = new char[5][5];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (j % 2 == 1) {
					testBoard[i][j] = 'B';
				} else
					testBoard[i][j] = '-';
			}
		}
		MinesweeperBoard board = new MinesweeperBoard(testBoard);
		List<String> expectedStringArray = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			expectedStringArray.add("");
			for (int j = 0; j < 5; j++) {
				expectedStringArray.set(i, expectedStringArray.get(i) + "- ");
			}
		}
		/*
		 * current board: allow B to represent an untouched bomb
		 * 
		 * 					- B - B -
		 * 					- B - B -
		 * 					- B - B -
		 * 					- B - B -
		 * 					- B - B -
		 */
		
		// dig(x,y) on non-bomb
		board.dig(0, 0);
		List<String> boardRep = board.dig(0, 1);
		expectedStringArray.set(0, "2 - - - - ");
		expectedStringArray.set(1, "3 - - - - ");
		assertTrue(boardRep.equals(expectedStringArray));
		
		// dig(x,y) on a bomb, check that we get a null board
		boardRep = board.dig(1,0);
		assertTrue(boardRep == null);
	}
}
