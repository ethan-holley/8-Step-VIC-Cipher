/*=============================================================================
 |   Assignment:  Program # 1
 |       Author:  Ethan Holley (ethanholley@arizona.edu)
 |
 |       Course:  345 (Analysis of Discrete Structures), Fall 2024
 |   Instructor:  L. McCann
 |     Due Date:  9/19/24, at the beginning of class
 |
 |     Language:  Program Language: Java
 |     Packages:  java.io.File, java.util.ArrayList, java.util.Scanner
 |
 |  Compile/Run:  Compile: javac DecryptVIC.java
 |				  Run: Decrypt.java
 |
 +-----------------------------------------------------------------------------
 |
 |  Description:  This program's goal is to decrypt an inputed message from the
 |				command line. It first reads the path name and checks for errors
 |				within the file. For example, date from first line of input 
 |				file needs to be of length six or it produces an error. Within the file, 
 |				it consists of a date in the format YYMMDD (line1), a phrase (line 2), 
 |				an anagram consisting of length 10 with 2 spaces (line 3), and a message 
 |				(line 4). First, it extracts the agentID from the input message from line
 |				4 and updates both messageNoId and agentId variables accordingly. Then, it 
 |				calls the noCarryAddition method from the VICOperations.java file. This 
 |				method takes the agentID and first 5 chars of the Date String, and returns 
 |				a single string that adds together each integer value from the strings
 |				disregarding carry-ins. It is completed by adding the current digits 
 |				from each String together and diving by mod 10 to get just the first 
 |				digit of the added value. Next, the program uses the output from the 
 |				previous method and calls chainAddition. This method takes two inputs, 
 |				the previously calculated String and returns an extended version of 
 |				the String to length 10 by adding appending digits derived from the 
 |				number's original digits. Then the program calls the digitPermutation
 |				method which passes through the previous result from chainAddition and
 |				returns a permutation of the digits 0-9 based on the first 10 characters
 |				of the string. This method uses the algorithm Bubble Sort to sort the 
 |				indices of an array and then assign the digits to the corresponding
 |				characters. For the next two steps, the program calls both noCarryAddition
 |				and digitPermutation again using the previous outputs from the previous
 |				method calls (noCarryAddition uses previous two method call outputs and
 |				digitPermutation uses that noCarryAddition output as its argument). The 
 |				next method that is called is the straddlingCheckerboard which takes the
 |				second digitPermutation output (String) and the anagram (String) from 
 |				the file as inputs. It returns an ArrayList<String> where each letter
 |				in the alphabet is mapped to an integer. If the letter is in the anagram, 
 |				then the letter is mapped to the corresponding integer at that index.
 |				Otherwise, the first 10 letters of the alphabet not in the anagram
 |				get assigned the integer of the first space + the corresponding letter
 |				at the column index and the final 8 letters get assigned the integer of 
 |				the second space + the corresponding letter at the column index. Finally,
 |				the program calls one private method within the DecryptVIC class called
 |				decodeMessage. The decodeMessage method passes the messageNoId String from, 
 |				the previously calculated ArrayList, the anagram from the file, and the second
 |				permutation string result. It finds the spaces of the indices from the anagram
 |				and iterates over the message, extracts each integers corresponding arrayList
 |				index value to the letters in the alphabet, and uses adds it to the result
 |				String. It results in each of the letters calculated from the alphabet 
 |				concatenated into one String of upper case letters with no spaces and prints the
 |				result out in the main method.
 |                
 |        Input:  The user is required to input one command line argument consisting of a
 |				file of 4 lines: a date in the format YYMMDD (line 1), a phrase (line 2),
 |				an anagram consisting of length 10 with 2 spaces (line 3), and a 
 |				messageOriginal (line 4).
 |
 |       Output:  The output will produce a single line of the calculated decoded message with
 | 				no spaces and all capital letters.
 |
 |   Techniques:  One standard algorithm implemented is in the method call digitPermutation.
 |				In this method, Bubble Sort is used as a helper method to sort the indices 
 |				of an array and then assigns the digits to the correctly sorted characters.
 |
 |   Required Features Not Included: All required features are included.
 |
 |             
 |   Known Bugs:  None; the program operates correctly.
 |
 *===========================================================================*/

import java.io.File; // for access to file input
import java.util.ArrayList; // for checkerBoard array
import java.util.Scanner; // for file checking


public class DecryptVIC {
	
	public static int     ID_LEN          = 5;   // # of chars in agent ID
    public static int     DATE_LEN        = 6;   // # of chars in date
    public static int     PHRASE_LEN      = 10;  // # letters of phrase to use
    public static int     ANAGRAM_LEN     = 10;  // # of chars in anagram
    public static int     ANAGRAM_LETTERS = 8;   // # of letters in anagram

    public static char    SPACE           = (char)32; // space is ASCII 32
    public static boolean DEBUG           = false;    // toggle debug prints

    
	public static void main(String[] args) {
		// pass through command line argument
		VICData vicData = readVICData(args[0]);
        
        // get last digit of date string
		char getLastDigit = vicData.date.charAt(vicData.date.length() - 1);
		
		// convert last digit of date string to integer
		int lastDigitInt = getLastDigit - '0';
		
		// Extract agentID from original Message
		String agentID = vicData.messageOriginal.substring(lastDigitInt, lastDigitInt + 5);
		
		// create new String holding original message without the agent ID
		String messageNoId = vicData.messageOriginal.substring(0, lastDigitInt) + vicData.messageOriginal.substring(lastDigitInt + 5);
		
		// get noCarryAddition result String of adding agentID with the first 5 digits of date
		String firstNoCarry = VICOperations.noCarryAddition(agentID, vicData.date.substring(0,5));
		
		// expand firstNoCarry String from previous operation to 10 integers
		String expandResult = VICOperations.chainAddition(firstNoCarry, 10);
		
		// create a permutation String of the phrase input
		String firstPermutation = VICOperations.digitPermutation(vicData.phrase);
	
		// add together the expanded String and permutation String
		String secondNoCarry = VICOperations.noCarryAddition(expandResult, firstPermutation);
		
		// create a second permutation String of the second no carry addition result
		String secondPermutation = VICOperations.digitPermutation(secondNoCarry);
		
		// create array checkerBoard from second Permutation
		ArrayList<String> checkerBoard = VICOperations.straddlingCheckerboard(secondPermutation, vicData.anagram);
		
		// call decodeMessage method to return the decrypted message and print
	    String result = decodeMessage(messageNoId, checkerBoard, vicData.anagram, secondPermutation);
	    System.out.println(result);
	}
	
	
	/*--------------------------------------------------- decodeMessage -----
    |  Method decodeMessage (String, ArrayList<String>, String, String)
    |
    |  Purpose:  Uses the message from the input file and previously calculated 
    |		checker board to produce the encrypted message without the agentID. 
    |		Makes it easier to insert the agentID into the returned encrypted 
    |		message.
    |
    |  Pre-condition: The message from file with agentID extracted correctly, the
    |		ArrayList checkerBoard that maps letters to integers, the anagram for
    |		finding the indices of the spaces, the secondPermutation String
    |		to get the correct char of the space indices. 
    |
    |  Post-condition: The decoded message is returned in all caps with no spaces
    |		by extracting the substring of each character in the message, finding
    |		the index of the substring on the checker board, and adding the 
    |		corresponding letter in the alphabet that matches that index to the 
    |		result String.
    |
    |  Parameters:
    |       String message    -- message from line 4 of file, with agentID extracted
    |		checkerBoard      -- arrayList of mapped letters to integers
    |		anagram           -- line 3 of file input
    |		secondPermutation -- String result from second digitPermutation method call
    |	   
    |
    |  Returns:  an encrypted message without userID added
    *-------------------------------------------------------------------*/
	public static String decodeMessage(String message, ArrayList<String> checkerBoard, String anagram, String secondPermutation) {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // alphabet string to return corresponding char
		
		String result = ""; // result string to be added to and returned
		
		String messageNoId = message.trim(); // trim off extra space if there is any
		
		int getSpace1 = anagram.indexOf(" "); // find index of first space in anagram
		int getSpace2 = anagram.indexOf(" ", getSpace1 + 1); // find index of second space in anagram
		
		char space1 = secondPermutation.charAt(getSpace1); // get char num of space1 from permutation
		char space2 = secondPermutation.charAt(getSpace2); // get char num of space2 from permutation

		for (int i = 0; i < messageNoId.length(); i++) { // iterate through message without agentID
			
			// check if current char is the same num as the space character variables
			if (messageNoId.charAt(i) == space1 || messageNoId.charAt(i) == space2) { 
				
				String num = messageNoId.substring(i, i + 2); // add extra char to string num since its a space character
				
				int index = checkerBoard.indexOf(num); // get index of the num String from checkerBoard array
				
				char getChar = alphabet.charAt(index); // get corresponding char in alphabet from index
				
				result += getChar; // add char to result String
				
				i ++; // increment i twice to skip over extra letter added
			} else {
				String num = "" + messageNoId.charAt(i); // get singular char 
				
				int index = checkerBoard.indexOf(num); // get index of num String from checkerBoard array
				
				char getChar = alphabet.charAt(index); // get corresponding char in alphabet from index
				
				result += getChar; // add to result string
			}
		}
		return result;
	}
	
	
	/*---------------------------------------------------------------------
    |  Method readVICData (pathName)
    |
    |  Purpose:  VIC requires four pieces of information for an
    |            encoding to be performed: (1) date (form:YYMMDD), 
    |			 (2) a phrase containing at least 10 letters, (3) an 
    |			 anagram of 8 commonly-used letters and 2 spaces, and 
    |			 (4) a message of all integers in a String to be decoded. 
    |			 This method reads these pieces from the given filename 
    |			 (or path + filename), one per line, and all pieces are 
    |			 sanity-checked. When reasonable, illegal characters are 
    |			 dropped.  The program is halted if an unrecoverable problem 
    |			 with the data is discovered.
    |
    |  Pre-condition:  None.  (We even check to see if the file exists.)
    |
    |  Post-condition:  The returned VICData object's fields are populated
    |            with legal data.
    |
    |  Parameters:  pathName -- The filename or path/filename of the text
    |            file containing the file pieces of data.  If only a
    |            file name is provided, it is assumed that the file
    |            is located in the same directory as the executable.
    |
    |  Returns:  A reference to an object of class VICData that contains
    |            the five pieces of data.
    *-------------------------------------------------------------------*/

public static VICData readVICData (String pathName)
{
    VICData vic = new VICData(); // Object to hold the VIC input data
    Scanner inFile = null;       // Scanner file object reference

    try {
          inFile = new Scanner(new File(pathName));
    } catch (Exception e) {
          System.out.println("File does not exist: " + pathName + "!\n");
          System.exit(1);
    }

            // Read and sanity-check agent ID.  Needs to be ID_LEN long
            // and numeric.

//    if (inFile.hasNext()) {
//          vic.agentID = inFile.nextLine();
//    } else {
//          System.out.println("ERROR:  Agent ID not found!\n");
//          System.exit(1);
//    }
//
//    if (vic.agentID.length() != ID_LEN) {
//          System.out.printf("ERROR:  Agent ID length is %d, must be %d!\n",
//                            vic.agentID.length(),ID_LEN);
//          System.exit(1);
//    }
//
//    try {
//        long idValue = Long.parseLong(vic.agentID);
//    } catch (NumberFormatException e) {
//          System.out.println("Agent ID `" + vic.agentID 
//                           + "contains non-numeric characters!\n");
//          System.exit(1);
//    }

            // Read and sanity-check date.  Needs to be DATE_LEN long
            // and numeric.

    if (inFile.hasNext()) {
          vic.date = inFile.nextLine();
    } else {
          System.out.println("ERROR:  Date not found!\n");
          System.exit(1);
    }

    if (vic.date.length() != DATE_LEN) {
          System.out.printf("ERROR:  Date length is %d, must be %d!\n",
                            vic.date.length(),DATE_LEN);
          System.exit(1);
    }

    try {
        long dateValue = Long.parseLong(vic.date);
    } catch (NumberFormatException e) {
          System.out.println("Date `" + vic.date 
                           + "contains non-numeric characters!\n");
          System.exit(1);
    }

            // Read and sanity-check phrase.  After removing non-letters,
            // at least PHRASE_LEN letters must remain.

    if (inFile.hasNext()) {
          vic.phraseOriginal = inFile.nextLine();
          StringBuffer sb = new StringBuffer(vic.phraseOriginal);
          for (int i = 0; i < sb.length(); i++) {
              if (!Character.isLetter(sb.charAt(i))) {
                  sb.deleteCharAt(i);
                  i--;  // Don't advance to next index o.w. we miss a char
              }
          }
          vic.phrase = sb.toString().toUpperCase();
          if (vic.phrase.length() >= PHRASE_LEN) {
              vic.phrase = vic.phrase.substring(0,PHRASE_LEN);
          }
    } else {
          System.out.println("ERROR:  Phrase not found!\n");
          System.exit(1);
    }

    if (vic.phrase.length() != PHRASE_LEN) {
          System.out.printf("ERROR:  Phrase contains %d letter(s), "
                          + "must have at least %d!\n",
                            vic.phrase.length(),PHRASE_LEN);
          System.exit(1);
    }

            // Read and sanity-check anagram.  Must be ANAGRAM_LEN long,
            // and contain ANAGRAM_LETTERS letters and the rest spaces.

    if (inFile.hasNext()) {
          vic.anagram = inFile.nextLine().toUpperCase();
    } else {
          System.out.println("ERROR:  Anagram not found!\n");
          System.exit(1);
    }

    if (vic.anagram.length() != ANAGRAM_LEN) {
        System.out.printf("ERROR:  Anagram length is %d, must be %d!\n",
                          vic.anagram.length(),ANAGRAM_LEN);
        System.exit(1);
    }

    for (int i = 0; i < vic.anagram.length(); i++) {
        if (    !Character.isLetter(vic.anagram.charAt(i))
             && vic.anagram.charAt(i) != SPACE             ) {
            System.out.printf("ERROR:  Anagram contains character `%c'.\n",
                              vic.anagram.charAt(i) );
            System.exit(1);
        }
    }

    int numLetters = 0;
    for (int i = 0; i < vic.anagram.length(); i++) {
        if (Character.isLetter(vic.anagram.charAt(i))) {
            numLetters++;
        }
    }
    if (numLetters != ANAGRAM_LETTERS) {
        System.out.printf("ERROR:  Anagram contains %d letters, "
                        + "should have %d plus %d spaces.\n",
                          numLetters, ANAGRAM_LETTERS,
                          ANAGRAM_LEN - ANAGRAM_LETTERS);
        System.exit(1);
    }

            // Read and sanity-check message.  After removing non-letters
            // and capitalizing, at least one letter must remain.

    if (inFile.hasNext()) {
          vic.messageOriginal = inFile.nextLine();
          StringBuffer sb = new StringBuffer(vic.messageOriginal);
          for (int i = 0; i < sb.length(); i++) {
              if (!Character.isLetter(sb.charAt(i))) {
                  sb.deleteCharAt(i);
                  i--;  // Don't advance to next index o.w. we miss a char
              }
          }
          //vic.message = sb.toString().toUpperCase();
    } else {
          System.out.println("ERROR:  Message not found!\n");
          System.exit(1);
    }

//    if (vic.message.length() == 0) {
//          System.out.printf("ERROR:  Message contains no letters!\n");
//          System.exit(1);
//    }


    if (DEBUG) {
        //System.out.printf("vic.agentID = %s\n",vic.agentID);
        System.out.printf("vic.date = %s\n",vic.date);
        System.out.printf("vic.phrase = %s\n",vic.phrase);
        System.out.printf("vic.anagram = %s\n",vic.anagram);
        System.out.printf("vic.message = %s\n",vic.message);
    }

    return vic;
}


}
