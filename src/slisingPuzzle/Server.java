package slisingPuzzle;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Vector;


public class Server {
	
	Score[] scores;
	private ServerSocket server;
	Vector<Worker> workers;
	
	
	
	public Server() throws IOException {
		server=new ServerSocket(9696);
		workers=new Vector<Worker>();
		new Listener().start();
		getHighScoreFromFile();
	}
	
	private void getHighScoreFromFile() throws IOException  {
		BufferedReader reader=new BufferedReader(new FileReader("highScore.txt"));
		scores=new Score[3];
		for(int i=0;i<3;i++) {
			String s = reader.readLine();	
			String[] parts=s.split("-");
			int time=Integer.parseInt(parts[0]);
			String name=parts[1];
			scores[i]=new Score(time,name);
		}	
		reader.close();
	}
	
	private void saveHighScoreToFile() throws IOException  {
		PrintWriter printer = new PrintWriter("highScore.txt");
		for(int i=0;i<3;i++) {
			printer.println(""+scores[i].time+"-"+scores[i].name);
		}	
		printer.close();
	}
	
	private void annouceHighScore(int type) throws IOException {
		for(Worker worker:workers) 
			worker.sendHighScore(type);
	}
	
	class Worker extends Thread{
		Socket conn;
		DataOutputStream dos;
		DataInputStream dis;

		public Worker(Socket conn) throws IOException{
			this.conn=conn;
			dos=new DataOutputStream(conn.getOutputStream());
			dis=new DataInputStream(conn.getInputStream());
		}
		@Override
		public void run() {
			try {
				while(true) {
					byte command=dis.readByte();
					if(command==1) sendRandom();
					if(command==2) takeScore();
					if(command==3) sendAllHighScore();
 				}

			}catch(Exception e) {
				workers.remove(this);
			}
		}
		
		private void sendAllHighScore() throws IOException {
			for(int i=3;i<6;i++) sendHighScore(i);
		}
		
		private void sendHighScore(int type) throws IOException {
				dos.writeByte(3);
				dos.writeInt(type);
				dos.writeInt(scores[type-3].time);
				dos.writeUTF(scores[type-3].name);
		}
		private void takeScore() throws IOException {
			int type=dis.readInt();
			int time=dis.readInt();
			String newName=dis.readUTF();
			if(scores[type-3].time>time) {
				scores[type-3].time=time;
				scores[type-3].name=newName;
				saveHighScoreToFile();
				annouceHighScore(type);
			}	
		}
		private void sendRandom() throws IOException {
			int n=dis.readByte();
			n=n*n;
			int[] randArray=randomPuzzle(n);
			dos.writeByte(1);
			for(int i=0;i<n;i++) {
				dos.writeInt(randArray[i]);
			}
			dos.writeInt(-1);
		}
		
		private int[] randomPuzzle(int n) {
			int[] ans=new int[n];
			Random ran=new Random();int pos=0;
			HashSet<Integer> set=new HashSet<Integer>();
			for(int i=1;i<n+1;i++) set.add(i);
			while(set.size()>0) {
				int ranPos=ran.nextInt(set.size());
				int value=(int) set.toArray()[ranPos];
				ans[pos]=value;
				pos++;
				set.remove(value);
			}
			if(!checkSolvable(ans)) {
				ArrayList<Integer> listPos=new ArrayList<Integer>();
				for(int i=n-1;listPos.size()<2;i--) if(ans[i]!=n){
					listPos.add(i);
				}
				int temp=ans[listPos.get(0)];
				ans[listPos.get(0)]=ans[listPos.get(1)];
				ans[listPos.get(1)]=temp;
			}
			
			return ans;
		}
		
		boolean checkSolvable(int[] inp) {
			int sumInvention=0;
			for(int i=inp.length-2;i>=0;i--) if(inp[i]!=inp.length) {
				for(int j=i+1;j<inp.length;j++) if(inp[i]>inp[j]) sumInvention++;
					
			}
			return sumInvention%2==0;
		}
		
	}
	
	class Listener extends Thread{
		@Override
		public void run() {
			while(true) {
				try {
					Socket conn=server.accept();
					Worker worker=new Worker(conn);
					workers.add(worker);
					worker.start();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
			
	}
	
	
	
	public static void main(String[] args) {
		try {
			new Server();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
