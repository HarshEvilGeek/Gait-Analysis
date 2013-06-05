import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class MarkerAcquisition extends JFrame{
	private JLabel prompt;
	private JLabel hprompt;
	private JLabel wprompt;
	private JLabel npprompt;
	private JLabel side;
	private JTextField frequency;
	private JTextField heightfield;
	private JTextField weightfield;
	private JTextField npfield;
	private JTextField sidefield;
	private JButton submit;
	private JLabel invalid;
	int nullmarker;
	int freq=0;
	public static int height=0;
	public static double weight=0;
	char leg;
	
	public MarkerAcquisition()
	{
		
		setLayout(new GridLayout(6,2));
		prompt=new JLabel("  Enter the frequency:");
		add(prompt);
		
		frequency=new JTextField(3);
		add(frequency);
		
		hprompt=new JLabel("  Enter the patients height in cms:");
		add(hprompt);
		
		heightfield=new JTextField(4);
		add(heightfield);
		
		wprompt=new JLabel("  Enter the patients weight in kgs:");
		add(wprompt);
		
		weightfield=new JTextField(4);
		add(weightfield);
		
		npprompt=new JLabel("  Null Marker (7/15):");
		add(npprompt);
		
		npfield=new JTextField(3);
		add(npfield);
		
		side=new JLabel("  Leg that was on the Force Plate (L/R):");
		add(side);
		
		sidefield=new JTextField(3);
		add(sidefield);
		
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
				nullmarker= (int)(Double.parseDouble(npfield.getText()));
				leg=sidefield.getText().charAt(0);
				
				if(nullmarker!=7&&nullmarker!=15)
				{
					invalid.setText("    Please Enter Appropriate Values for Null Marker!");
					flag=false;
				}
				if(leg!='R'&leg!='L')
				{
					invalid.setText("    Please Enter Appropriate Values for the Side!");
					flag=false;
				}
			}
			catch(Exception ex){
				invalid.setText("    Please Enter Appropriate Values!");
				flag=false;
			}
			if(flag)
			{
				JFrame gui=new JFrame();
				gui.setTitle("Simulator");
				gui.setSize(1300,800);
				gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				Painter panel=new Painter(nullmarker,freq);
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
				TorqueGraph tpanel=new TorqueGraph(leg);
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
		gui.setSize(650,160);
		gui.setTitle("Gait Simulator");
	}
}
