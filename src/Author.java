import java.util.ArrayList;

/*This class represents an author.*/
public class Author extends Person{
	private ArrayList<BookDetails> books; //stores list of books written by author
	
	/*Adds a book to this author's list of books written*/
	public void addBook(BookDetails book){
		books.add(book);
	}
	
	public ArrayList<BookDetails> getBooks(){
		return books;
	}
	
	public String toString(){
		String middlename = !getMiddleName().equals("null") ? getMiddleName() : ""; //checks if author has a middlename. If not then set middlename to an empty string.
		return getFirstName() + " " + middlename + " " + getLastName();
	}
}
