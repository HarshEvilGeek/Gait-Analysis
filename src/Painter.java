import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;

public class Painter extends JPanel implements ActionListener{
	
	Image dbImage;
	Graphics dbGraphics;
	public static int TotalFrames;
	int radius;
	public static long[][] x, y, z;
	long X,Y,Z;
	private BufferedReader br;
	String line;
	int sensors;int i=0;
	int CurrentFrame;
	long startTime;
	long endTime;
	int framewidth;
	long[][] possiblenull1,possiblenull2;
	int freq;
	double hiplength;
	double scale;
	public static double[][] CoMx,CoMy,realCoMx,realCoMy;
	public static double[][] angle;
	Timer tm;
	int start, rightmaxspeed, leftmaxspeed, stancecount, leftcount, rightcount;
	public static int stancecounttot=0, leftcounttot=0, rightcounttot=0;
	public static int[] stance, leftswing, rightswing;
	public static double maxllength;
	
	public Painter(int nullmarker, int freq){
		stancecounttot=rightcounttot=leftcounttot=0;
		maxllength=0;
		stancecount=1;
		leftcount=rightcount=0;
		rightmaxspeed=leftmaxspeed=0;
		start=0;
		if(freq==0) freq=10;
		tm=new Timer((1000/freq),this);
		possiblenull1=new long[1000][3];
		possiblenull2=new long[1000][3];
		stance=new int[50];
		leftswing=new int[50];
		rightswing=new int[50];
		for(int i=0;i<50;i++)
		{
			stance[i]=leftswing[i]=rightswing[i]=-1;
		}
		CoMx=new double[1000][7];
		CoMy=new double[1000][7];
		realCoMx=new double[1000][7];
		realCoMy=new double[1000][7];
		angle=new double[1000][9];
		CurrentFrame=0;
		framewidth=1300;
		try
		{
		br=new BufferedReader(new FileReader("/Users/akhil/Documents/recentmarker.txt"));
		startTime = System.currentTimeMillis();
		sensors=15;
		x=new long[1000][sensors];
		y=new long[1000][sensors];
		z=new long[1000][sensors];
		}
		catch(FileNotFoundException f)
		{
			System.out.println("File not Found");
		}
		
		radius=8;	
		int flag=0,counter=0;
		int status=0;
		TotalFrames=0;
		long minX=2000,minY=2000,minZ=2000,maxX=-2000,maxY=-2000,maxZ=-2000;
		int p=0;
		while(flag==0)
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
			char a;
			long[] temp=new long[6];
			int sflag=0;
			int c=0, len=line.length();
			long current=0;
			for(int k=0;k<len;k++)
			{
				if(line.charAt(k)==' ');
				else if(line.charAt(k)=='.');
				else if(line.charAt(k)=='-')
					sflag=1;
				else if(line.charAt(k)==',')
				{
					if(sflag==1)
						temp[c]=-current;
					else
						temp[c]=current;
					sflag=0;
					c++;
					current=0;
				}
				else
				{
					current*=10;
					a=line.charAt(k);
					current+=Character.getNumericValue(a);
				}
			}
			if(sflag==1)
				temp[c]=-current;
			else
				temp[c]=current;
			if(temp[2]<minX) minX=temp[2];
			if(temp[3]<minY) minY=temp[3];
			if(temp[4]<minZ) minZ=temp[4];
			if(temp[2]>maxX) maxX=temp[2];
			if(temp[3]>maxY) maxY=temp[3];
			if(temp[4]>maxZ) maxZ=temp[4];
			i++;
			status+=temp[5];
			if (temp[1]==7)
			{
				p=1;
				possiblenull1[(int)temp[0]][0]=temp[2];
				possiblenull1[(int)temp[0]][1]=temp[3];
				possiblenull1[(int)temp[0]][2]=temp[4];
				
			}
			else if(temp[1]==15)
			{
				possiblenull2[(int)temp[0]][0]=temp[2];
				possiblenull2[(int)temp[0]][1]=temp[3];
				possiblenull2[(int)temp[0]][2]=temp[4];
			}
			else
			{
				x[(int) temp[0]][(int)(temp[1]-p)]=temp[2];
				y[(int) temp[0]][(int)(temp[1]-p)]=temp[3];
				z[(int) temp[0]][(int)(temp[1]-p)]=temp[4];
			}
			counter++;
			if(counter==16)
			{
				if(status>5)
				{
					flag=1;
				}
				status=0;
				TotalFrames++;
				counter=0;
				p=0;
			}
		}
		
		
		if(nullmarker==7)
		{
			for(int i=0;i<TotalFrames;i++)
			{
				x[i][14]=possiblenull2[i][0];
				y[i][14]=possiblenull2[i][1];
				z[i][14]=possiblenull2[i][2];
			}
		}
		
		if(nullmarker==15)
		{
			for(int i=0;i<TotalFrames;i++)
			{
				x[i][14]=possiblenull1[i][0];
				y[i][14]=possiblenull1[i][1];
				z[i][14]=possiblenull1[i][2];
			}
		}

		long temp;
		if(nullmarker==15)
		{
			for(int i=0;i<TotalFrames;i++)
			{
				for(int j=0;j<7;j++)
				{
					temp=x[i][j];
					x[i][j]=x[i][j+7];
					x[i][j+7]=temp;
					temp=y[i][j];
					y[i][j]=y[i][j+7];
					y[i][j+7]=temp;
					temp=z[i][j];
					z[i][j]=z[i][j+7];
					z[i][j+7]=temp;
				}
			}
		}
			
		
		Z=(long)maxZ-(long)minZ;
		X=(long)maxX-(long)minX;
		Y=(long)maxY-(long)minY;
		if(minX<0)
			minX=-minX;
		if(minY<0)
			minY=-minY;
		if(minZ<0)
			minZ=-minZ;
		int nearupvalid;
		for(int i=0;i<TotalFrames;i++)
		{
			for(int j=0;j<15;j++)
			{
				if(x[i][j]==0L&y[i][j]==0L)
				{
					nearupvalid=1;
					while(x[i+nearupvalid][j]==0L&i+nearupvalid<TotalFrames)
						nearupvalid++;
					x[i][j]=((x[i+nearupvalid][j]+(x[i-1][j]*(long)nearupvalid))/((long)nearupvalid+1));
					y[i][j]=((y[i+nearupvalid][j]+(y[i-1][j]*(long)nearupvalid))/((long)nearupvalid+1));
					z[i][j]=((z[i+nearupvalid][j]+(z[i-1][j]*(long)nearupvalid))/((long)nearupvalid+1));				
				}
			}
		}
		double magx1,magy1,magx2,magy2,m1,m2,mdiff;
		for(int i=0;i<TotalFrames;i++)
		{
			for(int j=0;j<15;j++)
			{
				x[i][j]+=minX;
				y[i][j]+=minY;
				z[i][j]+=minZ;
			}
			magx1=x[i][0]-x[i][1];
			magy1=y[i][0]-y[i][1];
			magx2=x[i][3]-x[i][2];
			magy2=y[i][3]-y[i][2];
			m1=magy1/magx1;
			m2=magy2/magx2;
			mdiff=(m1-m2)/(1+(m1*m2));
			angle[i][0]=Math.atan(mdiff);
			angle[i][0]*=57.2958;
			if(angle[i][0]<0)
				angle[i][0]+=180;
			angle[i][0]-=96;
			magx1=x[i][3]-x[i][2];
			magy1=y[i][3]-y[i][2];
			magx2=x[i][4]-x[i][5];
			magy2=y[i][4]-y[i][5];
			m1=magy1/magx1;
			m2=magy2/magx2;
			mdiff=(m1-m2)/(1+(m1*m2));
			angle[i][1]=Math.atan(mdiff);
			angle[i][1]*=57.2958;
			angle[i][1]=-angle[i][1];
			magx1=x[i][5]-x[i][4];
			magy1=y[i][5]-y[i][4];
			magx2=x[i][6]-x[i][5];
			magy2=y[i][6]-y[i][5];
			m1=magy1/magx1;
			m2=magy2/magx2;
			mdiff=(m1-m2)/(1+(m1*m2));
			angle[i][2]=Math.atan(mdiff);
			angle[i][2]*=57.2958;
			if(angle[i][2]<0)
				angle[i][2]+=180;
			magx1=y[i][4]-y[i][5];
			magy1=z[i][4]-z[i][5];
			magx2=y[i][5]-y[i][6];
			magy2=z[i][5]-z[i][6];
			m1=magy1/magx1;
			m2=magy2/magx2;
			mdiff=(m1-m2)/(1+(m1*m2));
			angle[i][3]=Math.atan(mdiff);
			angle[i][3]*=57.2958;
			if(angle[i][3]<0)
				angle[i][3]+=180;
			magx1=x[i][7]-x[i][8];
			magy1=y[i][7]-y[i][8];
			magx2=x[i][10]-x[i][9];
			magy2=y[i][10]-y[i][9];
			m1=magy1/magx1;
			m2=magy2/magx2;
			mdiff=(m1-m2)/(1+(m1*m2));
			angle[i][4]=Math.atan(mdiff);
			angle[i][4]*=57.2958;
			if(angle[i][4]<0)
				angle[i][4]+=180;
			angle[i][4]-=96;
			magx1=x[i][9]-x[i][10];
			magy1=y[i][9]-y[i][10];
			magx2=x[i][12]-x[i][11];
			magy2=y[i][12]-y[i][11];
			m1=magy1/magx1;
			m2=magy2/magx2;
			mdiff=(m1-m2)/(1+(m1*m2));
			angle[i][5]=Math.atan(mdiff);
			angle[i][5]*=57.2958;
			angle[i][5]=-angle[i][5];
			magx1=x[i][11]-x[i][12];
			magy1=y[i][11]-y[i][12];
			magx2=x[i][13]-x[i][12];
			magy2=y[i][13]-y[i][12];
			m1=magy1/magx1;
			m2=magy2/magx2;
			mdiff=(m1-m2)/(1+(m1*m2));
			angle[i][6]=Math.atan(mdiff);
			angle[i][6]*=57.2958;
			if(angle[i][6]<0)
				angle[i][6]+=180;
			magx1=y[i][12]-y[i][11];
			magy1=z[i][12]-z[i][11];
			magx2=y[i][13]-y[i][12];
			magy2=z[i][13]-z[i][12];
			m1=magy1/magx1;
			m2=magy2/magx2;
			mdiff=(m1-m2)/(1+(m1*m2));
			angle[i][7]=Math.atan(mdiff);
			angle[i][7]*=57.2958;
			angle[i][7]=-angle[i][7];
			if(i>0)
			{
				if(angle[i][7]>angle[i-1][7]+8|angle[i][7]<angle[i-1][7]-8)
					angle[i][7]=angle[i-1][7];
			}
			
		}
		for(int i=0;i<TotalFrames;i++)
		{
			if(nullmarker==15){
				angle[i][3]-=180;
				angle[i][7]=-angle[i][7];
				angle[i][3]=-angle[i][3];
			}
		}
		scale=(double)framewidth/(double)X;
		scale*=2;
		double hypsq=((double)X*(double)X)+((double)Z*(double)Z);
		double hyp=Math.pow(hypsq,0.5);
		double cos=(double)X/(double)hyp;
		for(int i=0;i<TotalFrames;i++)
		{
			for(int j=0;j<15;j++)
			{	
				if(i>0)
					x[i][j]=(long)((double)x[i][j]/cos);
			}
		}
		for(int i=0;i<TotalFrames;i++)
		{
			for(int j=0;j<15;j++)
			{	
				x[i][j]=(int)((double)x[i][j]*scale);
				y[i][j]=(int)((double)y[i][j]*scale);
				if(i>0)
				{
					if(x[i][2]-x[i-1][2]>rightmaxspeed)
						rightmaxspeed=(int)(x[i][2]-x[i-1][2]);
					if(x[i][8]-x[i-1][8]>leftmaxspeed)
						leftmaxspeed=(int)(x[i][2]-x[i-1][2]);
				}
				else
					hiplength=Math.sqrt((double)(((x[i][5]-x[i][4])*(x[i][5]-x[i][4]))+((y[i][5]-y[i][4])*(y[i][5]-y[i][4]))))*191/245;
			}
			if(i>1)
			{
				if(x[i][2]>x[i-1][2]+3|x[i][8]>x[i-1][8]+3)
					start=1;
			}

			if(maxllength<Math.sqrt(((x[i][1]-x[i][6])*(x[i][1]-x[i][6]))+((y[i][1]-y[i][6])*(y[i][1]-y[i][6]))))
				maxllength=Math.sqrt(((x[i][1]-x[i][6])*(x[i][1]-x[i][6]))+((y[i][1]-y[i][6])*(y[i][1]-y[i][6])));
			if(start==1&i>1)
			{
					if(x[i][2]<x[i-1][2]+4&x[i][8]<x[i-1][8]+4&stancecount==1)
					{
						stance[stancecounttot]=i;
						stancecounttot++;
						stancecount=0;
						rightcount=1;
						leftcount=1;
					}
					if(x[i][2]>x[i-1][2]+4&x[i][8]<x[i-1][8]+4&rightcount==1)
					{
						rightswing[rightcounttot]=i;
						rightcounttot++;
						stancecount=1;
						rightcount=0;
					}
					if(x[i][2]<x[i-1][2]+4&x[i][8]>x[i-1][8]+4&leftcount==1)
					{
						leftswing[leftcounttot]=i;
						leftcounttot++;
						leftcount=0;
						stancecount=1;
					}
				
			}
			CoMx[i][0]=(((x[i][0]%1300)+((x[i][1]-x[i][0]))*0.4));
			CoMy[i][0]=600-((y[i][0]+(y[i][1]-y[i][0])*0.4));
			CoMx[i][1]=(((x[i][2]%1300)+((x[i][3]-x[i][2]))*0.567));
			CoMy[i][1]=600-((y[i][2]+(y[i][3]-y[i][2])*0.567));
			CoMx[i][2]=(((x[i][4]%1300)+((x[i][5]-x[i][4]))*0.567));
			CoMy[i][2]=600-((y[i][4]+(y[i][5]-y[i][4])*0.567));
			CoMx[i][3]=(((x[i][7]%1300)+((x[i][8]-x[i][7]))*0.4));
			CoMy[i][3]=600-((y[i][7]+(y[i][8]-y[i][7])*0.4));
			CoMx[i][4]=(((x[i][9]%1300)+((x[i][10]-x[i][9]))*0.567));
			CoMy[i][4]=600-((y[i][9]+(y[i][10]-y[i][9])*0.567));
			CoMx[i][5]=(((x[i][11]%1300)+((x[i][12]-x[i][11]))*0.567));
			CoMy[i][5]=600-((y[i][11]+(y[i][12]-y[i][11])*0.567));	
			
			realCoMx[i][0]=(((x[i][0])+((x[i][1]-x[i][0]))*0.4));
			realCoMy[i][0]=600-((y[i][0]+(y[i][1]-y[i][0])*0.4));
			realCoMx[i][1]=(((x[i][2])+((x[i][3]-x[i][2]))*0.567));
			realCoMy[i][1]=600-((y[i][2]+(y[i][3]-y[i][2])*0.567));
			realCoMx[i][2]=(((x[i][4])+((x[i][5]-x[i][4]))*0.567));
			realCoMy[i][2]=600-((y[i][4]+(y[i][5]-y[i][4])*0.567));
			realCoMx[i][3]=(((x[i][7])+((x[i][8]-x[i][7]))*0.4));
			realCoMy[i][3]=600-((y[i][7]+(y[i][8]-y[i][7])*0.4));
			realCoMx[i][4]=(((x[i][9])+((x[i][10]-x[i][9]))*0.567));
			realCoMy[i][4]=600-((y[i][9]+(y[i][10]-y[i][9])*0.567));
			realCoMx[i][5]=(((x[i][11])+((x[i][12]-x[i][11]))*0.567));
			realCoMy[i][5]=600-((y[i][11]+(y[i][12]-y[i][11])*0.567));	
			
		}
		for (int i =0;i<stancecounttot;i++)
			System.out.print(stance[i]+" ");
		System.out.println();
		for (int i =0;i<rightcounttot;i++)
			System.out.print(rightswing[i]+" ");
		System.out.println();
		for (int i =0;i<leftcounttot;i++)
			System.out.print(leftswing[i]+" ");
		System.out.println();
	}
	
	public void update(Graphics g)
	{
		if(dbImage==null)
		{
			dbImage=createImage(1300, 700);
			dbGraphics = dbImage.getGraphics();
		}
		dbGraphics.setColor(this.getBackground());
		dbGraphics.fillRect(0,0,1300,700);
		dbGraphics.setColor(this.getForeground());
		paint(dbGraphics);
		g.drawImage(dbImage,0,0,this);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setColor(Color.blue);
		if(CurrentFrame==0)
			stancecount=leftcount=rightcount=0;
		if(CurrentFrame>stance[stancecount]&CurrentFrame<=rightswing[rightcount]&CurrentFrame<=leftswing[leftcount])
		{
			if(CurrentFrame==rightswing[rightcount]|CurrentFrame==leftswing[leftcount])
				stancecount++;
			g.setColor(Color.black);
		}
		if(CurrentFrame>rightswing[rightcount])
		{
			if(CurrentFrame==stance[stancecount])
				rightcount++;
			g.setColor(Color.green);
		}
		if(CurrentFrame>leftswing[leftcount])
		{
			if(CurrentFrame==stance[stancecount])
				leftcount++;
			g.setColor(Color.blue);
		}
		g.drawLine((((int)x[CurrentFrame][0])%1300),600-(int)y[CurrentFrame][0],(((int)x[CurrentFrame][1])%1300),600-(int)y[CurrentFrame][1]);
		g.drawLine((((int)x[CurrentFrame][2])%1300),600-(int)y[CurrentFrame][2],(((int)x[CurrentFrame][3])%1300),600-(int)y[CurrentFrame][3]);
		g.drawLine((((int)x[CurrentFrame][4])%1300),600-(int)y[CurrentFrame][4],(((int)x[CurrentFrame][5])%1300),600-(int)y[CurrentFrame][5]);
		g.drawLine((((int)x[CurrentFrame][7])%1300),600-(int)y[CurrentFrame][7],(((int)x[CurrentFrame][8])%1300),600-(int)y[CurrentFrame][8]);
		g.drawLine((((int)x[CurrentFrame][9])%1300),600-(int)y[CurrentFrame][9],(((int)x[CurrentFrame][10])%1300),600-(int)y[CurrentFrame][10]);
		g.drawLine((((int)x[CurrentFrame][11])%1300),600-(int)y[CurrentFrame][11],(((int)x[CurrentFrame][12])%1300),600-(int)y[CurrentFrame][12]);
		
		g.setColor(Color.red);
		g.fillOval((int)CoMx[CurrentFrame][0]-radius,(int)CoMy[CurrentFrame][0]-radius,radius*2,radius*2);
		g.fillOval((int)CoMx[CurrentFrame][1]-radius,(int)CoMy[CurrentFrame][1]-radius,radius*2,radius*2);
		g.fillOval((int)CoMx[CurrentFrame][2]-radius,(int)CoMy[CurrentFrame][2]-radius,radius*2,radius*2);
		g.fillOval((int)CoMx[CurrentFrame][3]-radius,(int)CoMy[CurrentFrame][3]-radius,radius*2,radius*2);
		g.fillOval((int)CoMx[CurrentFrame][4]-radius,(int)CoMy[CurrentFrame][4]-radius,radius*2,radius*2);
		g.fillOval((int)CoMx[CurrentFrame][5]-radius,(int)CoMy[CurrentFrame][5]-radius,radius*2,radius*2);
		tm.start();
	}
	public void actionPerformed(ActionEvent e)
	{
		CurrentFrame++;
		if(CurrentFrame<TotalFrames)
			repaint();
	}
}
