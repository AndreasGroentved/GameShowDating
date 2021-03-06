package dating.innovative.gameshowdating.model;

/*  Andreas Jeppesen - ajepp09@student.sdu.dk
    Emil Jensen - emije14@student.sdu.dk
    Nicolai Jensen - nije214@student.sdu.dk */

public class Message {

    public String self;
    public String match;
    public String messageFromSelf;
    public String messageFromMatch;
    public String timestamp;

    public Message() {
    }

    public Message(String self, String match, String messageFromSelf, String messageFromMatch) {
        this.self = self;
        this.match = match;
        this.messageFromSelf = messageFromSelf;
        this.messageFromMatch = messageFromMatch;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getMessageFromSelf() {
        return messageFromSelf;
    }

    public void setMessageFromSelf(String messageFromSelf) {
        this.messageFromSelf = messageFromSelf;
    }

    public String getMessageFromMatch() {
        return messageFromMatch;
    }

    public void setMessageFromMatch(String messageFromMatch) {
        this.messageFromMatch = messageFromMatch;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
