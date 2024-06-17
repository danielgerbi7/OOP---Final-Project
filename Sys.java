package daniel_strasser_daniel_jerbi;

import java.io.Serializable;
import java.util.Arrays;

@SuppressWarnings("serial")
public class Sys implements Serializable {

	private Repository[] repArr;
	private int numOfReps;
	
	public Sys(String[] strArr) {
		
		this.numOfReps = strArr.length;
		this.repArr = new Repository[numOfReps];
		for (int i = 0; i < numOfReps; i++) {
			repArr[i] = new Repository(1, strArr[i]);
		}
	}
	
	public void addRep(Repository rep) {
		if (repArr.length == numOfReps) {
			repArr = Arrays.copyOf(repArr, repArr.length + 1);
		}
		repArr[numOfReps++] = rep;
	}
	
	public void removeRep(Repository rep) {
		for (int i=0; i< numOfReps; i++) {
			if (repArr[i] == rep) {
				// repArr[i].removeData();
				repArr[i] = null;
				for (int j=i; j < numOfReps-1; j++) {
					repArr[j] = repArr[j+1];
				}
			}
		}
		numOfReps--;
	}
	
	public int getNumOfReps() {
		return numOfReps;
	}
	
	
	public boolean noQuestions() {
		for (int i=0; i < numOfReps; i++) {
			if (repArr[i].noQuestions() == false) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuffer res = new StringBuffer();
		for (int i=0; i<numOfReps; i++) {
			res.append(repArr[i].toString());
		}
		return res.toString();
	}
	
	public String classesToString() {
		StringBuffer res = new StringBuffer();
		for (int i=0; i<numOfReps; i++) {
			res.append("Class #" + (i+1) + ": " + repArr[i].getClassName() + "\n");
		}
		return res.toString();
	}	
		
	public boolean validClassNum(int n) {
		return (n > 0 && n <= numOfReps);
	}
	
	public Repository getRepByNum(int n) {
		return repArr[n-1];
	}
	
	public boolean classExists(String name) {
		for (int i=0; i < numOfReps; i++) {
			if (repArr[i].getClassName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	@Override 
	public boolean equals(Object other) {
		if (!(other instanceof Sys)) {
			return false;
		}
		Sys s = (Sys)other;
		return s.repArr.equals(this.repArr) && s.numOfReps == this.numOfReps;
	}
	}



