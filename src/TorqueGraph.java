import java.awt.*;
import java.io.*;

import javax.swing.*;

public class TorqueGraph extends JPanel {
	
	int lstart,rstart,lstop,rstop,start,stop;
	double[][] angle,w,alpha,torque,vx,vy,accx,accy,CoMX,CoMY,weightLeg;
	int[] stance,lswing,rswing;
	long[][] x,y;
	long x1,x2,y1,y2;
	int total,phaseshift;
	double height;
	double[][] force; 
	int phase[]=new int[4];
	String line;
	double weight,fg,torg,actorq,inclineangle1,inclineangle2,m1,m2,mdiff,weightx,weighty,I;
	BufferedReader br;
	double scale,llength,spotv1,spotv2,spotw1,spotw2,footmass,shankmass,thighmass,hnatmass,footlength,shanklength,thighlength;
	
	public TorqueGraph(char side)
	{
		total=Painter.TotalFrames;
		force=new double[880][3];
		try
		{
		br=new BufferedReader(new FileReader("/Users/akhil/Documents/ForceData.txt"));
		}
		catch(FileNotFoundException f)
		{
			System.out.println("ForceData file not found");
		}
		
		long[][] forcein=new long[880][3];
		char a;
		int eflag;
		long current,maxweight=1,maxframe=0;
		
		for(int i=0;i<total;i++)
		{
			try 
			{
				line = br.readLine();
			} 
			catch (IOException e) 
			{
				System.exit(0);
			}
			if(line==null)
				break;
			
			int len=line.length(),sflag=0,c,decflag=0,sigfig=0;
			c=0;
			current=eflag=0;
			for(int k=0;k<len;k++)
			{
				if(line.charAt(k)==' ')
					eflag=0;
				else if(line.charAt(k)=='E')
				{
					current=0;
					while((line.charAt(k+1)!=' ')&k<len-2)
						k++;
					k++;
					eflag=1;
					sigfig=0;
					decflag=0;
				}
				else if(line.charAt(k)=='.')
				{
					sigfig=0;
					decflag=1;
				}
				else if(line.charAt(k)=='-')
					sflag=1;
				else if(line.charAt(k)==',')
				{
					if(sigfig<6)
					{
						current*=10;
						sigfig++;
					}
					if(sflag==1)
						forcein[i][c]=-current;
					else
						forcein[i][c]=current;
						
					sflag=0;
					c++;
					current=0;
					sigfig=0;
					decflag=0;
					eflag=0;
				}
				else
				{
					current*=10;
					a=line.charAt(k);
					current+=Character.getNumericValue(a);
					if(decflag==1)
						sigfig++;
					if(sigfig>=6)
					{
						
						while((line.charAt(k+1)!=',')&(line.charAt(k+1)!='E')&k<len-2)
							k++;
						
					}
				}
			}
			if(sigfig<6)
			{
				current*=10;
				sigfig++;
			}
			if(eflag==1);
			else if(sflag==1)
				forcein[i][c]=-current;
			else
			{
				forcein[i][c]=current;
				if(current>maxweight)
				{
					maxweight=current;
					maxframe=i;
				}
			}
			current=0;
			sigfig=0;
			decflag=0;
		}
		
		
		
		height=(double)MarkerAcquisition.height;
		weight=MarkerAcquisition.weight;
		stance=Painter.stance;
		rswing=Painter.rightswing;
		lswing=Painter.leftswing;
		int scount=Painter.stancecounttot;
		int lcount=Painter.leftcounttot;
		int rcount=Painter.rightcounttot;
		int platephase1=0;
		weightLeg=new double[400][6];
		llength=Painter.maxllength;
		if(height==0|weight==0)
		{
			height=175;
			weight=70;
		}
		height/=100;
		double scale=(double)weight/(double)maxweight;
		for(int i=0;i<total;i++)
		{
			for(int j=0;j<3;j++)
				force[i][j]=(double)forcein[i][j]*scale;
		}
		int flag=0, flag2=0, start=0, stop=0;
		for(int i=0;i<total;i++)
		{
			if(force[i][2]>0.2&flag==0)
			{
				start=i;
				flag=1;
				flag2=1;
			}
			if(force[i][0]>0&flag2==1)
			{
				flag2=0;
				platephase1=i;
			}
			if(force[i][2]<0.2&flag==1)
			{
				stop=i;
				break;
			}
		}
		if(start>(int)maxframe|stop<(int)maxframe)
		{
			System.out.println("Invalid Force Data");
			System.exit(0);
		}
		int mindistance=1000,dif=0;
		for(int i=0;i<scount;i++)
		{
			dif=start-stance[i];
			if(dif<0) break;
			if(dif<mindistance)
			{
				mindistance=dif;
				phase[0]=stance[i];
				phase[3]=stance[i+2];
			}
		}
		phaseshift=mindistance;
		mindistance=1000;
		if(side=='L')
		{
			for(int i=0;i<rcount;i++)
			{
				dif=rswing[i]-phase[0];
				if(dif<0);
				else if(dif<mindistance)
				{
					mindistance=dif;
					phase[1]=rswing[i];
				}
			}
		}
		else if(side=='R')
		{
			for(int i=0;i<lcount;i++)
			{
				dif=lswing[i]-phase[0];
				if(dif<0);
				else if(dif<mindistance)
				{
					mindistance=dif;
					phase[1]=lswing[i];
				}
			}
		}
		mindistance=1000;
		if(side=='L')
		{
			for(int i=0;i<lcount;i++)
			{
				dif=stop-lswing[i];
				if(dif<0);
				else if(dif<mindistance)
				{
					mindistance=dif;
					phase[2]=lswing[i];
				}
			}
		}
		else if(side=='R')
		{
			for(int i=0;i<rcount;i++)
			{
				dif=stop-rswing[i];
				if(dif<0);
				else if(dif<mindistance)
				{
					mindistance=dif;
					phase[2]=rswing[i];
				}
			}
		}
		int f,s,t;
		if(side=='R')
		{
			f=0;
			s=1;
			t=2;
		}
		else
		{
			f=4;
			s=5;
			t=6;	
		}
		phaseshift+=mindistance+(platephase1-phase[1]);
		phaseshift/=3;
		System.out.println("Phase Shift = "+phaseshift);
		
		for(int i=0;i<total-phaseshift;i++)
		{
			force[i]=force[i+phaseshift];	
			forcein[i]=forcein[i+phaseshift];
		}
		
		phase[1]=phase[0]+(((phase[1]-phase[0])+(platephase1-start))/2);
		
		System.out.println("Start "+start);
		start-=phaseshift;
		stop-=phaseshift;
		phase[1]+=start-phase[0];
		phase[0]=start;
		System.out.println("Start "+start);
		System.out.println("Phase starts at "+phase[0]);
		System.out.println("Force Inverts at "+platephase1);
		System.out.println("Next phase starts at "+phase[2]);
		System.out.println("Last phase starts at "+phase[3]);
		
		footmass=weight*0.0145;
		shankmass=weight*0.0465;
		thighmass=weight*0.1;
		hnatmass=weight*0.678;
		footlength=0.151*height;
		shanklength=0.25*height;
		thighlength=0.3*height;
		scale=height/(1.66*llength);
		angle=Painter.angle;
		x=Painter.x;
		y=Painter.y;
		w=new double[800][3];
		alpha=new double[800][3];
		torque=new double[500][3];
		vx=new double[500][3];
		vy=new double[500][3];
		accx=new double[500][3];
		accy=new double[500][3];
		CoMX=Painter.realCoMx;
		CoMY=Painter.realCoMy;
		
		double wtemp1,wtemp2;
		for(int i=phase[0]-32;i<phase[3]+32;i++)
		{
			wtemp1=angle[i][f]-angle[i-1][f];
			wtemp2=angle[i+1][f]-angle[i][f];
			w[i][0]=(wtemp1+wtemp2)/2;
			w[i][0]/=(110/57.296);
			wtemp1=angle[i][s]-angle[i-1][s];
			wtemp2=angle[i+1][s]-angle[i][s];
			w[i][1]=(wtemp1+wtemp2)/2;
			w[i][1]/=(110/57.296);
			wtemp1=angle[i][t]-angle[i-1][t];
			wtemp2=angle[i+1][t]-angle[i][t];
			w[i][2]=(wtemp1+wtemp2)/2;
			w[i][2]/=(110/57.296);		
		}
		double[][] tempw=w;
		double tempsum1,tempsum2,tempsum3;
		for(int i=phase[0]-16;i<phase[3]+16;i++)
		{
			tempsum1=tempsum2=tempsum3=0;
			for(int j=-2;j<2;j++)
			{
				tempsum1+=tempw[i+j][0];
				tempsum2+=tempw[i+j][1];
				tempsum3+=tempw[i+j][2];
			}
			w[i][0]=tempsum1/4;
			w[i][1]=tempsum2/4;
			w[i][2]=tempsum3/4;
		}
		
		double alphatemp1,alphatemp2;
		for(int i=phase[0]-16;i<phase[3]+16;i++)
		{
			alphatemp1=w[i][0]-w[i-1][0];
			alphatemp2=w[i+1][0]-w[i][0];
			alpha[i][0]=(alphatemp1+alphatemp2)/2;
			alpha[i][0]*=110;
			alphatemp1=w[i][1]-w[i-1][1];
			alphatemp2=w[i+1][1]-w[i][1];
			alpha[i][1]=(alphatemp1+alphatemp2)/2;
			alpha[i][1]*=110;
			alphatemp1=w[i][2]-w[i-1][2];
			alphatemp2=w[i+1][2]-w[i][2];
			alpha[i][2]=(alphatemp1+alphatemp2)/2;
			alpha[i][2]*=110;		
		}
		double[][] tempalpha=alpha;
		for(int i=phase[0];i<phase[3];i++)
		{
			tempsum1=tempsum2=tempsum3=0;
			for(int j=-8;j<8;j++)
			{
				tempsum1+=tempalpha[i+j][0];
				tempsum2+=tempalpha[i+j][1];
				tempsum3+=tempalpha[i+j][2];
			}
			alpha[i][0]=tempsum1/16;
			alpha[i][1]=tempsum2/16;
			alpha[i][2]=tempsum3/16;
		}
		if(side=='L')
		{
			f=3;
			s=4;
			t=5;
		}
		double vxtemp1,vxtemp2,vytemp1,vytemp2;
		for(int i=phase[0]-32;i<phase[3]+32;i++)
		{
			vxtemp1=CoMX[i][f]-CoMX[i-1][f];
			vxtemp2=CoMX[i+1][f]-CoMX[i][f];
			vx[i][0]=(vxtemp1+vxtemp2)/2;
			vx[i][0]/=(110/57.296);
			vxtemp1=CoMX[i][s]-CoMX[i-1][s];
			vxtemp2=CoMX[i+1][s]-CoMX[i][s];
			vx[i][1]=(vxtemp1+vxtemp2)/2;
			vx[i][1]/=(110/57.296);
			vxtemp1=CoMX[i][t]-CoMX[i-1][t];
			vxtemp2=CoMX[i+1][t]-CoMX[i][t];
			vx[i][2]=(vxtemp1+vxtemp2)/2;
			vx[i][2]/=(110/57.296);
			vytemp1=CoMX[i][f]-CoMX[i-1][f];
			vytemp2=CoMX[i+1][f]-CoMX[i][f];
			vy[i][0]=(vytemp1+vytemp2)/2;
			vy[i][0]/=(110/57.296);
			vytemp1=CoMX[i][s]-CoMX[i-1][s];
			vytemp2=CoMX[i+1][s]-CoMX[i][s];
			vy[i][1]=(vytemp1+vytemp2)/2;
			vy[i][1]/=(110/57.296);
			vytemp1=CoMX[i][t]-CoMX[i-1][t];
			vytemp2=CoMX[i+1][t]-CoMX[i][t];
			vy[i][2]=(vytemp1+vytemp2)/2;
			vy[i][2]/=(110/57.296);		
		}
		double[][] tempvx=vx,tempvy=vy;
		for(int i=phase[0]-16;i<phase[3]+16;i++)
		{
			tempsum1=tempsum2=tempsum3=0;
			for(int j=-4;j<4;j++)
			{
				tempsum1+=tempvx[i+j][0];
				tempsum2+=tempvx[i+j][1];
				tempsum3+=tempvx[i+j][2];
			}
			vx[i][0]=tempsum1/8;
			vx[i][1]=tempsum2/8;
			vx[i][2]=tempsum3/8;
			tempsum1=tempsum2=tempsum3=0;
			for(int j=-4;j<4;j++)
			{
				tempsum1+=tempvy[i+j][0];
				tempsum2+=tempvy[i+j][1];
				tempsum3+=tempvy[i+j][2];
			}
			vy[i][0]=tempsum1/8;
			vy[i][1]=tempsum2/8;
			vy[i][2]=tempsum3/8;
		}
		
		double acctemp1,acctemp2;
		for(int i=phase[0]-16;i<phase[3]+16;i++)
		{
			acctemp1=vx[i][0]-vx[i-1][0];
			acctemp2=vx[i+1][0]-vx[i][0];
			accx[i][0]=(acctemp1+acctemp2)/2;
			accx[i][0]*=110;
			acctemp1=vx[i][1]-vx[i-1][1];
			acctemp2=vx[i+1][1]-vx[i][1];
			accx[i][1]=(acctemp1+acctemp2)/2;
			accx[i][1]*=110;
			acctemp1=vx[i][2]-vx[i-1][2];
			acctemp2=vx[i+1][2]-vx[i][2];
			accx[i][2]=(acctemp1+acctemp2)/2;
			accx[i][2]*=110;	
			acctemp1=vy[i][0]-vy[i-1][0];
			acctemp2=vy[i+1][0]-vy[i][0];
			accy[i][0]=(acctemp1+acctemp2)/2;
			accy[i][0]*=110;
			acctemp1=vy[i][1]-vy[i-1][1];
			acctemp2=vy[i+1][1]-vy[i][1];
			accy[i][1]=(acctemp1+acctemp2)/2;
			accy[i][1]*=110;
			acctemp1=vy[i][2]-vy[i-1][2];
			acctemp2=vy[i+1][2]-vy[i][2];
			accy[i][2]=(acctemp1+acctemp2)/2;
			accy[i][2]*=110;	
		}
		double[][] tempaccx=accx;
		double[][] tempaccy=accy;
		for(int i=phase[0];i<phase[3];i++)
		{
			tempsum1=tempsum2=tempsum3=0;
			for(int j=-6;j<6;j++)
			{
				tempsum1+=tempaccx[i+j][0];
				tempsum2+=tempaccx[i+j][1];
				tempsum3+=tempaccx[i+j][2];
			}
			accx[i][0]=tempsum1/12;
			accx[i][1]=tempsum2/12;
			accx[i][2]=tempsum3/12;
			tempsum1=tempsum2=tempsum3=0;
			for(int j=-6;j<6;j++)
			{
				tempsum1+=tempaccy[i+j][0];
				tempsum2+=tempaccy[i+j][1];
				tempsum3+=tempaccy[i+j][2];
			}
			accy[i][0]=tempsum1/12;
			accy[i][1]=tempsum2/12;
			accy[i][2]=tempsum3/12;
		} 
		
		double fulc1,fulc2,fulc3;
		System.out.println(height);
		fulc1=height*0.036;
		fulc2=height*0.305;
		fulc3=height*0.53;
		
		double basetorque,weighttorque,nettorque,Ifoot;
		int he,kn,hip;
		Ifoot=((0.69*footlength)*(0.69*footlength))*footmass;
		
		if(side=='R')
		{
			he=1;
			kn=3;
			hip=5;
		}
		else
		{
			he=8;
			kn=10;
			hip=12;
		}
		for(int i=phase[0];i<phase[1];i++)
		{
			basetorque=fulc1*force[i][0];
			weighttorque=(CoMX[i][f]-x[i][he])*footmass*scale;
			nettorque=Ifoot*alpha[i][0];
			if(weighttorque<0)
				weighttorque=-weighttorque;
			if(nettorque<0)
				nettorque=-nettorque;
			if(basetorque<0)
				basetorque=-basetorque;
			torque[i][0]=weighttorque+basetorque-nettorque;
			System.out.println("base torque = "+basetorque+" Weight torque = "+ weighttorque+ " net Torque = "+ nettorque+ " Total Torque = "+ torque[i][0]);
			basetorque=y[i][kn]*scale*force[i][0];
			weighttorque=(force[i][2]-footmass-shankmass)*scale*(CoMX[i][s]-x[i][kn]);
		}
		
		for(int i=phase[1];i<phase[2];i++)
		{
			weighttorque=-force[i][2]*footlength*0.5;
			torque[i][0]=(weighttorque+torque[i-1][0])/2;
		}
		
		for(int i=phase[2];i<=phase[3];i++)
		{
			nettorque=Ifoot*alpha[i][0];
			torque[i][0]=(nettorque+torque[i-1][0])/2;
		}
	}

	public void paintComponent(Graphics g)
	{
		g.setColor(Color.blue);
		g.drawLine(80, 50, 80, 240);
		g.drawLine(80, 240, 370, 240);
		g.drawString("Ankle Torque", 180, 60);
		g.setColor(Color.red);
		int k=80;
		for(int i=phase[0];i<phase[3];i++)
		{
			g.drawLine(k,(100-((int)(9.8*torque[i][0]))),k+2,(100-((int)(9.8*torque[i+1][0]))));
			k+=2;
		}
		g.setColor(Color.blue);
		g.drawLine(480, 50, 480, 240);
		g.drawLine(480, 240, 770, 240);
		g.drawString("Knee Torque", 580, 60);
		g.setColor(Color.red);
		k=480;
		for(int i=phase[0];i<phase[3];i++)
		{
			g.drawLine(k,(100-((int)(9.8*torque[i][1]))),k+2,(100-((int)(9.8*torque[i+1][1]))));
			k+=2;
		}
		g.setColor(Color.blue);
		g.drawLine(880, 50, 880, 240);
		g.drawLine(880, 240, 1170, 240);
		g.drawString("Hip Torque",980,60);
		g.setColor(Color.red);
		k=880;
		for(int i=phase[0];i<phase[3];i++)
		{
			g.drawLine(k,(100-((int)(9.8*torque[i][2]))),k+2,(100-((int)(9.8*torque[i+1][2]))));
			k+=2;
		}
	}
	
}