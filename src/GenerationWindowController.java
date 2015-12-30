import com.google.zxing.WriterException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class GenerationWindowController {

    @FXML
    private TextField voter_id;

    @FXML
    public void handleParametersReadyButtonAction(ActionEvent actionEvent) throws NoSuchAlgorithmException, WriterException, IOException {

        String voterId = voter_id.getText();
        GenerateKeys.generateKeysAndUploadPublicKey(voterId);

        // Close window
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();

    }
}
