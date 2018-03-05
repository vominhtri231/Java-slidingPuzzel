package slisingPuzzle;


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
	JLabel annouce,highScoreLabel,autherLabel;
	Socket soc;
	DataInputStream dis;
	DataOutputStream dos;
	Score[] scores;
	JComboBox<String> typeChoiser;
	String[] type;
	int choisedType;
	
	
	public MainFrame() throws UnknownHostException, IOException {
		createUI();
		scores=new Score[3];
		connectServer();
	}
	
	private void connectServer() throws UnknownHostException, IOException {
		soc=new Socket("localhost",9696);
		dis=new DataInputStream(soc.getInputStream());
		dos=new DataOutputStream(soc.getOutputStream());
		new Listener().start();
		dos.writeByte(3);	
	}
	
	private void createUI() {
		setSize(800,500);
		setLocation(300,70);
		setLayout(null);

		this.add(createControlPanel());
		
		playField=new PlayField();
		playField.frame=this;
		this.add(playField);
		this.addKeyListener(playField);
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
	}
	private JPanel createControlPanel() {
		JPanel panel=new JPanel();
		panel.setLayout(null);
		
		panel.setSize(300, 360);
		startButton=new  JButton("start");
		startButton.addActionListener(this);
		startButton.setLocation(100,140);
		startButton.setSize(70,30);
		
		annouce=new JLabel();
		annouce.setBounds(20, 30, 340, 35);
		annouce.setFont(new java.awt.Font("Times New Romans", 0, 20));
		annouce.setForeground(new java.awt.Color(74, 0, 74));
		annouce.setText("CLick start to begin!!!");
		panel.add(annouce);
		
		highScoreLabel=new JLabel();
		highScoreLabel.setBounds(20, 220, 340, 35);
		highScoreLabel.setFont(new java.awt.Font("Times New Romans", 0, 20));
		highScoreLabel.setForeground(new java.awt.Color(74, 0, 74));
		panel.add(highScoreLabel);
		
		autherLabel=new JLabel();
		autherLabel.setBounds(20, 260, 340, 35);
		autherLabel.setFont(new java.awt.Font("Times New Romans", 0, 20));
		autherLabel.setForeground(new java.awt.Color(74, 0, 74));
		panel.add(autherLabel);
		
		type=new String[]{"3-3","4-4","5-5"};
		typeChoiser=new JComboBox<String>(type);
		choisedType=3;
		typeChoiser.setBounds(90, 110,100,20);
		typeChoiser.addActionListener(this);
		panel.add(typeChoiser);
		
		panel.add(startButton);
		return panel;
	}
	
	public void endGame(int time,int type) throws IOException {
		this.annouce.setText("You solved "+type+"X"+type+ " in "+time +" milisecs");
		
		if(scores[type-3].time>time) {
			String name=JOptionPane.showInputDialog("Enter your name");
			if(name==null) name="Unknow player";
			dos.writeByte(2);
			dos.writeInt(type);
			dos.writeInt(time);
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
			highScoreLabel.setText("Sorted time :"+ scores[choisedType-3].time +" milisecs");
			autherLabel.setText("By "+scores[choisedType-3].name);
		}
	}
	
	class Listener extends Thread{
		
		boolean stop=false;
		public void run() {
			while(true) {
				if(stop) break;
				try {
					byte header=dis.readByte();
					
					if(header==1) getRandomPuzzel();
					if(header==3) getHighScore();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void getRandomPuzzel() throws IOException {
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
		
		private void getHighScore() throws IOException {
			int type=dis.readInt();
			int time=dis.readInt();
			String name=dis.readUTF();
			scores[type-3]=new Score(time,name);
			if(choisedType==type) {
				highScoreLabel.setText("Sorted time :"+ scores[choisedType-3].time);
				autherLabel.setText("By "+scores[choisedType-3].name);
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
