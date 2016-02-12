
/*This class represents a student.*/
public class StudentDetails extends Person{
	private int grade;
	private int bookIdBorrowed = 0; //stores the ID number of the book currently borrowed by student
	private double totalfine = 0; //stores the total fine accrued by student during the school year
		
	/*Sets student's grade and returns true if correct grade is inputed. Leaves grade blank and 
	 * returns false if invalid grade is inputed. Students in high school can only be between grades 
	 * 9 -12.*/
	public boolean setGrade(int g){
		if(g < 9 || g > 12){
			System.out.println("Invalid Grade Input: Grade can only be between 9 - 12");
			return false;
		}
		grade = g;
		return true;
	}
	public int getGrade(){
		return grade;
	}
	
	/*Sets the ID of the book borrowed by the student.*/
	public void setBookIdBorrowed(int bookId){
		bookIdBorrowed = bookId;
	}
	public int getBookIdBorrowed(){
		return bookIdBorrowed;
	}
	
	//increase student's total fine by f. 
	public void addFine(double f){
		totalfine += f;
	}
	public double getTotalFine(){
		return totalfine;
	}
	/*sets the total fine for a student. Will be used by the loadStudents() method in the main program
	to set the overall total fine for a student*/
	public void setTotalFine(double t){
		totalfine = t;
	}
	
	/*Returns all details about student except the ID of the book borrowed by the student.*/
	public String toString(){
		String middlename = !getMiddleName().equals("null") ? getMiddleName() : ""; //checks if student has a middlename. If not then set middlename to an empty string.
		return "Full Name: " + getFirstName() + " " + middlename + " " + getLastName() + " | Grade: " + grade + " | Total Fine: Ghc" + totalfine;
	}
	
	/*Returns student's full name.*/
	public String getFullName(){
		String middlename = !getMiddleName().equals("null") ? getMiddleName() : ""; //checks if student has a middlename. If not then set middlename to an empty string.
		return "Student Id: " + getId() + " | " + getFirstName() + " " + middlename + " " + getLastName();
 	}
	
	/*Parses StudentDetails in a format that will be written to the students.txt file in the MainProgram*/
	public String ParseForTextFile(){
		return getId() + "\t" + getFirstName() + "\t" + getMiddleName() + "\t" + getLastName() + "\t" 
	+ getGrade() + "\t" + getBookIdBorrowed() + "\t" + getTotalFine() + "\n";
	}
}
