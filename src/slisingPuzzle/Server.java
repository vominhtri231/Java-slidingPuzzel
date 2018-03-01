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
	
	int highScore;String name;
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
		String s = reader.readLine();	
		String[] parts=s.split("-");
		highScore=Integer.parseInt(parts[0]);
		name=parts[1];
		reader.close();
	}
	
	private void saveHighScoreToFile() throws IOException  {
		PrintWriter printer = new PrintWriter("highScore.txt");
		printer.print(""+highScore+"-"+name);
		printer.close();
	}
	
	private void annouceHighScore() throws IOException {
		for(Worker worker:workers) 
			worker.sendHighScore();
	}
	
	class Worker extends Thread{
		Socket conn;
		DataOutputStream dos;
		DataInputStream dis;
		int[]a= {1,2,3,4,5,6,7,8,9},b={1,5,8,4,2,3,6,7,9};
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
					if(command==3) sendHighScore();	
 				}

			}catch(Exception e) {
				workers.remove(this);
			}
		}
		
		private void sendHighScore() throws IOException {
			dos.writeByte(3);
			dos.writeInt(highScore);
			dos.writeUTF(name);
		}
		private void takeScore() throws IOException {
			int score=dis.readInt();
			String newName=dis.readUTF();
			if(highScore>score) {
				highScore=score;
				name=newName;
				saveHighScoreToFile();
				annouceHighScore();
			}	
		}
		private void sendRandom() throws IOException {
			dos.writeByte(1);
			int[] randArray=rand();
			for(int i=0;i<9;i++) {
				dos.writeInt(randArray[i]);
			}
		}
		
		private int[] rand() {
			int[] ans=new int[9];
			Random ran=new Random();int pos=0;
			HashSet<Integer> set=new HashSet<Integer>();
			for(int i=1;i<10;i++) set.add(i);
			while(set.size()>0) {
				int ranPos=ran.nextInt(set.size());
				int value=(int) set.toArray()[ranPos];
				ans[pos]=value;
				pos++;
				set.remove(value);
			}
			if(!checkSolvable(ans)) {
				ArrayList<Integer> listPos=new ArrayList<Integer>();
				for(int i=8;i>0&&listPos.size()<2;i--) if(ans[i]!=9){
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
			for(int i=7;i>=0;i--) if(inp[i]!=9) {
				for(int j=i+1;j<9;j++) if(inp[i]>inp[j]) sumInvention++;
					
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
