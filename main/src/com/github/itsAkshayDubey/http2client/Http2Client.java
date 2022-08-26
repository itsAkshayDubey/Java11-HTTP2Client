package com.github.itsAkshayDubey.http2client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Http2Client {

    private static String URL = "http://www.example.com";
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final System.Logger LOGGER = System.getLogger(Http2Client.class.getName());

    public static void main(String[] args) {

        if(args.length>0)
            if(!(args[0].isEmpty()
                    || args[0].isBlank()
                    || args[0].equals(null)
                    || args[0]==""))
                URL = args[0];

        doSyncGetCall();
        doAsyncGetCall();
        doSyncPostCall();
        doAsyncPostCall();

    }

    private static void doAsyncPostCall() {
        LOGGER.log(System.Logger.Level.INFO,"Executing async POST call to "+URL+".");
        CompletableFuture<HttpResponse<String>> response = null;
        try {
            response = CLIENT.sendAsync(HttpRequest
                            .newBuilder(new URI(URL))
                            .POST(HttpRequest.BodyPublishers.noBody())
                            .build()
                    , HttpResponse.BodyHandlers.ofString()
            );

            while(!response.isDone()){
                LOGGER.log(System.Logger.Level.DEBUG,"Waiting . . .");
            }
            processResponse(response.get());
        }
        catch (URISyntaxException | ExecutionException | InterruptedException e) {
            LOGGER.log(System.Logger.Level.ERROR,"Error occred",e.getCause());
        }

    }

    private static void doSyncPostCall() {
        LOGGER.log(System.Logger.Level.INFO,"Executing sync POST call to "+URL+".");
        HttpResponse<String> response = null;
        try {
            response = CLIENT.send(HttpRequest
                            .newBuilder(new URI(URL))
                            .POST(HttpRequest.BodyPublishers.noBody())
                            .build()
                    , HttpResponse.BodyHandlers.ofString()
            );
            processResponse(response);
        }
        catch (IOException | InterruptedException | URISyntaxException e) {
            LOGGER.log(System.Logger.Level.ERROR,"Error occred",e.getCause());
        }
    }

    private static void doAsyncGetCall() {
        LOGGER.log(System.Logger.Level.INFO,"Executing async GET call to "+URL+".");
        CompletableFuture<HttpResponse<String>> response = null;
        try {
            response = CLIENT.sendAsync(HttpRequest
                            .newBuilder(new URI(URL))
                            .GET()
                            .build()
                    , HttpResponse.BodyHandlers.ofString()
            );

            while(!response.isDone()){
                LOGGER.log(System.Logger.Level.DEBUG,"Waiting . . .");
            }
            processResponse(response.get());
        }
        catch (URISyntaxException | ExecutionException | InterruptedException e) {
            LOGGER.log(System.Logger.Level.ERROR,"Error occred",e.getCause());
        }


    }

    private static void doSyncGetCall() {
        LOGGER.log(System.Logger.Level.INFO,"Executing sync GET call to "+URL+".");
        HttpResponse<String> response = null;
        try {
            response = CLIENT.send(HttpRequest
                            .newBuilder(new URI(URL))
                            .GET()
                            .build()
                    , HttpResponse.BodyHandlers.ofString()
            );
            processResponse(response);
        }
        catch (IOException | InterruptedException | URISyntaxException e) {
            LOGGER.log(System.Logger.Level.ERROR,"Error occred",e.getCause());
        }
    }

    private static void processResponse(HttpResponse<String> response) {
        LOGGER.log(System.Logger.Level.INFO,"Call to "
                +response.uri()+
                " is complete with status code "
                +response.statusCode()+".");
    }
}
