/**
 * 
 */
package cn.edu.tsinghua.java_Course;

import java.io.Serializable;

/**
 * @author huangyao
 *
 */
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

