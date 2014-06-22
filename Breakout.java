/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 600;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;

/** Runs the Breakout program. */
	public void run() {
		boardSetup();	//sets up the board
	}
	
	/*
	 * sets up the board at the start of the game based on the number of bricks per row, 
	 * the number of rows and the brick separation
	 */
	
	private void boardSetup(){
		
		//initialise top of the board
		int yCoord = BRICK_Y_OFFSET;
		//add bricks
		for(int i = 0; i < NBRICK_ROWS; i++){
			Color color = setColor(i);
			createRow(color, yCoord);
			yCoord += BRICK_HEIGHT + BRICK_SEP;
		}
		addPaddle();
		
	}
	

	/*
	 * This creates the rows of bricks one at a time centred in the graphics window
	 */
	private void createRow(Color color, int yCoord){
		//find centre of window
		int windowCentre = getWidth()/2;
		//find starting x coord
		int xCoord = windowCentre - WIDTH/2 + BRICK_SEP /2;
		//add bricks to graphics window
		for(int i = 0; i < NBRICKS_PER_ROW; i++){
			GRect rect = new GRect(xCoord, yCoord, BRICK_WIDTH, BRICK_HEIGHT); 
			rect.setFilled(true);
			rect.setFillColor(color);
			add(rect);
			xCoord += BRICK_WIDTH + BRICK_SEP;
		}
	}
	
	/*
	 * Sets the color scheme for the bricks
	 */
	private Color setColor(int i){
		if(i < 2){
			return Color.RED;
		}
		else if (i<4){
			return Color.ORANGE;
		}
		else if (i<6){
			return Color.YELLOW;
		}
		else if (i<8){
			return Color.GREEN;
		}
		else return Color.CYAN;
		
	}
	
	/*
	 * Adds paddle to the game board at the start
	 */
	private void addPaddle(){
		int xCoord = WIDTH/2 - PADDLE_WIDTH / 2;
		yPaddle = HEIGHT - PADDLE_Y_OFFSET;
		GRect paddle = new GRect (xCoord, yPaddle, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		add(paddle);
	}
	
	//private instance variables
	private GObject gObj;
	private GRect paddle; //initialises paddle as a GRect
	private GPoint last; //gives me the last xCoord of the paddle
	private int yPaddle; //sets the y location of the paddle
}
