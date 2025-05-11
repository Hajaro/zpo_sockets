package laby.lab3_javafx;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class ServerSocketReader implements Runnable {
    private ServerSocket socket;
    private BlockingQueue<Data> dataQueue;

    public ServerSocketReader(BlockingQueue<Data> dataQueue) {
        this.dataQueue = dataQueue;
    }
    @Override
    public void run() {
        while(true) {
            try {
                Data data = getData();
                dataQueue.put(data);
                Thread.sleep(1000);

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private Data getData() throws IOException, ClassNotFoundException {
        connectToSocket();
        Socket clientSocket = socket.accept();
        InputStream inputStream = clientSocket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Data data =  (Data) objectInputStream.readObject();
        socket.close();
        return data;
    }
    private void connectToSocket() throws IOException {
        socket = new ServerSocket(8080);
    }
}
