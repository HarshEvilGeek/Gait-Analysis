import java.awt.*;
import java.io.*;
import javax.swing.*;

public class GraphPainter extends JPanel {

	long[][] x,y;
	int[] stance,rswing,lswing;
	double[][] angle;
	double[][] CoMx,CoMy;
	public static int rlegstart,rlegstop,llegstart,llegstop;
	int total;
	
	public GraphPainter()
	{
		x=Painter.x;
		y=Painter.y;
		stance=Painter.stance;
		rswing=Painter.rightswing;
		lswing=Painter.leftswing;
		angle=Painter.angle;
		CoMx=Painter.CoMx;
		CoMy=Painter.CoMy;
		total=Painter.TotalFrames;
		if(lswing[0]>rswing[0])
		{
			llegstart=stance[2];
			llegstop=stance[4]+5;
			rlegstart=stance[1];
			rlegstop=stance[3]+5;
		}
		else
		{
			rlegstart=stance[2];
			rlegstop=stance[4]+5;
			llegstart=stance[1];
			llegstop=stance[3]+5;
		}
	}
	public void paintComponent(Graphics g)
	{
		g.setColor(Color.blue);
		g.drawLine(80, 10, 80, 200);
		g.drawLine(80, 200, 370, 200);
		g.drawString("Right Ankle ROM", 180, 60);
		g.setColor(Color.red);
		int k=80;
		for(int i=rlegstart;i<rlegstop;i++)
		{
			g.drawLine(k,(120-(3*(int)(angle[i][0]))),k+2,(120-(3*(int)(angle[i+1][0]))));
			k+=2;
		}
		g.setColor(Color.blue);
		g.drawLine(480, 10, 480, 200);
		g.drawLine(480, 200, 770, 200);
		g.drawString("Right Knee ROM",580,60);
		g.setColor(Color.red);
		k=480;
		for(int i=rlegstart;i<rlegstop;i++)
		{
			g.drawLine(k,(200-(3*(int)(angle[i][1]))),k+2,(200-(3*(int)(angle[i+1][1]))));
			k+=2;
		}
		g.setColor(Color.blue);
		g.drawLine(880, 10, 880, 200);
		g.drawLine(880, 200, 1170, 200);
		g.drawString("Right Thigh ROM",980,60);
		g.setColor(Color.red);
		k=880;
		for(int i=rlegstart;i<rlegstop;i++)
		{
			g.drawLine(k,(200-(3*(int)(angle[i][2]))),k+2,(200-(3*(int)(angle[i+1][2]))));
			k+=2;
		}
		g.setColor(Color.blue);
		g.drawLine(80, 240, 80, 430);
		g.drawLine(80, 430, 370, 430);
		g.drawString("Left Ankle ROM",180,290);
		g.setColor(Color.red);
		k=80;
		for(int i=llegstart;i<llegstop;i++)
		{
			g.drawLine(k,(350-(3*(int)(angle[i][4]))),k+2,(350-(3*(int)(angle[i+1][4]))));
			k+=2;
		}
		g.setColor(Color.blue);
		g.drawLine(480, 240, 480, 430);
		g.drawLine(480, 430, 770, 430);
		g.drawString("Left Knee ROM",580,290);
		g.setColor(Color.red);
		k=480;
		for(int i=llegstart;i<llegstop;i++)
		{
			g.drawLine(k,(430-(3*(int)(angle[i][5]))),k+2,(430-(3*(int)(angle[i+1][5]))));
			k+=2;
		}
		g.setColor(Color.blue);
		g.drawLine(880, 240, 880, 430);
		g.drawLine(880, 430, 1170, 430);
		g.drawString("Left Thigh ROM",980,290);
		g.setColor(Color.red);
		k=880;
		for(int i=llegstart;i<llegstop;i++)
		{
			g.drawLine(k,(430-(3*(int)(angle[i][6]))),k+2,(430-(3*(int)(angle[i+1][6]))));
			k+=2;
		}
		g.setColor(Color.blue);
		g.drawLine(80, 470, 80, 660);
		g.drawLine(80, 660, 370, 660);
		g.drawString("Right Hip (Abduction) ROM",180,520);
		g.setColor(Color.red);
		k=80;
		for(int i=rlegstart;i<rlegstop;i++)
		{
			g.drawLine(k,(630-(3*(int)(angle[i][3]))),k+2,(630-(3*(int)(angle[i+1][3]))));
			k+=2;
		}
		g.setColor(Color.blue);
		g.drawLine(480, 470, 480, 660);
		g.drawLine(480, 660, 770, 660);
		g.drawString("Left Hip (Abduction) ROM",580,520);
		g.setColor(Color.red);
		k=480;
		for(int i=llegstart;i<llegstop;i++)
		{
			g.drawLine(k,(130-(3*(int)(angle[i][7]))),k+2,(130-(3*(int)(angle[i+1][7]))));
			k+=2;
		}
		
		
	}
}
