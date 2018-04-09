import java.util.*;
import java.io.*;
import java.util.concurrent.*;
/* right: 0; compile error: 1; time limit: 2; runtime error: 3; wrong answer: 4*/
/* how to delete the class file */
public class CompareFinal{
	public static void main(String[] args) throws Exception{
		String a = extract("C:\\Users\\Elizabeth\\IdeaProjects\\Research-autograder\\qtest-questions\\answers-F17\\answers-F17\\CS170.quiz01-codeanswers\\CS170.quiz01.s2e9643b0.SquareDiag1.answer");
		String b = extract("C:\\Users\\Elizabeth\\IdeaProjects\\Research-autograder\\qtest-questions\\questions-F17\\quiz01-questions\\question6-a.txt");
		System.out.println(compareSolution(a, b));
	}

	public static int compareSolution(String student, String instructor) throws Exception{
		//extract the main method from instructor's note
		int indexstart = instructor.indexOf("public static void main");
		int indexstop = instructor.indexOf("{", indexstart) + 1;
		int bracket = 1;
		while (bracket != 0) {
			if (instructor.charAt(indexstop) == '{') {
				bracket++;
			}else if (instructor.charAt(indexstop) == '}') {
				bracket--;
			}
			indexstop++;
		}
		String main = instructor.substring(indexstart, indexstop);
		// replace the main method in students' code if there is any
		int indexmain = student.indexOf("public static void main");
		if (indexmain == -1) {
			int start = student.lastIndexOf("}");
			student = student.substring(0, start) + main + "\n" + "}";
		}else {
			int stop = student.indexOf("{", indexmain) + 1;
			int countbracket = 1;
			while(countbracket != 0) {
				if (student.charAt(stop) == '{') {
					countbracket++;
				}else if (student.charAt(stop) == '}') {
					countbracket--;
				}
				stop++;
			}
			student = student.substring(0, indexmain) + main + "\n" + student.substring(stop);
		}
		//find the class name in instructor's code
		String classname = "";
		int start = instructor.indexOf("class") + 5;
		while (instructor.charAt(start) == ' ') {
			start++;
		}
		int end = start;
		while(instructor.charAt(end) != ' ') {
			end++;
		}
		classname = instructor.substring(start, end) + ".java";
		
		//write the student's code into the java file with same name (so replace the instructor's code)and execute it
		System.out.println(classname);
        File f = new File(classname);
		FileWriter pws = new FileWriter(f);
		pws.write(student);
		pws.close();
		Process pros1 = Runtime.getRuntime().exec("javac " + classname);
    	pros1.waitFor();
    	Process pros2 = Runtime.getRuntime().exec("java " + classname.substring(0, classname.length()-5));
		boolean timecheck = pros2.waitFor(5, TimeUnit.SECONDS);
		if (timecheck == false) {
    		System.out.println("time limit exceed");
    		f.delete();
    		return 2;
    	}
    	//keep track of the output
    	Scanner ins = new Scanner(new InputStreamReader(pros2.getInputStream()));
		Scanner error = new Scanner(new InputStreamReader(pros2.getErrorStream()));
    	String lines = "";
		String errline = "";
    	while (ins.hasNextLine()) {
        	lines+=ins.nextLine();
    	}
    	System.out.println("stu: " + lines);
    	ins.close();
    	while (error.hasNextLine()) {
        	errline+=error.nextLine();
    	}
    	error.close();
    	if (lines.compareTo("") == 0) {
    		System.out.println("The program doesn't compile");
    		f.delete();
    		return 1;
    	}
    	if (errline.length() != 0) {
			System.out.println("The program has runtime error");
			System.out.println(errline);
			f.delete();
			return 3;	
		}

    	//write the instructor's code into a java file and execute it
		FileWriter pw = new FileWriter(f);
		pw.write(instructor);
		pw.close();
		Process pro1 = Runtime.getRuntime().exec("javac " + classname);
    	pro1.waitFor();
    	Process pro2 = Runtime.getRuntime().exec("java " + classname.substring(0, classname.length()-5));
    	// keep trace of the output
    	Scanner in = new Scanner(new InputStreamReader(pro2.getInputStream()));
    	String line = "";
    	while (in.hasNextLine()) {
        	line+=in.nextLine();
    	}
    	System.out.println("ins: " + line);
    	in.close(); 
    	if (lines.equals(line)) {
    		f.delete();
    		return 0;
    	}
    	System.out.println("wrong answer");
    	f.delete();
    	return 4; 
	}
	
	public static String extract(String filename) throws IOException{
		Scanner scan = new Scanner(new File(filename));
		String result = "";
		while(scan.hasNextLine()) {
			result += scan.nextLine() + "\n";
		}
		scan.close();
		return result;
	}
}
