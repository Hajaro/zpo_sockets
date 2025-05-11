package laby.lab3_javafx;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.json.JSONObject;
import org.json.JSONArray;


public class ServerController implements Initializable, Runnable {
    private BlockingQueue<Data> dataQueue;
    private ArrayList<QuestionData> questions;
    @FXML
    private VBox MainPane;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dataQueue = new ArrayBlockingQueue<>(10);
        Thread serverSocketReaderThread = new Thread(new ServerSocketReader(dataQueue));
        serverSocketReaderThread.start();
        Thread dataReaderThread = new Thread(this);
        dataReaderThread.start();
        try {
            questions = getQuestions();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Data data = dataQueue.take();
                consumeData(data);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    public void consumeData(Data data) {
        Label label = new Label();
        QuestionData currentQuestion = questions.getFirst();
        if (data.getAnswer().equals(currentQuestion.getAnswer())) {
            label.setText("Correct answer. Answered by: " + data.getUsername() + " from IP: " + data.getIp());
            if (questions.isEmpty()) {
                label.setText("All questions answered. Game over.");
                //TODO: Reset the game
            } else {
                questions.removeFirst();
            }
        } else {
            label.setText("Incorrect answer. Try again.");
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
//                MainPane.getChildren().clear();
                MainPane.getChildren().add(label);
            }
        });
//        MainPane.getChildren().add(label);

    }

    public ArrayList<QuestionData> getQuestions() throws IOException {
        ArrayList<QuestionData> questions = new ArrayList<QuestionData>();
        String jsonString = getStringFromFile();
        JSONArray json = new JSONArray(jsonString);
        for(int i=0; i<json.length(); i++){
            questions.add(new QuestionData(json.getJSONObject(i).getString("question"), json.getJSONObject(i).getString("answer")));
        }
        return questions;
    }

    private String getStringFromFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/data/questions.json"));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        reader.close();

        String content = stringBuilder.toString();
        return content;
    }
}
