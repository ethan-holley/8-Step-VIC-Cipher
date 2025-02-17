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
 |  Compile/Run:  Compile: javac EncryptVIC.java
 |				  Run: EncryptVIC
 |
 +-----------------------------------------------------------------------------
 |
 |  Description:  This program's goal is to encrypt an inputed message from the
 |				command line. It first reads the path name and checks for errors
 |				within the file. For example, ID_length from first line of input 
 |				file needs to be of length five or it produces an error. Within the file, 
 |				it consists of an AgentID (line1), a date in the format YYMMDD
 |				(line2), a phrase (line 3), an anagram consisting of length 10 with
 |				2 spaces (line 4), and a message (line5). First, it calls the 
 |				noCarryAddition method from the VICOperations.java file. This method
 |				takes the agentID and first 5 chars of the Date String, and returns 
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
 |				the program calls two private methods within the EncryptVIC class called
 |				encodeMessage and insertID. The encodeMessage method passes the message
 |				from line 5 of the input file and the previously calculated ArrayList 
 |				checker board. It iterates over the message, finds each letters corresponding 
 |				index in the alphabet, and uses that index to get the corresponding String 
 |				value from the checkerBoard ArrayList. It results in each of the mapped 
 |				letter values concatenated into one String. Finally, the method 
 |				insertID takes the last character of the date String from the file (line 2) 
 |				and inserts the agentID into the index of the current encodedMessage String
 |				and then the main method to print out the result. 
 |                
 |        Input:  The user is required to input one command line argument consisting of a
 |				file of 5 lines: AgentID (line1), a date in the format YYMMDD
 |				(line2), a phrase (line 3), an anagram consisting of length 10 with
 |				2 spaces (line 4), and a message (line5).
 |
 |       Output:  The output will produce a single line of the calculated encrypted message, 
 |				with the agentID from (line1) of the file hidden somewhere in the message. 
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

import java.io.File; // for file reading
import java.util.ArrayList; // for checkerBoard array
import java.util.Scanner; // for file checking


public class EncryptVIC {
	
	public static int     ID_LEN          = 5;   // # of chars in agent ID
    public static int     DATE_LEN        = 6;   // # of chars in date
    public static int     PHRASE_LEN      = 10;  // # letters of phrase to use
    public static int     ANAGRAM_LEN     = 10;  // # of chars in anagram
    public static int     ANAGRAM_LETTERS = 8;   // # of letters in anagram

    public static char    SPACE           = (char)32; // space is ASCII 32
    public static boolean DEBUG           = false;    // toggle debug prints

	public static void main(String[] args) {

        // Read VIC data from the input file
        VICData vicData = readVICData(args[0]);
        
        // stores the last integer of the date 
        char lastDateInt = vicData.date.charAt(vicData.date.length() - 1);
        
        // call noCarryAddition using agentID and first 5 integers of date
        String firstNoCarry = VICOperations.noCarryAddition(vicData.agentID, vicData.date.substring(0,5));
        
        // expand the firstnoCarry String to 10 integers using chainAddition
        String expandResult = VICOperations.chainAddition(firstNoCarry, 10);
        
        // create a permutation by calling digitPermutation with the phrase from the file
        String firstPermutation = VICOperations.digitPermutation(vicData.phrase);
        
        // call noCarryAddition again with expanded String and result from firstPermutation
        String secondNoCarry = VICOperations.noCarryAddition(expandResult, firstPermutation);
        
        // create a second permutation passing through the second noCarryAddition output
        String secondPermutation = VICOperations.digitPermutation(secondNoCarry);
        
        // create the ArrayList checkerBoard by passing through the secondPermutation and anagram from file
        ArrayList<String> checkerBoard = VICOperations.straddlingCheckerboard(secondPermutation, vicData.anagram);
        
        // call encodeMessage method to return the encrypted message
        String message = encodeMessage(vicData.message, checkerBoard);
        
        // insert the agentID into the message by using the index of the lastDateInt
        String result = insertId(message, lastDateInt, vicData.agentID);
        
        // print completed encrypted message
        System.out.println(result);
	}
	
	
	/*--------------------------------------------------- encodeMessage -----
    |  Method encodeMessage (String, ArrayList<String>)
    |
    |  Purpose:  Uses the message from the input file and previously calculated 
    |		checker board to produce the encrypted message without the agentID. 
    |		Makes it easier to insert the agentID into the returned encrypted 
    |		message.
    |
    |  Pre-condition:  The vicData.message from line 5 in the input file and 
    |		the checker board array representing the alphabet as mapped integers for
    |		each letter.
    |
    |  Post-condition: The vicData.message is returned as an encrypted message,
    |		getting the index of the correct letter of the alphabet and finding
    |		its corresponding mapped value in the checker board. 
    |
    |  Parameters:
    |       String message -- inputed vicData.message from line 5 of the file
    |		checkerBoard -- arrayList of mapped letters to integers
    |	   
    |
    |  Returns:  an encrypted message without userID added
    *-------------------------------------------------------------------*/

	private static String encodeMessage (String message, ArrayList<String> checkerBoard) {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // alphabet string for indexing
		
		String result = ""; // string being added to
		
		for (int i = 0; i < message.length(); i++) { // iterate through message
			
			char letter = message.charAt(i);  // get current letter in message
			
			int letterIndex = alphabet.indexOf(letter); // find index of letter in the alphabet
			
			String position = checkerBoard.get(letterIndex); // get the String from checkerBoard at letterIndex
			
			result += position; // add String to result
		}
		return result;
	}
	
	
	/*--------------------------------------------------- insertId -----
    |  Method insertId (String, char, String)
    |
    |  Purpose:  Returns the program's final message as it adds the agentID 
    |		to correct index in the String encryptMessage.
    |
    |  Pre-condition:  The encryptMessage from encodeMessage() method, the lastDigit
    |		of the date String from line 2 of input file, the agentId from line 1 of 
    |		the input file.
    |
    |  Post-condition: The overall encrypted message is returned by adding the agentId
    |		as a substring into the index of the last digit of the date from the
    |		encrypted message String.
    |
    |  Parameters:
    |       String encryptMessage -- output produced from encodeMessage method
    |		char lastDigit - last digit of the date String from file input
    |		String agentID - first line from file input
    |	   
    |
    |  Returns:  an encrypted message with userID added to correct index
    *-------------------------------------------------------------------*/
	private static String insertId(String encryptMessage, char lastDigit, String agentID) {
		
		int lastDigitInt = lastDigit - '0'; // convert last char from Date into an int
		
		// add the current encryptMessage up to lastDigit Index, add agentId, then add rest of encrypt
		String result = encryptMessage.substring(0, lastDigitInt) + agentID + encryptMessage.substring(lastDigitInt);
		
		return result;
	}
	
	
	
	/*---------------------------------------------------------------------
    |  Method readVICData (pathName)
    |
    |  Purpose:  VIC requires five pieces of information for an
    |            encoding to be performed:  (1) agent ID (form: #####),
    |            (2) date (form:YYMMDD), (3) a phrase containing at least
    |            10 letters, (4) an anagram of 8 commonly-used letters
    |            and 2 spaces, and (5) a message of at least one letter
    |            to be encoded.  This method reads these pieces from the
    |            given filename (or path + filename), one per line, and
    |            all pieces are sanity-checked.  When reasonable,
    |            illegal characters are dropped.  The program is halted
    |            if an unrecoverable problem with the data is discovered.
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

    if (inFile.hasNext()) {
          vic.agentID = inFile.nextLine();
    } else {
          System.out.println("ERROR:  Agent ID not found!\n");
          System.exit(1);
    }

    if (vic.agentID.length() != ID_LEN) {
          System.out.printf("ERROR:  Agent ID length is %d, must be %d!\n",
                            vic.agentID.length(),ID_LEN);
          System.exit(1);
    }

    try {
        long idValue = Long.parseLong(vic.agentID);
    } catch (NumberFormatException e) {
          System.out.println("Agent ID `" + vic.agentID 
                           + "contains non-numeric characters!\n");
          System.exit(1);
    }

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
          vic.message = sb.toString().toUpperCase();
    } else {
          System.out.println("ERROR:  Message not found!\n");
          System.exit(1);
    }

    if (vic.message.length() == 0) {
          System.out.printf("ERROR:  Message contains no letters!\n");
          System.exit(1);
    }


    if (DEBUG) {
        System.out.printf("vic.agentID = %s\n",vic.agentID);
        System.out.printf("vic.date = %s\n",vic.date);
        System.out.printf("vic.phrase = %s\n",vic.phrase);
        System.out.printf("vic.anagram = %s\n",vic.anagram);
        System.out.printf("vic.message = %s\n",vic.message);
    }

    return vic;
}

}
