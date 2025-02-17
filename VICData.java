/*+----------------------------------------------------------------------
||
|| Class VICData
||
|| Author: Ethan Holley
||
|| Purpose: This class is designed to store and manage essential data used
|| 		during the encryption and decryption process in the VIC cipher system.
||		It holds various components such as agent ID, date, phrase, anagram, and
|| 		the message (either to be encrypted or decrypted).
||
|| Inherits From: None
||
|| Interfaces: None
||
|+-----------------------------------------------------------------------
||
|| Constants: None
||
|+-----------------------------------------------------------------------
||
|| Constructors: 
||		VICData() -- initalizes components to hold necessary info for encryption
||				or decryption process
||		
||
|| Class Methods: None
||
|| Inst. Methods: None
||
++-----------------------------------------------------------------------*/

public class VICData {
		public String agentID; // first line of file (String)
	    public String date; // first or second line of file (String)
	    public String phrase; // second or third line of file (String)
	    public String phraseOriginal;
	    public String anagram; // third or fourth line of file (String)
	    public String message; // last line of file for encrypt (String)
	    public String messageOriginal; // last line of file for decrypt (String)
	} // class VICData

