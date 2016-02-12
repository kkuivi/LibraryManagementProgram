import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/*This class represents a book.*/
public class BookDetails {
	private int bookId;
	private String bookTitle;
	private Author author = new Author();
	private String publisher; //name of book's publisher
	private Genre genre; //Book's genre. A book's genre can only take one of the values from the predetermined set of allowable values
	private Condition condition; //Book's condition. A book's condition can either be 'New', 'Good', 'Worn', or 'Damaged'
	private int studentBorrowed = 0; //Id of student who has currently borrowed the book. Default value is 0 since no student will have an Id number = 0.
	private Calendar dateToBeReturned = new GregorianCalendar(1,1,1); //default date value that is used when a book has not been borrowed. 
																	//It is a sufficient default value because it is so far back, that it is realistically impossible that a book had to be returned on that date.
	private boolean borrowed; //stores whether book is currently borrowed or not. True --> borrowed, False --> not borrowed
	
	
	public enum Genre{
		/*Allowable values for a book's genre*/
		Biology("Biology"), Mathematics("Mathematics"), Chemistry("Chemistry"), Physics("Physics"), Science_Fiction("Science_Fiction"), 
		Fantasy("Fantasy"),Action("Action"), Drama("Drama"), Romance("Romance"), Horror("Horror"), History("History"), 
		Autobiography("Autobiography"), Biography("Biography");
		
		public String genre_name; //the string version of a genre
		
		private Genre(String s){
			genre_name = s;
		}
		
		public String toString(){
			return genre_name;
		}
	}
	
	public enum Condition{
		/*Allowable values for a book's condition*/
		New("New"), Good("Good"), Worn("Worn"), Damaged("Damaged");
		
		public String book_condition; //the string version of a condition
		
		private Condition(String c){
			book_condition = c;
		}
		
		public String toString(){
			return book_condition;
		}
	}
	
	/*Sets the id of the student who is currently borrowing this book.*/
	public void setStudentBorrowed(int id){
		studentBorrowed = id;
	}
	public int getStudentBorrowed(){
		return studentBorrowed;
	}
	
	public void setBookId(int id){
		bookId = id;
	}
	public int getBookId(){
		return bookId;
	}
	
	public void setBookTitle(String title){
		bookTitle = title;
	}
	public String getBookTitle(){
		return bookTitle;
	}
	
	public boolean setAuthorFirstName(String f){
		return author.setFirstName(f); //returns True --> if name input was valid. returns False --> name input contained non-alphabet characters
	}
	public boolean setAuthorLastName(String l){
		return author.setLastName(l); //returns True --> if name input was valid. returns False --> name input contained non-alphabet characters
	}
	public boolean setAuthorMiddleName(String m){
		return author.setMiddleName(m); //returns True --> if name input was valid. returns False --> name input contained non-alphabet characters 
	}
	/*Returns the Author object*/
	public Author getAuthor(){
		return author;
	}
	
	public void setPublisher(String p){
		publisher = p;
	}
	public String getPublisher(){
		return publisher;
	}
	
	/*Sets the book's condition. This method will be used in the loadBooks() and addNewBook() methods of the Main Program.
	 *Returns false if the parameter value is not one of the allowable values for Condition.*/
	public boolean setCondition(String c){
		boolean correct_input = true;
		if(c.equalsIgnoreCase("new"))
			condition = Condition.New;
		else if(c.equalsIgnoreCase("good"))
			condition = Condition.Good;
		else if(c.equalsIgnoreCase("worn"))
			condition = Condition.Worn;
		else if(c.equalsIgnoreCase("damaged"))
			condition = Condition.Damaged;
		else{//invalid condition input
			correct_input = false;
			System.out.println("Invalid Condition Input: Condition entered was invalid");
			System.out.println("Valid Input for Condition are: New, Good, Worn, or Damaged");
		}
		return correct_input;
	}
	public Condition getCondition(){
		return condition;
	}
	
	/*Set the borrowed state of the book. True --> borrowed, False --> not borrowed.*/
	public void setBorrowed(boolean s){
		borrowed = s;
	}
	public boolean getBorrowed(){
		return borrowed;
	}
	
	/*Sets the book's genre. This method will be used in the loadBooks() and addNewBook() methods of the Main Program.
	 Returns false if the parameter value is not one of the allowable values for Genre.*/
	public boolean setGenre(String g){
		boolean correct_input = true;
		if(g.equalsIgnoreCase("biology"))
			genre = Genre.Biology;
		else if(g.equalsIgnoreCase("mathematics"))
			genre = Genre.Mathematics;
		else if(g.equalsIgnoreCase("chemistry"))
			genre = Genre.Chemistry;
		else if(g.equalsIgnoreCase("physics"))
			genre = Genre.Physics;
		else if(g.equalsIgnoreCase("science_fiction"))
			genre = Genre.Science_Fiction;
		else if(g.equalsIgnoreCase("fantasy"))
			genre = Genre.Fantasy;
		else if(g.equalsIgnoreCase("action"))
			genre = Genre.Action;
		else if(g.equalsIgnoreCase("drama"))
			genre = Genre.Action;
		else if(g.equalsIgnoreCase("romance"))
			genre = Genre.Romance;
		else if(g.equalsIgnoreCase("horror"))
			genre = Genre.Horror;
		else if(g.equalsIgnoreCase("history"))
			genre = Genre.History;
		else if(g.equalsIgnoreCase("autobiography"))
			genre = Genre.Autobiography;
		else if(g.equalsIgnoreCase("biography"))
			genre = Genre.Biography;
		else{ //invalid genre input
			correct_input = false;
			System.out.println("Invalid Genre Input: Genre entered was invalid");
			System.out.println("Check documentation for allowable set of values for Genre");
		}
		return correct_input;
	}
	/*Returns the Genre object*/
	public Genre getGenre(){
		return genre;
	}
	
	/*Method will be used in MainProgram when a book is being checked out and its 
	 * return date is to be set. In this instance, the book's return date cannot be set to
	 * before the current date*/
	public boolean setDateToReturn(Calendar c){
		Calendar cal = Calendar.getInstance(); //get today's date
		if(c.before(cal.getTime())) //checks if date input is before today's date since a book cannot be returned before the current day
			return false;
		dateToBeReturned = c;
		return true;
	}
	/*Method will be used in the loadBooks() method of MainProgram. 
	 * In this instance, the book's return date is just set the value it was 
	 * in the books.txt file. And this value can be before the current date.*/
	public void setDateToReturn(int year, int month, int day){
		dateToBeReturned = new GregorianCalendar(year,month,day);
	}
	public Calendar getDateToReturn(){
		return dateToBeReturned;
	}
	
	/*Resets book's dateToBeReturned value. This method will be used when a book is returned. After a book is returned,
	 * its return date is reset to the default value.*/
	public void resetDateToReturn(){
		dateToBeReturned = new GregorianCalendar(1,1,1); //default date value that is used when a book has not been borrowed. 
														//It is a sufficient default value because it is so far back that is realistically impossible that a book had to be returned on that date.
	}
	
	/*Parses BookDetails in a format that will be written to the books.txt file in the MainProgram*/
	public String ParseForTextFile(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); //Date Format to be used for the book's return date
		return bookId + "\t" + bookTitle + "\t" + author.getFirstName() + "\t" + author.getMiddleName() + "\t" + author.getLastName() + "\t" + getPublisher() + "\t" + genre.toString() + "\t" 
	+ getCondition().toString() + "\t" + getStudentBorrowed() + "\t" + sdf.format(dateToBeReturned.getTime()) + "\t" + getBorrowed() + "\n"; 
	}	
}
