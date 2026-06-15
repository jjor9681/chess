// Basically a copy paste from the petshop version of this file.
// When the server sends a ws message, facade forwards it here.
package client.websocket;

public interface NotificationHandler {

    void notify(String rawMessage);
}