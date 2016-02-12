import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/*This class contains the program that is run when a user interacts with the Library Management System. */
public class MainProgram {
	private static String USERNAME = "Library_Admin"; //correct username for the program
	private static String PASSWORD = "tis123_#$@"; //correct password for the program
	private static Hashtable<Integer, BookDetails> ALL_BOOKS = new Hashtable<Integer, BookDetails>(); //stores the list of all books in the library. Matches a book's Id to it's respective bookDetails object
	private static ArrayList<HashSet<String>> ALL_GENRES = new ArrayList<HashSet<String>>(13); //stores list of books of a specific Genre. Used an ArrayList<HashSet<>> so as to eliminate duplicate book titles being added to the list. 
																						//Capacity is set to 13 because each index will represent a specific Genre.
	private static Hashtable<String, HashSet<String>> ALL_PUBLISHERS = new Hashtable<String, HashSet<String>>(); //stores list of books of a specific Publisher. Used a HashSet as its value so as to eliminate duplicate book titles
																													//being added to the Hashtable.
	private static ArrayList<StudentDetails> ALL_STUDENTS = new ArrayList<StudentDetails>(); //stores the list of students
	private static Hashtable<String, HashSet<String>> ALL_AUTHORS = new Hashtable<String, HashSet<String>>(); //stores all authors and their book titles available in the library. Used a HashSet<String> as its value so as to eliminate duplicate book titles
																									  //being added to the Hashtable.
	private static Hashtable<Integer, String> FINED_STUDENTS = new Hashtable<Integer, String>(); //stores all fined students. Hashtable maps the student's Id to the StudentDetails.toString().
	private static final File BOOKS_FILE = new File("books.txt"); //file that contains details of all books in the library
	private static final File STUDENTS_FILE = new File("students.txt"); //file that contains details of all students in the library
	private static final double  FINE_RATE = 1.50; //fine rate for each day overdue a book was returned
	private static final long MILLISECS_IN_DAY = 1000 * 60 * 60 * 24; //the number of milliseconds in a day
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd"); //Date Format to be used on a book's return date 
	
	public static void main(String[] args) throws IOException{
		runProgram();
	}
	
	/*initialize ALL_GENRES ArrayList so later methods can access indices for respective
	 * Genres. ALL_GENRES has capacity 13 because there are 13 Genres; therefore, each index represents a specific 
	 * Genre.*/
	private static void initializeAllGenresList(){
		for(int i = 0; i < 13; i ++){
			ALL_GENRES.add(new HashSet<String>());
		}
	}
	
	/*Method run when user starts the Library Management System.*/
	public static void runProgram() throws IOException{
		ALL_STUDENTS.add(new StudentDetails()); //dummy StudentDetails object set to index 0 of the ArrayList. This is to ensure no student can have his/her Id set to 0.
		initializeAllGenresList();
		Scanner string_input = new Scanner(System.in); //stores string input from the user
		boolean authenticated = false; //stores whether user entered correct username and password combination
		
		while(!authenticated){
			String username_input;
			String password_input;
			System.out.println("Enter username: ");
			username_input = string_input.next(); //store input for username
			System.out.println("Enter password: ");
			password_input = string_input.next(); //store input for password
			authenticated = authenticateUser(username_input, password_input); //checks whether correct username and password combination was entered
		}
		
		System.out.println();
		/*load all the BookDetails objects in the books.txt file into ALL_BOOKS, then
		 * load all the StudentDetails objects in the students.txt file into ALL_STUDENTS
		 */
		if(loadBooks() && loadStudents()){ //if there's no error loading all the BookDetails and StudentDetails
			System.out.println();
			displayMainMenu();
			int menu_option = getIntegerInput(); //ensures user input is an integer value
			executeMenuOption(menu_option);	//execute menu option entered by user
		}
		
		else{
			System.out.println("Sorry an error occurred while loading! Please check documentation to fix error and then restart the program.");
		}
	}

	/*Get user input. Ensure user input is an integer value*/
	private static int getIntegerInput() {
		Scanner integer_input = new Scanner(System.in);
		boolean valid_inputType = false;
		int menu_option = -1;
		
		/*Repeatedly asks user to enter input until user enters an integer value as input*/
		while(!valid_inputType){
			try{
				menu_option = integer_input.nextInt(); //get menu option to execute
				valid_inputType = true;
			}
			catch(Exception e){
				System.out.println("ERROR! Invalid Input Type: Input must be a number");
				System.out.println("Re-enter input: ");
				integer_input = new Scanner(System.in); //clears value previously in integer_input so that user can enter a new value into menu_option.
			}
		}
		return menu_option;
	}

	private static void displayMainMenu() {
		/*Display Menu*/
		System.out.println("Library Management System");
		System.out.println("1 --> Add New Book");
		System.out.println("2 --> Checkout Book");
		System.out.println("3 --> Return Book");
		System.out.println("4 --> Query Books");
		System.out.println("5 --> Query Students");
		System.out.println("Any other number --> Exit");
		System.out.println("Enter Menu Choice: ");
	}
	
	//Runs a respective method based on user's input.
	private static void executeMenuOption(int user_option) throws IOException{
		boolean exit = false; 
		
		while(!exit){
		switch(user_option){
			case 1:
				System.out.println();
				addNewBook();
				break;
			case 2:
				System.out.println();
				checkoutBook();
				break;
			case 3:
				System.out.println();
				returnBook();
				break;
			case 4:
				System.out.println();
				queryBook();
				break;
			case 5:
				System.out.println();
				printFinedStudents();
				break;
			default:
				System.out.println();
				saveChanges(); //Overwrite the students.txt and books.txt files with the content of the ALL_STUDENTS and ALL_BOOKS data structures respectively
				exit = true; //exit program 
				break;
		  }
			if(!exit){ //if user has not exited the program, then display the main menu again and get user's input for the menu option
				System.out.println();
				displayMainMenu();
				user_option = getIntegerInput(); //get menu option to execute
			}
		}
	}
	
	/*Overwrite the students.txt and books.txt files with the content of the ALL_STUDENTS and ALL_BOOKS data structures
	 * respectively*/
	private static void saveChanges() throws IOException{
		writeToStudentsFile(); //copy all the StudentDetails objects in ALL_STUDENTS and write them to the students.txt file
		writeToBooksFile(); //copy all the BookDetails objects in ALL_BOOKS and write them to the books.txt file
		System.out.println("All Changes Successfully Saved!");
		System.out.println("Thanks for using the Library Management System");
	}
	
	/*Overwrite students.txt file with the Parsed Form of each student in the ALL_STUDENTS ArrayList*/
	private static void writeToStudentsFile() throws IOException{
		FileWriter fw = new FileWriter(STUDENTS_FILE, false); //overwrites the students.txt file
		BufferedWriter bw = new BufferedWriter(fw);
		
		Iterator<StudentDetails> iter = ALL_STUDENTS.listIterator(1); //Get all StudentDetails objects from index 1 of ALL_STUDENTS. This is because the value at index 0 is just a dummy student
		while(iter.hasNext()){
			StudentDetails student = iter.next();
			bw.write(student.ParseForTextFile()); //writes the Parsed Form of the StudentDetails object to the students.txt file
		}
		
		bw.close();
	}
	
	/*Overwrite books.txt file with the Parsed Form of each book in the ALL_BOOKS Hashtable*/
	private static void writeToBooksFile() throws IOException{
		FileWriter fw = new FileWriter(BOOKS_FILE, false); //overwrites the students.txt file
		BufferedWriter bw = new BufferedWriter(fw);
		
		Set<Integer> keys = ALL_BOOKS.keySet(); //returns list of all keys in ALL_BOOKS
		for(int key : keys){
			bw.write(ALL_BOOKS.get(key).ParseForTextFile()); //writes the Parsed Form of the BookDetails object to the books.txt file
		}
		bw.close();
	}
	
	/*Prints out a list of all students who have been fined (students whose total fine is greater than 0).*/
	private static void printFinedStudents(){
		Set<Integer> keys = FINED_STUDENTS.keySet(); //returns list of all keys in FINED_STUDENTS
		int count = 0;
		System.out.println("Fined Students are: ");
		for(int k : keys){
			count ++;
			System.out.println(count + ". " + FINED_STUDENTS.get(k));
		}
		
		if(keys.size() == 0)
			System.out.println("Empty! No Students have been fined");
	}
	
	/*Displays a menu that asks the user to choose whether to query books by Author, 
	 * Genre, or Publisher*/
	private static void queryBook(){
		System.out.println("===Book Queries Menu===");
		
		/*Display menu options*/
		System.out.println("1--> Query Books by Author");
		System.out.println("2--> Query Books by Genre");
		System.out.println("3--> Query Books by Publisher");
		System.out.println("Any other number --> Exit To Main Menu");
		System.out.println("Enter menu option: ");
		int menu_option = getIntegerInput(); //ensures user input is an integer value
		
		switch(menu_option){
			case 1:
				System.out.println();
				queryBooksByAuthor();
				break;
			case 2:
				System.out.println();
				queryBooksByGenre();
				break;
			case 3:
				System.out.println();
				queryBooksByPublisher();
				break;
			default:
				break;
		}
	}
	
	/*Returns list of books of the published by the publisher entered by the user*/
	private static void queryBooksByPublisher(){
		System.out.println("===Query Books By Publisher===");
		Scanner string_input = new Scanner(System.in);
		
		System.out.println("Enter Publisher to Query Books by: ");
		String publisher = string_input.nextLine();
		
		if(ALL_PUBLISHERS.containsKey(publisher)){
			Iterator<String> iter = ALL_PUBLISHERS.get(publisher).iterator();
			System.out.println("All books published by " + publisher + " are:"); 
			printAllElements(iter);
		}
		
		else
			System.out.println("Sorry! There are no such publishers in our records!");
	}

	/*Print all elements in the iterator.*/
	private static void printAllElements(Iterator<String> iter) {
		int count = 0;
		
		if(!iter.hasNext()) //if the list is empty then print out 'Empty!'
			System.out.println("Sorry! There are no such books in our records!");
		
		//else if list is not empty then print out all its elements
		while(iter.hasNext()){
			count++;
			String bookTitle = iter.next();
			System.out.println(count + ". " + bookTitle);
		}
	}
	
	/*Returns list of books of the genre entered by the user*/
	private static void queryBooksByGenre(){
		System.out.println("===Query Books By Genre===");
		Scanner string_input = new Scanner(System.in);
		
		System.out.println("Enter Genre to Query Books by: ");
		String genre = string_input.next();
		int genre_index = getGenreIndex(genre); //returns the index of the genre entered, in ALL_GENRES
		
		if(genre_index > -1){//valid genre was entered
			Iterator<String> iter = ALL_GENRES.get(genre_index).iterator();
			System.out.println("All " + genre + " books are: ");
			printAllElements(iter);
		}
	}
	
	/*Returns the respective index of the given genre in the ALL_GENRES ArrayList*/
	private static int getGenreIndex(String genre){
		int index = -1;
		if(genre.equalsIgnoreCase("biology"))
			index = 0;
		else if(genre.equalsIgnoreCase("mathematics"))
			index = 1;
		else if(genre.equalsIgnoreCase("chemistry"))
			index = 2;
		else if(genre.equalsIgnoreCase("physics"))
			index = 3;
		else if(genre.equalsIgnoreCase("science_fiction"))
			index = 4;
		else if(genre.equalsIgnoreCase("fantasy"))
			index = 5;
		else if(genre.equalsIgnoreCase("action"))
			index = 6;
		else if(genre.equalsIgnoreCase("drama"))
			index = 7;
		else if(genre.equalsIgnoreCase("romance"))
			index = 8;
		else if(genre.equalsIgnoreCase("horror"))
			index = 9;
		else if(genre.equalsIgnoreCase("history"))
			index = 10;
		else if(genre.equalsIgnoreCase("autobiography"))
			index = 11;
		else if(genre.equalsIgnoreCase("biography"))
			index = 12;
		else{
			System.out.println("Invalid Genre Input: Genre entered was invalid");
		}
		return index;
	}
	
	/*Method asks user to enter the first name, middle name, and last name of the author whose list of book titles
	 * the user wants to view, and returns list of books written by the author.*/
	private static void queryBooksByAuthor(){
		System.out.println("===Query Books By Author===");
		Scanner string_input = new Scanner(System.in);
		
		System.out.println("Enter First Name of Author to Query Books by: ");
		String author_firstname = string_input.next();
		
		System.out.println("Enter Middle Name of Author to Query Books by (Type 'null' if Author has no middlename): ");
		String author_middlename = string_input.next();
		if(author_middlename.equalsIgnoreCase("null"))
			author_middlename = "";
		
		System.out.println("Enter Last Name of Author to Query Books by: ");
		String author_lastname = string_input.next();
		
		String author_fullname = author_firstname + " " + author_middlename + " " + author_lastname;
		
		if(ALL_AUTHORS.containsKey(author_fullname)){//if author_full_name exists in the Hashtable then print out all of the author's book titles
			System.out.println("Author found!");
			System.out.println(author_fullname  + "'s list of book titles are: "); 
			Iterator<String> iter = ALL_AUTHORS.get(author_fullname).iterator();
			printAllElements(iter);
		}
		else
			System.out.println("Sorry Author name entered does not exist in our records");
	}
	
	/*Adds a new Book to the ALL_Books ArrayList*/
	private static void addNewBook(){
		System.out.println("===Add New Book===");
		BookDetails new_book = new BookDetails();
		Scanner string_input = new Scanner(System.in); //takes string input from user
		Scanner integer_input = new Scanner(System.in); //takes integer input from user
		
		System.out.println("Enter BookId: ");
		int bookId = getIntegerInput(); //ensures user input is an integer value
		/*if bookId entered already exists then continually ask user to enter bookId until a bookId that does not exist is entered. 
		 * Do the same if bookId is less than 1 because book's in the library will not have Id's less than 1.*/
		while(ALL_BOOKS.containsKey(bookId) || bookId < 1){
			System.out.println("Book Id entered already exists! Enter BookId: ");
			bookId = integer_input.nextInt();
		}
		new_book.setBookId(bookId);
		
		System.out.println("Enter Book Title: ");
		String bookTitle = string_input.nextLine();
		new_book.setBookTitle(bookTitle);
		
		String author_firstname;
		do{
			System.out.println("Enter Author's First Name: ");
			author_firstname = string_input.next();
		}while(!new_book.setAuthorFirstName(author_firstname)); //loop until name input is valid (contains only letters) 
		
		String author_middlename;
		do{
			System.out.println("Enter Author's Middle Name (Type 'null' if author has no middle name): ");
			author_middlename = string_input.next();
		}while(!new_book.setAuthorMiddleName(author_middlename)); //loop until name input is valid (contains only letters) 
		
		String author_lastname;
		do{
			System.out.println("Enter Author's Last Name: ");
			author_lastname = string_input.next();
		}while(!new_book.setAuthorLastName(author_lastname)); //loop until name input is valid (contains only letters) 
		
		string_input = new Scanner(System.in);
		System.out.println("Enter Book's Publisher: ");
		String publisher = string_input.nextLine();
		new_book.setPublisher(publisher);
		
		
		/*Set book's genre. Valid values for a book's Genre are Biology, Mathematics, Chemistry, Physics,
		 * Science_Fiction, Fantasy, Action, Drama, Romance, Horror, History, Autobiography, Biography.*/
		String genre;
		do{
			System.out.println("Enter Book's Genre: ");
			genre = string_input.next();
		}while(!new_book.setGenre(genre)); //loop until valid genre value is entered
		
		/*Set book's condition. Valid values for a book's Condition are New, Good, Worn, Damaged.*/
		String condition;
		do{
			System.out.println("Enter Book's Condition: ");
			condition = string_input.next();
		}while(!new_book.setCondition(condition)); //loop until valid value for condition is entered
		
		ALL_BOOKS.put(new_book.getBookId(), new_book);
		addToPublisherTable(new_book.getPublisher(), new_book.getBookTitle()); //add book's title to the ALL_PUBLISHERS Hashtable
		addToGenreTable(new_book, new_book.getBookTitle());  //add book's title to the ALL_GENRES ArrayList
		addToAuthorTable(new_book.getAuthor().toString(), new_book.getBookTitle()); //add book's title to the ALL_AUTHORS Hashtable
		System.out.println("Book Successfully Added!");
	}
	
	/*Adds bookTitle to the ALL_AUTHORS ArrayList<author_name, HashSet<bookTitle>>*/
	private static void addToAuthorTable(String author_name, String bookTitle) {
		if(ALL_AUTHORS.containsKey(author_name)) 
			ALL_AUTHORS.get(author_name).add(bookTitle);
		/*put author_name as a new key in ALL_AUTHORS*/
		else{
			HashSet<String> bookTitles_list = new HashSet<String>();
			bookTitles_list.add(bookTitle);
			ALL_AUTHORS.put(author_name, bookTitles_list);
		}
	}

	/*Adds book title to respective index of its genre to the ALL_GENRES ArrayList<HashSet<bookTitle>>*/
	private static void addToGenreTable(BookDetails book, String bookTitle) {
		//ALL_GENRES representation --> Biology|Mathematics|Chemistry|Physics|Science_Fiction|Fantasy|Action|Drama|Romance|Horror|History|Autobiography|Biography 
		switch(book.getGenre()){
			case Biology:
				ALL_GENRES.get(0).add(bookTitle);
				break;
			case Mathematics:
				ALL_GENRES.get(1).add(bookTitle);
				break;
			case Chemistry:
				ALL_GENRES.get(2).add(bookTitle);
				break;
			case Physics:
				ALL_GENRES.get(3).add(bookTitle);
				break;
			case Science_Fiction:
				ALL_GENRES.get(4).add(bookTitle);
				break;
			case Fantasy:
				ALL_GENRES.get(5).add(bookTitle);
				break;
			case Action:
				ALL_GENRES.get(6).add(bookTitle);
				break;
			case Drama:
				ALL_GENRES.get(7).add(bookTitle);
				break;
			case Romance:
				ALL_GENRES.get(8).add(bookTitle);
				break;
			case Horror:
				ALL_GENRES.get(9).add(bookTitle);
				break;
			case History:
				ALL_GENRES.get(10).add(bookTitle);
				break;
			case Autobiography:
				ALL_GENRES.get(11).add(bookTitle);
				break;
			case Biography:
				ALL_GENRES.get(12).add(bookTitle);
				break;
			default:
				System.out.println("ERROR: Invalid Genre Input");
				break;
		}
	}

	/*Adds bookTitle to the ALL_PUBLISHERS ArrayList<publisher, HashSet<bookTitle>>*/
	private static void addToPublisherTable(String publisher, String bookTitle) {
		if(ALL_PUBLISHERS.containsKey(publisher))
			ALL_PUBLISHERS.get(publisher).add(bookTitle);
		/*put publisher as a new key in ALL_AUTHORS*/
		else{
			HashSet<String> bookTitles_list = new HashSet<String>();
			bookTitles_list.add(bookTitle);
			ALL_PUBLISHERS.put(publisher, bookTitles_list);
		}
		
	}

	/*This method asks the user to enter the Id of the book to be borrowed, and checks if the book has already 
	 * been borrowed. If the book has not already been borrowed then the user enters the details of the student
	 * borrowing the book; if the student does not currently have another book already borrowed, then the student 
	 * is allowed to checkout the book.*/
	private static void checkoutBook(){
		System.out.println("===Book Checkout===");
		Scanner string_input = new Scanner(System.in); //gets String values entered by the user
		Scanner integer_input = new Scanner(System.in); //gets integer values entered by the user
		boolean exit = false; //indicates whether method should be exited
		
		System.out.println("Enter Id of book to be checked out: ");
		int bookId = getIntegerInput();//ensures user input is an integer value
		//if bookId entered does not exist then continually ask user to enter bookId until a bookId that exists is entered
		while(!ALL_BOOKS.containsKey(bookId)){
			System.out.println("Book Id entered does not exist! Enter BookId: ");
			bookId = integer_input.nextInt();
		}
		
		/*Print statements to notify book has been found, the book's details, and its availabilty*/
		System.out.println("Book Found!");
		System.out.println(ALL_BOOKS.get(bookId).getBookTitle() + " | Written By: " + ALL_BOOKS.get(bookId).getAuthor().toString() + " | Published By: " + ALL_BOOKS.get(bookId).getPublisher());
		String availabilty = !ALL_BOOKS.get(bookId).getBorrowed() ? "Available" : "Currently Borrowed"; //stores whether book is currently borrowed or not.
		System.out.println(availabilty);
		
		if(!ALL_BOOKS.get(bookId).getBorrowed()){//if book is available to be borrowed then change it's status and set the date it should be returned (Next Month From Date of Borrowing)
			System.out.println("Enter Id of student borrowing the book: ");
			int studentId = getIntegerInput();//ensures user input is an integer value
			if(!studentExists(studentId)){ //if student Id entered does not exists
				System.out.println("Student Id entered does not exist. Do you want to add a new student? (Y --> Yes/ Any other button --> No): ");
				String user_option = string_input.next();
				if(user_option.equalsIgnoreCase("Y"))
					studentId = addNewStudent(); //adds a new student to the ALL_STUDENTS ArrayList and returns this new student's Id 
				else
					exit = true; //exit to main menu
			}
			
			/*Students cannot borrow more than one book at a time. Thus if the student's bookIdBorrowed is greater than 0 then
			 * it implies the student currently has a book borrowed.*/
			else if(ALL_STUDENTS.get(studentId).getBookIdBorrowed() > 0){ 
				StudentDetails student = ALL_STUDENTS.get(studentId); //stores student trying to checkout the book
				BookDetails bookBorrowed = ALL_BOOKS.get(student.getBookIdBorrowed()); //stores book currently borrowed by the student
				System.out.println("Sorry! Only one book can be borrowed at a time.");
				System.out.println(student.getFullName() + " currently has borrowed " + bookBorrowed.getBookTitle());
				System.out.println("Please come back later when " + bookBorrowed.getBookTitle() + " is returned");
				exit = true; //exit to main menu
			}
			
			/*If the method has not been exited at this point then it implies the student can be borrow the book.*/
			if(!exit){
				BookDetails book = ALL_BOOKS.get(bookId);
				Calendar return_date = new GregorianCalendar(); //get the current date
				return_date.add(Calendar.MONTH, 1); //set the return date to the next month from the current date
				book.setBorrowed(true); //indicates book has been borrowed
				book.setDateToReturn(return_date); //set book's return date
			
				ALL_STUDENTS.get(studentId).setBookIdBorrowed(book.getBookId()); //indicate that the student has borrowed this book
				book.setStudentBorrowed(studentId); //indicates book has been borrowed by the student with the given ID
				
				
				System.out.println("You Are All Set!");
				System.out.println(book.getBookId() + " | " + book.getBookTitle() + " is due " + SDF.format(return_date.getTime()));
			}
		}
		
		else
			System.out.println("Sorry check back later to borrow this book");	
	}
	
	/*Checks if studentId entered exists by checking if the given student Id entered does not lead to 
	 * an IndexOutOfBoundsException in the ALL_STUDENTS ArrayList*/
	private static boolean studentExists(int studentId){
		try{
			ALL_STUDENTS.get(studentId);
			return true;
		}
		catch(IndexOutOfBoundsException e){
			return false;
		}
	}
	
	/*Adds a new student to the database*/
	private static int addNewStudent(){
		System.out.println("===Add New Student To Database===");
		Scanner string_input = new Scanner(System.in); //gets String input from the user
		Scanner integer_input = new Scanner(System.in); //get integer input from the user
		StudentDetails new_student = new StudentDetails();
		
		String firstname;
		do{
			System.out.println("Enter new student's first name: ");
			firstname = string_input.next();
		}while(!new_student.setFirstName(firstname)); //loop until name input is valid
		
		String middlename;
		do{
			System.out.println("Enter new student's middle name (Type 'null' if author has no middle name): ");
			middlename = string_input.next();
		}while(!new_student.setMiddleName(middlename)); //loop until name input is valid
		
		String lastname;
		do{
			System.out.println("Enter new student's last name: ");
			lastname = string_input.next();
		}while(!new_student.setLastName(lastname)); //loop until name input is valid
		
		int grade;
		do{
			System.out.println("Enter new student's grade (9 - 12): ");
			grade = integer_input.nextInt();
		}while(!new_student.setGrade(grade)); //loop until name input is valid
		
		ALL_STUDENTS.add(new_student);
		int new_studentId = ALL_STUDENTS.size() - 1; //since student was the most recently added element to the ALL_STUDENTS ArrayList, its index should be ALL_STUDENTS.size() - 1. Student's index in this ArrayList should be the new student's Id.
		new_student.setId(new_studentId); //set the new student's Id
		
		return new_student.getId();
	}
	
	/*This method asks the user to enter the Id of the book being returned. If the records indicate that the given book was borrowed
	 * then it allows the user to enter the Id of the student doing the return. If the student Id matches the Id of the student who
	 * borrowed the book, then the method calculates how much the student should be fined and completes the return. */
	private static void returnBook(){
		System.out.println("===Book Return===");
		Scanner integer_input = new Scanner(System.in);
		
		System.out.println("Enter Id of book being returned: ");
		int bookId = getIntegerInput();//ensures user input is an integer value
		//if bookId entered does not exist then continually ask user to enter bookId until a bookId that exists is entered
				while(!ALL_BOOKS.containsKey(bookId)){
					System.out.println("Book Id entered does not exist! Enter BookId: ");
					bookId = integer_input.nextInt();
				}
		
		/*Print statements to notify book has been found, the book's details, and its availabilty*/
		System.out.println("Book Found!");
		System.out.println(ALL_BOOKS.get(bookId).getBookTitle() + " | Written By: " + ALL_BOOKS.get(bookId).getAuthor().toString() + "| Published By: " + ALL_BOOKS.get(bookId).getPublisher());
		String availabilty = !ALL_BOOKS.get(bookId).getBorrowed() ? "Available" : "Currently Borrowed"; //stores whether book is currently borrowed or not.
		System.out.println(availabilty);
		
		
		
		if(ALL_BOOKS.get(bookId).getBorrowed()){//if book was borrowed then validate student doing the book return, change the book's status, and calculate how much student should be fined 	
			BookDetails book = ALL_BOOKS.get(bookId);
			
			System.out.println();
			System.out.println("Enter Id of student returning the book: ");
			int studentId = getIntegerInput();//ensures user input is an integer value
			
			/*Verify student Id entered matches the Id of the student who borrowed the book*/
			if(studentId == book.getStudentBorrowed()){
				FineResult fine = calculateFine(book.getDateToReturn()); //calculate how many days overdue the book was returned and how much student should be fined.
																		//If book was returned on time then this method will return 0 for both the number of days overdue and the student's fine.
				book.setBorrowed(false); //indicates book has been returned and is now available to be borrowed by someone else
				book.resetDateToReturn();; //reset book's return date
				book.setStudentBorrowed(0); //reset to default value, indicating no student is current borrowing this book
				
				/*Display Statements to user indicating book was successfully returned, and how much student was fined*/
				System.out.println("You Are All Set!");
				System.out.println("Book Id: " + book.getBookId() + " | " + book.getBookTitle() + " has been returned");
				System.out.println(ALL_STUDENTS.get(studentId).getFullName() + " returned the book " + fine.days_overdue + " days overdue" + " | Fine Amount Charged: " + fine.days_overdue + " * " + FINE_RATE + " = GHc" + fine.fine);
				
				ALL_STUDENTS.get(studentId).setBookIdBorrowed(0); //reset to default value, indicating no book is currently borrowed by this student
				ALL_STUDENTS.get(studentId).addFine(fine.fine); //add the respective fine to this students fine total. If student accrued no fine on this return, fine value added will be 0.
				System.out.println("Total Fine on account: GHc" + ALL_STUDENTS.get(studentId).getTotalFine());
				/*If Student's total fine is greater than 0 then add student to the FINED_STUDENTS Hashtable*/
				addFinedStudentsToTable(ALL_STUDENTS.get(studentId));
			}
			else{
				System.out.println("ERROR Verifying Student Id: Only Student who borrowed the book can return this book");
			}
		}
		else
			System.out.println("ERROR: Can't return book that was not borrowed!");
	}
	
	/*Return type used by the calculateFine() method that will store the number of days overdue a book
	 * was returned and the fine accrued as a result.*/
	private static class FineResult{
		int days_overdue;
		double fine;
	}
	
	/*Checks if book was returned past the return date. And if it was overdue, then calculate how much the 
	 * student should be fined. If book was returned on time then this method will return 0 for both the
	 * number of days overdue and the student's fine.*/
	private static FineResult calculateFine(Calendar book_return_date){
		Calendar today = new GregorianCalendar();
		int elapsedDays = 0; //default value for number of days overdue the book was returned
		double fine = 0; //default value for the fine accrued by the student
		FineResult result = new FineResult(); //will store the number of days overdue book was returned and how much student will be fined as a result
		
		long return_in_millisecs = book_return_date.getTimeInMillis(); //book's return date in milliseconds
		long today_in_millisecs = today.getTimeInMillis(); //today's date in milliseconds
		long difference = today_in_millisecs - return_in_millisecs;
		
		if(difference > 0){//if today's date is past the return date
			elapsedDays =  (int) (difference / MILLISECS_IN_DAY); //get the number of days elapsed from book's correct return date to today
			fine = FINE_RATE * elapsedDays;
		}
		result.days_overdue = elapsedDays;
		result.fine = fine;
		return  result;
	}
	
	/*Authenticates if username and password combination entered is correct*/
	private static boolean authenticateUser(String username_input, String password_input){
		if(!username_input.equals(USERNAME) || !password_input.equals(PASSWORD)){
			System.out.println("Invalid Credentials Entered!");
			return false;
		}
		else //username and password entered were correct
			System.out.println("User Credentials Authenticated! Please wait while books and students are being loaded.");
		return true;
	}
	
	/*This loads all books in the book.txt file and stores each BookDetails object in the
	 * ALL_BOOKS ArrayList*/
	private  static boolean loadBooks() throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(BOOKS_FILE));
		boolean error_loading = false; //stores whether there was an error loading a book
		
		String line = reader.readLine(); //read the properties of a book in the books.txt file
		/*read books.txt file, set the input for each BookDetails object and add each BookDetails object to the ALL_BOOKS Hashtable*/
		while(line != null){
			BookDetails book = new BookDetails();
			String[] details = line.split("\t"); //the properties of each BookDetails object are separated by tab spaces in the books.txt file
			for(int i = 0; i < details.length; i ++){
				if(!setBookInput(i, details[i], book)){ //checks if there was an error in setting the BookDetails object's properties with the values in the books.txt file
					System.out.println("ERROR Loading Books");
					error_loading = true;
					break;
				}
			}
			if(error_loading) //if there was an error loading a book then stop the process of loading books
				break;
			ALL_BOOKS.put(book.getBookId(), book); //add BookDetails object to ALL_BOOKS
			addToPublisherTable(book.getPublisher(), book.getBookTitle()); //add book's title to the ALL_PUBLISHERS Hashtable
			addToGenreTable(book, book.getBookTitle());  //add book's title to the ALL_GENRES ArrayList
			addToAuthorTable(book.getAuthor().toString(), book.getBookTitle()); //add book's title to the ALL_AUTHORS Hashtable
			line = reader.readLine(); 
		}
		reader.close();
		
		if(!error_loading){
			System.out.println("Loading Books - Complete!");
			return true;
		}
		return  false; //error encountered while loading books
	}
	
	/*This method sets a BookDetails object's respective property based on the index of i.*/
	private static boolean setBookInput(int i, String value, BookDetails book){
		switch (i){
		case 0:
			book.setBookId(Integer.parseInt(value));
			return true;
		case 1:
			book.setBookTitle(value);
			return true;
		case 2:
			return book.setAuthorFirstName(value); //returns false if name input was invalid (contains non-alphabet characters)
		case 3:
			return book.setAuthorMiddleName(value); //returns false if name input was invalid (contains non-alphabet characters)
		case 4:
			return book.setAuthorLastName(value); //returns false if name input was invalid (contains non-alphabet characters)
		case 5:
			book.setPublisher(value);
			return true;
		case 6:
			return book.setGenre(value); //returns false if value for Genre is invalid
		case 7:
			return book.setCondition(value); //returns false if value for Condition is invalid
		case 8:
			book.setStudentBorrowed(Integer.parseInt(value));
			return true;
		case 9:
			String[] date = value.split("/"); //Dates are stored in the format yyyy/MM/dd
			int year = Integer.parseInt(date[0]);
			int month = Integer.parseInt(date[1]);
			int day = Integer.parseInt(date[2]);
			book.setDateToReturn(year,month,day);

			return true;
		case 10:
			boolean status = Boolean.parseBoolean(value);
			book.setBorrowed(status);
			return true;
		default: //error input, a book can only have up to 11 fields
			System.out.println("Invalid Property");
			return false;
		}	
	}
	
	/*This loads all students in the students.txt file and stores each StudentDetails object in the 
	 * ALL_Students ArrayList*/
	private  static boolean loadStudents() throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(STUDENTS_FILE));
		boolean error_loading = false; //stores whether there was an error loading a student
		
		String line = reader.readLine(); //read the properties of each student in the students.txt file
		/*read students.txt file, set the input for each studentDetails object and add each StudentDetails object to ALL_STUDENTS ArrayList*/
		while(line != null){
			StudentDetails student = new StudentDetails();
			String[] details = line.split("\t"); //the properties of each StudentDetails object are separated by tab spaces in the students.txt file
			for(int i = 0; i < details.length; i ++){
				if(!setStudentInput(i, details[i], student)){ //checks if there was an error in setting the StudentDetails object's properties with the values in the students.txt file
					System.out.println("ERROR Loading Students");
					error_loading = true;
					break;
				}
			}
			if(error_loading) //if there was an error loading a book then stop the process of loading books
				break;
			
			ALL_STUDENTS.add(student);
			/*Add Students who have been fined (total fine > 0) to the FINED_STUDENTS Hashtable*/
			addFinedStudentsToTable(student);
			line = reader.readLine();
		}
		reader.close();
		
		if(!error_loading){
			System.out.println("Loading Students - Complete!");
			return true;
		}
		return  false; //error encountered while loading books
	}

	/*Adds students whose total fines are greater than 0 to the FINED_STUDENTS Hashtable*/
	private static void addFinedStudentsToTable(StudentDetails student) {
		if(student.getTotalFine() > 0)
			FINED_STUDENTS.put(student.getId(), student.toString());
	}
	
	/*This method sets a StudentDetails object's respective property based on the index of i.*/
	private static boolean setStudentInput(int i, String value, StudentDetails student){
		switch (i){
		case 0:
			if(Integer.parseInt(value) < 1){//checks if value for student Id is < 1. Students cannot have Id numbers less than 1.
				System.out.println("ERROR: Invalid Student Id! Student Id cannot be less than 1.");
				return false;
			}
			student.setId(Integer.parseInt(value));
			return true;
		case 1:
			return student.setFirstName(value); //returns false if name input was invalid
		case 2:
			return student.setMiddleName(value); //returns false if name input was invalid
		case 3:
			return student.setLastName(value); //returns false if name input was invalid
		case 4:
			return student.setGrade(Integer.parseInt(value)); //returns false if grade input was invalid			
		case 5:
			/*check if bookId exists. If bookId exists set student's bookIdBorrowed property to bookId. If student has
			not borrowed a book, then bookId borrowed should be 0.*/
			if(ALL_BOOKS.containsKey(Integer.parseInt(value)) || Integer.parseInt(value) == 0){
				student.setBookIdBorrowed(Integer.parseInt(value));
				return true; //indicates no error was encountered when setting bookIdBorrowed
			}
			else{
				System.out.println("ERROR: BookId does not exist!");
				return false; //indicates error setting bookIdBorrowed
			}
		case 6:
			student.setTotalFine(Double.parseDouble(value));
			return true;
		default: //error input, a student can only have up to 7 fields
			System.out.println("Invalid Property");
			return false;
		}	
	}
}
