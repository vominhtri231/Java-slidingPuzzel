package slisingPuzzle;


import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;




@SuppressWarnings("serial")
public class PlayField extends JPanel implements KeyListener ,Runnable{
	
	int[][] position;
	ImageIcon[] icons;
	JLabel[][] pieces;
	JLabel mainImage,clock;
	Point specialPieceLocation;
	MainFrame frame;
	boolean start;
	Thread thread;
	
	public PlayField() {
		
		setSize(450, 450);
		setLocation(200,0);
		setBackground(new Color(86,63,26));
		
		setLayout(null);
		this.setVisible(true);
		icons=new ImageIcon[9];
		pieces=new JLabel[3][3];
		for(int i=0;i<3;i++) 
			for(int j=0;j<3;j++)
			{
				pieces[i][j]=new JLabel();
				pieces[i][j].setBounds(90+j*68, 120+i*68, 65, 65);
				this.add(pieces[i][j]);
			}	
		
		for(int i=0;i<8;i++) {
			icons[i]=new ImageIcon(getClass().getResource("/ima/cropped ("+(i+1)+").jpg"));
		}		
		icons[8]=new ImageIcon(getClass().getResource("/ima/cropped.jpg"));
		
		mainImage=new JLabel();
		mainImage.setBounds(30, 10, 80, 80);
		mainImage.setIcon(new ImageIcon(getClass().getResource("/ima/main.jpg")));
		
		clock=new JLabel();
		clock.setBounds(200,10 ,200, 50);
        clock.setText("The Clock");
        clock.setForeground(new java.awt.Color(200, 0, 0));
        clock.setFont(new java.awt.Font("DigifaceWide", 0, 40)); 
		
		this.add(mainImage);
		this.add(clock);
		start=false;	
		
	}
	
	
	public void init(int[][] position) {
		this.position=position;
		specialPieceLocation=this.findSpecialPieces();		
		update();
		start=true;
		thread=new Thread(this);
		thread.start();
	}
	
	private void update() {
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {	
				int value=position[i][j];
				pieces[i][j].setIcon(icons[value-1]);
			}
		}
		if(isEnd()) {
			start=false;
		}
	}

	
	public void press(int key ) {
		if(start) {
			int x=specialPieceLocation.x,y=specialPieceLocation.y;
			if(key==39) {
				if(y!=0) {
					int temp=position[x][y];
					position[x][y]=position[x][y-1];
					position[x][y-1]=temp;
					specialPieceLocation.setLocation(x, y-1);
					update();
				}
			}
			if(key==37) {
				if(y<2) {
					int temp=position[x][y];
					position[x][y]=position[x][y+1];
					position[x][y+1]=temp;
					specialPieceLocation.setLocation(x, y+1);
					update();
				}
			}
			if(key==40) {
				if(x!=0) {
					int temp=position[x][y];
					position[x][y]=position[x-1][y];
					position[x-1][y]=temp;
					specialPieceLocation.setLocation(x-1, y);
					update();
				}
			}
			if(key==38) {
				if(x<2) {
					int temp=position[x][y];
					position[x][y]=position[x+1][y];
					position[x+1][y]=temp;
					specialPieceLocation.setLocation(x+1, y);
					update();
				}
			}
			
		}
		
	}

	Point findSpecialPieces() {
		for(int i=0;i<3;i++)
			for(int j=0;j<3;j++) {
				if(this.position[i][j]==9) return new Point(i,j);
			}
		return null;
	}
	
	boolean isEnd() {
		for(int i=0;i<3;i++)
			for(int j=0;j<3;j++) {
				if(i*3+j+1!=position[i][j]) return false;
			}
		return true;
	}


	@Override
	public void keyPressed(KeyEvent e) {
		int key=e.getKeyCode();
		press(key);
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}


	@SuppressWarnings("static-access")
	@Override
	public void run() {
		int time=0;
		while(start) {
			try {
				int temp=time,a;
				
				a=temp%100;
				String mili=a<10?"0"+a:""+a;
				temp/=100;
				
				a=temp%60;
				String sec=a<10?"0"+a:""+a;
				temp/=60;
				
				a=temp;
				String min=a<10?"0"+a:""+a;
				
				this.clock.setText(min+"-"+sec+"-"+mili);
				
				thread.sleep(10);
				time++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			frame.endGame(time-1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
