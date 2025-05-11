package laby.lab3_javafx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    private String SERVER_ADDRESS;
    private int SERVER_PORT;
    private static Socket socket;
    @FXML
    private Button SendButton;
    @FXML
    private TextField AnswerTextField;
    @FXML
    private TextField NameTextField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SERVER_ADDRESS = "localhost";
        SERVER_PORT = 8080;
        SendButton.setOnAction(event -> {
            try {

                Data data = setData(AnswerTextField.getText(),
                        NameTextField.getText());
                sendDataToServer(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
    private void sendDataToServer(Data data) throws IOException {
        connectToSocket();
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(data);
        objectOutputStream.flush();
        objectOutputStream.close();
        socket.close();

    }
    private void connectToSocket() throws IOException {
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
    }
    private Data setData(String answer, String name) throws UnknownHostException {
        return new Data(name, Inet4Address.getLocalHost().getHostAddress(), answer);
    }

}
