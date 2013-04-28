import java.awt.event.*;
import java.awt.*;

import javax.swing.*;

public class MarkerAcquisition extends JFrame{
	private JLabel prompt;
	private JLabel hprompt;
	private JLabel wprompt;
	private JLabel prompt1;
	private JLabel prompt2;
	private JLabel prompt3;
	private JLabel prompt4;
	private JLabel prompt5;
	private JLabel prompt6;
	private JLabel prompt7;
	private JLabel prompt8;
	private JLabel prompt9;
	private JLabel prompt10;
	private JLabel prompt11;
	private JLabel prompt12;
	private JLabel prompt13;
	private JLabel prompt14;
	private JLabel prompt15;
	private JTextField frequency;
	private JTextField heightfield;
	private JTextField weightfield;
	private JTextField marker1;
	private JTextField marker2;
	private JTextField marker3;
	private JTextField marker4;
	private JTextField marker5;
	private JTextField marker6;
	private JTextField marker7;
	private JTextField marker8;
	private JTextField marker9;
	private JTextField marker10;
	private JTextField marker11;
	private JTextField marker12;
	private JTextField marker13;
	private JTextField marker14;
	private JTextField marker15;
	private JButton submit;
	private JLabel invalid;
	int ledorder[]=new int[15];
	int freq=0;
	public static int height=0;
	public static double weight=0;
	
	public MarkerAcquisition()
	{
		
		setLayout(new GridLayout(19,2));
		prompt=new JLabel("  Enter the frequency:");
		add(prompt);
		
		frequency=new JTextField(5);
		add(frequency);
		
		hprompt=new JLabel("  Enter the patients height in cms:");
		add(hprompt);
		
		heightfield=new JTextField(5);
		add(heightfield);
		
		wprompt=new JLabel("  Enter the patients weight in kgs (upto two decimals):");
		add(wprompt);
		
		weightfield=new JTextField(6);
		add(weightfield);
		
		prompt1=new JLabel("  Enter LED number (0-15) of the Right fifth metatarsil head:");
		add(prompt1);
		
		marker1=new JTextField(3);
		add(marker1);
		
		prompt2=new JLabel("  Enter LED number (0-15) of the Right lateral heel:");
		add(prompt2);
		
		marker2=new JTextField(3);
		add(marker2);
		
		prompt3=new JLabel("  Enter LED number (0-15) of the Right malleolus:");
		add(prompt3);
		
		marker3=new JTextField(3);
		add(marker3);
		
		prompt4=new JLabel("  Enter LED number (0-15) of the Right fibula head:");
		add(prompt4);
		
		marker4=new JTextField(3);
		add(marker4);
		
		prompt5=new JLabel("  Enter LED number (0-15) of the Right Lateral Femoral Condyle:");
		add(prompt5);
		
		marker5=new JTextField(3);
		add(marker5);
		
		prompt6=new JLabel("  Enter LED number (0-15) of the Right Greater Trochanter:");
		add(prompt6);
		
		marker6=new JTextField(3);
		add(marker6);
		
		prompt7=new JLabel("  Enter LED number (0-15) of the Right ASIS:");
		add(prompt7);
		
		marker7=new JTextField(3);
		add(marker7);
		
		prompt8=new JLabel("  Enter LED number (0-15) of the Sacrum:");
		add(prompt8);
		
		marker8=new JTextField(3);
		add(marker8);
		
		prompt9=new JLabel("  Enter LED number (0-15) of the Left Fifth Metatarsil Head:");
		add(prompt9);
		
		marker9=new JTextField(3);
		add(marker9);
		
		prompt10=new JLabel("  Enter LED number (0-15) of the Left Lateral Heel:");
		add(prompt10);
		
		marker10=new JTextField(3);
		add(marker10);
		
		prompt11=new JLabel("  Enter LED number (0-15) of the Left Lateral Malleolus:");
		add(prompt11);
		
		marker11=new JTextField(3);
		add(marker11);

		prompt12=new JLabel("  Enter LED number (0-15) of the Left Fibula Head:");
		add(prompt12);
		
		marker12=new JTextField(3);
		add(marker12);

		prompt13=new JLabel("  Enter LED number (0-15) of the Left Lateral Femoral Condyle:");
		add(prompt13);
		
		marker13=new JTextField(3);
		add(marker13);

		prompt14=new JLabel("  Enter LED number (0-15) of the Left Greater Trochanter:");
		add(prompt14);
		
		marker14=new JTextField(3);
		add(marker14);

		prompt15=new JLabel("  Enter LED number (0-15) of the Left ASIS:");
		add(prompt15);
		
		marker15=new JTextField(3);
		add(marker15);
		
		invalid=new JLabel("");
		add(invalid);
		
		submit=new JButton("Submit");
		add(submit);
		
		event e=new event();
		submit.addActionListener(e);
		
		}
	
	public class event implements ActionListener 
	{
		public void actionPerformed(ActionEvent e)
		{

			boolean flag=true;
			try
			{
				freq=(int)(Double.parseDouble(frequency.getText()));
				height=(int)(Double.parseDouble(heightfield.getText()));
				weight=(Double.parseDouble(weightfield.getText()));
				ledorder[0]= (int)(Double.parseDouble(marker1.getText()));
				ledorder[1]= (int)(Double.parseDouble(marker2.getText()));
				ledorder[2]= (int)(Double.parseDouble(marker3.getText()));
				ledorder[3]= (int)(Double.parseDouble(marker4.getText()));
				ledorder[4]= (int)(Double.parseDouble(marker5.getText()));
				ledorder[5]= (int)(Double.parseDouble(marker6.getText()));
				ledorder[6]= (int)(Double.parseDouble(marker7.getText()));
				ledorder[7]= (int)(Double.parseDouble(marker8.getText()));
				ledorder[8]= (int)(Double.parseDouble(marker9.getText()));
				ledorder[9]= (int)(Double.parseDouble(marker10.getText()));
				ledorder[10]= (int)(Double.parseDouble(marker11.getText()));
				ledorder[11]= (int)(Double.parseDouble(marker12.getText()));
				ledorder[12]= (int)(Double.parseDouble(marker13.getText()));
				ledorder[13]= (int)(Double.parseDouble(marker14.getText()));
				ledorder[14]= (int)(Double.parseDouble(marker15.getText()));
			}
			catch(Exception ex){
				invalid.setText("                                 Please Enter Numbers!");
				//flag=false;
			}
			if(flag)
			{
				JFrame gui=new JFrame();
				gui.setTitle("Simulator");
				gui.setSize(1300,800);
				gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				Painter panel=new Painter(ledorder,freq);
				Container pane = gui.getContentPane();
				pane.setLayout(new GridLayout(1,1));
				pane.add(panel);
				gui.setVisible(true);
				JFrame graph=new JFrame();
				graph.setTitle("Graph Data");
				graph.setSize(1300,800);
				graph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				GraphPainter gpanel=new GraphPainter();
				Container gpane=graph.getContentPane();
				gpane.setLayout(new GridLayout(1,1));
				gpane.add(gpanel);
				graph.setVisible(true);
				JFrame tgraph=new JFrame();
				tgraph.setTitle("Torque Graph Data");
				tgraph.setSize(1300,800);
				tgraph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				TorqueGraph tpanel=new TorqueGraph();
				Container tgpane=tgraph.getContentPane();
				tgpane.setLayout(new GridLayout(1,1));
				tgpane.add(tpanel);
				tgraph.setVisible(true);
			}
		}
	}
	
	
	public static void main (String args[])
	{
		MarkerAcquisition gui = new MarkerAcquisition();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setVisible(true);
		gui.setSize(850,600);
		gui.setTitle("Gait Simulator");
	}
}
