import java.util.*; // allows for method access

/*+----------------------------------------------------------------------
||
|| Class VICOperations
||
|| Author: Ethan Holley
||
|| Purpose: This class exists to assist in both encrypting and decrypting 
||		messages based on file input from the user. It performs methods such
||		as noCarryAddition, chainAddition, digitPermutation, Bubble Sort, 
||		and straddlingCheckerBoard. These methods were used for the
||		VIC Cipher Algorithm.
||
|| Inherits From: None
||
|| Interfaces: None
||
|+-----------------------------------------------------------------------
||
|| Constants: None
||		  					
||
|+-----------------------------------------------------------------------
||
|| Constructors: None
||
|| Class Methods:
||		- straddlingCheckerboard(String digits, String anagram) returns ArrayList<String>
||        Maps the letters of the alphabet to numbers based on a digit permutation
||        and an anagram with two spaces to form a checkerboard used in encryption.
||
||		- digitPermutation(String input) returns String
||        Takes a string of length 10 and creates a digit permutation by assigning
||        a unique number to each character based on their alphabetical order.
||
||		- bubbleSort(List<Integer> indices, char[] chars) returns void
||        Helper method for sorting the indices of a string based on their corresponding
||        characters using the Bubble Sort algorithm.
||
||		- chainAddition(String num, int length) -> String
||        Extends the length of a given string by repeatedly adding adjacent digits 
||        without carrying over and continues until the string reaches the specified
||        length.
||
||		- noCarryAddition(String num1, String num2) returns String
||        Adds two strings representing numbers, but ignores any carry over between 
||        digits during addition.
||
|| Inst. Methods: None
||
++-----------------------------------------------------------------------*/

public class VICOperations {
	
	/*--------------------------------------------------- straddlingCheckerboard -----
    |  Method straddlingCheckerboard (String, String)
    |
    |  Purpose:  This method maps letters to integers using a digitPermuation and short
    |		anagram as arguments. The rest of the grid is composed of the unused 
    |		alphabetical letters labeled by the row number that the spaces are in from the
    |		anagram. 
    |		 
    |
    |  Pre-condition:  The method expects a two Strings, the first being a digitPermuation
   	|		of length 10, the second being a 10 character anagram containing two spaces.
    |
    |  Post-condition: Each character in the alphabet is matched to a corresponding integer,
    |		leading zeros is possible. 
    |
    |  Parameters:
    |		String digits -- the digit permutation matching indices to chars
    |		String anagram -- the anagram string being matched to a checker board index
    |   
    |
    |  Returns:  an array list of the checker board index for each letter in alphabet
    *-------------------------------------------------------------------*/
	public static ArrayList<String> straddlingCheckerboard (String digits, String anagram) {
		
		// check lengths of inputs
		if (digits.length() != 10 || anagram.length() != 10) {
			return null;  
		}
		
		anagram = anagram.toUpperCase(); // make anagram all upper case
		
		int numSpaces = 0;      // int to count num of spaces in anagram
		
		int firstSpaceIndex = -1; // variable for first space index
		int secondSpaceIndex = -1; // variable for second space index
		
		for (int i = 0; i < anagram.length(); i ++) {  // iterate through anagram
			if (anagram.charAt(i) == ' '){   
				numSpaces ++;   // incr numSpaces if char is a space
				
				if (firstSpaceIndex == -1) {
					firstSpaceIndex = i; // get index of first space
				} else {
					secondSpaceIndex = i; // get index of second space
				}
			} 
		}
		
		if (numSpaces != 2) {
			return null;     // null if space count is not 2
		}
		
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // create alphabet string
		
		// create Array List with size of alphabet
		 ArrayList<String> result = new ArrayList<>(Collections.nCopies(26, null));
		
		int secondRow = 0; // int of num letters in second row
		int column = 0; // int to keep track of column
		
		for (int i = 0; i < alphabet.length(); i++) { // iterate over each letter in alphabet
			
			char currentLetter = alphabet.charAt(i); // get current Letter
			int anagramIndex = anagram.indexOf(currentLetter); // get index of letter in anagram
			
			if (anagramIndex != -1) {    // if letter is in anagram string
				result.set(i, String.valueOf(digits.charAt(anagramIndex))); // add the corresponding digit that is the same index from anagram
			} else {
				if (secondRow < 10) { // keep track of secondRow spaces
					
					// add firstSpace Index + digits[column] to correct letter in result
					result.set(i, String.valueOf(digits.charAt(firstSpaceIndex) + String.valueOf(digits.charAt(column))));
					
				} else {
					if (column == 10) { // reset column to go to new row
						column = 0;
					}
					
					// add secondSpaceIndex + digits[column] to correct letter in result
					result.set(i,  String.valueOf(digits.charAt(secondSpaceIndex) + String.valueOf(digits.charAt(column))));				
				}
				
				column ++; // increment column count
				secondRow++; // increment row count
			}
		}
		return result;
	}
	
	
	 /*--------------------------------------------------- digitPermutation -----
    |  Method digitPermutation (String)
    |
    |  Purpose:  This method organizes a string of letters into a digit Permutation
    | 			based on it's earliest letter in the alphabet. The string must be of length
    |		10 and assigns its corresponding digit to that specific letter. The method
    |		reads letters from left to right and if there are multiple letters that are
    |		the same in the String, it assigns the lower value to the first occurrence of
    |		that letter.
    |		 
    |
    |  Pre-condition:  The method expects a String of length 10 and if shorter will
    |		return null.
    |
    |  Post-condition: Each letter in the input String is assigned a corresponding value
    |		that is returned in the final String all together. 
    |
    |  Parameters:
    |		String input -- the String that is getting modified
    |   
    |
    |  Returns:  a new string of the corresponding matching digits from the input string
    *-------------------------------------------------------------------*/
	public static String digitPermutation(String input) {
		
	    // check if the input string has at least 10 characters
		if (input.length() < 10) {
	        return null;
	    }
		
		input = input.replaceAll("\\p{P}", ""); // remove punctuation
		input = input.replace(" ", "");  // remove spaces
		input = input.toUpperCase(); // make entire string upper case
		
	    // create an array to store characters and their original positions
	    char[] chars = input.substring(0, 10).toCharArray();
	    
	    // array to store original indices 
	    List<Integer> originalIndices = new ArrayList<>(); 
	    
	    // add index values to original index array
	    for (int i = 0; i < chars.length; i++) {
	        originalIndices.add(i);
	    }

	    // sort the originalIndices list based on the characters at those indices
	    bubbleSort(originalIndices, chars);
	    
	    // array to store the result
	    char[] result = new char[10];

	    // assign digits 0-9 to the sorted characters
	    for (int i = 0; i < 10; i++) {
	    	result[originalIndices.get(i)] = Character.forDigit(i, 10); // base 10 digits
	    }
	    
	    // return the result as a String
	    return new String(result);
	}
	
	 /*--------------------------------------------------- bubbleSort -----
    |  Method bubbleSort (String)
    |
    |  Purpose:  This method is a helper method for the digitPermutation. It sorts
    |		the origingalInices list checking the characters at those indices using 
    |		the algorithm bubble sort.  
    |		 
    |
    |  Pre-condition:  The method expects an two arrays, one of indices of length 10
    |		and one of chars from the original input String.
    |
    |  Post-condition: Sorts the indices array by comparing the current position to the
    |		position 1 to the right, swapping the positions if the current position index 
    |		is greater than the other index.
    |
    |  Parameters:
    |		indices - an array of orginalIndices list
    |		chars - an array of the the chars from the input String
    |   
    |  Returns: None
    *-------------------------------------------------------------------*/
	private static void bubbleSort(List<Integer> indices, char[] chars) {
	    int n = indices.size();  // n is the size of indices list
	    
	    for (int i = 0; i < n - 1; i++) { // iterate until second to last element of indices
	    	
	        for (int j = 0; j < n - i - 1; j++) { // loop to compare adjacent elements
	        
	            int index1 = indices.get(j);   // index 1 is current element
	            
	            int index2 = indices.get(j + 1); // index 2 is element after index 1
	            
	            if (chars[index1] > chars[index2]) { // compare indices and swap if meets condition
	            	
	            	int temp = indices.get(j);  // store the value at index j in temp
	            	
	                indices.set(j, indices.get(j + 1)); // move value at index j+1 to index j
	                
	                indices.set(j + 1, temp);   // set the stored temp value to index j+1
	            }
	        }
	    }
	}


	 /*--------------------------------------------------- chainAddition -----
    |  Method chainAddition (String, int)
    |
    |  Purpose:  This method extends the length of a number by adding
    | 		digits derived from the number, in this case, String integer's, 
    | 		original digits. It uses the method noCarryAddition for implementation.
    |
    |  Pre-condition:  The method expects a String of only integers and n of 
    		any integer.
    |
    |  Post-condition: The given String is extended to length n. If the number has
    |		contains just one digit, zero is assumed as the preceding digit. If
    |		argument n is less than the length of the String, then it returns 
    |		a prefix of that length.
    |
    |  Parameters:
    |		String integer -- the String that is being modified from the addition
    |       n (IN) -- the integer that extends String to its length
    |
    |  Returns:  a string extended to length n using chain addition
    *-------------------------------------------------------------------*/
	public static String chainAddition(String num, int length) {
		if (num.length() == 1) {
	        num = num + num;  // add num to current String to make length 2
	    }
		
	    StringBuilder result = new StringBuilder(num);  // result String being returned

	    while (result.length() < length) {
	    	
	        StringBuilder temp = new StringBuilder(); // temp string for addition
	       
	        int originalLength = result.length();  // stores the original length of result String
	        
	        for (int i = originalLength - num.length(); i < result.length() - 1; i++) { // Only consider newly added digits for addition
	            
	        	String num1 = Character.toString(result.charAt(i)); // get current digit
	        	
	            String num2 = Character.toString(result.charAt(i + 1)); // get digit after digit1
	          
	            String noCarrySum = noCarryAddition(num1, num2); // call noCarryAddition
	            
	            // append only the last digit of the noCarryAddition result to temp
	            temp.append(noCarrySum.charAt(noCarrySum.length() - 1));
	        }
	        result.append(temp);  // append the noCarrySum's last digit to result String
	    }
	    
	    return result.substring(0, length); // get result String from 0 to length n
	}


	
	 /*--------------------------------------------------- noCarryAddition -----
    |  Method noCarryAddition (String, String)
    |
    |  Purpose: This method adds two integers together without using a carry when
    |		adding. Like normal addition, it starts in the ones place and adds the 
    |		two integers together, however if it is a number that requires a carry
    |		over to the next column, we discard the carry and take the first
    |		number. Used as a helper function as well for the chainAddition method.
    |
    |  Pre-condition: Two different strings passed as arguments where
    |			String num1 represents the first string and String num2 represents
    |			the second string.
    |
    |  Post-condition: String num1 and String num2 are added together, discarding
    |			the carry over resulting in a new String which can have values with
    |			leading zeroes.
    |
    |  Parameters:
    |      		String num1 (IN) -- the first string consisting of all integers
    |	   		String num2 (IN) -- the second string consisting of all integers
    |
    |  Returns:  a new string where num1 and num2 have been added together
    |			discarding any carry overs when doing addition.
    *-------------------------------------------------------------------*/
	public static String noCarryAddition(String num1, String num2) {
		
		StringBuilder stringSum = new StringBuilder(); // create empty StringBuilder to add to
		
		int difference = Math.abs(num1.length() - num2.length()); // int difference in length between two strings
		
		if (num1.length() < num2.length()) { // add number of leading zeros to shorter string
			num1 = "0".repeat(difference) + num1;
		} else if (num2.length() < num1.length()) {
			num2 = "0".repeat(difference) + num2;
		}
		
		for (int i = 0; i < num1.length(); i++) {
			int digit1 = num1.charAt(i) - '0'; // convert digit1[i] to int
			int digit2 = num2.charAt(i) - '0'; // convert digit2[i] to int
			
			int sum = (digit1 + digit2) % 10; // get noCarry integer and append to string_sum
			stringSum.append(sum);
		}
		return stringSum.toString(); 
	}

}
