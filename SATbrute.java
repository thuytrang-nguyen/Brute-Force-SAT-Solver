// CS 301 - Algorithms
// Brute Force SAT solver
// Weronika (Thuy Trang) Nguyen
// Due 22.Feb.2019

// To run prob1.txt and prob2.txt: please get rid of space " " in clause "4 0 " in the textfile

import java.util.*;
import java.io.File; 
import java.util.Scanner; 
import java.io.*;

public class SATbrute{

	private int varnum;			// number of variables in the problem
	private int clausenum;		// number of clauses
	private int[][] clauses;	// 2D array containing processed clauses 
	private ArrayList<String> solutions;	// arraylist for solutions 
	private String filename;	// the problem filename 

	// constructor
	public SATbrute(String file){
		this.varnum = 0;
		this.clausenum = 0;
		this.clauses = null;
		this.solutions = new ArrayList<String>();
		this.filename=file;
		
		// call processClauses()
		try{
			this.processClauses(this.filename);
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
		// calculate the set of solutions
		this.calculateSolutions();

	}

	// this method handles file processing and initializes the 2D clauses array
	public void processClauses(String filename) throws Exception{

		File file = new File(filename); 
		Scanner sc = new Scanner(file); 
		String all_cl = "";
		while (sc.hasNextLine()){
    		String l = sc.nextLine(); 

    		String[] split = l.split("\\s+");

    		if (split[0].equals("c")==false){
	    		if (split[0].equals("p")){
	    			this.varnum=Integer.parseInt(split[2]);
	    			this.clausenum=Integer.parseInt(split[3]);
	    			this.clauses = new int[this.clausenum][];
	    		}
	    		else if(!(split[0].equals("%") || split[0].equals("0"))){
	    			all_cl += l+" ";
	    		}
	    	}
	    }
	    // additional file processing for files in form u...cnf
	    if ((this.filename).charAt(0)=='u'){
		    if ((all_cl.substring(0,1)).equals(" ")){
		    	all_cl = all_cl.substring(1,all_cl.length());
		    }
		    if (all_cl.substring(all_cl.length()-1).equals(" ")){
		    	all_cl = all_cl.substring(0,all_cl.length()-1);
		    }
		}
		// initialize the clauses[][] array
	    String[] all_split = all_cl.split(" 0 ");

	    for (int j=0; j<all_split.length; j++){
	    	String[] ugh = (all_split[j]).split(" ");
	    	int size = ugh.length;
	    	int[] clause = new int[size];
	    	for (int i=0; i<size; i++){
	    		clause[i]=Integer.parseInt(ugh[i]);
	    	}
	    	this.clauses[j]=clause;
	    }
	}  

	// this method calculates solutions from all possible combinations
	// that satisfy the processed clauses
	private void calculateSolutions(){
		// current combination
		int curr = 0;
		// calculate total number of possible variable assignment combinations
		int num_comb = (int) Math.pow(2, this.varnum);
		for (int i=0; i<num_comb; i++){
			String binary = String.format("%"+this.varnum+"s", Integer.toBinaryString(curr)).replace(' ', '0');
			if (checkSolution(binary)=='1'){
				(this.solutions).add(binary);
			}
			// new combination - add one to the previous number
			curr += 1;
		}
	}

	// this method checks if an assignment satisfies all the clauses
	private char checkSolution(String k){
		char asg[] = new char[this.varnum];
		for (int i=0;i<k.length();i++){
			asg[i]=k.charAt(i);
		}

		int len = (this.clauses).length;

		char outcome = '1';	// total value of the outcome (combining the clauses)

		for (int j=0; j<len; j++){
			char clause = '0'; // value of each OR clause
			for (int m=0; m<((this.clauses)[j]).length; m++){
				int vari = clauses[j][m];
				if (vari>0){
					if (asg[vari-1]=='1'){
						clause = '1';
					}
				}else{
					if (asg[(-vari)-1]=='0'){
						clause = '1';
					}
				}
			}
			// we check if the clause satisfies the previous clauses 
			outcome = this.and(outcome,clause);
			if (outcome=='0'){
				return outcome;
			}
		}
		return outcome;

	}

	// this method prints out the solution arraylist
	public void printSolutions(){
		if ((this.solutions).size() == 0){
			System.out.println(this.filename + " is unsatisfiable");
		}else{
			System.out.println("Solutions for " + this.filename + ": ");
			String sol="";
			for (String k: this.solutions){
				sol=sol.replace("1","1");
				sol=k.replace("0","-1");
				System.out.println(sol);
			}
		}
	}

	// the truth table for "AND" clause that returns char '1' if true, '0' ow.
	public static char and(char a, char b){
		if (a=='1' && b=='1'){
			return '1';
		}
		return '0'; 
	}


	public static void main(String[] args) throws Exception{
		// make sure there is 1 cmd line argument
		if (args.length != 1){
			System.out.println("Incorrect number of input arguments!");
			System.exit(2);
		}
		// keep track of time passed
		long startTime = System.currentTimeMillis();
		// calculate solution for a problem set specified in cmd line
		SATbrute sat = new SATbrute(args[0]);
		long t_passed = System.currentTimeMillis()-startTime;
		// print the solutions
		sat.printSolutions();
		System.out.println("Time passed: " + t_passed + " ms");
	}
}