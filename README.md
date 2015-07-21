# VoterKeysGenerator
Second part of the project [*Voter-Ballot Self Verification*](www.cjgomez.cl).

Generates the public key to encrypt the ballots, and all the shares on which the private key is separated, to distribute among all the authorities.

## Files
1. **GenerateKeys_CORE.java**: 
2. **GenerateKeys_light.java**:
3. **GenerateKeys_swing.java**:

## How to Use
* Download the .jar file [here](www.cjgomez.cl).
* Put the file voterKeysGenerator.jar in the project folder.
* Execute voterKeysGenerator.jar with `$ java -jar voterKeysGenerator.jar`
* The program asks the id of the voter who is creating their keys.
* Next, the program shows on screen a QR-Code containing the private key of the voter.
* The voter needs to read this QR-Code with the app [SignatureApp](www.github.com/CamiloG).
* Then, the program uploads the public key of the voter to the Bulletin Board server. After this, the program finishes.