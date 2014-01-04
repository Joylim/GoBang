/**
 * 
 */
package cn.edu.tsinghua.java_Course;

/**
 * @author huangyao
 *
 */
public class test_GetBests {
	chessBoard board;
	int[][] p=new int[15][15];
	public test_GetBests(){
		for(int i=0;i<15;i++)
			for(int j=0;j<15;j++)
				p[i][j]=2;
		p[7][7]=0;
		p[7][5]=0;
		p[7][6]=0;
		p[8][6]=0;
		p[7][8]=0;
		board=new chessBoard(p,0);
	}
	/*
	public static void main(String[] args){
		//test getBests:
		test_GetBests t=new test_GetBests();
		int[][] best=new int[8][3];
		best=t.board.getBests(1);
		for(int i=0;i<8;i++)
			System.out.print("["+best[i][0]+"]["+best[i][1]+"]="+best[i][2]+"\n");
		//test get-Types
		int type=t.board.getType(7,4,0);
		System.out.print("type test: "+type+"\n");
		//test count()
		int[] count=new int[2];
		count=t.board.count(6,6,1,0,0);
		System.out.print("test count:"+count[0]+","+count[1]+"\n");
		//test makeSence(x,y,ex,ey,color)
		boolean b=t.board.makeSence(6,6,1,0,0);
		if(b)
			System.out.print("makeSence test: makesence\n ");
		else
			System.out.print("nosence!\n");
		
	}
	*/

}
