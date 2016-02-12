
/*This abstract class represents a person. This abstract class contains fields and methods that should be common to 
 * both an author and a student.*/
public abstract class Person {
	protected int id;
	protected String firstname;
	protected String middlename;
	protected String lastname;
	
	protected void setId(int i){
		id = i;
	}
	protected int getId(){
		return id;
	}
	
	/*Sets firstname and returns true if f contains only letters. Returns false and
	 * leaves firstname blank if f contains non-alphabet characters.*/
	protected boolean setFirstName(String f){
		boolean name_valid = true; //stores whether f is correct input for a name
		name_valid = nameValid(f); //checks if f is correct input for a name. Returns false if f contains non-alphabet characters. 
		
		/*Set firstname if f contains no invalid character*/
	    if(name_valid) 
	    	firstname = f;
	    return name_valid;
	}
	protected String getFirstName(){
		return firstname;
	}
	
	/*Sets middlename and returns true if f contains only letters. Returns false and
	 * leaves middlename blank if f contains non-alphabet characters.*/
	protected boolean setMiddleName(String m){
		boolean name_valid = true; //stores whether m is correct input for a name
		name_valid = nameValid(m); //checks if m is correct input for a name. Returns false if f contains non-alphabet characters.
		
		/*Set middlename if f contains no invalid character*/
	    if(name_valid) 
	    	middlename = m;
	    return name_valid;
	}
	protected String getMiddleName(){
		return middlename;
	}
	
	/*Sets lastname and returns true if f contains only letters. Returns false and
	 * leaves lastname blank if f contains non-alphabet characters.*/
	protected boolean setLastName(String l){
		boolean name_valid = true; //stores whether l is correct input for a name
		name_valid = nameValid(l); //checks if l is correct input for a name. Returns false if f contains non-alphabet characters.
		
		/*Set lastname if l contains no invalid character*/
	    if(name_valid) 
	    	lastname = l;
	    return name_valid;
	}
	protected String getLastName(){
		return lastname;
	}
	
	/*Checks whether the name input was valid. If the name input contained non-alphabet characters then return
	 * false to indicate that the name input contains invalid characters. */
	private boolean nameValid(String l) {
		boolean name_valid = true;
		for(int i = 0; i < l.length(); i++){
			char c = l.charAt(i);
			/*if c is not an alphabet-character then display an error message and return false. */
			if( c < 'A' || (c > 'Z' && c < 'a') || c > 'z'){
				invalidNameError(); //print out error statement indicating name input was invalid
				return false;
			}
		}
		return name_valid;
	}
	
	/*Prints out error statement indicating name input was invalid*/
	private void invalidNameError(){
		System.out.println("Invalid Name Input: Name can only contain letters");
	}
}
