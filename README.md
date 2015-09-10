# Voter Keys Generator (moca-voter-keys-generator)
Second part of the [*MoCa QR*](http://mocaqr.niclabs.cl) Voting System project.

Generates both keys for the voter to sign her encryption of the ballot. The public is uploaded to the Bulletin Board server and the private is stored using [VoterApp](http://www.github.com/niclabs/moca-voter-app).

## Files
1. **GenerateKeys.java**: Main class of the program, where are all the logic and the methods to the generation, uploading and displaying of the public and private keys of each voter.

2. **GUIJavaFX.java**: Class that manages the JavaFX GUI environment. This environment also needs the presence of the following files: mainWindow.fxml, configWindow.fxml, generationWindow.fxml, MainWindowController.java, ConfigWindowController.java, GenerationWindowController.java, javaFx.css and background.jpg.

3. **VoterPublicKeyResponse.java**: Class for the creation of the Voter Public Key object after the retrieving of the JSON from the Bulletin Board server.

## External Libraries
1. **[ZXing](https://github.com/zxing/zxing)**: Java library for the 1D/2D barcode image process.

2. **[Gson](https://github.com/google/gson)**: Java library to convert Java Object to their JSON representation and viceversa.

## How to Use
* Download the .jar file [here](https://github.com/CamiloG/moca_qr/blob/master/KeyGeneration_Apps/VoterKeysGenerator_light.jar?raw=true).
* Put the file voterKeysGenerator.jar in the project folder.
* Execute voterKeysGenerator.jar with `$ java -jar voterKeysGenerator.jar`

### Configuration
* First of all you have to configure the root address for the Bulletin Board server. Select 'Configure Bulletin Board address'.
* The address is now shown on the top box of the main window.

### Key Generation Process
* Select 'Generate Keys'
* The program asks the id of the voter who is creating their keys.
* Next, the program shows on screen a QR-Code containing the private key of the voter.
* The voter needs to read this QR-Code with [VoterApp](https://github.com/niclabs/moca-voter-app).
* Then, the program uploads the public key of the voter to the Bulletin Board server. After this, the program finishes.
