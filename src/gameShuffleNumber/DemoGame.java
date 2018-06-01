package gameShuffleNumber;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class DemoGame extends JFrame implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton []btnumber = new JButton [10] ;
	JLabel lbtittle;
	JButton btnexit;
	public DemoGame(){
		this.setTitle("Game shuffle number by Tim");
		this.setLayout(new BorderLayout());
		this.add(createInput(), BorderLayout.NORTH);
		this.add(createTable(), BorderLayout.CENTER);
		this.add(createExit(), BorderLayout.SOUTH);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	public JPanel createInput(){
		JPanel pn1 = new JPanel();
		lbtittle= new JLabel("Game Shuffle number");
		pn1.add(lbtittle);
		return pn1;
	}
	public JPanel createTable(){
		JPanel pn = new JPanel();
		pn.setLayout(new GridLayout(3, 3));
		for(int i=1;i<10;i++){
			if(i==9){
				pn.add(btnumber[9]= createButton(""));
			
			}
			else{
				pn.add(btnumber[i]= createButton(i+""));
			}
		}
		return pn;
	}
	public JPanel createExit(){
		JPanel pn = new JPanel();
		pn.add(btnexit= new JButton("exit"));
		btnexit.addActionListener(this);
		return pn;
	}
	public JButton createButton(String s){
		JButton btn = new JButton(s);
		btn.addActionListener(this);
		return btn;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()== btnumber[1]){
			if(btnumber[2].getText().equals("")){
				btnumber[2].setText(btnumber[1].getText());
				btnumber[1].setText("");
			}
			else if(btnumber[4].getText().equals("")){
				btnumber[4].setText(btnumber[1].getText());
				btnumber[1].setText("");
			}
			Result();
		}
		if(e.getSource()== btnumber[2]){
			if(btnumber[5].getText().equals("")){
				btnumber[5].setText(btnumber[2].getText());
				btnumber[2].setText("");
			}
			else if(btnumber[1].getText().equals("")){
				btnumber[1].setText(btnumber[2].getText());
				btnumber[2].setText("");
			}
			else if(btnumber[3].getText().equals("")){
				btnumber[3].setText(btnumber[2].getText());
				btnumber[3].setText("");
			}
			Result();
		}
		if(e.getSource()== btnumber[3]){
			if(btnumber[2].getText().equals("")){
				btnumber[2].setText(btnumber[3].getText());
				btnumber[3].setText("");
			}
			else if(btnumber[6].getText().equals("")){
				btnumber[6].setText(btnumber[3].getText());
				btnumber[3].setText("");
			}
			Result();
		}
		if(e.getSource()== btnumber[4]){
			if(btnumber[5].getText().equals("")){
				btnumber[5].setText(btnumber[4].getText());
				btnumber[4].setText("");
			}
			else if(btnumber[7].getText().equals("")){
				btnumber[7].setText(btnumber[4].getText());
				btnumber[4].setText("");
			}
			else if(btnumber[1].getText().equals("")){
				btnumber[1].setText(btnumber[4].getText());
				btnumber[4].setText("");
			}
			Result();
		}
		if(e.getSource()== btnumber[5]){
			if(btnumber[2].getText().equals("")){
				btnumber[2].setText(btnumber[5].getText());
				btnumber[5].setText("");
			}
			else if(btnumber[4].getText().equals("")){
				btnumber[4].setText(btnumber[5].getText());
				btnumber[5].setText("");
			}
			else if(btnumber[6].getText().equals("")){
				btnumber[6].setText(btnumber[5].getText());
				btnumber[5].setText("");
			}
			else if(btnumber[8].getText().equals("")){
				btnumber[8].setText(btnumber[5].getText());
				btnumber[5].setText("");
			}
			Result();
		}
		if(e.getSource()== btnumber[6]){
			if(btnumber[5].getText().equals("")){
				btnumber[5].setText(btnumber[6].getText());
				btnumber[6].setText("");
			}
			else if(btnumber[3].getText().equals("")){
				btnumber[3].setText(btnumber[6].getText());
				btnumber[6].setText("");
			}
			else if(btnumber[9].getText().equals("")){
				btnumber[9].setText(btnumber[6].getText());
				btnumber[6].setText("");
			}
			Result();
		}
		if(e.getSource()== btnumber[7]){
			if(btnumber[4].getText().equals("")){
				btnumber[4].setText(btnumber[7].getText());
				btnumber[7].setText("");
			}
			else if(btnumber[8].getText().equals("")){
				btnumber[8].setText(btnumber[7].getText());
				btnumber[7].setText("");
			}
			Result();
		}
		if(e.getSource()== btnumber[8]){
			if(btnumber[5].getText().equals("")){
				btnumber[5].setText(btnumber[8].getText());
				btnumber[8].setText("");
			}
			else if(btnumber[7].getText().equals("")){
				btnumber[7].setText(btnumber[8].getText());
				btnumber[8].setText("");
			}
			else if(btnumber[9].getText().equals("")){
				btnumber[9].setText(btnumber[8].getText());
				btnumber[8].setText("");
			}
			Result();
		}
		if(e.getSource()== btnumber[9]){
			if(btnumber[6].getText().equals("")){
				btnumber[6].setText(btnumber[9].getText());
				btnumber[9].setText("");
			}
			else if(btnumber[8].getText().equals("")){
				btnumber[8].setText(btnumber[9].getText());
				btnumber[9].setText("");
			}
			Result();
		}
		if(e.getSource()== btnexit)
			this.dispose();
	}
	public void Result(){
		if(btnumber[1].getText().equals("1")&&btnumber[2].getText().equals("2")&&btnumber[3].getText().equals("3")&&
				btnumber[4].getText().equals("4")&&btnumber[5].getText().equals("5")&&btnumber[6].getText().equals("6")&&
				btnumber[7].getText().equals("7")&&btnumber[8].getText().equals("")&&btnumber[9].getText().equals("8"))
			 System.out.println("Thanh cong");
	}
	public static void main(String[] args) {
		new DemoGame();
	}
}
