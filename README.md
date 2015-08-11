# VoterKeysGenerator
Second part of the [*MoCa QR*](https://github.com/CamiloG/moca_qr) Voting System project.

Generates both keys for the voter to sign her encryption of the ballot. The public is uploaded to the Bulletin Board server and the private is stored in the [SignatureApp](http://www.github.com/CamiloG/SignatureApp).

## Files
1. **GenerateKeys.java**: Main class of the program, where are all the logic and the methods to the generation, uploading and displaying of the public and private keys of each voter.

2. **GUILanterna.java**: Class that manages the Lanterna GUI environment, made to run on console-text-only devices (Raspberry PI for example).

3. **GUISwing.java**: Class for the creation of the object after the retrieving of the JSON from the Bulletin Board server.

## How to Use
* Download the .jar file [here](https://github.com/CamiloG/moca_qr/blob/master/KeyGeneration_Apps/VoterKeysGenerator_light.jar?raw=true).
* Put the file voterKeysGenerator.jar in the project folder.
* Execute voterKeysGenerator.jar with `$ java -jar voterKeysGenerator.jar`

### Configuration
* First of all you have to configure the root address for the Bulletin Board server. Select 'Configure Bulletin Board address'.
* The address is now shown on the top box of the window.

### Key Generation Process
* Select 'Generate Keys'
* The program asks the id of the voter who is creating their keys.
* Next, the program shows on screen a QR-Code containing the private key of the voter.
*Note: showing the QR-Code works now on a Java-Swing window, even if the program is running on a Lanterna environment*
* The voter needs to read this QR-Code with [SignatureApp](https://github.com/CamiloG/SignatureApp).
* Then, the program uploads the public key of the voter to the Bulletin Board server. After this, the program finishes.