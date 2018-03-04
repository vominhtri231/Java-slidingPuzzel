package slisingPuzzle;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
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
	JComboBox<String> typeChoiser;
	String[] type;
	int choisedType;
	public MainFrame() throws UnknownHostException, IOException {

		setSize(700,500);
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
		startButton.setLocation(50,140);
		startButton.setSize(70,30);
		
		annouce=new JLabel();
		annouce.setBounds(10, 30, 250, 35);
		annouce.setFont(new java.awt.Font("Times New Romans", 0, 20));
		annouce.setForeground(new java.awt.Color(74, 0, 74));
		annouce.setText("Well come!!!");
		panel.add(annouce);
		
		highScoreLabel=new JLabel();
		highScoreLabel.setBounds(10, 220, 250, 35);
		highScoreLabel.setFont(new java.awt.Font("Times New Romans", 0, 20));
		highScoreLabel.setForeground(new java.awt.Color(74, 0, 74));
		panel.add(highScoreLabel);
		
		nameLabel=new JLabel();
		nameLabel.setBounds(10, 260, 250, 35);
		nameLabel.setFont(new java.awt.Font("Times New Romans", 0, 20));
		nameLabel.setForeground(new java.awt.Color(74, 0, 74));
		panel.add(nameLabel);
		
		type=new String[]{"3-3","4-4","5-5"};
		typeChoiser=new JComboBox<String>(type);
		choisedType=3;
		typeChoiser.setBounds(40, 110,100,20);
		typeChoiser.addActionListener(this);
		panel.add(typeChoiser);
		
		panel.add(startButton);
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
				dos.writeByte(choisedType);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			this.setFocusable(true);
			this.requestFocusInWindow(true);
		}
		if(e.getSource()==typeChoiser) {
			choisedType=typeChoiser.getSelectedIndex()+3;
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
						ArrayList<Integer> list=new ArrayList<Integer>();
						do {
							int t=dis.readInt();
							if(t<0) break;
							list.add(t);
						}while(true);
						
						int l=list.size();l=(int)Math.sqrt(l);
						int[][] pos=new int[l][l];
						for(int i=0;i<l;i++)
							for(int j=0;j<l;j++){
								int value=list.remove(0);
								pos[i][j]=value;
							}
						
						playField.init(pos);
					}
					if(header==3) {
						highScore=dis.readInt();
						name=dis.readUTF();
						highScoreLabel.setText("Sorted time:"+highScore );
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
