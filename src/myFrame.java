/**
 * 
 */
package cn.edu.tsinghua.java_Course;

/**
 * @author huangyao
 *
 */
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import java.math.*;

import javax.swing.*;

import java.util.*;


public class myFrame extends JFrame {
	//color=0 player0:black;color=1 player1:black;
	Dialog myDialog;
	int color;
	//computer=0:人机对战 ；computer=1:人人对战
	boolean computer;
	//chessBoard[x][y]=2:没有棋子;chessBoard[x][y]=0:player0;chessBoard[x][y]=1
	//:player2
	//int[][] chessBoard=new int[15][15];
	int state;
	int lastState;
	chessBoard Board;
	Vector<Point> Data;
	//Level=1  初级   Level=2 中级 Level=3 高级
	int Level;
	//胜率记录
	int WIN_LOW,LOSE_LOW;
	int WIN_MEDIUM,LOSE_MEDIUM;
	int WIN_HIGH,LOSE_HIGH;
	//File f=new File("RankData.txt");
	//BufferedWriter bw1=new BufferedWriter(new FileWriter(f));
	
	
	//MENU菜单构建
	MenuBar menuBar = new MenuBar();
	Menu menuGame=new Menu("游戏");
	Menu menuLevel=new Menu("难度等级");
	Menu menuStart=new Menu("开始游戏");
	//Menu item
	MenuItem menuItem_PAUSE=new MenuItem("暂停游戏");
	MenuItem menuItem_CONTINUE=new MenuItem("继续游戏");
	MenuItem menuItem_RESTART=new MenuItem("重新开始");
	MenuItem menuItem_UNDO=new MenuItem("悔棋");
	MenuItem menuItem_EXIT=new MenuItem("退出");
	//
	MenuItem menuItem_FIRST=new MenuItem("先手（黑）");
	MenuItem menuItem_LAST=new MenuItem("后手（白）");
	//
	MenuItem menuItem_LOW=new MenuItem("初级");
	MenuItem menuItem_MEDIUM=new MenuItem("中级");
	MenuItem menuItem_HIGH=new MenuItem("高级");
	//Add Save Menu
	Menu menuFile=new Menu("文件");
	MenuItem menuItem_SAVE=new MenuItem("保存");
	MenuItem menuItem_OPEN=new MenuItem("导入");
	MenuItem menuItem_OPEN_RESTART=new MenuItem("继续上次");
	//Add Rank Menu
	Menu menuRank=new Menu("成绩排名");
	MenuItem menuItem_LOW_RANK=new MenuItem("初级胜率");
	MenuItem menuItem_MEDIUM_RANK=new MenuItem("中级胜率");
	MenuItem menuItem_HIGH_RANK=new MenuItem("高级胜率");
	//Add Help Menu
	Menu menuHelp=new Menu("帮助");
	MenuItem menuItem_INTRODUCE=new MenuItem("简介");
	MenuItem menuItem_RULE=new MenuItem("规则");
	MenuItem menuItem_FUNCTION=new MenuItem("功能");
	
	public void init(){
		color=0;
		state=2;
		Level=2;
		computer=true;
		int[][] t=new int[15][15];
		for(int i=0;i<15;i++)
			for(int j=0;j<15;j++)
				t[i][j]=2;
		Board=new chessBoard(t,color);
		Data=new Vector<Point>();
		
		//排名隶属数据的读取
		FileReader fr;
		try{
			fr=new FileReader("Rank_Data.txt");
			int[] win=new int[20];
			String s;
			char ch;
			int count=0;
			BufferedReader bw = new BufferedReader(fr);
			while((s=bw.readLine())!=null){
					win[count++]=Integer.parseInt(s);
			}
			fr.close();
			WIN_LOW=win[0];
			LOSE_LOW=win[1];
			WIN_MEDIUM=win[2];
			LOSE_MEDIUM=win[3];
			WIN_HIGH=win[4];
			LOSE_HIGH=win[5];
			   
		}catch(Exception exc){
			exc.printStackTrace();
		}
	}
	public myFrame(){
		super("Five-Chess");
		setSize(540,560);
		init();
		//菜单栏设置
		setMenuBar(menuBar);
		menuBar.add(menuGame);
		menuBar.add(menuLevel);
		menuGame.add(menuStart);
		menuStart.add(menuItem_FIRST);
		menuStart.add(menuItem_LAST);
		menuGame.add(menuItem_PAUSE);
		menuGame.add(menuItem_CONTINUE);
		menuGame.add(menuItem_RESTART);
		menuGame.add(menuItem_UNDO);
		menuGame.add(menuItem_EXIT);
		//
		menuLevel.add(menuItem_LOW);
		menuLevel.add(menuItem_MEDIUM);
		menuLevel.add(menuItem_HIGH);
		//add file
		menuBar.add(menuFile);
		menuFile.add(menuItem_SAVE);
		menuFile.add(menuItem_OPEN);
		menuFile.add(menuItem_OPEN_RESTART);
		//add rank
		menuBar.add(menuRank);
		menuRank.add(menuItem_LOW_RANK);
		menuRank.add(menuItem_MEDIUM_RANK);
		menuRank.add(menuItem_HIGH_RANK);
		//add help
		menuBar.add(menuHelp);
		menuHelp.add(menuItem_INTRODUCE);
		menuHelp.add(menuItem_RULE);
		menuHelp.add(menuItem_FUNCTION);
		//菜单栏监听
		menuItem_FIRST.addActionListener(new ack_menuItem_FIRST());
		menuItem_LAST.addActionListener(new ack_menuItem_LAST());
		menuItem_RESTART.addActionListener(new ack_menuItem_RESTART());
		menuItem_PAUSE.addActionListener(new ack_menuItem_PAUSE());
		menuItem_CONTINUE.addActionListener(new ack_menuItem_CONTINUE());
		menuItem_EXIT.addActionListener(new ack_menuItem_EXIT());
		menuItem_LOW.addActionListener(new ack_menuItem_LOW());
		menuItem_MEDIUM.addActionListener(new ack_menuItem_MEDIUM());
		menuItem_HIGH.addActionListener(new ack_menuItem_HIGH());
		menuItem_UNDO.addActionListener(new ack_menuItem_UNDO());
		//
		menuItem_LOW_RANK.addActionListener(new ack_menuItem_LOW_RANK());
		menuItem_MEDIUM_RANK.addActionListener(new ack_menuItem_MEDIUM_RANK());
		menuItem_HIGH_RANK.addActionListener(new ack_menuItem_HIGH_RANK());
		//
		menuItem_SAVE.addActionListener(new ack_menuItem_SAVE());
		menuItem_OPEN.addActionListener(new ack_menuItem_OPEN());
		menuItem_OPEN_RESTART.addActionListener(new ack_menuItem_OPEN_RESTART());
		//frame监听
		menuItem_PAUSE.setEnabled(false);
		menuItem_CONTINUE.setEnabled(false);
		menuItem_UNDO.setEnabled(false);
		//12-24
		menuItem_INTRODUCE.addActionListener(new ack_menuItem_INTRODUCE());
		menuItem_RULE.addActionListener(new ack_menuItem_RULE());
		menuItem_FUNCTION.addActionListener(new ack_menuItem_FUNCTION());
		
		addMouseListener(new chessMouse());
		
		//window
		addWindowListener(new windowClose());
		
		setVisible(true);
		//
		//java.net.URL file1=getClass().getResource("thu.mp3");
	    //AudioClip sound1=java.applet.Applet.newAudioClip(file1);
	    //sound1.play();
	}
	public void paint(Graphics g){
		g.setColor(new Color(240,120,20));
		g.fillRect(30,60,460,460);
		g.setColor(Color.darkGray);
		//绘制棋盘网格 
		for(int i=0;i<15;i++){
			g.drawLine(50,80+i*30,470,80+i*30);
			g.drawLine(50+30*i, 80, 50+30*i, 500);
		}
		g.setColor(Color.black);
        g.fillOval(50+3*30-4, 80+3*30-4, 8, 8);
        g.fillOval(50+11*30-4, 80+3*30-4, 8, 8);
        g.fillOval(50+3*30-4, 80+11*30-4, 8, 8);
        g.fillOval(50+11*30-4, 80+11*30-4, 8, 8);
        g.fillOval(50+7*30-4, 80+7*30-4, 8, 8);	
        
        //为悔棋而设计
        
        Point p;
        int Size = Data.size();                                                        //绘制棋子
        for (int i=0; i<Size; i++) {
            p = (Point)Data.elementAt(i);
            if (p.getColor() == 0) {
                g.setColor(new Color(20, 20, 20));
            } else {
                g.setColor(Color.white);
            }
            g.fillOval(50 + 30 * p.getX() - 10, 80 + 30 * p.getY() - 10, 20, 20);
        }
        
	}
	//menuItem_FIRST.addActionListener(new ack_menuItem_FIRST());
	//menuItem_LAST.addActionListener(new ack_menuItem_LAST());
	//frame监听
	//addMouseListener(new chessMouse());

/*
class Point implements Serializable{
	int x;
	int y;
		int color;
		Point(int x,int y,int color){
			this.x=x;
			this.y=y;
			this.color=color;
		}
		int getX(){
			return x;
		}
		int getY(){
			return y;
		}
		int getColor(){
			return color;
		}	
	}
*/

//监听器
class ack_menuItem_FIRST implements ActionListener{
	public void actionPerformed(ActionEvent e){
		Data.clear();
		//repaint();
		state=0;
		color=0;
		menuItem_PAUSE.setEnabled(true);
		menuItem_UNDO.setEnabled(true);
		//12-10
		menuItem_LOW_RANK.setEnabled(false);
		menuItem_MEDIUM_RANK.setEnabled(false);
		menuItem_HIGH_RANK.setEnabled(false);
		menuItem_LAST.setEnabled(false);
		menuItem_FIRST.setEnabled(false);
		//
		menuItem_LOW.setEnabled(false);
		menuItem_MEDIUM.setEnabled(false);
		menuItem_HIGH.setEnabled(false);
	}
}
class ack_menuItem_LAST implements ActionListener{
	public void actionPerformed(ActionEvent e){
		Data.clear();
		Data.add(new Point(7,7,0));
		//repaint();
		state=0;
		color=1;
		menuItem_PAUSE.setEnabled(true);
		menuItem_UNDO.setEnabled(true);
		System.out.print("ack_LAST: color= "+color);
		Board.chess_Board[7][7]=0;
		Graphics g=myFrame.this.getGraphics();
		g.setColor(new Color(20,20,20));
		g.fillOval(50+30*7-10,80+30*7-10,20,20);
		//System.out.print("blackblack");
		//setVisible(true);
		menuItem_LOW_RANK.setEnabled(false);
		menuItem_MEDIUM_RANK.setEnabled(false);
		menuItem_HIGH_RANK.setEnabled(false);
		menuItem_FIRST.setEnabled(false);
		menuItem_LAST.setEnabled(false);
		//
		menuItem_LOW.setEnabled(false);
		menuItem_MEDIUM.setEnabled(false);
		menuItem_HIGH.setEnabled(false);
	}
}
class ack_menuItem_RESTART implements ActionListener{
	public void actionPerformed(ActionEvent e){
		Data.clear();
		init();
		menuItem_LOW.setEnabled(true);
		menuItem_MEDIUM.setEnabled(true);
		menuItem_HIGH.setEnabled(true);
		menuItem_FIRST.setEnabled(true);
		menuItem_LAST.setEnabled(true);
		repaint();
	}
}
class ack_menuItem_PAUSE implements ActionListener{
	public void actionPerformed(ActionEvent e){
		lastState=state;
		state=2;
		menuItem_CONTINUE.setEnabled(true);
	}
}
//继续游戏
class ack_menuItem_CONTINUE implements ActionListener{
	public void actionPerformed(ActionEvent e){
		state=lastState;
		/*int size=Data.size();
		Point p;
		int num0,num1;
		num0=num1=0;
		for (int i=0;i<size;i++){
			p=Data.elementAt(i);
			if(p.getColor()==color)
				num0++;//player0
			else if(p.getColor()==1-color) //player1
				num1++;
		}
		if(num0>num1){
			state=1;
		}
		if(num1>num0){
			state=0;
		}
		if(num1==num0){
			state=color;
		}*/
	}
}
/*
 * 退出事件
 */
class ack_menuItem_EXIT implements ActionListener{
	public void actionPerformed(ActionEvent e){
		/*
		 try{
		 
			FileOutputStream file=new FileOutputStream("five_chess.c5db");
			ObjectOutputStream out=new ObjectOutputStream(file);
			try{
				out.writeObject(Data);
			}finally{
				out.close();
			}
		}catch(IOException ex){
			//ex.printStackTrace();
			 fLogger.log(Level.WARNING, "Failed to perform output when exiting.", ex);
		}
		*/
		System.exit(0);
	}
}

//难度等级监听
class ack_menuItem_LOW implements ActionListener{
	public void actionPerformed(ActionEvent e){
		Level=1;
		menuItem_LOW.setEnabled(false);
		//menuItem_MEDIUM.setEnabled(false);
		//menuItem_HIGH.setEnabled(false);
	}	
}
class ack_menuItem_MEDIUM implements ActionListener{
	public void actionPerformed(ActionEvent e){
		Level=2;
		//menuItem_LOW.setEnabled(false);
		menuItem_MEDIUM.setEnabled(false);
		//menuItem_HIGH.setEnabled(false);
	}
}
class ack_menuItem_HIGH implements ActionListener{
	public void actionPerformed(ActionEvent e){
		Level=3;
		//menuItem_LOW.setEnabled(false);
		//menuItem_MEDIUM.setEnabled(false);
		menuItem_HIGH.setEnabled(false);
	}
}
//悔棋
class ack_menuItem_UNDO implements ActionListener{
	public void actionPerformed(ActionEvent e){
		//repaint();
		Graphics g=myFrame.this.getGraphics();
		
		int size=Data.size();
		if(size<2)
			return;
		Point p1,p2;
		p1=Data.elementAt(size-1);
		p2=Data.elementAt(size-2);
		Board.chess_Board[p1.getX()][p1.getY()]=2;
		Board.chess_Board[p2.getX()][p2.getY()]=2;
		Data.remove(size-1);
		Data.remove(size-2);
		System.out.print("undo size :"+size);
		/*
		for(int i=0;i<size-2;i++){
			Point p=(Point)Data.elementAt(i);
			if(p.getColor()==0){
	        	g.setColor(new Color(20,20,20));
	        }
	        else{
	        	g.setColor(Color.WHITE);
	        }
			/*
			if(p.getColor()==0){
				g.setColor(new Color(20,20,20));
			}
			else if(p.getColor()==1){
				g.setColor(Color.WHITE);
			}
			int x=p.getX();
			int y=p.getY();
			System.out.print("(x,y)="+x+","+y+","+p.getColor()+"\n");
			g.fillOval(50+30*x-10,80+30*y-10,20,20);
			System.out.print("xxxxx");
		} */
		//setVisible(true);
		//state=0;
		repaint();
	}
}
//成绩排名计算监听器
//初级成绩
class ack_menuItem_LOW_RANK implements ActionListener{
	public void actionPerformed(ActionEvent e){
		FileReader fr;
		try{
			//File f=new File("Rank_Data.txt");
			fr=new FileReader("Rank_Data.txt");
			int[] win=new int[20];
			String s;
			char ch;
			int count=0;
			//System.out.print(fr.read());
			BufferedReader bw = new BufferedReader(fr);
			while((s=bw.readLine())!=null){
					win[count++]=Integer.parseInt(s);
					//System.out.print(+" ");
			}
			fr.close();
			for(int j=0;j<6;j++)
			   System.out.print(win[j]+"  ");
			float reslut=.2f;
			reslut=100*((float)win[0]/(win[1]+win[0]));
			BigDecimal b = new BigDecimal(reslut); 
			float f = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue(); 
			System.out.print("初级水平胜率为： "+f+"%"+"\n");
			//System.out.print("the rank data are as follows: "+result);
			myDialog=new Dialog(myFrame.this,"RANK");
        	myDialog.setSize(200,100);
        	myDialog.setLayout(new FlowLayout(FlowLayout.CENTER,1000,10));
        	String str1="初级水平胜率为： "+f+"%";
        	myDialog.add(new Label(str1));
        	Button myDialogBotton = new Button("确定");
        	myDialog.add(myDialogBotton);
        	myDialog.setResizable(false);
        	myDialog.setVisible(true);
        	 myDialogBotton.addActionListener(new ActionListener() {
                 public void actionPerformed(ActionEvent e) {
                     myFrame.this.setEnabled(true);
                     myFrame.this.myDialog.dispose();
                 }
             });
			
		}catch(Exception exc){
			exc.printStackTrace();
		}
		
	}
	
}
class ack_menuItem_MEDIUM_RANK implements ActionListener{
	public void actionPerformed(ActionEvent e){
		FileReader fr;
		try{
			fr=new FileReader("Rank_Data.txt");
			int[] win=new int[20];
			String s;
			char ch;
			int count=0;
			BufferedReader bw = new BufferedReader(fr);
			while((s=bw.readLine())!=null){
					win[count++]=Integer.parseInt(s);
			}
			fr.close();
			for(int j=0;j<6;j++)
			   System.out.print(win[j]+"  ");
			float result=.2f;
			result=100*((float)win[2]/(win[2]+win[3]));
			BigDecimal b = new BigDecimal(result); 
			float f = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue(); 
			System.out.print("中级水平胜率为： "+f+"%"+"\n");
			//12-24
			myDialog=new Dialog(myFrame.this,"RANK");
        	myDialog.setSize(200,100);
        	myDialog.setLayout(new FlowLayout(FlowLayout.CENTER,1000,10));
        	String str1="中级水平胜率为： "+f+"%";
        	myDialog.add(new Label(str1));
        	Button myDialogBotton = new Button("确定");
        	myDialog.add(myDialogBotton);
        	myDialog.setResizable(false);
        	myDialog.setVisible(true);
        	 myDialogBotton.addActionListener(new ActionListener() {
                 public void actionPerformed(ActionEvent e) {
                     myFrame.this.setEnabled(true);
                     myFrame.this.myDialog.dispose();
                 }
             });
        	
		}catch(Exception exc){
			exc.printStackTrace();
		}
		
	}
	
}
class ack_menuItem_HIGH_RANK implements ActionListener{
	public void actionPerformed(ActionEvent e){
		FileReader fr;
		try{
			fr=new FileReader("Rank_Data.txt");
			int[] win=new int[20];
			String s;
			char ch;
			int count=0;
			BufferedReader bw = new BufferedReader(fr);
			while((s=bw.readLine())!=null){
					win[count++]=Integer.parseInt(s);
			}
			fr.close();
			for(int j=0;j<6;j++)
			   System.out.print(win[j]+"  ");
			float reslut=0.2f;
			reslut=100*((float)win[4]/(win[4]+win[5]));
			BigDecimal b = new BigDecimal(reslut); 
			float f = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue(); 
			System.out.print("高级水平胜率为： "+f+"%"+"\n");
			myDialog=new Dialog(myFrame.this,"RANK");
        	myDialog.setSize(200,100);
        	myDialog.setLayout(new FlowLayout(FlowLayout.CENTER,1000,10));
        	String str1="高级水平胜率为： "+f+"%";
        	myDialog.add(new Label(str1));
        	Button myDialogBotton = new Button("确定");
        	myDialog.add(myDialogBotton);
        	myDialog.setResizable(false);
        	myDialog.setVisible(true);
        	 myDialogBotton.addActionListener(new ActionListener() {
                 public void actionPerformed(ActionEvent e) {
                     myFrame.this.setEnabled(true);
                     myFrame.this.myDialog.dispose();
                 }
             });
			
		}catch(Exception exc){
			exc.printStackTrace();
		}
		
	}
	
}


//Save 2013-12-13
class ack_menuItem_SAVE implements ActionListener{
	public void actionPerformed(ActionEvent e){
		try{
			FileOutputStream file=new FileOutputStream("hy.data");
			ObjectOutputStream output=new ObjectOutputStream(file);
			//Object obj=null;
			for(int i=0;i<Data.size();i++){
				output.writeObject(Data.elementAt(i));
			}
			output.close();
			//color,level写入
			File f=new File("hy.txt");
			FileWriter fw=new FileWriter(f);
			
			fw.write(((Integer)color).toString());
    		fw.write("\r\n");
    		fw.write(((Integer)Level).toString());
    		fw.close();
			
		}catch(Exception exc){
			
		}
	}
}
class ack_menuItem_OPEN implements ActionListener{
	public void actionPerformed(ActionEvent e){
		try{
			FileInputStream file=new FileInputStream("hy.data");
			ObjectInputStream input=new ObjectInputStream(file);
			Object obj=null;
			int i=0;
			Data.clear();
			try{
			   while((obj=input.readObject())!=null){
				  Point p=(Point)obj;
				  Data.add(i,p);
				  Board.chess_Board[p.getX()][p.getY()]=p.getColor();
				  //System.out.print("Data[i]");
				  i++;
			   }
			}catch(EOFException exc){
				System.out.print("END!");
			}
			input.close();
			repaint();
			System.out.print("打开保存的游戏文件，请继续!");
		}catch(Exception exc){
			exc.printStackTrace();
		}
	}
}
class ack_menuItem_OPEN_RESTART implements ActionListener{
	public void actionPerformed(ActionEvent e){
		FileReader fr;
		try{
		fr=new FileReader("hy.txt");
		BufferedReader bw = new BufferedReader(fr);
		String s;
		int[] hy=new int[10];
		int count=0;
		while((s=bw.readLine())!=null){
				hy[count++]=Integer.parseInt(s);
		}
		fr.close();
		for(int j=0;j<10;j++){
			System.out.print("hy[i]="+hy[j]+" ");
		}
		state=0;
		color=hy[0];
		Level=hy[1];
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
}
class ack_menuItem_INTRODUCE implements ActionListener{
	public void actionPerformed(ActionEvent e){
		try{
		System.out.print("docx open");
		
		//Runtime.getRuntime().exec("cmd /c \"H:\\thu.doc\"");
		Runtime.getRuntime().exec("notepad H:\\大四上\\Java_Programming\\Java_workspace\\Java_Final_Project\\introduce.txt");
		//String[] cmdarray = new String[] {"cmd.exe", "/c", "H:\\大四上\\Java_Programming\\Java_workspace\\Java_Final_Project\\user.doc"};
		//Process p = Runtime.getRuntime().exec(cmdarray);
		}catch(Exception exc){
			exc.printStackTrace();
		}
	   }
}
class ack_menuItem_RULE implements ActionListener{
	public void actionPerformed(ActionEvent e){
		try{
		Runtime.getRuntime().exec("notepad H:\\大四上\\Java_Programming\\Java_workspace\\Java_Final_Project\\rule.txt");
		}catch(Exception exc){
			exc.printStackTrace();
		}
	   }
}
//menuItem_FUNCTION
class ack_menuItem_FUNCTION implements ActionListener{
	public void actionPerformed(ActionEvent e){
		try{
		Runtime.getRuntime().exec("notepad H:\\大四上\\Java_Programming\\Java_workspace\\Java_Final_Project\\function.txt");
		}catch(Exception exc){
			exc.printStackTrace();
		}
	   }
}
class chessMouse extends MouseAdapter{
	public void mouseClicked(MouseEvent e){
		int x,y;
		x=e.getX();
		y=e.getY();
		x = (x - 35)/30;
        y = (y - 65)/30;
        //System.out.print("("+x+","+y+")");
        if((x<0)||(x>15)||(y<0)||(y>15))
        	return;
        if(Board.chess_Board[x][y]==color || Board.chess_Board[x][y]==1-color)
        	return;
        Graphics g=myFrame.this.getGraphics();
        if(color==0){
        	g.setColor(new Color(20,20,20));
        }
        else{
        	g.setColor(Color.WHITE);
        }
        if (state==0){
            Board.chess_Board[x][y]=color;
            Point p=new Point(x,y,color);
            Data.add(p);
            g.fillOval(50+30*x-10,80+30*y-10,20,20);
            state=1;
            //repaint();
        }
        /*
        for(int i=0;i<15;i++)
        	for(int j=0;j<15;j++)
        		if(Board.chess_Board[i][j]!=2)
        		     System.out.print("Board["+i+"]["+j+"]="+Board.chess_Board[i][j]);
        System.out.print("\n");
        */
        
        if(Board.hasFive(color)){
        	System.out.print("Win!! color= "+color);
        	myDialog=new Dialog(myFrame.this,"END");
        	myDialog.setSize(200,100);
        	myDialog.setLayout(new FlowLayout(FlowLayout.CENTER,1000,10));
        	myDialog.add(new Label("YOU WIN!!!"));
        	//2013-12-10
        	if(Level==1){
        		WIN_LOW++;
        	}
        	else if(Level==2){
        		WIN_MEDIUM++;
        	}
        	else if(Level==3){
        		WIN_HIGH++;
        	}
        	/*
        	try{
        		BufferedWriter bw=new BufferedWriter(new FileWriter(f));
        		bw.write(((Integer)WIN_LOW).toString());
        		//bw.write("\n");
        		bw.write(((Integer)LOSE_LOW).toString());
        		//bw.write("\n");
        		bw.write(((Integer)WIN_MEDIUM).toString());
        		//bw.write("\n");
        		bw.write(((Integer)LOSE_MEDIUM).toString());
        		//bw.write("\n");
        		bw.write(((Integer)WIN_HIGH).toString());
        		//bw.write("\n");
        		bw.write(((Integer)LOSE_HIGH).toString());
        		System.out.print("rangkDATA HAS SAVES");
        	}catch(Exception exc){
        		exc.printStackTrace();
        	}
        	*/
        	//
        	state=0;
        	Button myDialogBotton = new Button("确定");
        	myDialog.add(myDialogBotton);
        	myDialog.setResizable(false);
        	myDialog.setVisible(true);
        	myDialogBotton.addActionListener(new ActionListener() {
                 public void actionPerformed(ActionEvent e) {
                     myFrame.this.setEnabled(true);
                     myFrame.this.myDialog.dispose();
                 }
             });
        
        }
        if(computer){
        	int[] t=new int[2];
        	if(0==color)
        	   t=Board.putOne(1-color,Level);
        	else if(1==color)
        		t=Board.putOne(color,Level);
        	System.out.print("the computer x,y:"+t[0]+","+t[1]+"\n");
        	if(state==1){
        		if(color==0)
        	        g.setColor(Color.WHITE);
        		else if(color==1)
        			g.setColor(new Color(20,20,20));
        	    Board.chess_Board[t[0]][t[1]]=1-color;
        	    
        	    g.fillOval(50+30*t[0]-10,80+30*t[1]-10,20,20);
        	    Point p2=new Point(t[0],t[1],1-color);
        	    Data.add(p2);
        	    //repaint();
        	    state=0;
        	    if(Board.hasFive(1-color)){
        	    	System.out.print("computer wins!\n");
        	    	myDialog=new Dialog(myFrame.this,"END");
                	myDialog.setSize(200,100);
                	myDialog.setLayout(new FlowLayout(FlowLayout.CENTER,1000,10));
                	myDialog.add(new Label("Computer WIN!!!"));
                	//12-10
                    if(Level==1){
                    	LOSE_LOW++;
                    }
                    else if(Level==2){
                    	LOSE_MEDIUM++;
                    }
                    else if(Level==3){
                    	LOSE_HIGH++;
                    }
                    //12-10
                    /*
                    try{
                		BufferedWriter bw=new BufferedWriter(new FileWriter(f));
                		bw.write(((Integer)WIN_LOW).toString());
                		//bw.write("\n");
                		bw.write(((Integer)LOSE_LOW).toString());
                		//bw.write("\n");
                		bw.write(((Integer)WIN_MEDIUM).toString());
                		//bw.write("\n");
                		bw.write(((Integer)LOSE_MEDIUM).toString());
                		//bw.write("\n");
                		bw.write(((Integer)WIN_HIGH).toString());
                		//bw.write("\n");
                		bw.write(((Integer)LOSE_HIGH).toString());
                		System.out.print("rankData has saves");
                		
                	}catch(Exception exc){
                		exc.printStackTrace();
                	}
                    */
                    System.out.print("LOSE_LOW "+LOSE_LOW);
                	state=1;
                	Button myDialogBotton = new Button("确定");
                	myDialog.add(myDialogBotton);
                	myDialog.setResizable(false);
                	myDialog.setVisible(true);
                	 myDialogBotton.addActionListener(new ActionListener() {
                         public void actionPerformed(ActionEvent e) {
                             myFrame.this.setEnabled(true);
                             myFrame.this.myDialog.dispose();
                         }
                     });
        	    }
        	    //state=0;
        	}
        	
        }
        
      
	}
}
//计算胜率

//关闭窗口的一些工作 
class windowClose extends WindowAdapter{
	public void windowClosing(WindowEvent e){
		try{
			
			FileOutputStream file=new FileOutputStream("Five_chess.data");
			ObjectOutputStream output=new ObjectOutputStream(file);
			for(int i=0;i<Data.size();i++){
					Point p=Data.get(i);
					output.writeObject(p);
				    //output.writeObject(Data);
				}
			output.close();
			//RANK DATA SAVE
			File f=new File("Rank_Data.txt");
			FileWriter fw=new FileWriter(f);
			
			fw.write(((Integer)WIN_LOW).toString());
    		fw.write("\r\n");
    		fw.write(((Integer)LOSE_LOW).toString());
    		fw.write("\r\n");
    		fw.write(((Integer)WIN_MEDIUM).toString());
    		fw.write("\r\n");
    		fw.write(((Integer)LOSE_MEDIUM).toString());
    		fw.write("\r\n");
    		fw.write(((Integer)WIN_HIGH).toString());
    		fw.write("\r\n");
    		fw.write(((Integer)LOSE_HIGH).toString());
    		fw.close();
    		System.out.print("rankData has saves");
			
		}catch(IOException exc){
			exc.printStackTrace();
		}
		System.exit(0);
	}
}

private static final Logger fLogger =
    Logger.getLogger(myFrame.class.getPackage().getName());

}




