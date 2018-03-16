/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fabian Ardila
 */
public class HandleRequest implements Runnable {

    private Socket clientSocket = null;

    public HandleRequest(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        String output = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: ";/*text/html\r\n"
                + "Content-Length: ";*/
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String recurso = in.readLine();
            byte[] byteData = null;
            recurso = recurso.split(" ")[1];
            if (recurso.endsWith(".html")) {
                output += "text/html\r\nContent-Length: ";
                byteData = Files.readAllBytes(new File("./" + recurso).toPath());
                output += byteData.length + "\r\n\r\n";
            } else if (recurso.endsWith(".jpg")) {
                output += "image/jpg\r\nContent-Length: ";
                byteData = Files.readAllBytes(new File("./" + recurso).toPath());
                output += byteData.length + "\r\n\r\n";
            } else if (recurso.endsWith(".ico")) {
                output += "image/x-icon\r\nContent-Length: ";
                byteData = Files.readAllBytes(new File("./" + recurso).toPath());
                output += byteData.length + "\r\n\r\n";
            } else {
                byteData = Files.readAllBytes(new File("./index.html").toPath());
                output += byteData.length + "\r\n\r\n";
            }
            byte [] hByte = output.getBytes();
            byte[] rta = new byte[byteData.length + hByte.length];
            
            for (int i = 0; i < hByte.length; i++) {
                rta[i] = hByte[i];
            }
            
            for (int i = hByte.length; i < hByte.length + byteData.length; i++) {
                rta[i] = byteData[i - hByte.length];
            }
            clientSocket.getOutputStream().write(rta);
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(HandleRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
