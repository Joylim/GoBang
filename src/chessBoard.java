/**
 * 
 */
package cn.edu.tsinghua.java_Course;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;
//import java.io.Serializable;

/**
 * @author huangyao
 *
 */
public class chessBoard {
	int[][] chess_Board=new int[15][15];
	//记录数据
	//Vector<Point> data;
	int startColor;
	
	public chessBoard(int[][] chess_Board,int startColor){
		this.startColor=startColor;
		for(int i=0;i<15;i++)
			for(int j=0;j<15;j++)
				this.chess_Board[i][j]=chess_Board[i][j];
	}
	//point 
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
	}*/
    //统计特定方向上特定棋子相连的数目
	public int[] count(int x,int y, int ex,int ey,int color){
		int rt=1;
		int i;
		if (chess_Board[x][y]!=2)
			throw new IllegalArgumentException("Poition must be empty!");
		for(i=1;x+i*ex<15 && x+i*ex>=0 && y+i*ey<15 && y+i*ey>=0;i++){
			if(chess_Board[x+i*ex][y+i*ey]==color)
				rt++;
			else
				break;
		}
		int ok=0;
		if(x+i*ex<15 && x+i*ex>=0 && y+i*ey<15 && y+i*ey>=0 && chess_Board[x+i*ex][y+i*ey]==2)
			ok++;
	
		for(i=1;x-i*ex>=0 && x-i*ex<15 && y-i*ey>=0 && y-i*ey<15;i++){
			if(chess_Board[x-i*ex][y-i*ey]==color)
				rt++;
			else
				break;
		}
		if(x-i*ex<15 && x-i*ex>=0 && y-i*ey<15 && y-i*ey>=0 && chess_Board[x-i*ex][y-i*ey]==2)
			ok++;
		
		if(rt<5 && !makeSence(x,y,ex,ey,color))
			return new int[] {0,1};
		
		return new int[] {rt,ok};
	}
	public boolean makeSence(int x,int y,int ex,int ey,int color){
		int rt=1;
		//统计可放棋子数目
		for(int i=1;x+i*ex<15 && x+i*ex>=0 && y+i*ey<15 && y+i*ey>=0 && rt<15;i++){
			if(chess_Board[x+i*ex][y+i*ey]!=1-color)
				rt++;
			else
				break;
		}
		for(int i=1;x-i*ex<15 && x-i*ex>=0 && y-i*ey<15 && y-i*ey>=0 && rt<15;i++){
			if(chess_Board[x-i*ex][y-i*ey]!=1-color)
				rt++;
			else
				break;
		}
		return (rt>5);	
	}
	/**
	 * 得到特定落子的棋型
	 * @param x
	 * @param y
	 * @param color
	 * @return
	 * 1：成5
	 * 2：成活4或双死4或死四活3
	 * 3：成双活3
	 * 4：成死3活3
	 * 5：成死4
	 * 6：单活3
	 * 7：成双活2
	 * 8：成死3
	 * 9：成死2活2
	 * 10：成活2
	 * 11：成死2
	 * 0:其他
	 * 
	 */
	public int getType(int x,int y,int color){
		if(chess_Board[x][y]!=2){
			return -1;
		}
		int[][] types=new int[4][2];
		types[0]=count(x,y,0,1,color);
		types[1]=count(x,y,1,0,color);
		types[2]=count(x,y,-1,1,color);
		types[3]=count(x,y,1,1,color);
		int c5,s4,h4,s3,h3,s2,h2;
		c5=s4=h4=s3=h3=s2=h2=0;
		for(int i=0;i<4;i++){
			if(types[i][0]==5){
				c5++;
			}
			else if(types[i][0]==4 && types[i][1]==2){
				h4++;
			}
			else if(types[i][0]==4 && types[i][1]!=2){
				s4++;
			}
			else if(types[i][0]==3 && types[i][1]==2){
				h3++;
			}
			else if(types[i][0]==3 && types[i][1]!=2){
				s3++;
			}
			else if(types[i][0]==2 && types[i][1]==2){
				h2++;
			}
			else if(types[i][0]==2 && types[i][1]!=2){
				s2++;
			}
		}
		if(c5!=0)
			return 1;
		if(h4!=0)
			return 2;
		if(h3>=2)
			return 3; //双活3
		if(s3!=0 && h3!=0)
			return 4;
		if(s4!=0)
			return 5;
		if(h3!=0)
			return 6;
		if(h2>=2)
			return 7;
		if(s3!=0)
			return 8;
		if(h2!=0 && s2!=0)
			return 9;
		if(h2!=0)
			return 10;
		if(s2!=0)
			return 11;
		return 0;
	}
    private int getMark(int k){
    	switch(k){
    	case 1:
    		return 100000;
    	case 2:
    		return 10000;
    	case 3:
    		return 5000;
    	case 4:
    		return 1000;
    	case 5:
    		return 500;
    	case 6:
    		return 200;
    	case 7:
    		return 100;
    	case 8:
    		return 50;
    	case 9:
    		return 10;
    	case 10:
    		return 5;
    	case 11:
    		return 3;
    	default:
    		return 0;
    	}
    	
    }
    //评估函数 
    /**
     * 
     * @param 
     * @return
     */
    public int evaluate(){
    	int score=0;
    	for(int i=0;i<15;i++)
    		for(int j=0;j<15;j++)
    			if(chess_Board[i][j]==2){
    				int type=getType(i,j,1-startColor);
    				score+=getMark(type);
    				type=getType(i,j,startColor);
    				score-=getMark(type);
    			}
        return score;
    }
    //局部最优
    public int[][] getBests(int color){
    	int[][] rt=new int[225][3];
    	int n=0;
    	//int num2=0;
    	//int num=0;
    	for (int i=0;i<15;i++)
    		for(int j=0;j<15;j++)
    		//{
    			if(chess_Board[i][j]==2){
    				//num2++;
    				rt[n][0]=i;
    				rt[n][1]=j;
    				rt[n][2]=getMark(getType(i,j,color))+getMark(getType(i,j,1-color));
    				//if(rt[n][2]==0)
    					//{System.out.print("rt[][2]>0 in getBests "+getMark(getType(i,j,color))+","+getMark(getType(i,j,color))+"\n");
    				    //num++;}
    				n++;
    			}
    			//else if(chess_Board[i][j]!=2)
    				//System.out.print("board[][]=2("+i+","+j+")="+chess_Board[i][j]+"\n");
    		//}
    	//System.out.print("n = "+n);
    	//System.out.print("getBests;\n");
    	Arrays.sort(rt,new ArrComparator());
    	int size=8 > n? n:8;
    	int[][] bests=new int[size][3];
    	System.arraycopy(rt,0,bests,0,size);
    	return bests;
    }
    //极大极小算法
    /**
     * 
     * @param alpha
     * @param beta
     * @param step
     * @return
     */
    public int max(int alpha,int beta,int step){
    	int mx=alpha;
    	if(step==0){
    		return evaluate();
    	}
    	for(int i=3;i<11;i++)
    		for(int j=3;j<11;j++)
    			if(chess_Board[i][j]==2){
    				if(getType(i,j,1-startColor)==1)
    					return 100*getMark(1);
    				chess_Board[i][j]=1-startColor;
    				int t=min(mx,beta,step-1);
    				chess_Board[i][j]=2;
    				if(t>mx)
    					mx=t;
    				if(mx>=beta)
    					return mx;
    			}
    	return mx;
    }
    /**
     * 
     * @param alpha
     * @param beta
     * @param step
     * @return
     */
    public int min(int alpha,int beta,int step){
    	int mn=beta;
    	if(step==0)
    		return evaluate();
    	int[][] rt=getBests(startColor);
    	for(int i=0;i<rt.length;i++){
    		int ii=rt[i][0];
    		int jj=rt[i][1];
    		if(getType(ii,jj,startColor)==1)
    			return -100*getMark(1);
    		chess_Board[ii][jj]=startColor;
    		int t=max(alpha,mn,step-1);
    		chess_Board[ii][jj]=2;
    		if(t<mn)
    			mn=t;
    		if(mn<=alpha)
    			return mn;
    	}
    	return mn;
    	
    }
    //下棋
    public int[] putOne(int color,int Level){
    	int it=-1;
    	int jt=-1;
    	int mx=-100000000;
    	int[][] bests=getBests(color);
    	//System.out.print(bests.length);
    	//test
    	/*
    	for(int i=0;i<8;i++)
    		{for(int j=0;j<3;j++)
    			System.out.print("the bests:"+bests[i][j]);
    	    System.out.print("\n");}
    	*/
    	
    	for(int k=0;k<bests.length;k++){
    		int i=bests[k][0];
    		int j=bests[k][1];
    		if(getType(i,j,color)==1){
    			it=i;
    			jt=j;
    			break;
    		}
    		chess_Board[i][j]=color;
    		int t;
    		//if(color==0){
    		  t=min(-100000000,100000000,Level);
    		//else
    			//t=max(-100000000,100000000,3);
    		
    		chess_Board[i][j]=2;
    		if(t>mx||(t==mx && randomTest(3*(Math.abs(7-i)+Math.abs(7-j))+2))){
    			//if(t==mx)
    				//System.out.print("putone t(mx):"+t);
    			it=i;
    			jt=j;
    			mx=t;
    		}		
    	}
    	
    	return new int[] {it,jt};
    	
    }
    private boolean randomTest(int kt){
    	Random rm=new Random();
    	return rm.nextInt() % kt==0;
    }
	//判断color棋子是否出现五连子,即判断color方是否获胜
	public boolean hasFive(int color){
		int[][] chess_BoardState=new int[15][];
		//水平方向
		for(int i=0;i<15;i++){
			chess_BoardState[i]=new int[15];
			chess_BoardState[i][0]=(chess_Board[i][0]==color)? 1:0;
			for(int j=1;j<15;j++){
				chess_BoardState[i][j]=(chess_Board[i][j]==color)? (chess_BoardState[i][j-1]+1):0;
			    if(chess_BoardState[i][j]==5){
			    	System.out.print("第一个\n");
			    	return true;
			    }
			}
		}
		//竖直方向
		for(int j=0;j<15;j++){
			chess_BoardState[0][j]=(chess_Board[0][j]==color) ? 1:0;
			for(int i=1;i<15;i++){
				chess_BoardState[i][j]=(chess_Board[i][j]==color) ? (chess_BoardState[i-1][j]+1):0;
			    if(chess_BoardState[i][j]==5){
			    	System.out.print("第二个\n");
			    	return true;
			    }
		    }
		}
		//左上到右下
		for(int i=0;i<15;i++){
			chess_BoardState[0][i]=(chess_Board[0][i]==color) ? 1:0;
			chess_BoardState[i][0]=(chess_Board[i][0]==color) ? 1:0;
		}
		for(int i=1;i<15;i++){
			for(int j=1;j<15;j++){
				chess_BoardState[i][j]=(chess_Board[i][j]==color)?(chess_BoardState[i-1][j-1]+1):0;
				if(chess_BoardState[i][j]==5){
					System.out.print("第三个\n");
					return true;
				}
			}
		}
		//右上到左下
		for(int i=0;i<15;i++){
			chess_BoardState[0][i]=(chess_Board[0][i]==color) ? 1:0;
			chess_BoardState[i][14]=(chess_Board[i][14]==color)? 1:0;
		}
		for(int i=1;i<15;i++){
			for(int j=13;j>=0;j--){
				chess_BoardState[i][j]=(chess_Board[i][j]==color)? (chess_BoardState[i-1][j+1]+1) : 0;
				if(chess_BoardState[i][j]==5){
					System.out.print("第四个\n");
					return true;
				}
			}
		}
		return false;
   }	

}


    
   
