package edu.escuelaing.arep;

import java.net.*;
import java.io.*;



public class HttpServer {



    public static void main (String [ ] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        boolean running = true;

        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;
            boolean primeraLinea = true;
            String file = "";
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                if (primeraLinea) {
                    file = inputLine.split(" ")[1];
                    System.out.println("File: " + file);
                    primeraLinea = false;
                }
                if (!in.ready()) {
                    break;
                }
            }



            if (file.startsWith("/consulta?lugar=")) {
                obtenerTemperatura(file);
            } else {
                outputLine = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + "<!DOCTYPE html>"
                        + "<html>"
                        + "<head>"
                        + "<meta charset=\"UTF-8\">"
                        + "<title>No se encontro la pagina</title>\n"
                        + "</head>"
                        + "<body>"
                        + "My Web Site"
                        + "</body>"
                        + "</html>";
            }
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    private static void obtenerTemperatura(String file) throws IOException {
        String [] ciudadOLugar =file.split("=");
        String conexionOpenWeather = "https://api.openweathermap.org/data/2.5/weather?q="
                + ciudadOLugar[1] + "&appid=bc337958c219159e9a81d75502e4204d";
        crearUrl(conexionOpenWeather);
    }

    private static void crearUrl(String conexionOpenWeather) throws IOException {
        URL newurl = new URL(conexionOpenWeather);
        HttpURLConnection conectar = (HttpURLConnection) newurl.openConnection();
        conectar.setRequestMethod("GET");
        conectar.getResponseCode();

    }


}
