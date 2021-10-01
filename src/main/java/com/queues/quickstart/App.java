package com.queues.quickstart;

/**
 * Azure Queue Storage client library v12 quickstart
 */

 // Include the following imports to use queue APIs
import com.azure.core.util.*;
import com.azure.storage.queue.*;
import com.azure.storage.queue.models.*;
import java.io.*;
import java.time.*;

public class App
{
    public static void main( String[] args ) throws IOException
    {
        System.out.println("Azure Queue Storage client library v12 - Java quickstart sample\n");

        // Retrieve the connection string for use with the application. The storage
        // connection string is stored in an environment variable on the machine
        // running the application called AZURE_STORAGE_CONNECTION_STRING. If the environment variable
        // is created after the application is launched in a console or with
        // Visual Studio, the shell or application needs to be closed and reloaded
        // to take the environment variable into account.

        String connectStr = System.getenv("AZURE_STORAGE_CONNECTION_STRING");

        
        // Create a unique name for the queue
        String queueName = "quickstartqueues-" + java.util.UUID.randomUUID();

        System.out.println("Creating queue: " + queueName);

        // Instantiate a QueueClient which will be
        // used to create and manipulate the queue
        QueueClient queueClient = new QueueClientBuilder()
                                        .connectionString(connectStr)
                                        .queueName(queueName)
                                        .buildClient();

        // Create the queue
        queueClient.create();

        //Add message to the Queue  _ this where the custom paylod will be added. 
        System.out.println("\nAdding messages to the queue...");

        // Send several messages to the queue
        queueClient.sendMessage("First message");
        queueClient.sendMessage("Second message");

        // Save the result so we can update this message later
        SendMessageResult result = queueClient.sendMessage("Third message");

        System.out.println("\nPeek at the messages in the queue...");

        // Peek all messages in queue. It is supposed to print "Hello World" 3 times.
       // queueClient.peekMessages(10, null, null).forEach(
           // peekedMessage -> System.out.println("Here is the msg: " + peekedMessage.getBody().toString()));

           // System.out.println("\nPeek at the messages in the queue...");

            // Peek at messages in the queue
            queueClient.peekMessages(10, null, null).forEach(
                peekedMessage -> System.out.println("Message: " + peekedMessage.getMessageText()));


                System.out.println("\nUpdating the third message in the queue...");

            // Update a message using the result that
            // was saved when sending the message
            queueClient.updateMessage(result.getMessageId(),
                                    result.getPopReceipt(),
                                    "Third message has been updated",
                                    Duration.ofSeconds(1));

                                    System.out.println("\nPeek at the messages in the queue again...");

                                    // Peek at messages in the queue
                                    queueClient.peekMessages(10, null, null).forEach(
                                        peekedMessage -> System.out.println("Message: " + peekedMessage.getMessageText()));


                                        System.out.println("\nPress Enter key to receive messages and delete them from the queue...");
                System.console().readLine();

                // Get messages from the queue
                queueClient.receiveMessages(10).forEach(
                    // "Process" the message
                    receivedMessage -> {
                        System.out.println("Message: " + receivedMessage.getMessageText());


                    /* 
                    
                    Here is where you can create and add message to new queue. 

                    // Create a unique name for the queue
                  String queueName = "quickstartqueues-" + java.util.UUID.randomUUID();

                        System.out.println("Creating queue: " + queueName);

                    // Instantiate a QueueClient which will be
                    // used to create and manipulate the queue
                    QueueClient queueClient = new QueueClientBuilder()
                                                    .connectionString(connectStr)
                                                    .queueName(queueName)
                                                    .buildClient();

        // Create the queue
        queueClient.create();

                    */
                        // Let the service know we're finished with
                        // the message and it can be safely deleted.
                        queueClient.deleteMessage(receivedMessage.getMessageId(), receivedMessage.getPopReceipt());
                    }
                );
    }

   
}