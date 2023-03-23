/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.io.Serializable;

/**
 *
 * @author MSI
 */
public class ChatClient implements Serializable{
    
    
    
    int id;
    String username;
    String nickname;
    String email;
    //ImageIcon avatar;

    public ChatClient(int id, String username, String nickname, String email) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        //this.avatar = avatar;
    }

    
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

//    public ImageIcon getAvatar() {
//        return avatar;
//    }

 
    
    
    
    
    
    
}
