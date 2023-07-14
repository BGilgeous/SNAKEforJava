package snakeGamePackage;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{
	
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH* SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75;
	final int x[] = new int [GAME_UNITS];
	final int y[] = new int [GAME_UNITS];
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random; 
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
		
	}
	public void draw(Graphics g) {
		//Grid Format for Game 
		if(running) {
			for(int i=0;i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
				g.drawLine(i*UNIT_SIZE,0,i*UNIT_SIZE ,SCREEN_HEIGHT);
				g.drawLine(0,i*UNIT_SIZE,SCREEN_WIDTH ,i*UNIT_SIZE);
			}
			//Apple Formatting
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
		
			//Snake Body Formatting 
			for(int i = 0;i< bodyParts;i++) {
				if (i == 0){
					g.setColor(Color.green);
					g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
				}
				else {
					g.setColor(new Color(45,180,0));
					g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i],y[i],UNIT_SIZE, UNIT_SIZE);
				}
			}
			//In-Game Score Formatting.
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten,(SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2,g.getFont().getSize());
			
		}
		else {
			gameOver(g);
		}
	}
	public void newApple(){
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
		
		
	}
	public void move() { //Snake Body Parts
		for (int i = bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		} 
		
		switch(direction) {
		case 'U': //Moves up
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D': // Moves down
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L': // Moves to the left
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R': // Moves to the right
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
		
		
	}
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
		
	}
	public void checkCollisions() {
		//checks if head collides with body
		for (int i = bodyParts;i>0;i--){
			 if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		// Checks if head touches left border
		if (x[0]< 0) {
			running = false;
		}
		// Checks if head touches right border.
		if (x[0]> SCREEN_WIDTH) {
			running = false;
		}
		// Checks if head touches top border
		if (y[0]<0) {
			running = false;
		}
		// Checks if head touches bottom border
		if (y[0]> SCREEN_HEIGHT) {
			running = false;
		}
		
		if(!running){
			timer.stop();
			
		}
	}
	private int bodyParts(boolean b, int i) {
		// TODO Auto-generated method stub
		return 0;
	}
	public void gameOver(Graphics g) {
		// Score
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten,(SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2,g.getFont().getSize());
		
		// Game over text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("GAME OVER",(SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2);
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running){
			move();
			checkApple();
			checkCollisions();
		}
		repaint();	
	}
	//Keyboard movements for the Snake. 
	public class MyKeyAdapter extends KeyAdapter{
		@Override 
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:		//Moves Snake Left
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:		// Moves Snake Right 
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:		//Moves Snake Up
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:		//Moves Snake Down
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}

}
