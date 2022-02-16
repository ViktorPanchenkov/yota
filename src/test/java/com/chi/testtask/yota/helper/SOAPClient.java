package com.chi.testtask.yota.helper;

//import org.w3c.dom.Document;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class SOAPClient {

    private int httpCode;
    private String respond;

    public String send(String address, String soapAction, String request) throws Exception {

        HttpURLConnection connection;
        URL url = new URL(address);
        String line;
        connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
        connection.setRequestProperty("SOAPAction", soapAction);
        connection.setDoOutput(true);
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);

        PrintWriter pw = new PrintWriter(connection.getOutputStream());
        pw.write(request);
        pw.flush();

        connection.connect();
        Thread.sleep(2000);
        httpCode = connection.getResponseCode();


        BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        respond = rd.readLine();
        while ((line = rd.readLine()) != null) {
            //TODO: write to LOG
            //System.out.println(line + "\n");
            respond = line;
        }

        connection.disconnect();
        return respond;
    }

    public Integer getCurrentStatusCodeFromSoap() {
        return httpCode;
    }

    public String respondSoap() {
        return respond;
    }
}
