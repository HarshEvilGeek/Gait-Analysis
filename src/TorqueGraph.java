import java.awt.*;
import java.io.*;
import javax.swing.*;

public class TorqueGraph extends JPanel {
	
	int lstart,rstart,lstop,rstop,start,stop;
	double[][] angle,w,alpha,torque,vx,vy,accx,accy,CoMX,CoMY,weightLeg;
	int[] stance,lswing,rswing;
	long[][] x,y;
	long x1,x2,y1,y2;
	int height;
	double weight,fg,torg,actorq,inclineangle1,inclineangle2,m1,m2,mdiff,weightx,weighty,I;
	double scale,llength,spotv1,spotv2,spotw1,spotw2,footmass,shankmass,thighmass,hnatmass,footlength,shanklength,thighlength;
	
	public TorqueGraph()
	{
		height=MarkerAcquisition.height;
		weight=MarkerAcquisition.weight;
		stance=Painter.stance;
		rswing=Painter.rightswing;
		lswing=Painter.leftswing;
		height=MarkerAcquisition.height;
		weight=MarkerAcquisition.weight;
		weightLeg=new double[400][6];
		llength=Painter.maxllength;
		if(height==0|weight==0)
		{
			height=175;
			weight=70;
		}
		height/=100;
		footmass=weight*0.0145;
		shankmass=weight*0.0465;
		thighmass=weight*0.1;
		hnatmass=weight*0.678;
		footlength=0.151*height;
		shanklength=0.25*height;
		thighlength=0.3*height;
		scale=height/(1.66*llength);
		lstart=GraphPainter.llegstart;
		rstart=GraphPainter.rlegstart;
		lstop=GraphPainter.llegstop;
		rstop=GraphPainter.rlegstop;
		angle=Painter.angle;
		x=Painter.x;
		y=Painter.y;
		w=new double[500][6];
		alpha=new double[500][6];
		torque=new double[500][6];
		vx=new double[500][6];
		vy=new double[500][6];
		accx=new double[500][6];
		accy=new double[500][6];
		CoMX=Painter.CoMx;
		CoMY=Painter.CoMy;	
		if(lstart<rstart)
		{
			start=lstart;
			stop=rstop;
		}
		else
		{
			start=rstart;
			stop=lstop;
		}
		int k;
		for(int i=start-1;i<stop+2;i++)
		{
			for(int j=0;j<6;j++)
			{
				spotv1=CoMX[i][j]-CoMX[i-1][j];
				spotv2=CoMX[i+1][j]-CoMX[i][j];
				vx[i][j]=((((spotv1+spotv2)/2)*110*scale)/100);
				spotv1=CoMY[i][j]-CoMY[i-1][j];
				spotv2=CoMY[i+1][j]-CoMY[i][j];
				vy[i][j]=((((spotv1+spotv2)/2)*110*scale)/100);
				k=j;
				if(k>=3)
					k++;
				spotw1=((angle[i][k]-angle[i-1][k])*110)/57.2958;
				spotw2=((angle[i+1][k]-angle[i][k])*110)/57.2958;
				w[i][j]=(spotw1+spotw2)/2;
				if(i>start)
				{
					k=i-1;
					accx[k][j]=((((vx[k+1][j]-vx[k][j])/2)+((vx[k][j]-vx[k-1][j])/2))/2);
					accy[k][j]=((((vy[k+1][j]-vy[k][j])/2)+((vy[k][j]-vy[k-1][j])/2))/2);
					alpha[k][j]=((((w[k+1][j]-w[k][j])/2)+((w[k][j]-w[k-1][j])/2))/2);
				}
			}
		}
		int q1,q2,q3,q4,q5;
		if (rstart==stance[1])
		{
			q1=stance[1];
			q2=lswing[0];
			q3=stance[2];
			q4=rswing[1];
			q5=rstop;
		}
		else
		{
			q1=stance[2];
			q2=lswing[1];
			q3=stance[3];
			q4=rswing[1];
			q5=rstop;
		}
		for(int i=q1;i<q2;i++)
		{
			weightLeg[i][0]=(0.4*(weight-footmass))+(0.6*((weight-footmass)*(double)(i-q1))/(double)(q2-q1));
			weightLeg[i][1]=(0.4*(weight-footmass-shankmass))+(0.6*((weight-footmass-shankmass)*(double)(i-q1))/(double)(q2-q1));
			weightLeg[i][2]=(0.4*(weight-footmass-shankmass-thighmass))+(0.6*((weight-footmass-shankmass-thighmass)*(double)(i-q1))/(double)(q2-q1));
			fg=footmass;
			x2=x[i][1]-x[i][0];
			y2=y[i][1]-y[i][0];
			m2=y2/x2;
			inclineangle1=Math.atan(m2);
			torg=fg*footlength*0.5*Math.cos(inclineangle1);
			if(inclineangle1<0)
				inclineangle1=-inclineangle1;
			actorq=accy[i][0]*footmass*footlength*0.5*Math.cos(inclineangle1);
			x2=x[i][3]-x[i][2];
			y2=y[i][3]-y[i][2];
			m2=y2/x2;
			inclineangle2=Math.atan(m2);
			weightx=weightLeg[i][0]*Math.cos(inclineangle2);
			if(weightx<0)
				weightx=-weightx;
			torg+=(weightx*height*0.035);
			torque[i][0]=(torg-actorq)*9.8;
		}
		for(int i=q2;i<q4;i++)
		{
			x2=x[i][1]-x[i][0];
			y2=y[i][1]-y[i][0];
			m2=y2/x2;
			inclineangle1=Math.atan(m2);
			weightLeg[i][0]=weight-footmass;
			weightLeg[i][1]=weight-footmass-shankmass;
			weightLeg[i][2]=weight-footmass-shankmass-thighmass;
			if(i>(q2+((q3-q2)/2))&i<q3)
			{
				weightLeg[i][0]-=(((i-(q2+((q3-q2)/2)))*(weight-footmass))/(q3-q2));
				weightLeg[i][1]-=(((i-(q2+((q3-q2)/2)))*(weight-footmass-shankmass))/(q3-q2));
				weightLeg[i][2]-=(((i-(q2+((q3-q2)/2)))*(weight-footmass-footmass-shankmass-thighmass))/(q3-q2));
			}
			else if(i==q3)
			{
				weightLeg[i][0]=(weight-footmass)/2;
				weightLeg[i][1]=(weight-footmass-shankmass)/2;
				weightLeg[i][2]=(weight-footmass-footmass-shankmass-thighmass)/2;
			}
			else if(i>q3)
			{
				weightLeg[i][0]=weightLeg[q3][0]-(((weight-footmass)/2)*((double)(i-q3)/(double)(q4-q3)));
				weightLeg[i][1]=weightLeg[q3][0]-(((weight-footmass-shankmass)/2)*((double)(i-q3)/(double)(q4-q3)));
				weightLeg[i][2]=weightLeg[q3][0]-(((weight-footmass-footmass-shankmass-thighmass)/2)*((double)(i-q3)/(double)(q4-q3)));
			}
			if(inclineangle1<0)
				inclineangle1=-inclineangle1;
			else
				torque[i][0]=-1*(Math.cos(inclineangle1)*0.80*footlength*weightLeg[i][0])*9.8*0.4;
		}
		for(int i=q4;i<q5;i++)
		{
			x2=x[i][1]-x[i][0];
			y2=y[i][1]-y[i][0];
			m2=y2/x2;
			inclineangle1=Math.atan(m2);
			torg=footmass*footlength*0.5*Math.cos(inclineangle1);
			I=Math.pow((0.69*footlength),2)*footmass;
			torque[i][0]=((alpha[i][0]*I)+torg)*9.8;
		}
		if (lstart==stance[1])
		{
			q1=stance[1];
			q2=rswing[0];
			q3=stance[2];
			q4=lswing[1];
			q5=lstop;
		}
		else
		{
			q1=stance[2];
			q2=rswing[1];
			q3=stance[3];
			q4=lswing[1];
			q5=lstop;
		}
		for(int i=q1;i<q2;i++)
		{
			weightLeg[i][3]=(0.4*(weight-footmass))+(0.6*((weight-footmass)*(double)(i-q1))/(double)(q2-q1));
			weightLeg[i][4]=(0.4*(weight-footmass-shankmass))+(0.6*((weight-footmass-shankmass)*(double)(i-q1))/(double)(q2-q1));
			weightLeg[i][5]=(0.4*(weight-footmass-shankmass-thighmass))+(0.6*((weight-footmass-shankmass-thighmass)*(double)(i-q1))/(double)(q2-q1));
			fg=footmass;
			x2=x[i][8]-x[i][7];
			y2=y[i][8]-y[i][7];
			m2=y2/x2;
			inclineangle1=Math.atan(m2);
			torg=fg*footlength*0.5*Math.cos(inclineangle1);
			if(inclineangle1<0)
				inclineangle1=-inclineangle1;
			actorq=accy[i][0]*footmass*footlength*0.5*Math.cos(inclineangle1);
			x2=x[i][10]-x[i][9];
			y2=y[i][10]-y[i][9];
			m2=y2/x2;
			inclineangle2=Math.atan(m2);
			weightx=weightLeg[i][0]*Math.cos(inclineangle2);
			if(weightx<0)
				weightx=-weightx;
			torg+=(weightx*height*0.035);
			torque[i][3]=(torg-actorq)*9.8;
		}
		for(int i=q2;i<q4;i++)
		{
			x2=x[i][8]-x[i][7];
			y2=y[i][8]-y[i][7];
			m2=y2/x2;
			inclineangle1=Math.atan(m2);
			weightLeg[i][3]=weight-footmass;
			weightLeg[i][4]=weight-footmass-shankmass;
			weightLeg[i][5]=weight-footmass-shankmass-thighmass;
			if(i>(q2+((q3-q2)/2))&i<q3)
			{
				weightLeg[i][3]-=(((i-(q2+((q3-q2)/2)))*(weight-footmass))/(q3-q2));
				weightLeg[i][4]-=(((i-(q2+((q3-q2)/2)))*(weight-footmass-shankmass))/(q3-q2));
				weightLeg[i][5]-=(((i-(q2+((q3-q2)/2)))*(weight-footmass-footmass-shankmass-thighmass))/(q3-q2));
			}
			else if(i==q3)
			{
				weightLeg[i][3]=(weight-footmass)/2;
				weightLeg[i][4]=(weight-footmass-shankmass)/2;
				weightLeg[i][5]=(weight-footmass-footmass-shankmass-thighmass)/2;
			}
			else if(i>q3)
			{
				weightLeg[i][3]=weightLeg[q3][3]-(((weight-footmass)/2)*((double)(i-q3)/(double)(q4-q3)));
				weightLeg[i][4]=weightLeg[q3][4]-(((weight-footmass-shankmass)/2)*((double)(i-q3)/(double)(q4-q3)));
				weightLeg[i][5]=weightLeg[q3][5]-(((weight-footmass-footmass-shankmass-thighmass)/2)*((double)(i-q3)/(double)(q4-q3)));
			}
			if(inclineangle1<0)
				inclineangle1=-inclineangle1;
			else
				torque[i][3]=-1*(Math.cos(inclineangle1)*0.80*footlength*weightLeg[i][3])*9.8*0.4;
		}
		for(int i=q4;i<q5;i++)
		{
			x2=x[i][8]-x[i][7];
			y2=y[i][8]-y[i][7];
			m2=y2/x2;
			inclineangle1=Math.atan(m2);
			torg=footmass*footlength*0.5*Math.cos(inclineangle1);
			I=Math.pow((0.69*footlength),2)*footmass;
			torque[i][3]=((alpha[i][3]*I)+torg)*9.8;
		}
	}

	public void paintComponent(Graphics g)
	{
		g.setColor(Color.blue);
		g.drawLine(80, 50, 80, 240);
		g.drawLine(80, 240, 370, 240);
		g.drawString("Right Ankle Torque", 180, 60);
		g.setColor(Color.red);
		int k=80;
		for(int i=rstart;i<rstop;i++)
		{
			g.drawLine(k,(120-((int)(3*torque[i][0]))),k+2,(120-((int)(3*torque[i+1][0]))));
			k+=2;
		}
		g.setColor(Color.blue);
		g.drawLine(80, 450, 80, 640);
		g.drawLine(80, 640, 370, 640);
		g.drawString("Right Ankle Torque", 180, 460);
		g.setColor(Color.red);
		k=80;
		for(int i=lstart;i<lstop;i++)
		{
			g.drawLine(k,(520-((int)(3*torque[i][3]))),k+2,(520-((int)(3*torque[i+1][3]))));
			k+=2;
		}
	}
	
}