
package services;

import interfaces.Message;
//import com.mysql.cj.protocol.Message;
import interfaces.ChatClient;
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
    
    
    
    
    
    
}
