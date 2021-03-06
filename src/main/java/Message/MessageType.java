package Message;

import java.io.Serializable;

/**
 * @author Krishna Chaitanya Pullela, Manoj Mallidi, Benoit Gallet
 */
public enum MessageType implements Serializable
{
    READ_REQUEST,           // The client requests the amount of an account
    READ_RESULT,            // The server sends the amount of an account
    WRITE_REQUEST,          // The client requests to update the amount of an account
    WRITE_RESULT,           // The server sends the result of the update
    FINISH_TRANSACTION,     // Finish a transaction
    ABORT_TRANSACTION,      // Aborts the current transaction
    START_TRANSACTION,      // Start a new transaction between the client and the server
    CLOSE_TRANSACTION       // Close the connection between the client and the server
}
