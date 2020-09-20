
import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.Border;

public class Frame1 extends JFrame implements ActionListener {
	 private final static int GRID = 9; 
	 private final static Border GREEN_BORDER = BorderFactory.createLineBorder(Color.green, 3);
	 private final static Border RED_BORDER = BorderFactory.createLineBorder(Color.red, 3);
	 private final static Border BLUE_BORDER = BorderFactory.createLineBorder(Color.blue, 3); 
	
	 private int[][] board = new int[9][9];
	 private int[][] boardCopy = new int[9][9];
	 private Timer timer = new Timer(1, this);
	 private int rRow = 0, rCol = 0; // use these to navigate the backtracking inside the timer listener.
	 private Set<Integer> preset = new HashSet<>(); // preset cells ; can't be changed
	 private Set<Integer> usergiven = new HashSet<>(); // user set cells, can't be changed unless board is cleared
	 boolean solved = false;
	 boolean solveBtnPressed = false;
 
	 private JFrame frame;
	 private JPanel sudokuPanel;
	 private JPanel[][] sudokuBox = new JPanel[3][3];
	 private JLabel[][] cell = new JLabel[9][9];
	 
	 // override the listener for the timer
	 // This takes place of the solve recursive function, by updating the configuartion of Rrow, Rcol and halting if it reaches the end
	 // with a viable number.
	 public void actionPerformed(ActionEvent e) {
		 	int i;
		 	for (i = board[rRow][rCol] + 1; i <= 9; i++) {
	    		if (isValid(i, rRow, rCol)) {
	    			board[rRow][rCol] = i;
	    			cell[rRow][rCol].setText(Integer.toString(i));
	    			cell[rRow][rCol].setBorder(GREEN_BORDER);
	    			break;
	    		}
	    	}
	    	
	    	if (i < 10) 
	    		updateCoeff(true);
	    	else {
	    		board[rRow][rCol] = 0;
	    		cell[rRow][rCol].setText("");
	    		cell[rRow][rCol].setBorder(RED_BORDER);
	    		//setCellBorder(rRow,rCol,Color.red);
	    		updateCoeff(false);
	    	}
	 }
	
	 // Helper method for the "Solve" button listener. It moves the current cell to the right and downwards if the parameter is true, otherwise to the left and upwards.
	 // If it reaches the end of the board and still needs to keep going, that means that we have solved the puzzle, so it stops the timer.
	 // If it reaches the start of the board and still needs to go backwards, that means that it is unsolvable.
	 // The program takes care to let the user insert values that keep the board solvable, so that the second case will never occur. 
	 private void updateCoeff(boolean onwards) {
		 if (onwards) {
			 while(board[rRow][rCol] != 0) {
				 if (rRow == GRID - 1 && rCol == GRID - 1) {
					 solved = true;
					 timer.stop();
					 for (int i = 0; i < GRID; i++)
						 for (int j = 0; j < GRID; j++)
							 setCellBorder(i,j, null);
					 return;
				 }
				 if (rCol < GRID -1) 
					 rCol++;
				 else {
					 rRow++;
					 rCol = 0;
				 }
			 }
		 }
		 else {
			 do {
				 if (rRow == 0 && rCol == 0) return;
				 else if (rCol > 0) 
					 rCol--;
				 else {
					 rRow--;
					 rCol = GRID - 1;
				 }
			 } while(preset.contains(index(rRow,rCol)) || usergiven.contains(index(rRow, rCol)));
		 }
	 }
	 
	 // Returns the 1-dimensional index of a given cell
	 private static int index(int row, int col) {
		 return row * GRID + col;
	 }
	 
	 // Checks if the given number, at the given cell is valid.
	 private boolean isValid(int num, int row, int col) {
	     // Check the given row
	     for (int c = 0; c < GRID; c++)
	         if (board[row][c] == num && c != col)
	             return false;

	     // Check the given column
	     for (int r = 0; r < GRID; r++)
	         if (board[r][col] == num && r != row)
	             return false;

	     // Check the 3 x 3 square containing the number
	     int squareX = row / 3;
	     int squareY = col / 3;
	     for (int x = 0; x < 3; x++)
	         for (int y = 0; y < 3; y++) {
	             int i = squareX * 3 + x;
	             int j = squareY * 3 + y;
	             if (i != row && j != col && board[i][j] == num)
	                 return false;
	         }

	     return true;
	 }

	 // String representation of the board for debugging purposes
	 public String toString() {
	     StringBuilder buf = new StringBuilder();
	     for (int i = 0; i < GRID; i++) {
	         if (i % 3 == 0 && i != 0) {
	             String dash = "-";
	             buf.append(dash.repeat(21));
	             buf.append("\n");
	         }
	         for (int j = 0; j < GRID; j++) {
	             if (j % 3 == 0 && j != 0)
	                 buf.append("| ");
	             buf.append(board[i][j]);
	             if (j != GRID - 1)
	                 buf.append(" ");
	         }
	         buf.append("\n");
	     }
	     return buf.toString();
	 }

	 public static void main(String[] args) {
		int[] a = {0, 1, 0, 0, 2, 0, 6, 0, 0,
                8, 0, 0, 9, 0, 0, 0, 3, 1,
                7, 3, 9, 5, 0, 0, 8, 2, 4,
                0, 0, 0, 0, 0, 3, 0, 0, 0,
                0, 7, 1, 6, 8, 0, 2, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 5, 6,
                0, 0, 0, 1, 4, 0, 0, 0, 0,
                0, 6, 2, 0, 0, 0, 0, 0, 7,
                3, 0, 0, 7, 0, 0, 0, 0, 5};
      
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame1 window = new Frame1(a);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});	
	}

	/**
	 * Create the application.
	 */
	public Frame1(int[] numbers) {
		if (numbers.length != GRID * GRID) throw new IllegalArgumentException("Array of wrong size");

	     for (int i = 0; i < numbers.length; i++) {
	         int row = i / GRID;
	         int col = i % GRID;
	         if (numbers[i] != 0) preset.add(i);
	         board[row][col] = numbers[i];
	         boardCopy[row][col] = numbers[i];
	     }
	    frame = new JFrame("Sudoku solver");
		frame.setResizable(false);
		frame.setLayout(new FlowLayout());
		frame.setBounds(100, 100, 500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		initializeBoard(numbers);
		frame.add(sudokuPanel);
		// Add button for solving and clearing
		JButton solveBtn = new JButton("Solve");
		JButton clearBtn = new JButton("Clear");
		clearBtn.setFocusable(false);
		solveBtn.setFocusable(false);
		
		// Solve button listener
		solveBtn.addActionListener(ae -> {
			if (solved) return;
			if (timer.isRunning()) return;
			
			// check for any blue,green or red buttons previously pressed. remove them if any
			for (int i = 0; i < GRID; i++)
				for (int j = 0; j < GRID; j++)
					if (cell[i][j].getBorder() == BLUE_BORDER || cell[i][j].getBorder() == RED_BORDER
					    || cell[i][j].getBorder() == GREEN_BORDER) 
						setCellBorder(i,j, null);
			updateCoeff(true);
			timer.start();
		});
		
		// Clear button listener
		clearBtn.addActionListener(ae -> {
			if (timer.isRunning()) return;
			solved = false;
			rRow = 0;
			rCol = 0;
			usergiven.clear();
			for (int i = 0; i < GRID; i++)
				for (int j = 0; j < GRID; j++) {
					board[i][j] = boardCopy[i][j];
					setCellBorder(i, j, null);
					if (board[i][j] != 0)
						cell[i][j].setText(Integer.toString(board[i][j]));
					else 
						cell[i][j].setText("");
				}
		});
		
		frame.add(solveBtn);
		frame.add(clearBtn);
		
		// Add keyboard listener
		frame.setFocusable(true);
		
		frame.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

		    public void keyPressed(KeyEvent e) {
		    }

		    public void keyReleased(KeyEvent e) {
		    	int num;
		    	num = getNumber(e.getKeyCode());
		    	if (num == 0) return;

		    	// First search for a blue cell, to insert the number.
		    	JLabel blueCell = null;
		    	int row = 0, col = 0;
		    	boolean blueFound = false;
		    	for (row = 0; row < GRID; row++) {
		    		for (col = 0; col < GRID; col++) {
		    			if (cell[row][col].getBorder() == BLUE_BORDER) {
		    				blueCell = cell[row][col];
		    				blueFound = true;
		    				break;
		    			}
		    		}
	    			if (blueFound) break;
		    	}
		    	
		    	// If there are no blue cells , number is not placed anywhere
		    	if (blueCell == null) return;
		    			    	
		    	// Try inserting the number and check if it is solvable
		    	if (isValid(num,row,col)) {
		    		board[row][col] = num;
		    	
			    	if (solve(0,0)) {
			    		usergiven.add(index(row,col));
			    		for (int i = 0; i < GRID; i++)
			    			for (int j = 0; j < GRID; j++)
			    				if (!usergiven.contains(index(i,j))) 
			    					board[i][j] = boardCopy[i][j];
			    		//board[row][col] = num;
			    		//usergiven.add(index(row,col));
			    		cell[row][col].setText(Integer.toString(num));
			    		cell[row][col].setBorder(GREEN_BORDER);
			    		//setCellBorder(row,col,Color.green);
			    		checkWinCondition();
			    	}
			    	else {
			    		System.out.println("WRONG ENTRY");
			    		for (int i = 0; i < GRID; i++)
			    			for (int j = 0; j < GRID; j++)
			    				board[i][j] = boardCopy[i][j];
			    		cell[row][col].setBorder(RED_BORDER);
			    		//setCellBorder(row,col,Color.red);
			    	}
		    	}
		    	else cell[row][col].setBorder(RED_BORDER);
		    		//setCellBorder(row,col,Color.red);
		    }
		});
	}
	
	// matches the key code of the key pressed to a number. if the key was 0, or Nan it return zero. If it was a positive integer , it return that integer.
	private static int getNumber(int c) {
		if ((c >= 48 && c <=57)) return c - 48; 
		else if(c >= 96 && c <=105) return c - 96;
		else return 0;
	}
	
	
	// Helper for key action listener. It checks if the board is solved after the user inserted a number, and if yes it generates
	// a winning animation
	private void checkWinCondition() {
		boolean solved = true;
		for (int row = 0; row < GRID; row++)
			for (int col = 0; col < GRID; col++)
				if (board[row][col] == 0 || !isValid(board[row][col], row, col)) {
					if (board[row][col] == 0) System.out.println("NOT VALID AT : " + row + " " + col);
					solved = false;
				}
		if (solved) {
			System.out.println("SSOLVED");
			winAnimation();
		}
 	}
	
	// Generates winning animation
	private void winAnimation() {
		int i = 0, j = 0;
		Timer winTimer = new Timer (20, new winTimerListener(i,j));
		winTimer.start();
	}
	
	private class winTimerListener implements ActionListener {
		private int row, col;
		public winTimerListener (int i, int j) {
			row = i;
			col = j;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (row == GRID - 1 && col == GRID - 1)  ((Timer)e.getSource()).stop();
			cell[row][col].setBorder(GREEN_BORDER);
			//setCellBorder(row,col,Color.red);
			if (col < GRID - 1) col++;
			else {
				row++;
				col = 0;
			}
		}
		
	}
	
	// Helper method for the key listener. It tries to solve the board, with the same algorithm that the Button listener could not implement recursively
	private boolean solve(int row, int col) {
        // If the coordinates refer to a preset cell, you will have to skip it, as it is not allowed to modify it.
        // If there are no more cells to visit, i.e. row = col = GRID - 1 then the puzzle is solved so return true;
        while (board[row][col] != 0) {
            if (col < GRID - 1)
                col++;
            else if (row < GRID - 1) {
                col = 0;
                row++;
            } else return true;
        }

        for (int i = 1; i <= 9; i++) {
            if (isValid(i, row, col))
                board[row][col] = i;
            else continue;

            int nextCol = col, nextRow = row;
            if (col < GRID - 1)
                nextCol++;
            else if (row < GRID - 1) {
                nextCol = 0;
                nextRow++;
            } else return true;

            if (solve(nextRow, nextCol))
                return true;
        }
        board[row][col] = 0;
        return false;
    }
	private void initializeBoard (int[] numbers) {
		sudokuPanel = new JPanel(new GridLayout(3,3));
		for (int k = 0; k < 3; k++) {
			for (int m = 0; m < 3; m++) {
				sudokuBox[k][m] = new JPanel(new GridLayout(3,3));
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						int row = k * 3 + i;
						int col = m * 3 + j;
						int index = row * 9 + col;
						String cellContent;
						if (board[row][col] == 0)  cellContent = "";
						else cellContent = Integer.toString(board[row][col]);
						cell[row][col] = (new JLabel(cellContent, SwingConstants.CENTER));
						cell[row][col].setPreferredSize(new Dimension(40,40));
						cell[row][col].setFont(new Font("Aria", Font.PLAIN, 20));
						cell[row][col].setBackground(Color.white);
						cell[row][col].setOpaque(true);
						cell[row][col].addMouseListener(new MouseAdapter() {
							public void mousePressed (MouseEvent e ) {
								// if the solver is functioning don't do anything
								if (timer.isRunning()) return;
			
								JLabel invokingCell = (JLabel) e.getSource();
								boolean sameCell = false;
								// if it is a preset cell you shouldn't change it
								if (invokingCell.getText() != "") return;
								// change the border, and also check if there any other blue, red or green cells on the board to turn them back to normal
								for (int i = 0; i < GRID; i++)
									for (int j = 0; j < GRID; j++) {
										if (cell[i][j].getBorder() == BLUE_BORDER) {
											setCellBorder(i, j ,null);
											if (invokingCell == cell[i][j]) sameCell = true;
											break;
										}
										if (cell[i][j].getBorder() == GREEN_BORDER || cell[i][j].getBorder() == RED_BORDER) 
											setCellBorder(i,j, null);
									}
								if (!sameCell)
									invokingCell.setBorder(BLUE_BORDER);
							}
						});
						setCellBorder(row, col,null);
						if (preset.contains(index(row, col))) cell[row][col].setBackground(Color.lightGray);
						sudokuBox[k][m].add(cell[row][col]);
					}
				}
				sudokuBox[k][m].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 1, Color.black));
				sudokuPanel.add(sudokuBox[k][m]);
			}
		}
	}
	
	private void setCellBorder(int row, int col , Color color) {
		int topBorder, leftBorder, botBorder, rightBorder;
		int i = row % 3;
		int j = col % 3;
		if (i == 0) {
			topBorder = 0;
			botBorder = 1;
		}
		else if (i == 2) {
			topBorder = 1;
			botBorder = 0;
		}
		else {
			topBorder = 1;
			botBorder = 1;
		}
		if (j == 0) {
			leftBorder = 0;
			rightBorder = 1;
		}
		else if (j == 2) {
			leftBorder = 1;
			rightBorder = 1;
		}
		else {
			rightBorder = 1;
			leftBorder = 1;
		}
		
		if (color == null) color = Color.black;
		cell[row][col].setBorder(BorderFactory.createMatteBorder(topBorder, leftBorder, botBorder, rightBorder, color));
	}
}
