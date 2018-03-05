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
	int time,choisedType;
	int len=312;
	public PlayField() {
		
		setSize(500, 500);
		setLocation(300,0);
		setBackground(new Color(86,63,26));
		
		setLayout(null);
		choisedType=0;
		
		mainImage=new JLabel();
		mainImage.setBounds(30, 10,80, 80);
		mainImage.setIcon(new ImageIcon(getClass().getResource("/ima/pic1/mini_pic1.png")));
		
		clock=new JLabel();
		clock.setBounds(200,10 ,200, 50);
        clock.setText("The Clock");
        clock.setForeground(new java.awt.Color(200, 0, 0));
        clock.setFont(new java.awt.Font("DigifaceWide", 0, 40)); 
		
		this.add(mainImage);
		this.add(clock);
		start=false;	
		this.setVisible(true);
	}
	
	
	public void init(int[][] position) {
		if(position.length!=choisedType) setUpNewType(position.length);
		time=0;
		this.position=position;
		specialPieceLocation=this.findSpecialPieces();	
		update();
		start=true;
		
		if(thread==null) thread=new Thread(this);
		if(!thread.isAlive()) thread.start();
	}
	
	private void setUpNewType(int type) {
		
		
		for(int i=0;i<choisedType;i++) 
			for(int j=0;j<choisedType;j++) {
				this.remove(pieces[i][j]);
			}
		
		choisedType=type;
		int miniLen=len/choisedType,border=2;
		icons=new ImageIcon[type*type];
		pieces=new JLabel[type][type];
		for(int i=0;i<type;i++) 
			for(int j=0;j<type;j++){
				pieces[i][j]=new JLabel();
				pieces[i][j].setBounds(90+j*(miniLen+border), 120+i*(miniLen+border), miniLen, miniLen);
				this.add(pieces[i][j]);
			}	
		
		for(int i=0;i<type*type-1;i++) {
			icons[i]=new ImageIcon(getClass().getResource("/ima/pic1/"+type+"/pic1("+i+").png"));
		}		
		
		this.repaint();
		
	}


	private void update() {
		for(int i=0;i<choisedType;i++) {
			for(int j=0;j<choisedType;j++) {	
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
				if(y<choisedType-1) {
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
				if(x<choisedType-1) {
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
		for(int i=0;i<choisedType;i++)
			for(int j=0;j<choisedType;j++) {
				if(this.position[i][j]==choisedType*choisedType) return new Point(i,j);
			}
		
		return null;
	}
	
	boolean isEnd() {
		for(int i=0;i<choisedType;i++)
			for(int j=0;j<choisedType;j++) {
				if(i*choisedType+j+1!=position[i][j]) return false;
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
			frame.endGame(time-1,this.choisedType);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.thread=null;
	}
}
