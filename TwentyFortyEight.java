import java.util.Random;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
public class TwentyFortyEight {
	public static void main(String[] args) throws Exception{
			
		String[][] nums = { { "-", "-", "-", "-" }, { "-", "-", "-", "-" },
			{ "-", "-", "-", "-" }, { "-", "-", "-", "-" } };

		intro();
		nums = generateFirstTile(nums);
		try {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.print("\n\tWelcome to 2048\n\n\n\t");

		for (int x = 0; x < nums.length; x++) {
			for (int y = 0; y < nums.length; y++) {
				System.out.print(nums[x][y] + "\t");
			}
			System.out.print("\n\n\t");
		}
		System.out.println();
		System.out.println("\tUse WASD as arrow keys:\n\n\t\tW\n\tA\t\tD\n\t\tS");
		System.out.print("\n\thit 'Q' to quit:\n\n\t");
		Scanner keyboard = new Scanner(System.in);
		
		while(true) {
			if (checkGameOver(nums)) {
				gameOver(nums);
				break;
			}
			String input = keyboard.next();
			if (input.equals("a")) {
				nums = Left(nums);
			}
			else if (input.equals("d")) {
				nums = Right(nums);
			}
			else if (input.equals("w")) {
				nums = Up(nums);
			}
			else if (input.equals("s")) {
				nums = Down(nums);
			}
			else if (input.equals("q")) {
				break;
			}
			try {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} catch (Exception e) {
				System.out.println(e);
			}
			System.out.print("\n\tWelcome to 2048\n\n\n\t");

			for (int x = 0; x < nums.length; x++) {
				for (int y = 0; y < nums.length; y++) {
					System.out.print(nums[x][y] + "\t");
				}
				System.out.print("\n\n\t");
			}
			System.out.println();
			System.out.println("\tUse WASD as arrow keys:\n\n\t\tW\n\tA\t\tD\n\t\tS");
			System.out.print("\n\thit 'Q' to quit:\n\n\t");
		}
		
	}

	public static boolean checkGameOver(String[][] board) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if(!isNumeric(board[i][j])) {
					return false;
				}
			}
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				if(board[i][j] == board[i][j+1]) {
					return false;
				}
			}
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				if(board[i][j] == board[i+1][j]) {
					return false;
				}
			}
		}
		return true;
	}

	public static String[][] Left(String[][] board) {
		String[][] oldBoard = new String[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				oldBoard[i][j] = board[i][j];
			}
		}
		String[][] tempBoard = swipeLeft(board);
		if (checkChange(oldBoard, tempBoard)) {
			board = generateTileLeft(tempBoard);
		}
		return board;
	}
	public static String[][] Right(String[][] board) {
		String[][] oldBoard = new String[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				oldBoard[i][j] = board[i][j];
			}
		}
		String[][] tempBoard = swipeRight(board);
		if (checkChange(oldBoard, tempBoard)) {
			board = generateTileRight(tempBoard);
		}
		return board;
	}
	public static String[][] Up(String[][] board) {
		String[][] tempBoard = swipeUp(board);
		if (checkChange(board, tempBoard)) {
			board = generateTileUp(tempBoard);
		}
		return board;
	}
	public static String[][] Down(String[][] board) {
		String[][] tempBoard = swipeDown(board);
		if (checkChange(board, tempBoard)) {
			board = generateTileDown(tempBoard);
		}
		return board;
	}

	public static String[][] swipeDown(String[][] board) {
		board = rotateMatrixRight(board);
		board = swipeLeft(board);
		board = rotateMatrixRight(board);
		board = rotateMatrixRight(board);
		board = rotateMatrixRight(board);

		return board;
	}
	public static String[][] swipeUp(String[][] board) {
		board = rotateMatrixRight(board);
		board = swipeRight(board);
		board = rotateMatrixRight(board);
		board = rotateMatrixRight(board);
		board = rotateMatrixRight(board);

		return board;
	}

	public static boolean checkChange(String[][] board, String[][] tempBoard) {
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				if (!(board[x][y].equals(tempBoard[x][y]))) {
					return true;
				}
			}
		}
		return false;
	}

	public static String[][] swipeLeft(String[][] board) {
		// combine first
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[x].length - 1; y++) {
				if (isNumeric(board[x][y])) {
					for (int z = y+1; z < board[x].length; z++) {
						if (isNumeric(board[x][z])
								&& board[x][z].equals(board[x][y])) {
							 board[x][y] = (Integer.parseInt(board[x][y]) * 2) + "";
							 board[x][z] = "-";
							 y = z; 	// so you still only go through each row once here
							 z = board[x].length;
						}
						else if (isNumeric(board[x][z])) {
							 z = board[x].length;
						}
					}
				}
			}
		}

		// then shift (to avoid shifting more than once)
		for (int x = 0; x < board.length; x++) {
			board[x] = combineLeft(board[x]);
		}
		return board;
	}

	public static String[][] swipeRight(String[][] board) {
		// combine first
		for (int x = board.length - 1; x >= 0; x--) {
			for (int y = board[x].length - 1; y > 0; y--) {
				if (isNumeric(board[x][y])) {
					for (int z = y-1; z >= 0; z--) {
						if (isNumeric(board[x][z])
								&& board[x][z].equals(board[x][y])) {
							 board[x][y] = (Integer.parseInt(board[x][y]) * 2) + "";
							 board[x][z] = "-";
							 y = z; 	// so you still only go through each row once here
							 z = -1;
						}
						else if (isNumeric(board[x][z])) {
							 z = -1;
						}
					}
				}
			}
		}

		// then shift (to avoid shifting more than once)
		for (int x = 0; x < board.length; x++) {
			board[x] = combineRight(board[x]);
		}
		return board;
	}

	public static String[] combineLeft(String[] board) {
		for (int y = 0; y < board.length; y++) {
			if (board[y].equals("-")) {
				int b = y;
				while (b < board.length && board[b].equals("-")) {
					b++;
				}
				if (b < board.length) {
					board[y] = board[b];
					board[b] = "-";
				}
			}
		}
		return board;
	}

	public static String[] combineRight(String[] board) {
		for (int y = board.length - 1; y >= 0; y--) {
			if (board[y].equals("-")) {
				int b = y;
				while (b >= 0 && board[b].equals("-")) {
					b--;
				}
				if (b >= 0) {
					board[y] = board[b];
					board[b] = "-";
				}
			}
		}
		return board;
	}
	
	public static boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static String[][] generateTileLeft(String[][] board) {
		Random rand = new Random();
		int xCoord = 3;
		int yCoord = rand.nextInt(4);
		while (isNumeric(board[yCoord][xCoord])) {
			yCoord = rand.nextInt(4);
		}
		if (rand.nextInt(10) % 8 == 0) {
			board[yCoord][xCoord] = 4 + "";

		}
		else {
			board[yCoord][xCoord] = 2 + "";
		}
		return board;
	}
	public static String[][] generateTileRight(String[][] board) {
		Random rand = new Random();
		int xCoord = 0;
		int yCoord = rand.nextInt(4);
		while (isNumeric(board[yCoord][xCoord])) {
			yCoord = rand.nextInt(4);
		}
		if (rand.nextInt(10) % 8 == 0) {
			board[yCoord][xCoord] = 4 + "";

		}
		else {
			board[yCoord][xCoord] = 2 + "";
		}
		return board;
	}
	public static String[][] generateTileDown(String[][] board) {
		Random rand = new Random();
		int xCoord = rand.nextInt(4);
		int yCoord = 0;
		while (isNumeric(board[yCoord][xCoord])) {
			xCoord = rand.nextInt(4);
		}
		if (rand.nextInt(10) % 8 == 0) {
			board[yCoord][xCoord] = 4 + "";

		}
		else {
			board[yCoord][xCoord] = 2 + "";
		}
		return board;
	}
	public static String[][] generateTileUp(String[][] board) {
		Random rand = new Random();
		int xCoord = rand.nextInt(4);
		int yCoord = 3;
		while (isNumeric(board[yCoord][xCoord])) {
			xCoord = rand.nextInt(4);
		}
		if (rand.nextInt(10) % 8 == 0) {
			board[yCoord][xCoord] = 4 + "";

		}
		else {
			board[yCoord][xCoord] = 2 + "";
		}
		return board;
	}
	public static String[][] generateFirstTile(String[][] board) {
		Random rand = new Random();
		int xCoord = rand.nextInt(4);
		int yCoord = rand.nextInt(4);
		
		board[yCoord][xCoord] = 2 + "";
		
		return board;
	}

	public static String[][] rotateMatrixRight(String[][] board) {
		String [][] newBoard = new String[4][4];
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				newBoard[x][y] = board[4-y-1][x];
			}
		}
		return newBoard;
	}

	public static void intro() {
		int lines = 17;
		while (lines > 0) {
			try {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} catch (Exception e) {
				System.out.println(e);
			}
			for (int i = 0; i < lines; i ++) {
				System.out.println();
			}
			System.out.println("\tWelcome to 2048\n\n");
			if (lines < 15)
				System.out.println("\tDeveloped by Scott Mallory");
			wait(300);
			lines--;
		}
		System.out.println("\n\n\tHit enter to begin:");
		Scanner keyboard = new Scanner(System.in);
		keyboard.nextLine();
	}

	public static void gameOver(String[][] nums) {
		int lines = 10;
		while (lines > 0) {
			try {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} catch (Exception e) {
				System.out.println(e);
			}
			System.out.print("\n\tWelcome to 2048\n\n\n\t");

			for (int x = 0; x < nums.length; x++) {
				for (int y = 0; y < nums.length; y++) {
					System.out.print(nums[x][y] + "\t");
				}
				System.out.print("\n\n\t");
			}
			for (int i = 0; i < lines; i ++) {
				System.out.println();
			}

			System.out.println("\tGame Over");
			wait(300);
			lines--;
		}
	}
	public static void wait (int n){
		long t0,t1;
		t0 = System.currentTimeMillis();
		do{
			t1 = System.currentTimeMillis();
		} while (t1 - t0 < n);
	}
}
