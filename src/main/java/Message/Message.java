package Message;

import java.io.Serializable;

public class Message implements Serializable
{
    private final MessageType messageType;
    private final int value;

    public Message(MessageType messageType, int value)
    {
        this.messageType = messageType;
        this.value = value;
    }

    public MessageType getMessageType()
    {
        return messageType;
    }

    public int getValue()
    {
        return value;
    }
}
