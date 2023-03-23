/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.mysql.cj.protocol.Message;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author MSI
 */
public interface Chat extends Remote{
    
    
    public void send_message(Message msg) throws RemoteException;

    public Message broadcast() throws RemoteException;
    
    public void subscribre(int group_id,ChatClient c) throws RemoteException;
    
    public void unsubscribre(int group_id,ChatClient c) throws RemoteException;
    
    public boolean is_subscribed(int client_id) throws RemoteException;

    public static class ChatClient {

        public ChatClient() {
        }

        int getId() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
    
    
    
    
    
}
