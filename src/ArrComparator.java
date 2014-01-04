/**
 * 
 */
package cn.edu.tsinghua.java_Course;

/**
 * @author huangyao
 *
 */
import java.util.Comparator;
public class ArrComparator implements Comparator{
	int column=2;
	int sortOrder=-1;
	public ArrComparator(){}
	public ArrComparator(int cl){
		column=cl;
	}
	public int compare(Object a,Object b){
		if(a instanceof int[]){
			return sortOrder*(((int[])a)[column]-((int[])b)[column]);
		}
		throw new IllegalArgumentException("param a,b must be int[].");
	}

}
