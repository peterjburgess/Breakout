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
	
/** delay between moves */
	private static final int DELAY = 10;
	
/** max value for horizontal velocity */
	private static final double VX_MAX = 3.0;

/** Runs the Breakout program. */
	
	public void run() {
		
		boardSetup();	//sets up the board
		addMouseListeners();
		play();

	}
	
	
	private void play(){
		waitForClick();
		remove(startLabel);
		while(!gameOver){
			if(brickCounter > 0){
				ball.move(vx,vy);
				pause(DELAY);
				checkForCollision();
			}
			else{
				declareWinner();
			}
		}
	}
	
	
	/*
	 * Checks for collisions with walls. If there is a collision, reverses ball velocity
	 */
	
	private void checkForCollision(){
		//check for collision with walls
		if(ball.getY() <= 0){
			vy = -vy;
		}
		if(ball.getX() <= 0 || ball.getX() >= APPLICATION_WIDTH - 2 * BALL_RADIUS){
			vx = -vx;
		}
		if(ball.getY() >= APPLICATION_HEIGHT - 2 * BALL_RADIUS)
		{
			lives --;
			remove(livesLabel);
			updateLivesLabel();
			if (lives == 0){
				gameOver = true;
			}
			else{
				remove(ball);
				addBall();
			}
		}
		//check for collision with objects
		GObject collider = getCollidingObject();

		if(collider != null && collider != livesLabel && collider != scoreLabel){
			vy = - vy;
			if (collider != paddle){
				Color c = collider.getColor();
				updateScore(c);
				remove(collider);
				brickCounter --;
				combo +=1;
			}
			else{
				getNewvx();
				combo = 1;
			}
		}
		
	}
	
	/*
	 * updates the score based on the brick colour and current combo number
	 */
	
	private void updateScore(Color c){
		
		if (c.equals(Color.CYAN)){
			score += 10;
		}
		else if (c.equals(Color.GREEN)){
			score += 20 * combo;
		}
		else if (c.equals(Color.YELLOW)){
			score += 50 * combo;
		}
		else if (c.equals(Color.ORANGE)){
			score += 100 * combo;
		}
		else if (c.equals(Color.RED)){
			score += 200 * combo;
		}
		remove(scoreLabel);
		updateScoreLabel();
	}
	/*
	 * method changes the horizontal velocity of the ball depending on where in the paddle the ball hits. The paddle is split
	 * into 5 sections, the middle section won't change vx, the next two either side will give vx 
	 * some component in that direction and the far ends will give it maximum velocity in that 
	 * direction
	 */
	private void getNewvx(){
		
		//separate the paddle into 5 sections
		int xFarLeft = (int) (paddle.getX() + PADDLE_WIDTH / 10);
		int xMidLeft = (int) (paddle.getX() + 2 * PADDLE_WIDTH / 5);
		int xMiddle = (int) (paddle.getX() + 3 * PADDLE_WIDTH / 5);
		int xMidRight = (int) (paddle.getX() + 9 * PADDLE_WIDTH / 10);
		int xFarRight = (int) (paddle.getX() + PADDLE_WIDTH);
		
		//find bottom of the ball
		int ballBottom = (int) (ball.getX() + BALL_RADIUS);
		
		if(ballBottom < xFarLeft){
			vx = - VX_MAX;
		}
		else if (ballBottom < xMidLeft){
			if (vx > -VX_MAX + 1.2)
				vx -= 1.2;
			else
				vx = -VX_MAX;	
		}
		else if (ballBottom < xMiddle){
			vx = vx;
		}
		else if (ballBottom < xMidRight){
			if(vx < VX_MAX - 1.2)
				vx += 1.2;
			else
				vx = VX_MAX;	
		}
		else{
			vx = VX_MAX;
		}
		
	}
	
	/*
	 * Method checks four corners of the box bounding ball to see if there is an object there. If there is,
	 * that object is returned. 
	 */
	private GObject getCollidingObject(){
		GObject collider = getElementAt(ball.getX(), ball.getY());
		for(int i = 0; i < 4; i++){
			if (i == 1){
				collider = getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY());
			}
			if (i == 2){
				collider = getElementAt(ball.getX(), ball.getY() + 2 * BALL_RADIUS);
			}
			if (i == 3){
				collider = getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS);
			}
			
			if (collider != null){
				return collider;
		}
		
		}
		collider = getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY());
		
		return null;
	}

	//tracks the centre of the paddle with the mouse position
	public void mouseMoved(MouseEvent e){
		if(e.getX() >= PADDLE_WIDTH/2 && e.getX() <= APPLICATION_WIDTH - PADDLE_WIDTH/2){
			paddle.setLocation(e.getX() - PADDLE_WIDTH / 2, yPaddle);
		}
		else if(e.getX() < PADDLE_WIDTH/2){
			paddle.setLocation(0, yPaddle);
		}
		else {
			paddle.setLocation(APPLICATION_WIDTH - PADDLE_WIDTH, yPaddle);
		}
	}
	

	/*
	 * sets up the board at the start of the game based on the number of bricks per row, 
	 * the number of rows and the brick separation
	 */
	
	private void boardSetup(){
		
		//initialise top of the board
		int yCoord = BRICK_Y_OFFSET;
		//add bricks
		brickCounter = 0; //initialises brick counter
		for(int i = 0; i < NBRICK_ROWS; i++){
			Color color = setColor(i);
			createRow(color, yCoord);
			yCoord += BRICK_HEIGHT + BRICK_SEP;
		}
		addPaddle();
		addBall();
		addStartLabel();
		gameOver = false;
		lives = NTURNS;
		score = 0;
		updateLivesLabel();
		updateScoreLabel();
	}
	
	//Adds the lives label and puts it in the bottom left, updates when life is lost
	private void updateLivesLabel(){
		livesLabel = new GLabel("Lives: " + lives, 0, getHeight());
		livesLabel.setFont("Serif-18");
		add(livesLabel);
	}
	
	//Updates score label with  new score when required
	private void updateScoreLabel(){
		scoreLabel = new GLabel("Score: " + score, 0, 0);
		scoreLabel.move(0, scoreLabel.getAscent());
		scoreLabel.setFont("Serif-18");
		add(scoreLabel);
	}
	
	//Adds the start label to the canvas and centres
	private void addStartLabel(){
		startLabel = new GLabel("Click to Start", 0, 0);
		startLabel.setFont(new Font("Serif", Font.PLAIN, 26));
		int xLabel = (int)(getWidth() - startLabel.getWidth()) / 2; //find screen centre in x direction
		int yLabel = (int)(getHeight() - startLabel.getAscent()) /2 ;//find screen centre in y direction
		startLabel.move(xLabel, yLabel);
		add(startLabel);
	}
	
	private void declareWinner(){
		GLabel winnerLabel = new GLabel("Congratulations, you won!", 0, 0);
		winnerLabel.setFont(new Font("Serif", Font.PLAIN, 26));
		int xLabel = (int)(getWidth() - winnerLabel.getWidth()) / 2; //find screen centre in x direction
		int yLabel = (int)(getHeight() - winnerLabel.getAscent()) /2 ;//find screen centre in y direction
		winnerLabel.move(xLabel, yLabel);
		add(winnerLabel);
		remove(ball);
		remove(paddle);
	}
	
	//Adds the ball to the canvas
	private void addBall(){
		//find xCoord and yCoord to put ball in screen center and add to canvas
		int xCoord = APPLICATION_WIDTH/2 - BALL_RADIUS;
		int yCoord = APPLICATION_HEIGHT/2 - BALL_RADIUS;
		ball = new GOval(xCoord, yCoord, BALL_RADIUS*2, BALL_RADIUS*2);
		ball.setFilled(true);
		add(ball);
		
		//set inital velocity of the ball
		vy = 3.0;//initial y velocity
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean()){
			vx = -vx;
		}
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
			rect.setColor(color);
			add(rect);
			xCoord += BRICK_WIDTH + BRICK_SEP;
			brickCounter ++;
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
		paddle = new GRect (xCoord, yPaddle, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		add(paddle);
	}
	
	//private instance variables
	private GRect paddle; //gives me the paddle
	private GOval ball; // gives me the ball
	private int yPaddle; //gives y coord of paddle
	private double vx, vy; //x and y components of ball's velocity
	private boolean gameOver;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private GLabel startLabel; //Gives me the label at the start of the game
	private int brickCounter; //Keeps track of the number of bricks on the screen.
	private int score; //Keeps track of the games score
	private int lives; //Keeps track of the number of lives remaining
	private GLabel scoreLabel; //displays the score
	private GLabel livesLabel; //displays the number of lives
	private int combo = 1;
}
