package slisingPuzzle;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;



@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ActionListener{
	
	JButton startButton,resetbutton;
	PlayField playField;
	JLabel annouce,highScoreLabel,nameLabel;
	Socket soc;
	DataInputStream dis;
	DataOutputStream dos;
	int highScore;String name;
	public MainFrame() throws UnknownHostException, IOException {
		
		
		
		setSize(650,450);
		setLocation(300,70);
		setLayout(null);
		
		this.add(createControlPanel());
		
		playField=new PlayField();
		playField.frame=this;
		this.add(playField);

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
		this.addKeyListener(playField);
		
		soc=new Socket("localhost",9696);
		dis=new DataInputStream(soc.getInputStream());
		dos=new DataOutputStream(soc.getOutputStream());
		new Listener().start();
		dos.writeByte(3);
		
	}
	private JPanel createControlPanel() {
		JPanel panel=new JPanel();
		panel.setLayout(null);
		
		panel.setSize(200, 360);
		startButton=new  JButton("start");
		startButton.addActionListener(this);
		startButton.setLocation(20,100);
		startButton.setSize(70,30);
		
		annouce=new JLabel();
		annouce.setBounds(22, 130, 250, 35);
		annouce.setFont(new java.awt.Font("Times New Romans", 0, 20));
		annouce.setForeground(new java.awt.Color(74, 0, 74));
		annouce.setText("Well come!!!");
		panel.add(annouce);
		
		highScoreLabel=new JLabel();
		highScoreLabel.setBounds(22, 10, 250, 35);
		highScoreLabel.setFont(new java.awt.Font("Times New Romans", 0, 20));
		highScoreLabel.setForeground(new java.awt.Color(74, 0, 74));
		panel.add(highScoreLabel);
		
		nameLabel=new JLabel();
		nameLabel.setBounds(22, 50, 250, 35);
		nameLabel.setFont(new java.awt.Font("Times New Romans", 0, 20));
		nameLabel.setForeground(new java.awt.Color(74, 0, 74));
		panel.add(nameLabel);
	
		panel.add(startButton,BorderLayout.SOUTH);
		return panel;
	}
	
	public void endGame(int score) throws IOException {
		this.annouce.setText("You solved it in "+score+" milisecs");
		
		if(score<highScore) {
			String name=JOptionPane.showInputDialog("Enter your name");
			dos.writeByte(2);
			dos.writeInt(score);
			dos.writeUTF(name);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==startButton) {
			try {
				dos.writeByte(1);
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			this.setFocusable(true);
			this.requestFocusInWindow(true);
		}
		
	}
	
	class Listener extends Thread{
		
		boolean stop=false;
		public void run() {
			while(true) {
				if(stop) break;
				try {
					byte header=dis.readByte();
					
					if(header==1) {
						int[][] pos=new int[3][3];
						for(int i=0;i<9;i++) {
							int value=dis.readInt();
							pos[i/3][i%3]=value;
						}
						playField.init(pos);
					}
					if(header==3) {
						highScore=dis.readInt();
						name=dis.readUTF();
						highScoreLabel.setText("High score:"+highScore );
						nameLabel.setText("By:"+name );
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			new MainFrame();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
