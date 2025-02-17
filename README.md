This program is a Java-based encryption and decryption system following a hyper-specific 8 step algorithm. 

ENCRYPT ALGORITHM:
1. Add the ID to the first five digits of the date, using no–carry addition
2. Expand the 5-digit result of (1) to 10 digits, using chain addition.
3. Use the phrase to create a digit permutation.
4. Add the results of (2) and (3) using no–carry addition.
5. Use the result from (4) to create a digit permutation.
6. Build a straddling checkerboard from (5)’s result and the anagram.
7. Use the straddling checkerboard to encode the message.
8. Insert the ID into the message


DECRYPT ALGORITHM:
1. Extract the ID from the message.
2. Perform steps 1 - 6 from the encoding algorithm.
3. Match the message digits to letters using the straddling checkerboard.
