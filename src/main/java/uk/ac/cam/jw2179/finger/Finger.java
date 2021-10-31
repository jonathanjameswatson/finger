package uk.ac.cam.jw2179.finger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Finger {

  private static final String errorMessage = "This application requires one argument: <user@host>";

  public static void main(String[] args) {

    if (args.length == 0) {
      System.err.println(errorMessage);
      return;
    }

    final String userAtHost = args[0];
    final String[] userAndHost = userAtHost.split("@");
    if (userAndHost.length != 2) {
      System.err.println(errorMessage);
      return;
    }

    final String user = userAndHost[0];
    final String host = userAndHost[1];

    try {
      final Socket socket = new Socket(host, 79);
      final InputStream inputStream = socket.getInputStream();
      final OutputStream outputStream = socket.getOutputStream();

      byte[] bytes = (user + "\r\n").getBytes();
      outputStream.write(bytes);

      byte[] buffer = new byte[1024];
      int bytesRead = inputStream.read(buffer);
      String text = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
      System.out.println(text);
      inputStream.close();
      outputStream.close();
    } catch (IOException e) {
      System.err.printf("Cannot connect to %s on port 79\n", host);
    }

  }
}
