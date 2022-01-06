import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class Minesweeper extends JFrame
{
	MouseWatcher m;
	ImageIcon [] numbers;
	ImageIcon [][] under;
	ImageIcon blank;
	ImageIcon flag;
	ImageIcon nomine;
	ImageIcon smile1;
	ImageIcon smile2;
	ImageIcon smile3;
	ImageIcon smile4;
	ImageIcon boom;
	JLabel minesleft;
	JLabel time;
	JFrame myFrame;
	JButton [][] tiles;
	JButton reset;
	int [][] field;
	int gameover;
	int seconds;
	int nminesleft;
	int clears;
	int Lpress;
	int Rpress;
	int flagcheck;
	int start;
	int x;
	int y;
	int mines;
	JPanel options;
	JPanel grid;
	Timer timer;
	TimerTask task;
	NewGame replay;
	NewSize difficulty;
	JMenuBar bar;
	JMenu game;
	JMenuItem beginner;
	JMenuItem intermediate;
	JMenuItem expert;
	JMenuItem custom;
	WindowCloser done;
	BufferedReader br;

    public Minesweeper(int height,int width,int bombs)
    {
    	x=height;
    	y=width;
    	mines=bombs;
    	start=0;
    	bar=new JMenuBar();
    	game=new JMenu("Game");
    	beginner=new JMenuItem("Beginner (9X9 grid with 10 mines)");
    	intermediate=new JMenuItem("Intermediate (16X16 grid with 40 mines)");
    	expert=new JMenuItem("Expert (16X30 grid with 99 mines)");
    	custom=new JMenuItem("Custom");
    	difficulty=new NewSize();
    	beginner.addActionListener(difficulty);
    	intermediate.addActionListener(difficulty);
    	expert.addActionListener(difficulty);
    	custom.addActionListener(difficulty);
    	game.add(beginner);
    	game.add(intermediate);
    	game.add(expert);
    	game.add(custom);
    	bar.add(game);
    	gameover=0;
    	seconds=0;
    	clears=0;
    	Lpress=0;
    	Rpress=0;
    	flagcheck=0;
    	nminesleft=mines;
    	replay=new NewGame();
    	myFrame=new JFrame("Minesweeper");
    	field=new int[x][y];
    	minesleft=new JLabel(""+nminesleft, 0);
    	time=new JLabel("0", 0);
    	options=new JPanel();
    	grid=new JPanel();
    	grid.setLayout(new GridLayout(x,y));
    	options.setLayout(new GridLayout(1,3));
    	blank=new ImageIcon("blank_tile.jpg");
    	smile1=new ImageIcon("happy_face.jpg");
    	smile2=new ImageIcon("hold_face.jpg");
    	smile3=new ImageIcon("win_face.jpg");
    	smile4=new ImageIcon("lose_face.jpg");
    	flag=new ImageIcon("flagged_tile.jpg");
    	boom=new ImageIcon("mine_clicked.jpg");
    	nomine=new ImageIcon("wrong_flag.jpg");
		m=new MouseWatcher();
    	numbers=new ImageIcon [10];
    	under=new ImageIcon [x][y];
    	reset=new JButton(smile1);
    	reset.addActionListener(replay);
    	tiles=new JButton [x][y];
    	options.add(minesleft);
    	options.add(reset);
    	options.add(time);
		timer=new Timer();
		task=new TimerTask()
		{
		   	public void run()
		   	{
		   		seconds++;
		   		time.setText(""+seconds);
		   	}
		};
  	  	for(int i=0;i<10;i++)
    	{
    		numbers[i]=new ImageIcon(i+".jpg");
    	}
    	for(int i=0;i<x;i++)
    	{
    		for(int j=0;j<y;j++)
    		{
    			tiles[i][j]=new JButton(blank);
    			tiles[i][j].addMouseListener(m);
    			tiles[i][j].addMouseMotionListener(m);
    			field[i][j]=0;
    			grid.add(tiles[i][j]);
    		}
    	}
    	for(int i=0;i<mines;i++)
    	{
    		int n=(int)(Math.random()*(x));
    		int m=(int)(Math.random()*(y));
    		if(field[n][m]<9)
    		{
    			for(int j=-1;j<2;j++)
    			{
    				for(int k=-1;k<2;k++)
    				{
    					if(n+j>=0&&n+j<x&&m+k>=0&&m+k<y)
    					{
    						if(j!=0 || k!=0)
	    					{
	    						field[n+j][m+k]++;
	    					}
	    					else
	    					{
	    						field[n][m]+=9;
	    					}
    					}
    				}
    			}
    		}
    		else
    		{
    			i--;
    		}
    	}
    	for(int i=0;i<x;i++)
    	{
    		for(int j=0;j<y;j++)
    		{
    			if(field[i][j]<9)
    			{
    				under[i][j]=numbers[field[i][j]];
    			}
    			else
    			{
    				under[i][j]=numbers[9];
    			}
    		}
    	}
    	myFrame.setJMenuBar(bar);
    	myFrame.add(options, BorderLayout.NORTH);
    	myFrame.add(grid);
    	myFrame.setVisible(true);
    	myFrame.setSize(20*y,20*x+70);
    	grid.setSize(20*y,20*x);
    	myFrame.setResizable(false);
    	done=new WindowCloser();
    	myFrame.addWindowListener(done);
    }
	public class NewGame implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			myFrame.setVisible(false);
			new Minesweeper(x, y, mines);
		}
	}
	public class NewSize implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			JMenuItem temp=(JMenuItem)e.getSource();
			if(temp==beginner)
			{
				x=9;
				y=9;
				mines=10;
				myFrame.setVisible(false);
				new Minesweeper(9, 9, 10);
			}
			else if(temp==intermediate)
			{
				x=16;
				y=16;
				mines=40;
				myFrame.setVisible(false);
				new Minesweeper(16, 16, 40);
			}
			else if(temp==expert)
			{
				x=16;
				y=30;
				mines=99;
				myFrame.setVisible(false);
				new Minesweeper(16, 30, 99);
			}
			else if(temp==custom)
			{
				try
				{
					br=new BufferedReader(new InputStreamReader(System.in));
					x=Integer.parseInt(JOptionPane.showInputDialog("Height:"));
					y=Integer.parseInt(JOptionPane.showInputDialog("Width:"));
					mines=Integer.parseInt(JOptionPane.showInputDialog("Mines:"));
					myFrame.setVisible(false);
					if(x<0)
					{
						x=1;
					}
					if(y<8)
					{
						y=8;
					}
					if(mines<0)
					{
						mines=0;
					}
					if(x>99)
					{
						x=99;
					}
					if(y>99)
					{
						y=99;
					}
					if(mines>x*y-1)
					{
						mines=x*y-1;
					}
					new Minesweeper(x, y, mines);
				}
				catch(Exception f)
				{}
			}
		}
	}
	public void movemine (int i,int j, int a, int b)
	{
		//according to https://www.reddit.com/r/AskReddit/comments/djnck/minesweeper_debate_can_you_can_lose_on_the_first/
		//any mines that need to be moved will be moved to the top left and if there is already a mine there, it will go
		//to the first square without one
		int n;
	   	int m=0;
	   	int stop=0;
	   	for(n=0;stop==0;n++)
	   	{
	   		for(m=0;m<y&&stop==0;m++)
	   		{
	   			if(field[n][m]<9 && (n!=a||m!=b))
	   			{
	   				if(mines<=x*y-9)
	   				{
	   					if(n<a-1||n>a+1||m<b-1||m>b+1)
	   					{
	   						stop=1;
	   						n--;
	   						m--;
	   					}
	   				}
	   				else
	   				{
	   					stop=1;
	   					n--;
	   					m--;
	   				}
	   			}
	   		}
	   	}
    	for(int k=-1;k<2;k++)
		{
		  	for(int l=-1;l<2;l++)
		  	{
		  		if(i+k>=0&&i+k<x&&j+l>=0&&j+l<y)
		  		{
		  			if(k!=0 || l!=0)
		   			{
		   				field[i+k][j+l]--;
		   				if(field[i+k][j+l]<9)
		   				{
		   					under[i+k][j+l]=numbers[field[i+k][j+l]];
		   				}
		   			}
		   			else
		   			{
		   				field[i][j]-=9;
		   				under[i][j]=numbers[field[i][j]];
		   			}
		  		}
		  		if(n+k>=0&&n+k<x&&m+l>=0&&m+l<y)
		  		{
		  			if(k!=0 || l!=0)
		   			{
		   				field[n+k][m+l]++;
		   				if(field[n+k][m+l]<9)
		   				{
		   					under[n+k][m+l]=numbers[field[n+k][m+l]];
		   				}
		   			}
		   			else
		   			{
		   				field[n][m]+=9;
		   				under[n][m]=numbers[9];
		   			}
		  		}
			}
		 }
    }
    public void uncover (int i, int j)
    {
    	if(start==0)
	   	{
	   		timer.scheduleAtFixedRate(task,1000,1000);
	   		start++;
	   		if(field[i][j]>=9)
	   		{
	   			movemine(i,j, 0, 0);
	   		}
	   		if(field[i][j]!=0 && mines<=x*y-9)
	   		{
	   			for(int k=-1;k<2;k++)
	    		{
	    			for(int l=-1;l<2;l++)
	    			{
	    				if(i+k>=0&&i+k<x&&j+l>=0&&j+l<y)
	    				{
	    					if(k!=0 || l!=0)
		    				{
		    					if(field[i+k][j+l]>=9)
		    					{
		    						movemine(i+k,j+l, i, j);
		    					}
		    				}
	    				}
	    			}
	    		}
	   		}
	   	}
    	clears++;
    	tiles[i][j].setIcon(under[i][j]);
	   	if(field[i][j]>8)
	   	{
	   		tiles[i][j].setIcon(boom);
	   		field[i][j]=-1;
	   		gameover();
	   	}
	   	else if(field[i][j]==0)
	   	{
	   		for(int k=-1;k<2;k++)
    		{
    			for(int l=-1;l<2;l++)
    			{
    				if(i+k>=0&&i+k<x&&j+l>=0&&j+l<y)
    				{
    					if(k!=0 || l!=0)
	    				{
	    					if(tiles[i+k][j+l].getIcon()==blank)
	    					{
	    						uncover(i+k, j+l);
	    					}
	    				}
    				}
    			}
    		}
	   	}
	   	else if(clears==x*y-mines)
	   	{
	   		win();
	   	}
    }
    public void win()
    {
    	for(int i=0;i<x;i++)
   		{
   			for(int j=0;j<y;j++)
   			{
   				if(tiles[i][j].getIcon()==blank)
   				{
   					tiles[i][j].setIcon(flag);
   				}
   			}
   		}
   		reset.setIcon(smile3);
   		minesleft.setText("0");
   		timer.cancel();
   		gameover=1;
    }
    public void gameover()
    {
    	for(int i=0;i<x;i++)
   		{
   			for(int j=0;j<y;j++)
   			{
   				if(tiles[i][j].getIcon()==flag)
   				{
   					if(field[i][j]<9)
   					{
   						tiles[i][j].setIcon(nomine);
   					}
   				}
   				else if(field[i][j]>8)
   				{
   					tiles[i][j].setIcon(numbers[9]);
   				}
   			}
   		}
   		reset.setIcon(smile4);
   		timer.cancel();
   		gameover=1;
    }

    private class MouseWatcher implements MouseListener, MouseMotionListener
    {
    	public void mouseClicked(MouseEvent e)
    	{
    		if(gameover==0)
    		{
    			int i;
			    int j=0;
			   	int stop=0;
			   	JButton temp=(JButton)e.getSource();
			   	for(i=0;i<x && stop==0;i++)
			   	{
			   		for(j=0;j<y && stop==0;j++)
			   		{
			   			if(temp.equals(tiles[i][j]))
			   			{
			   				stop=1;
			   				i--;
			   				j--;
			   			}
			   		}
			   	}
			   	if(SwingUtilities.isLeftMouseButton(e))
			   	{
			   		if(tiles[i][j].getIcon()==blank)
			   		{
			   			uncover(i,j);
			   		}
			   	}
	    		else if(SwingUtilities.isRightMouseButton(e))
			   	{
			   		if(tiles[i][j].getIcon()==blank)
			   		{
			   			tiles[i][j].setIcon(flag);
			   			nminesleft--;
			   			minesleft.setText(""+nminesleft);
			   		}
			   		else if(tiles[i][j].getIcon()==flag)
			   		{
			   			tiles[i][j].setIcon(blank);
			   			nminesleft++;
			   			minesleft.setText(""+nminesleft);
			   		}
			   	}
    		}
    	}
    	public void mouseEntered(MouseEvent e)
    	{

    	}
    	public void mouseExited(MouseEvent e)
    	{

    	}
    	public void mousePressed(MouseEvent e)
    	{
    		if(gameover==0)
    		{
    			reset.setIcon(smile2);
    			if(SwingUtilities.isLeftMouseButton(e))
			   	{
			   		Lpress=1;
			   	}
			   	else if(SwingUtilities.isRightMouseButton(e))
			   	{
			   		Rpress=1;
			   	}
    		}
    	}
    	public void mouseReleased(MouseEvent e)
    	{
    		if(gameover==0)
    		{
    			reset.setIcon(smile1);
    			if(Lpress==1&&Rpress==1)
    			{
    				int i;
				    int j=0;
				   	int stop=0;
				   	JButton temp=(JButton)e.getSource();
				   	for(i=0;i<x && stop==0;i++)
				   	{
				   		for(j=0;j<y && stop==0;j++)
				   		{
				   			if(temp.equals(tiles[i][j]))
				   			{
				   				stop=1;
				   				i--;
				   				j--;
				   			}
				   		}
				   	}
				   	if(tiles[i][j].getIcon()!=blank&&tiles[i][j].getIcon()!=flag)
				   	{
				   		for(int k=-1;k<2;k++)
		    			{
		    				for(int l=-1;l<2;l++)
		    				{
		    					if(i+k>=0&&i+k<x&&j+l>=0&&j+l<y)
		    					{
		    						if((k!=0 || l!=0)&&tiles[i+k][j+l].getIcon()==flag)
			    					{
			    						flagcheck++;
			    					}
		    					}
		    				}
		    			}
		    			if(flagcheck==field[i][j])
		    			{
		    				for(int k=-1;k<2;k++)
			    			{
			    				for(int l=-1;l<2;l++)
			    				{
			    					if(i+k>=0&&i+k<x&&j+l>=0&&j+l<y)
			    					{
			    						if((k!=0 || l!=0)&&tiles[i+k][j+l].getIcon()==blank)
				    					{
				    						uncover(i+k,j+l);
				    					}
			    					}
			    				}
			    			}
		    			}
				   	}
    			}
    			Lpress=0;
    			Rpress=0;
    			flagcheck=0;
    		}
    	}
    	public void mouseDragged(MouseEvent e)
    	{

    	}
    	public void mouseMoved(MouseEvent e)
    	{

    	}
    }
    private class WindowCloser implements WindowListener
    {
    	public void windowActivated(WindowEvent e)
    	{}
    	public void windowClosed(WindowEvent e)
    	{}
    	public void windowClosing(WindowEvent e)
    	{
    		System.exit(0);
    	}
    	public void windowDeactivated(WindowEvent e)
    	{}
    	public void windowDeiconified(WindowEvent e)
    	{}
    	public void windowIconified(WindowEvent e)
    	{}
    	public void windowOpened(WindowEvent e)
    	{}
    }
    public static void main(String[] args) throws Exception
    {
    	new Minesweeper(9,9,10);
    }
}