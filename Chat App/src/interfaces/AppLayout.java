/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package interfaces;


//import com.mysql.cj.protocol.Message;
import dbmanager.DBManager;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.AdjustmentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import pojos.Groups;
import pojos.Users;
import services.Chat;
import services.ChatService;

/**
 *
 * @author MSI
 */
public class AppLayout extends javax.swing.JFrame {

    /**
     * Creates new form AppLayout
     */
   
    int id;
    Registry reg;
    Chat chat;
    int active_group;

    static int xx, yy;
   
    ChatClient me;
    
    public AppLayout() {
        initComponents();
        
        
        textusername.setBackground(new java.awt.Color(0,0,0,1));
        textpassword.setBackground(new java.awt.Color(0,0,0,1));
        textregemail.setBackground(new java.awt.Color(0,0,0,1));
        textregusername.setBackground(new java.awt.Color(0,0,0,1));
        textregnickname.setBackground(new java.awt.Color(0,0,0,1));
        textregpassword.setBackground(new java.awt.Color(0,0,0,1));
        textgroupname.setBackground(new java.awt.Color(0,0,0,1));
        textgroupdescription.setBackground(new java.awt.Color(0,0,0,1));
    
        edit_username.setBackground(new java.awt.Color(0,0,0,1));
        edit_nickname.setBackground(new java.awt.Color(0,0,0,1));
        edit_password.setBackground(new java.awt.Color(0,0,0,1));
        client_chat_groups_panel.setBackground(new java.awt.Color(0,0,0,1));
        msg_typer.setBackground(new java.awt.Color(0,0,0,1));
        
        
        login_panel.setVisible(true);
        register_panel.setVisible(false);
        admin_panel.setVisible(false);
        create_chat_panel.setVisible(false);
        list_groups_panel.setVisible(false);
        edit_profile_panel.setVisible(false);
        manage_users_panel.setVisible(false);
        chat_panel.setVisible(false);
        
        
    }
    
    //added
    
    public void app_ui_reset(){
        login_panel.setVisible(false);
        register_panel.setVisible(false);
        admin_panel.setVisible(false);
        create_chat_panel.setVisible(false);
        list_groups_panel.setVisible(false);
        chat_panel.setVisible(false);
        edit_profile_panel.setVisible(false);
        manage_users_panel.setVisible(false);
        
    }
    
    //added
    
    public ImageIcon toImageIcon(byte[] img) {
        BufferedImage Imgavatar;
        ImageIcon avatar = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(img);
            Imgavatar = ImageIO.read(bis);
            if (Imgavatar != null) {
                avatar = new ImageIcon(Imgavatar);
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return avatar;
    }
    
    
    //done
    public BufferedImage ImageIconToBufferedImage(ImageIcon icon) {
        BufferedImage bi = new BufferedImage(
                icon.getIconWidth(),
                icon.getIconHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.createGraphics();
        // paint the Icon for BufferedImage.
        icon.paintIcon(null, g, 0, 0);
        g.dispose();

        return bi;
    }
    
    //done
    public void showUsers(){
          List data = DBManager.getDBM().allUsers();

     JTable tbl = new JTable();
     DefaultTableModel table_users = new DefaultTableModel(0, 0);
     String header[] = new String[] { "Prority", "Task Title", "Start",
      "Pause", "Stop", "Statulses" };
      table_users.setColumnIdentifiers(header);
      tbl.setModel(table_users);



          for (Iterator iterator = data.iterator(); iterator.hasNext();){
            Users user = (Users) iterator.next(); 
            
            table_users.addRow(new Object[] { "data", "data", "data",
        "data", "data", "data" });
 
          }
          
          
      }
    
    
    //done
    public ArrayList<String> validatelogin(String username, String password) {
        ArrayList<String> errors = new ArrayList<>();

        if ("Username".equals(username) || "".equals(username)) {
            errors.add("Username is requird");
        }

        if ("Password".equals(password) || "".equals(password)) {
            errors.add("Password is requird");
        }

        return errors;
    }
    
    //done
    
    public void start_client() {

        try {
            reg = LocateRegistry.getRegistry("localhost", 2123);
            chat = (Chat) reg.lookup("ChatAdmin");

        } catch (RemoteException | NotBoundException ex) {
            System.out.println(ex);
        }

    }
    
    //done
    
    public void sender() {
        String m = msg_typer.getText();
        if (m.equalsIgnoreCase("bye")) {
            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String time_now = myDateObj.format(myFormatObj);

            
            Message msg = new Message();
            msg.setDate_time(time_now);
            String user = me.getUsername();
            m = "****** " + user  + " has left the chat " + " ******";

       
            try {
                chat.unsubscribre(enterd_grup_id, me);
            } catch (RemoteException ex) {
                Logger.getLogger(AppLayout.class.getName()).log(Level.SEVERE, null, ex);
            }

            
            app_ui_reset();
            login_panel.setVisible(true);


            System.out.println(m);
            msg.setMessage(m);
            
  
            
            JScrollBar vertical = msgScrollPane.getVerticalScrollBar();
            msgScrollPane.setMaximumSize(vertical.getMaximumSize());
        

            try {
                chat.send_message(msg);
                
                msg_typer.setText("");
            } catch (RemoteException ex) {
                System.out.println(ex);
            }
            
            msgScrollPane.getVerticalScrollBar().addAdjustmentListener((AdjustmentEvent e) -> {
            e.getAdjustable().setValue(e.getAdjustable().getMaximum());
        });
        } else {

            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String time_now = myDateObj.format(myFormatObj);

            Message msg = new Message();
            msg.setGroup_id(enterd_grup_id);
            msg.setMsgid(msg.hashCode());
            msg.setUserid(me.getId());
            msg.setName(me.getUsername());
            msg.setMessage(m);
            msg.setDate_time(time_now);

            try {
                chat.send_message(msg);
                
                
           
        
                msg_typer.setText("");
            } catch (RemoteException ex) {
                System.out.println(ex);
            }
        }
        


    }
    
    //added
    public ArrayList<String> validateform(String email, String username,String password) {

        ArrayList<String> errors = new ArrayList<>();

        if (email.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$") == false) {
            errors.add("Invalid email");
        }

        if ("Username".equals(username) || "".equals(username)) {
            errors.add("Username is requird");
        }

        if ("Password".equals(password) || "".equals(password)) {
            errors.add("Password is requird");
        }

        if (password.length() < 4) {
            errors.add("Password must contain more than 4 characters");
        }

        return errors;
    }
    
    int y = 13;
        
        public void load_admin_group(boolean is_called_signin) {
            
         y = 13;
         List groups = DBManager.getDBM().get_chat_groups();

         admin_group_list.removeAll();

         
            for (Iterator iterator = groups.iterator(); iterator.hasNext();) {
            Groups next = (Groups) iterator.next();

            if (is_called_signin) {
             
                DBManager.getDBM().put_offline(next.getId());
            }

            JPanel group = new javax.swing.JPanel();
            group.setBackground(new java.awt.Color(66, 72, 245));
        
            group.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

            JLabel g_action = new javax.swing.JLabel();
            g_action.setCursor(new Cursor(Cursor.HAND_CURSOR));

            if (DBManager.getDBM().is_online(next.getId())) {
                g_action.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/end.png"))); 
            } else {
                g_action.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/start.png")));
            }

            g_action.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                
                    active_group = next.getId();
                    group_action(next.getId(), g_action);

                }
            });
            

            JLabel g_des = new javax.swing.JLabel();
            g_des.setForeground(new java.awt.Color(255, 255, 255));
            g_des.setText("<html>" + next.getDescription() + "</html>");
            
            

            JLabel g_name = new javax.swing.JLabel();
            g_name.setFont(new java.awt.Font("Tahoma", 1, 13)); 
            g_name.setForeground(new java.awt.Color(255, 255, 255));
            g_name.setText(next.getName());
            group.add(g_action, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 30, -1, 29));
            group.add(g_des, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 36, 163, 33));
            group.add(g_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 13, 160, -1));
            admin_group_list.add(group, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, y, 294, 90));
            

            y += 110;
        
        
        
         }
        }
        
        
        
        public void group_action(int chat_id, JLabel g_action) {
            

            File btn_icon = new File("");
            String abspath = btn_icon.getAbsolutePath();

            if (DBManager.getDBM().is_online(chat_id)) {
              DBManager.getDBM().put_offline(chat_id);
                ImageIcon icon = new ImageIcon(abspath + "\\src\\interfaces\\icons\\start.png");
                g_action.setIcon(icon);
            } else if (DBManager.getDBM().put_online(chat_id)) {
                
                 System.out.println("chat is offline");
                ImageIcon icon = new ImageIcon(abspath + "\\src\\interfaces\\icons\\end.png");
                g_action.setIcon(icon);
                
                start_server(chat_id);


            }
    }
        
        public void subscribe_action(int grp_id, JLabel sub_btn) {
        try {
            File btn_icon = new File("");
            String abspath = btn_icon.getAbsolutePath();

            if (chat.is_subscribed(me.getId())) {
                chat.unsubscribre(grp_id, me);
                ImageIcon icon = new ImageIcon(abspath + "\\src\\interfaces\\icons\\subscribe.png");
                sub_btn.setIcon(icon);
            } else {
                chat.subscribre(grp_id, me);
                ImageIcon icon = new ImageIcon(abspath + "\\src\\interfaces\\icons\\unsubscribe.png");
                sub_btn.setIcon(icon);
            }

        } catch (RemoteException ex) {
            System.out.println(ex);
        }
    }
        
        
        static int enterd_grup_id;
        public void enter_to_chat(int grup_id) {
            try {
                if (chat.is_subscribed(me.getId())) {
                    app_ui_reset();
                    chat_panel.setVisible(true);
                    
                    enterd_grup_id = grup_id;
                    retrivemsg.start();
                }

            } catch (RemoteException ex) {
                System.out.println(ex);
            }
    }
        
        
      int y1 = 13;   
      
      
      public void load_client_groups() {
            
        List chats = DBManager.getDBM().get_chat_groups();
        client_chat_groups_panel.removeAll();
        
            for (Iterator iterator = chats.iterator(); iterator.hasNext();) {
            Groups next = (Groups) iterator.next();
            
       

            JPanel client_grp_panel = new javax.swing.JPanel();
            client_grp_panel.setBackground(new java.awt.Color(232, 242, 220));
            client_grp_panel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            client_grp_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
            
            client_grp_panel.addMouseListener(new MouseAdapter() {
                
          
                public void mouseClicked(MouseEvent e) {
                    enter_to_chat(next.getId());

                }
            });
            
            boolean is_sub = false;
            
            JLabel subscribe = new javax.swing.JLabel();

            if (is_sub) {
                subscribe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/unsubscribe.png"))); 
            } else {
                subscribe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/subscribe.png"))); 
            }

            if (next.isStatus()== true) {
                subscribe.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        subscribe_action(next.getId(), subscribe);
                        String m = msg_typer.getText();
  
                        LocalDateTime myDateObj = LocalDateTime.now();
                        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String time_now = myDateObj.format(myFormatObj);
                        Message msg = new Message();
                        msg.setDate_time(time_now);
                        String user = me.getUsername();
                        m = "****** " + user  + " has join the chat " + " ******";
                        msg.setMessage(m);
                        try {
                            
                            chat.send_message(msg);
                            System.out.println(msg);
                        } catch (RemoteException ex) {
                            Logger.getLogger(AppLayout.class.getName()).log(Level.SEVERE, null, ex);
                        }


                    }
                });

            } else {
                subscribe.setEnabled(false);
                subscribe.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }

            JLabel grp_dec = new javax.swing.JLabel();
            grp_dec.setForeground(new java.awt.Color(58, 164, 58));
            grp_dec.setText(next.getDescription());

            JLabel statuts_txt = new javax.swing.JLabel();
            statuts_txt.setBackground(new java.awt.Color(211, 222, 211));
            statuts_txt.setForeground(new java.awt.Color(52, 88, 52));

            JLabel statuts_icon = new javax.swing.JLabel();

            if (next.isStatus()== true) {
                statuts_txt.setText("online");
                statuts_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/online.png")));
            } else {
                statuts_txt.setText("offline");
                statuts_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/offline.png")));
            }

            JLabel grp_name = new javax.swing.JLabel();
            grp_name.setFont(new java.awt.Font("Tahoma", 1, 13)); 
            grp_name.setForeground(new java.awt.Color(56, 90, 56));
            grp_name.setText(next.getName());

            client_grp_panel.add(subscribe, new org.netbeans.lib.awtextra.AbsoluteConstraints(184, 42, 99, 35));
            client_grp_panel.add(grp_dec, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 42, 160, 35));
            client_grp_panel.add(statuts_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(232, 13, 51, -1));
            client_grp_panel.add(statuts_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(207, 13, 18, 16));
            client_grp_panel.add(grp_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 13, 160, -1));
            client_chat_groups_panel.add(client_grp_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, y1, 299, 96));

            y1 += 110;

     
        }
        }
        
        
        
        
        
    
    
    
    
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        login_panel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        textusername = new javax.swing.JTextField();
        textpassword = new javax.swing.JPasswordField();
        btnlogin = new javax.swing.JButton();
        show = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        linkreg = new javax.swing.JLabel();
        text_login_errors = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        close4 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        close10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        titlebar = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        register_panel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        signup_profile_pic = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        textregemail = new javax.swing.JTextField();
        textregpassword = new javax.swing.JTextField();
        textregnickname = new javax.swing.JTextField();
        textregusername = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        btnreg = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        text_reg_errors = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        close8 = new javax.swing.JLabel();
        admin_panel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        text_admin_username = new javax.swing.JLabel();
        logout = new javax.swing.JLabel();
        img_profile = new javax.swing.JLabel();
        create_group = new javax.swing.JLabel();
        link_all_users = new javax.swing.JLabel();
        create_group3 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        admin_group_list = new javax.swing.JPanel();
        create_group2 = new javax.swing.JLabel();
        close1 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        create_chat_panel = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        text_admin_username2 = new javax.swing.JLabel();
        logout1 = new javax.swing.JLabel();
        img_profile3 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        btn_chat_groups = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        textgroupdescription = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        textgroupname = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        btn_create_group = new javax.swing.JButton();
        group_create_text = new javax.swing.JLabel();
        close2 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        list_groups_panel = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        img_profile5 = new javax.swing.JLabel();
        text_user_username = new javax.swing.JLabel();
        edit_profile_link_1 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        logout2 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel63 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        client_chat_groups_panel = new javax.swing.JPanel();
        close3 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        edit_profile_panel = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        img_profile4 = new javax.swing.JLabel();
        text_user_username1 = new javax.swing.JLabel();
        edit_profile_link_2 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        logout3 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel64 = new javax.swing.JLabel();
        close5 = new javax.swing.JLabel();
        edit_profile_image = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        edit_username = new javax.swing.JTextField();
        edit_nickname = new javax.swing.JTextField();
        edit_password = new javax.swing.JPasswordField();
        btnreg1 = new javax.swing.JButton();
        text_reg_error = new javax.swing.JLabel();
        update_msg = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        show_eyeedit = new javax.swing.JLabel();
        hide_eyeedit = new javax.swing.JLabel();
        manage_users_panel = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        text_admin_username3 = new javax.swing.JLabel();
        logout6 = new javax.swing.JLabel();
        img_profile6 = new javax.swing.JLabel();
        create_group1 = new javax.swing.JLabel();
        link_all_users1 = new javax.swing.JLabel();
        btn_chat_groups1 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        userlist1 = new javax.swing.JComboBox<String>();
        remove_user = new javax.swing.JButton();
        close9 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        text_delete = new javax.swing.JLabel();
        chat_panel = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        img_profile7 = new javax.swing.JLabel();
        text_user_username2 = new javax.swing.JLabel();
        edit_profile_link_3 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        logout5 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel65 = new javax.swing.JLabel();
        close7 = new javax.swing.JLabel();
        msg_typer = new javax.swing.JTextField();
        send_btn = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        msgScrollPane = new javax.swing.JScrollPane();
        chat_background = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLayeredPane1.setBackground(new java.awt.Color(255, 255, 255));
        jLayeredPane1.setPreferredSize(new java.awt.Dimension(880, 531));

        login_panel.setPreferredSize(new java.awt.Dimension(880, 531));
        login_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(17, 27, 33));

        jLabel2.setFont(new java.awt.Font("Georgia", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 168, 132));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("LOGIN");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 168, 132));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Hello! Let's get started");

        textusername.setBackground(new java.awt.Color(204, 204, 204));
        textusername.setForeground(new java.awt.Color(255, 255, 255));
        textusername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textusernameActionPerformed(evt);
            }
        });

        textpassword.setBackground(new java.awt.Color(204, 204, 204));
        textpassword.setForeground(new java.awt.Color(255, 255, 255));
        textpassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textpasswordActionPerformed(evt);
            }
        });

        btnlogin.setBackground(new java.awt.Color(153, 153, 153));
        btnlogin.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnlogin.setText("LOGIN");
        btnlogin.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnlogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnloginMouseClicked(evt);
            }
        });
        btnlogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnloginActionPerformed(evt);
            }
        });

        show.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/icons8_eye_32.png"))); // NOI18N
        show.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showMouseClicked(evt);
            }
        });

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/userrrr.png"))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Georgia", 0, 17)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 168, 132));
        jLabel6.setText("Don't you have an account?");

        linkreg.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        linkreg.setForeground(new java.awt.Color(153, 51, 0));
        linkreg.setText("Register");
        linkreg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                linkregMouseClicked(evt);
            }
        });

        text_login_errors.setForeground(new java.awt.Color(255, 0, 0));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 168, 132));
        jLabel22.setText("Username");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 168, 132));
        jLabel23.setText("Password");

        close4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/max.png"))); // NOI18N
        close4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                close4MouseClicked(evt);
            }
        });

        jLabel34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/minimize.png"))); // NOI18N
        jLabel34.setText("jLabel34");
        jLabel34.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel34MouseClicked(evt);
            }
        });

        close10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/max.png"))); // NOI18N
        close10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                close10MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(close10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(close4)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(253, 253, 253)
                .addComponent(btnlogin, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(185, 185, 185)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(160, 160, 160)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(show, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel22)
                                    .addComponent(textusername, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel23)
                                    .addComponent(textpassword, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(text_login_errors, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(linkreg))))
                .addGap(209, 209, 209))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(close4)
                    .addComponent(jLabel34)
                    .addComponent(close10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(43, 43, 43)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(textusername, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(28, 28, 28)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(show, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(textpassword, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(text_login_errors, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnlogin)
                .addGap(79, 79, 79)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(linkreg))
                .addContainerGap(60, Short.MAX_VALUE))
        );

        login_panel.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 0, 630, 530));

        jPanel2.setBackground(new java.awt.Color(32, 44, 51));

        titlebar.setFont(new java.awt.Font("Bookman Old Style", 1, 14)); // NOI18N
        titlebar.setForeground(new java.awt.Color(111, 117, 124));
        titlebar.setText(" Chat Base");
        titlebar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        titlebar.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                titlebarMouseDragged(evt);
            }
        });
        titlebar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                titlebarMousePressed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/chat image.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(312, 312, 312))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(titlebar, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titlebar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(71, 71, 71)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(197, Short.MAX_VALUE))
        );

        login_panel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 530));

        register_panel.setPreferredSize(new java.awt.Dimension(880, 531));
        register_panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                register_panelMouseClicked(evt);
            }
        });
        register_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(32, 44, 51));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        signup_profile_pic.setForeground(new java.awt.Color(204, 204, 204));
        signup_profile_pic.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        signup_profile_pic.setText("Insert Profile Photo");
        signup_profile_pic.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        signup_profile_pic.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                signup_profile_picMouseClicked(evt);
            }
        });
        jPanel3.add(signup_profile_pic, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 150, 140, 150));

        register_panel.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 530));

        jPanel4.setBackground(new java.awt.Color(17, 27, 33));

        jLabel9.setFont(new java.awt.Font("Georgia", 1, 36)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 168, 132));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("REGISTER");

        textregemail.setForeground(new java.awt.Color(255, 255, 255));

        textregpassword.setForeground(new java.awt.Color(255, 255, 255));

        textregnickname.setForeground(new java.awt.Color(255, 255, 255));

        textregusername.setForeground(new java.awt.Color(255, 255, 255));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 168, 132));
        jLabel10.setText("Email");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 168, 132));
        jLabel11.setText("Password");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 168, 132));
        jLabel12.setText("Username");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 168, 132));
        jLabel13.setText("Nickname");

        btnreg.setBackground(new java.awt.Color(0, 168, 132));
        btnreg.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnreg.setText("Register");
        btnreg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnregMouseClicked(evt);
            }
        });
        btnreg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnregActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Georgia", 0, 17)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 168, 132));
        jLabel14.setText("Do you have an account?");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(204, 51, 0));
        jLabel15.setText("Login");
        jLabel15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel15MouseClicked(evt);
            }
        });

        text_reg_errors.setForeground(new java.awt.Color(255, 51, 51));

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/mail_n.png"))); // NOI18N

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/uname.png"))); // NOI18N

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/nikn.png"))); // NOI18N

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/passss.png"))); // NOI18N

        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/minimize.png"))); // NOI18N
        jLabel35.setText("jLabel34");

        close8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/max.png"))); // NOI18N
        close8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                close8MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap(162, Short.MAX_VALUE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(123, 123, 123)
                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(close8))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel14)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(33, 33, 33)
                                        .addComponent(btnreg))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel15))))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(132, 132, 132)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(text_reg_errors, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel13)
                                        .addComponent(jLabel11)
                                        .addComponent(textregusername)
                                        .addComponent(textregnickname)
                                        .addComponent(textregemail)
                                        .addComponent(jLabel10)
                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textregpassword, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel9))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(close8)
                            .addComponent(jLabel35))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textregemail, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(textregusername, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(textregnickname, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addGap(52, 52, 52)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textregpassword, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(text_reg_errors, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnreg)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15))
                .addGap(40, 40, 40))
        );

        register_panel.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 0, 610, 530));

        admin_panel.setPreferredSize(new java.awt.Dimension(880, 531));

        jPanel5.setBackground(new java.awt.Color(32, 44, 51));
        jPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel5MouseClicked(evt);
            }
        });

        text_admin_username.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        text_admin_username.setForeground(new java.awt.Color(255, 255, 255));
        text_admin_username.setText("Welcome Admin");

        logout.setBackground(new java.awt.Color(255, 0, 0));
        logout.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        logout.setForeground(new java.awt.Color(255, 0, 0));
        logout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/log_out.png"))); // NOI18N
        logout.setText("Log out");
        logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutMouseClicked(evt);
            }
        });

        img_profile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/admin.png"))); // NOI18N

        create_group.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        create_group.setForeground(new java.awt.Color(0, 255, 255));
        create_group.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/create_group.png"))); // NOI18N
        create_group.setText("Create Group");
        create_group.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                create_groupMouseClicked(evt);
            }
        });

        link_all_users.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        link_all_users.setForeground(new java.awt.Color(0, 255, 255));
        link_all_users.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/user_1.png"))); // NOI18N
        link_all_users.setText("Manage Users");
        link_all_users.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                link_all_usersMouseClicked(evt);
            }
        });

        create_group3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        create_group3.setForeground(new java.awt.Color(0, 255, 255));
        create_group3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/groups n.png"))); // NOI18N
        create_group3.setText("Groups");
        create_group3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 153, 0), 1, true));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(img_profile, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(create_group3)
                                    .addComponent(link_all_users)
                                    .addComponent(create_group)))
                            .addComponent(text_admin_username)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(logout)))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(img_profile, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(text_admin_username)
                .addGap(27, 27, 27)
                .addComponent(create_group3)
                .addGap(18, 18, 18)
                .addComponent(create_group)
                .addGap(18, 18, 18)
                .addComponent(link_all_users)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logout)
                .addGap(14, 14, 14))
        );

        jPanel6.setBackground(new java.awt.Color(17, 27, 33));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel24.setBackground(new java.awt.Color(0, 168, 132));
        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 168, 132));
        jLabel24.setText("All Chat Groups");
        jPanel6.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 60, -1, -1));

        admin_group_list.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        create_group2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        create_group2.setForeground(new java.awt.Color(0, 168, 132));
        create_group2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/create_group.png"))); // NOI18N
        create_group2.setText("Create Group");
        admin_group_list.add(create_group2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, -1, -1));

        jScrollPane1.setViewportView(admin_group_list);

        jPanel6.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 140, 330, 260));

        close1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/max.png"))); // NOI18N
        close1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                close1MouseClicked(evt);
            }
        });
        jPanel6.add(close1, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 10, -1, -1));

        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/minimize.png"))); // NOI18N
        jLabel36.setText("jLabel34");
        jLabel36.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel36MouseClicked(evt);
            }
        });
        jPanel6.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 10, 30, -1));

        javax.swing.GroupLayout admin_panelLayout = new javax.swing.GroupLayout(admin_panel);
        admin_panel.setLayout(admin_panelLayout);
        admin_panelLayout.setHorizontalGroup(
            admin_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(admin_panelLayout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 677, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        admin_panelLayout.setVerticalGroup(
            admin_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
        );

        create_chat_panel.setPreferredSize(new java.awt.Dimension(880, 531));

        jPanel7.setBackground(new java.awt.Color(32, 44, 51));

        text_admin_username2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        text_admin_username2.setForeground(new java.awt.Color(255, 255, 255));
        text_admin_username2.setText("Welcome Admin");

        logout1.setBackground(new java.awt.Color(255, 0, 0));
        logout1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        logout1.setForeground(new java.awt.Color(255, 0, 0));
        logout1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/log_out.png"))); // NOI18N
        logout1.setText("Log out");
        logout1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logout1MouseClicked(evt);
            }
        });

        img_profile3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/admin.png"))); // NOI18N
        img_profile3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                img_profile3MouseClicked(evt);
            }
        });

        jLabel38.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(0, 255, 255));
        jLabel38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/create_group.png"))); // NOI18N
        jLabel38.setText("Create Group");
        jLabel38.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 153, 0), 1, true));

        jLabel40.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(0, 255, 255));
        jLabel40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/user_1.png"))); // NOI18N
        jLabel40.setText("Users");
        jLabel40.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel40MouseClicked(evt);
            }
        });

        btn_chat_groups.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_chat_groups.setForeground(new java.awt.Color(0, 255, 255));
        btn_chat_groups.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/groups n.png"))); // NOI18N
        btn_chat_groups.setText("Groups");
        btn_chat_groups.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_chat_groupsMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(img_profile3, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btn_chat_groups)
                                        .addComponent(jLabel40))
                                    .addGap(49, 49, 49))
                                .addComponent(text_admin_username2))
                            .addComponent(jLabel38)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(logout1)))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(img_profile3, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(text_admin_username2)
                .addGap(23, 23, 23)
                .addComponent(btn_chat_groups)
                .addGap(18, 18, 18)
                .addComponent(jLabel38)
                .addGap(14, 14, 14)
                .addComponent(jLabel40)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 170, Short.MAX_VALUE)
                .addComponent(logout1)
                .addGap(14, 14, 14))
        );

        jPanel8.setBackground(new java.awt.Color(17, 27, 33));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel42.setBackground(new java.awt.Color(0, 168, 132));
        jLabel42.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(0, 168, 132));
        jLabel42.setText("Create a Chat Group");
        jPanel8.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 70, -1, -1));

        jLabel52.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(0, 168, 132));
        jLabel52.setText("Group Description");
        jPanel8.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 250, -1, -1));

        textgroupdescription.setForeground(new java.awt.Color(255, 255, 255));
        textgroupdescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textgroupdescriptionActionPerformed(evt);
            }
        });
        jPanel8.add(textgroupdescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 280, 260, -1));
        jPanel8.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 270, 260, 30));

        textgroupname.setForeground(new java.awt.Color(255, 255, 255));
        jPanel8.add(textgroupname, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 210, 260, -1));
        jPanel8.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 230, 250, 20));

        jLabel55.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(0, 168, 132));
        jLabel55.setText("Group Name");
        jPanel8.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 180, -1, -1));

        btn_create_group.setBackground(new java.awt.Color(0, 168, 132));
        btn_create_group.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_create_group.setText("Create Group");
        btn_create_group.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_create_groupMouseClicked(evt);
            }
        });
        jPanel8.add(btn_create_group, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 350, 130, 40));

        group_create_text.setForeground(new java.awt.Color(255, 51, 51));
        jPanel8.add(group_create_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 320, 260, 20));

        close2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/max.png"))); // NOI18N
        close2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                close2MouseClicked(evt);
            }
        });
        jPanel8.add(close2, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 10, -1, -1));

        jLabel37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/minimize.png"))); // NOI18N
        jLabel37.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel37MouseClicked(evt);
            }
        });
        jPanel8.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 10, 30, -1));

        javax.swing.GroupLayout create_chat_panelLayout = new javax.swing.GroupLayout(create_chat_panel);
        create_chat_panel.setLayout(create_chat_panelLayout);
        create_chat_panelLayout.setHorizontalGroup(
            create_chat_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(create_chat_panelLayout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE))
        );
        create_chat_panelLayout.setVerticalGroup(
            create_chat_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
        );

        list_groups_panel.setPreferredSize(new java.awt.Dimension(880, 531));

        jPanel9.setBackground(new java.awt.Color(32, 44, 51));

        img_profile5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/user profile.png"))); // NOI18N

        text_user_username.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        text_user_username.setForeground(new java.awt.Color(255, 255, 255));
        text_user_username.setText("Welcome User");

        edit_profile_link_1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        edit_profile_link_1.setForeground(new java.awt.Color(0, 255, 255));
        edit_profile_link_1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/editProfile.png"))); // NOI18N
        edit_profile_link_1.setText("Edit Profile");
        edit_profile_link_1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                edit_profile_link_1MouseClicked(evt);
            }
        });

        jLabel60.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(0, 255, 255));
        jLabel60.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/joinGroup.png"))); // NOI18N
        jLabel60.setText("Join Group");
        jLabel60.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 1, true));

        logout2.setBackground(new java.awt.Color(255, 0, 0));
        logout2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        logout2.setForeground(new java.awt.Color(255, 0, 0));
        logout2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/log_out.png"))); // NOI18N
        logout2.setText("Log out");
        logout2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logout2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(logout2)
                .addContainerGap(108, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(34, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel60)
                    .addComponent(edit_profile_link_1)
                    .addComponent(text_user_username)
                    .addComponent(img_profile5, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(img_profile5, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(text_user_username)
                .addGap(26, 26, 26)
                .addComponent(jLabel60)
                .addGap(18, 18, 18)
                .addComponent(edit_profile_link_1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logout2)
                .addGap(15, 15, 15))
        );

        jPanel10.setBackground(new java.awt.Color(17, 27, 33));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel63.setBackground(new java.awt.Color(0, 168, 132));
        jLabel63.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(0, 168, 132));
        jLabel63.setText("Chat Groups");
        jPanel10.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 60, -1, -1));

        jScrollPane2.setBackground(new java.awt.Color(125, 156, 125));

        client_chat_groups_panel.setBackground(new java.awt.Color(33, 80, 33));
        client_chat_groups_panel.setForeground(new java.awt.Color(255, 255, 255));
        client_chat_groups_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jScrollPane2.setViewportView(client_chat_groups_panel);

        jPanel10.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 140, 450, 300));

        close3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/max.png"))); // NOI18N
        close3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                close3MouseClicked(evt);
            }
        });
        jPanel10.add(close3, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 10, -1, -1));

        jLabel39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/minimize.png"))); // NOI18N
        jLabel39.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel39MouseClicked(evt);
            }
        });
        jPanel10.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 10, 30, -1));

        javax.swing.GroupLayout list_groups_panelLayout = new javax.swing.GroupLayout(list_groups_panel);
        list_groups_panel.setLayout(list_groups_panelLayout);
        list_groups_panelLayout.setHorizontalGroup(
            list_groups_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, list_groups_panelLayout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 676, Short.MAX_VALUE))
        );
        list_groups_panelLayout.setVerticalGroup(
            list_groups_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        edit_profile_panel.setPreferredSize(new java.awt.Dimension(880, 531));

        jPanel11.setBackground(new java.awt.Color(32, 44, 51));

        img_profile4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        img_profile4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/user profile.png"))); // NOI18N

        text_user_username1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        text_user_username1.setForeground(new java.awt.Color(255, 255, 255));
        text_user_username1.setText("Welcome User");

        edit_profile_link_2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        edit_profile_link_2.setForeground(new java.awt.Color(0, 255, 255));
        edit_profile_link_2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/editProfile.png"))); // NOI18N
        edit_profile_link_2.setText("Edit Profile");
        edit_profile_link_2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 153, 0), 1, true));

        jLabel61.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(0, 255, 255));
        jLabel61.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/joinGroup.png"))); // NOI18N
        jLabel61.setText("Join Group");
        jLabel61.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel61MouseClicked(evt);
            }
        });

        logout3.setBackground(new java.awt.Color(255, 0, 0));
        logout3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        logout3.setForeground(new java.awt.Color(255, 0, 0));
        logout3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/log_out.png"))); // NOI18N
        logout3.setText("Log out");
        logout3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logout3MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(logout3)
                .addContainerGap(107, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addComponent(img_profile4, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(text_user_username1)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(edit_profile_link_2)
                                    .addComponent(jLabel61))))
                        .addGap(43, 43, 43))))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(img_profile4, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(text_user_username1)
                .addGap(30, 30, 30)
                .addComponent(jLabel61)
                .addGap(18, 18, 18)
                .addComponent(edit_profile_link_2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logout3)
                .addGap(15, 15, 15))
        );

        jPanel12.setBackground(new java.awt.Color(17, 27, 33));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel64.setBackground(new java.awt.Color(0, 168, 132));
        jLabel64.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(0, 168, 132));
        jLabel64.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel64.setText("Edit Profile");
        jPanel12.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, -1, -1));

        close5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/max.png"))); // NOI18N
        close5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                close5MouseClicked(evt);
            }
        });
        jPanel12.add(close5, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 10, -1, -1));

        edit_profile_image.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 255, 255)));
        edit_profile_image.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                edit_profile_imageMouseClicked(evt);
            }
        });
        jPanel12.add(edit_profile_image, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 190, 140, 141));

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 168, 132));
        jLabel26.setText("Username");
        jPanel12.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 130, 114, 28));

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(0, 168, 132));
        jLabel27.setText("Nickname");
        jPanel12.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 210, 114, 23));

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(0, 168, 132));
        jLabel28.setText("Password");
        jPanel12.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 290, 114, 14));

        edit_username.setForeground(new java.awt.Color(255, 255, 255));
        jPanel12.add(edit_username, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 160, 241, 33));

        edit_nickname.setForeground(new java.awt.Color(255, 255, 255));
        jPanel12.add(edit_nickname, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 240, 241, 33));

        edit_password.setForeground(new java.awt.Color(255, 255, 255));
        jPanel12.add(edit_password, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 310, 241, 33));

        btnreg1.setBackground(new java.awt.Color(204, 255, 255));
        btnreg1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnreg1.setText("SAVE");
        btnreg1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnreg1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnreg1MouseClicked(evt);
            }
        });
        btnreg1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnreg1ActionPerformed(evt);
            }
        });
        jPanel12.add(btnreg1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 390, 140, 40));

        text_reg_error.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        text_reg_error.setForeground(new java.awt.Color(255, 0, 0));
        text_reg_error.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        text_reg_error.setText("*");
        jPanel12.add(text_reg_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 350, 204, 24));

        update_msg.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        update_msg.setForeground(new java.awt.Color(255, 51, 51));
        update_msg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        update_msg.setText("*");
        jPanel12.add(update_msg, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 350, 240, 30));

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/icons8_male_user_32.png"))); // NOI18N
        jPanel12.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 160, 40, 40));

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/icons8_account_32.png"))); // NOI18N
        jPanel12.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 240, 40, 40));

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/icons8_eye_32.png"))); // NOI18N
        jPanel12.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 310, 40, 40));

        jLabel41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/minimize.png"))); // NOI18N
        jLabel41.setText("jLabel34");
        jLabel41.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel41MouseClicked(evt);
            }
        });
        jPanel12.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 10, 30, -1));

        show_eyeedit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        show_eyeedit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/show_eyes.png"))); // NOI18N
        show_eyeedit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                show_eyeeditMouseClicked(evt);
            }
        });
        jPanel12.add(show_eyeedit, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 310, 30, 30));

        hide_eyeedit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        hide_eyeedit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/hide_eye.png"))); // NOI18N
        hide_eyeedit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                hide_eyeeditMouseClicked(evt);
            }
        });
        jPanel12.add(hide_eyeedit, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 310, 30, 30));

        javax.swing.GroupLayout edit_profile_panelLayout = new javax.swing.GroupLayout(edit_profile_panel);
        edit_profile_panel.setLayout(edit_profile_panelLayout);
        edit_profile_panelLayout.setHorizontalGroup(
            edit_profile_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, edit_profile_panelLayout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE))
        );
        edit_profile_panelLayout.setVerticalGroup(
            edit_profile_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        manage_users_panel.setPreferredSize(new java.awt.Dimension(880, 531));

        jPanel13.setBackground(new java.awt.Color(32, 44, 51));

        text_admin_username3.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        text_admin_username3.setForeground(new java.awt.Color(255, 255, 255));
        text_admin_username3.setText("Welcome Admin");

        logout6.setBackground(new java.awt.Color(255, 0, 0));
        logout6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        logout6.setForeground(new java.awt.Color(255, 0, 0));
        logout6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/log_out.png"))); // NOI18N
        logout6.setText("Log out");
        logout6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logout6MouseClicked(evt);
            }
        });

        img_profile6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        img_profile6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/admin.png"))); // NOI18N

        create_group1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        create_group1.setForeground(new java.awt.Color(0, 255, 255));
        create_group1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/create_group.png"))); // NOI18N
        create_group1.setText("Create Group");
        create_group1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                create_group1MouseClicked(evt);
            }
        });

        link_all_users1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        link_all_users1.setForeground(new java.awt.Color(0, 255, 255));
        link_all_users1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/user_1.png"))); // NOI18N
        link_all_users1.setText("Manage Users");
        link_all_users1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 153, 0), 1, true));

        btn_chat_groups1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_chat_groups1.setForeground(new java.awt.Color(0, 255, 255));
        btn_chat_groups1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/groups n.png"))); // NOI18N
        btn_chat_groups1.setText("Groups");
        btn_chat_groups1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_chat_groups1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(logout6))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(img_profile6, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(create_group1)
                                    .addComponent(text_admin_username3)
                                    .addComponent(link_all_users1)
                                    .addComponent(btn_chat_groups1))))))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(img_profile6, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(text_admin_username3)
                .addGap(41, 41, 41)
                .addComponent(btn_chat_groups1)
                .addGap(18, 18, 18)
                .addComponent(create_group1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(link_all_users1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 160, Short.MAX_VALUE)
                .addComponent(logout6)
                .addGap(14, 14, 14))
        );

        jPanel14.setBackground(new java.awt.Color(17, 27, 33));

        jLabel31.setBackground(new java.awt.Color(0, 168, 132));
        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 168, 132));
        jLabel31.setText("Manage Users");

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(0, 168, 132));
        jLabel32.setText("Delete User");

        userlist1.setForeground(new java.awt.Color(255, 255, 255));

        remove_user.setBackground(new java.awt.Color(255, 255, 255));
        remove_user.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        remove_user.setForeground(new java.awt.Color(255, 0, 51));
        remove_user.setText("Remove");
        remove_user.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 0, 51), 1, true));
        remove_user.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                remove_userMouseClicked(evt);
            }
        });

        close9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/max.png"))); // NOI18N
        close9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                close9MouseClicked(evt);
            }
        });

        jLabel43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/minimize.png"))); // NOI18N
        jLabel43.setText("jLabel34");
        jLabel43.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel43MouseClicked(evt);
            }
        });

        text_delete.setForeground(new java.awt.Color(255, 102, 51));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(close9))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGap(206, 206, 206)
                                .addComponent(jLabel31))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGap(75, 75, 75)
                                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(userlist1, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(remove_user, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 103, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(166, 166, 166)
                .addComponent(text_delete, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43)
                    .addComponent(close9))
                .addGap(37, 37, 37)
                .addComponent(jLabel31)
                .addGap(111, 111, 111)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userlist1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(remove_user, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addComponent(text_delete, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout manage_users_panelLayout = new javax.swing.GroupLayout(manage_users_panel);
        manage_users_panel.setLayout(manage_users_panelLayout);
        manage_users_panelLayout.setHorizontalGroup(
            manage_users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manage_users_panelLayout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        manage_users_panelLayout.setVerticalGroup(
            manage_users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        chat_panel.setPreferredSize(new java.awt.Dimension(880, 531));

        jPanel15.setBackground(new java.awt.Color(32, 44, 51));

        img_profile7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        img_profile7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/user profile.png"))); // NOI18N

        text_user_username2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        text_user_username2.setForeground(new java.awt.Color(255, 255, 255));
        text_user_username2.setText("Welcome User");

        edit_profile_link_3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        edit_profile_link_3.setForeground(new java.awt.Color(0, 255, 255));
        edit_profile_link_3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/editProfile.png"))); // NOI18N
        edit_profile_link_3.setText("Edit Profile");
        edit_profile_link_3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                edit_profile_link_3MouseClicked(evt);
            }
        });

        jLabel62.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(0, 255, 255));
        jLabel62.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/joinGroup.png"))); // NOI18N
        jLabel62.setText("Join Group");

        logout5.setBackground(new java.awt.Color(255, 0, 0));
        logout5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        logout5.setForeground(new java.awt.Color(255, 0, 0));
        logout5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/log_out.png"))); // NOI18N
        logout5.setText("Log out");
        logout5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logout5MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(logout5)
                .addContainerGap(107, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addComponent(img_profile7, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel62)
                            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(text_user_username2)
                                .addGroup(jPanel15Layout.createSequentialGroup()
                                    .addComponent(edit_profile_link_3)
                                    .addGap(8, 8, 8))))
                        .addGap(43, 43, 43))))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(img_profile7, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(text_user_username2)
                .addGap(38, 38, 38)
                .addComponent(jLabel62)
                .addGap(18, 18, 18)
                .addComponent(edit_profile_link_3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logout5)
                .addGap(15, 15, 15))
        );

        jPanel16.setBackground(new java.awt.Color(17, 27, 33));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel65.setBackground(new java.awt.Color(0, 168, 132));
        jLabel65.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(0, 168, 132));
        jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel65.setText("Group Chat");
        jPanel16.add(jLabel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 50, -1, -1));

        close7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/max.png"))); // NOI18N
        close7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                close7MouseClicked(evt);
            }
        });
        jPanel16.add(close7, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 10, -1, -1));

        msg_typer.setBackground(new java.awt.Color(64, 86, 64));
        msg_typer.setForeground(new java.awt.Color(255, 255, 255));
        msg_typer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                msg_typerActionPerformed(evt);
            }
        });
        msg_typer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                msg_typerKeyPressed(evt);
            }
        });
        jPanel16.add(msg_typer, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 400, 340, 60));

        send_btn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        send_btn.setForeground(new java.awt.Color(0, 168, 132));
        send_btn.setText("SEND");
        send_btn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        send_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                send_btnMouseClicked(evt);
            }
        });
        send_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                send_btnActionPerformed(evt);
            }
        });
        jPanel16.add(send_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 410, 85, 37));

        jLabel33.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(102, 204, 0));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel16.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 480, 307, 30));

        chat_background.setBackground(new java.awt.Color(33, 80, 33));
        chat_background.setForeground(new java.awt.Color(255, 255, 255));
        chat_background.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        msgScrollPane.setViewportView(chat_background);

        jPanel16.add(msgScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 160, 445, 210));

        jLabel44.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/minimize.png"))); // NOI18N
        jLabel44.setText("jLabel34");
        jLabel44.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel44MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel44MouseEntered(evt);
            }
        });
        jPanel16.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 10, 30, -1));

        javax.swing.GroupLayout chat_panelLayout = new javax.swing.GroupLayout(chat_panel);
        chat_panel.setLayout(chat_panelLayout);
        chat_panelLayout.setHorizontalGroup(
            chat_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, chat_panelLayout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE))
        );
        chat_panelLayout.setVerticalGroup(
            chat_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(login_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(register_panel, javax.swing.GroupLayout.PREFERRED_SIZE, 880, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(admin_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(create_chat_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(list_groups_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(edit_profile_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(manage_users_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(chat_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addComponent(login_panel, javax.swing.GroupLayout.PREFERRED_SIZE, 531, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(register_panel, javax.swing.GroupLayout.PREFERRED_SIZE, 531, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(admin_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(create_chat_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(list_groups_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(edit_profile_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(manage_users_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(chat_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jLayeredPane1.setLayer(login_panel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(register_panel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(admin_panel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(create_chat_panel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(list_groups_panel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(edit_profile_panel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(manage_users_panel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(chat_panel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        getContentPane().add(jLayeredPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 880, -1));

        setSize(new java.awt.Dimension(880, 531));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnloginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnloginActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnloginActionPerformed

    private void textusernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textusernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textusernameActionPerformed

    private void textpasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textpasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textpasswordActionPerformed

    private void btnregActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnregActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnregActionPerformed

    private void textgroupdescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textgroupdescriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textgroupdescriptionActionPerformed

    private void titlebarMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_titlebarMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        int xx = 0;
        int yy = 0;

        this.setLocation(x - xx, y - yy);
    }//GEN-LAST:event_titlebarMouseDragged

    private void titlebarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_titlebarMousePressed
        int xx = evt.getX();
        int yy = evt.getY();
    }//GEN-LAST:event_titlebarMousePressed

    private void linkregMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_linkregMouseClicked
        
        register_panel.setVisible(true);
        login_panel.setVisible(false);
    }//GEN-LAST:event_linkregMouseClicked

    private void jLabel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseClicked
        login_panel.setVisible(true);
        register_panel.setVisible(false);
    }//GEN-LAST:event_jLabel15MouseClicked

    private void btnreg1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnreg1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnreg1ActionPerformed

    private void send_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_send_btnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_send_btnActionPerformed

    private void close4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_close4MouseClicked
        dispose();
    }//GEN-LAST:event_close4MouseClicked

    private void close8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_close8MouseClicked
        dispose();
    }//GEN-LAST:event_close8MouseClicked

    private void close1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_close1MouseClicked
        
         dispose();
    }//GEN-LAST:event_close1MouseClicked

    private void close2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_close2MouseClicked
        dispose();
    }//GEN-LAST:event_close2MouseClicked

    private void close3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_close3MouseClicked
        dispose();
    }//GEN-LAST:event_close3MouseClicked

    private void close5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_close5MouseClicked
        dispose();
    }//GEN-LAST:event_close5MouseClicked

    private void close9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_close9MouseClicked
         dispose();
    }//GEN-LAST:event_close9MouseClicked

    private void close7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_close7MouseClicked
        dispose();
    }//GEN-LAST:event_close7MouseClicked

    private void jLabel34MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel34MouseClicked
      this.setState(JFrame.ICONIFIED);
    }//GEN-LAST:event_jLabel34MouseClicked

    private void register_panelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_register_panelMouseClicked
       this.setState(JFrame.ICONIFIED);

    }//GEN-LAST:event_register_panelMouseClicked

    private void jLabel36MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel36MouseClicked
        this.setState(JFrame.ICONIFIED);

    }//GEN-LAST:event_jLabel36MouseClicked

    private void jLabel37MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel37MouseClicked
        this.setState(JFrame.ICONIFIED);

    }//GEN-LAST:event_jLabel37MouseClicked

    private void jLabel39MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel39MouseClicked
        this.setState(JFrame.ICONIFIED);

    }//GEN-LAST:event_jLabel39MouseClicked

    private void jLabel41MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel41MouseClicked
        this.setState(JFrame.ICONIFIED);

    }//GEN-LAST:event_jLabel41MouseClicked

    private void jLabel43MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel43MouseClicked
        this.setState(JFrame.ICONIFIED);

    }//GEN-LAST:event_jLabel43MouseClicked

    private void jLabel44MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel44MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel44MouseEntered

    private void jLabel44MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel44MouseClicked
        this.setState(JFrame.ICONIFIED);

    }//GEN-LAST:event_jLabel44MouseClicked

    private void btnloginMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnloginMouseClicked
                                             
        // TODO add your handling code here:
        String user_name = textusername.getText();
        String user_pwd = textpassword.getText();

        ArrayList<String> error = validatelogin(user_name, user_pwd);

        if (error.isEmpty() == false) {
            text_login_errors.setText(error.get(0));
        } else {

            List data = DBManager.getDBM().loginHandler(user_name, user_pwd);
            Iterator i = data.iterator();
            if (i.hasNext()) {
                Users user = (Users) i.next();

                String email = user.getEmail();
                String username = user.getUsername();
                String nickname = user.getNickname();
                String password = user.getPassword();
                byte[] profile_image = user.getProfileImage();
                id = user.getId();

      
                edit_username.setText(username);
                edit_nickname.setText(nickname);
                edit_password.setText(password);

                if(profile_image != null){

                    ImageIcon imageicon = toImageIcon(profile_image);

                    ImageIcon iconresized1 = new ImageIcon(imageicon.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));
                    img_profile.setIcon(iconresized1);
                    img_profile6.setIcon(iconresized1);
                    img_profile3.setIcon(iconresized1);
                    img_profile4.setIcon(iconresized1);
                    img_profile5.setIcon(iconresized1);
                    img_profile4.setIcon(iconresized1);

                    ImageIcon iconresized2 = new ImageIcon(imageicon.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
                    edit_profile_image.setIcon(iconresized2);
                }

                if (user.getUserType().equalsIgnoreCase("admin")) {
                    //admin user

                    text_admin_username.setText("Welcome " + username);
                    text_admin_username2.setText("Welcome " + username);
                    text_admin_username3.setText("Welcome " + username);
                    
                   List users = DBManager.getDBM().get_all_users();
            
                    for (Iterator iterator = users.iterator(); iterator.hasNext();) {
                        
                        Users next = (Users) iterator.next();
                        String del_userid =next.getId().toString(); 
                        String del_username = next.getUsername();
 
                        userlist1.addItem(del_userid +"- "+ del_username);

                    }
                    
                    List groups = DBManager.getDBM().get_chat_groups();

                    for (Iterator iterator = groups.iterator(); iterator.hasNext();) {
                        
                        Groups next = (Groups) iterator.next();
                        String del_groupid =next.getId().toString(); 
                        String del_groupname = next.getName();

                        
                    }
                    

                    

                    login_panel.setVisible(false);
                    load_admin_group(true);
                    admin_panel.setVisible(true);

                } else{
                    //Normal user

                    text_user_username.setText("Welcome " + username);
                    text_user_username1.setText("Welcome " + username);
                    text_user_username2.setText("Welcome " + username);

                    me = new ChatClient(user.getId(), user.getUsername(), user.getNickname(), user.getEmail());

                    load_client_groups();
                    this.start_client();
                    login_panel.setVisible(false);
                    list_groups_panel.setVisible(true);

                }

            } else {
                System.out.println("Username or Password Incorrect");
                text_login_errors.setText("Username or Password Incorrect");
            }

        }
    }//GEN-LAST:event_btnloginMouseClicked

    private void showMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showMouseClicked
        textpassword.setEchoChar((char)8226);
        
    }//GEN-LAST:event_showMouseClicked

    private void create_groupMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_create_groupMouseClicked
         app_ui_reset();
        create_chat_panel.setVisible(true);
    }//GEN-LAST:event_create_groupMouseClicked

    private void btn_create_groupMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_create_groupMouseClicked
         String name = textgroupname.getText();
        String desc = textgroupdescription.getText();

        if (DBManager.getDBM().create_chat_group(name, desc)) {
            group_create_text.setText("Group created sucessfully");
        } else {
            group_create_text.setText("Group can not create!");
        }
    }//GEN-LAST:event_btn_create_groupMouseClicked

    private void btn_chat_groupsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_chat_groupsMouseClicked
        app_ui_reset();
       admin_panel.setVisible(true);
    }//GEN-LAST:event_btn_chat_groupsMouseClicked

    private void logout1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logout1MouseClicked
        app_ui_reset();
        login_panel.setVisible(true);
    }//GEN-LAST:event_logout1MouseClicked

    private void img_profile3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_img_profile3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_img_profile3MouseClicked

    private void btnregMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnregMouseClicked
        // TODO add your handling code here:
        
        String email = textregemail.getText();
        String username = textregusername.getText();
        String nickname = textregnickname.getText();
        String password = textregpassword.getText();

        //error array
        ArrayList<String> error = validateform(email, username, password);

        if (error.isEmpty() == false) {
            text_reg_errors.setText(error.get(0));
        } else {
            text_reg_errors.setText(null);
            //intsert details
            byte[] img = null;
            ImageIcon avatar = (ImageIcon) signup_profile_pic.getIcon();
            if (avatar != null) {
                try {
                 
                    BufferedImage bImage = ImageIconToBufferedImage(avatar);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ImageIO.write(bImage, "jpg", bos);
                    img = bos.toByteArray();

                } catch (IOException ex) {
                    Logger.getLogger(AppLayout.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            if (DBManager.getDBM().insert(img, email, username,nickname, password)) {
                text_reg_errors.setText("You Registered Successfully");
            }

        }
    }//GEN-LAST:event_btnregMouseClicked

    private void signup_profile_picMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_signup_profile_picMouseClicked
    
        
        JFileChooser chooser = new JFileChooser(); //open image file file
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png"); //set image type filter
        chooser.setFileFilter(filter); //filter
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) { //if image selected
            File file = chooser.getSelectedFile(); //get selected file
            String strfilepath = file.getAbsolutePath(); //get abs path

            try {
                ImageIcon icon = new ImageIcon(strfilepath); //string image path open as a image icon
                ImageIcon iconresized = new ImageIcon(icon.getImage().getScaledInstance(120, 120, Image.SCALE_DEFAULT)); //resize image icon fit for profile icon label
                signup_profile_pic.setText(null); // remove label text
                signup_profile_pic.setIcon(iconresized); //set seleted image to profile icon label 


            } catch (Exception e) {
                System.out.println("Exception occurred : " + e.getMessage());
            }
        }
    }//GEN-LAST:event_signup_profile_picMouseClicked

    private void remove_userMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_remove_userMouseClicked
        String del_user = (String) userlist1.getSelectedItem();
       String del_user_id = del_user.split("-")[0];
       
       int user_id = Integer.parseInt(del_user_id);
       DBManager.getDBM().delete_user(user_id);
       text_delete.setText("User Successfully Deleted ");
       userlist1.removeItem(userlist1.getSelectedItem());
    }//GEN-LAST:event_remove_userMouseClicked

    private void btn_chat_groups1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_chat_groups1MouseClicked
        app_ui_reset();
         admin_panel.setVisible(true);
    }//GEN-LAST:event_btn_chat_groups1MouseClicked

    private void create_group1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_create_group1MouseClicked
         app_ui_reset();
         create_chat_panel.setVisible(true);
    }//GEN-LAST:event_create_group1MouseClicked

    private void logout6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logout6MouseClicked
         app_ui_reset();
        login_panel.setVisible(true);
    }//GEN-LAST:event_logout6MouseClicked

    private void btnreg1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnreg1MouseClicked
        // TODO add your handling code here:
        //Get user details
        String username = edit_username.getText().trim();
        String nickname = edit_nickname.getText().trim();
        String password = edit_password.getText().trim();
        
            byte[] img = null;
            ImageIcon avatar = (ImageIcon) edit_profile_image.getIcon();
            if (avatar != null) {
                try {
                 
                    BufferedImage bImage = ImageIconToBufferedImage(avatar);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ImageIO.write(bImage, "jpg", bos);
                    img = bos.toByteArray();

                } catch (IOException ex) {
                    Logger.getLogger(AppLayout.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        
        DBManager.getDBM().update(img, username, nickname,password, id);
        update_msg.setText("Sucessfully Updated..");
        img_profile4.setIcon(avatar);
        img_profile4.setIcon(avatar);
    }//GEN-LAST:event_btnreg1MouseClicked

    private void edit_profile_imageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_edit_profile_imageMouseClicked
        
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png"); //set image type filter
        chooser.setFileFilter(filter); 
        int returnVal = chooser.showOpenDialog(null);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) { 
            File file = chooser.getSelectedFile(); 
            String strfilepath = file.getAbsolutePath();

            try {
                ImageIcon icon = new ImageIcon(strfilepath); 
                ImageIcon iconresized = new ImageIcon(icon.getImage().getScaledInstance(120, 120, Image.SCALE_DEFAULT)); 
                edit_profile_image.setText(null); 
                edit_profile_image.setIcon(iconresized);

            } catch (Exception e) {
                System.out.println("Exception occurred : " + e.getMessage());
            }
        }
    }//GEN-LAST:event_edit_profile_imageMouseClicked

    private void jLabel61MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel61MouseClicked
        // TODO add your handling code here:
        
         app_ui_reset();
         list_groups_panel.setVisible(true);
    }//GEN-LAST:event_jLabel61MouseClicked

    private void logout3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logout3MouseClicked
        // TODO add your handling code here:
        
        app_ui_reset();
        login_panel.setVisible(true);
    }//GEN-LAST:event_logout3MouseClicked

    private void show_eyeeditMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_show_eyeeditMouseClicked
        // TODO add your handling code here:
        edit_password.setEchoChar((char)8226);
        hide_eyeedit.setVisible(true);
        hide_eyeedit.setEnabled(true);
        show_eyeedit.setVisible(false);
        show_eyeedit.setEnabled(false);
    }//GEN-LAST:event_show_eyeeditMouseClicked

    private void hide_eyeeditMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hide_eyeeditMouseClicked
        // TODO add your handling code here:
        edit_password.setEchoChar((char)0);
        hide_eyeedit.setVisible(false);
        hide_eyeedit.setEnabled(false);
        show_eyeedit.setVisible(true);
        show_eyeedit.setEnabled(true);
    }//GEN-LAST:event_hide_eyeeditMouseClicked

    private void edit_profile_link_1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_edit_profile_link_1MouseClicked
        // TODO add your handling code here:
        app_ui_reset();
        edit_profile_panel.setVisible(true);
    }//GEN-LAST:event_edit_profile_link_1MouseClicked

    private void logout2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logout2MouseClicked
        // TODO add your handling code here:
        app_ui_reset();
        login_panel.setVisible(true);
    }//GEN-LAST:event_logout2MouseClicked

    private void msg_typerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_msg_typerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_msg_typerActionPerformed

    private void send_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_send_btnMouseClicked
        // TODO add your handling code here:
        
        this.sender();
            
        msgScrollPane.getVerticalScrollBar().addAdjustmentListener((AdjustmentEvent e) -> {
            e.getAdjustable().setValue(e.getAdjustable().getMaximum());
        });
    }//GEN-LAST:event_send_btnMouseClicked

    private void msg_typerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_msg_typerKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.sender();
        }
    }//GEN-LAST:event_msg_typerKeyPressed

    private void logout5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logout5MouseClicked
        // TODO add your handling code here:
        app_ui_reset();
        login_panel.setVisible(true);
    }//GEN-LAST:event_logout5MouseClicked

    private void edit_profile_link_3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_edit_profile_link_3MouseClicked
        // TODO add your handling code here:
        app_ui_reset();
        edit_profile_panel.setVisible(true);
    }//GEN-LAST:event_edit_profile_link_3MouseClicked

    private void jPanel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel5MouseClicked

    private void link_all_usersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_link_all_usersMouseClicked
         app_ui_reset();
        manage_users_panel.setVisible(true);
    }//GEN-LAST:event_link_all_usersMouseClicked

    private void jLabel40MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel40MouseClicked
        app_ui_reset();
        manage_users_panel.setVisible(true);
    }//GEN-LAST:event_jLabel40MouseClicked

    private void logoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseClicked
        // TODO add your handling code here:
        app_ui_reset();
        login_panel.setVisible(true);
    }//GEN-LAST:event_logoutMouseClicked

    private void close10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_close10MouseClicked
         dispose();
    }//GEN-LAST:event_close10MouseClicked

    
    
    
    
    
    
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
     int y2 = 210;

    public void recive_msg_handler(Message msg) {

        chat_background.repaint();
        chat_background.revalidate();

        JLabel msg_content = new javax.swing.JLabel();
        msg_content.setForeground(new java.awt.Color(255, 255, 255));
        msg_content.setText("<html>" + msg.getMessage() + "</html>");

        JLabel msg_time = new javax.swing.JLabel();
        msg_time.setForeground(new java.awt.Color(255, 255, 255));
        msg_time.setText(msg.getDate_time());

        JLabel msg_name = new javax.swing.JLabel();
        msg_name.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        msg_name.setForeground(new java.awt.Color(255, 255, 255));
        msg_name.setText(msg.getName());

        JLabel msg_dp = new javax.swing.JLabel();
        msg_dp.setBackground(new java.awt.Color(17, 89, 153));

        List data = DBManager.getDBM().get_avatart(msg.getUserid());
        Iterator i = data.iterator();
        if (i.hasNext()) {
            Users user = (Users) i.next();
            ImageIcon iconresized = new ImageIcon(toImageIcon(user.getProfileImage()).getImage().getScaledInstance(35, 35, Image.SCALE_DEFAULT));
            msg_dp.setIcon(iconresized);
        }

        JPanel msg_layer = new javax.swing.JPanel();

        msg_layer.setBackground(
                new java.awt.Color(54, 63, 77));
        msg_layer.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        msg_layer.setLayout(
                new org.netbeans.lib.awtextra.AbsoluteLayout());

        msg_layer.add(msg_content,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 260, 40));
        msg_layer.add(msg_time,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 210, -1));
        msg_layer.add(msg_name,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, 210, -1));
        msg_layer.add(msg_dp,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 15, 35, 35));


        chat_background.add(msg_layer,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(20, y2, 280, 110));

        chat_background.repaint();
        chat_background.revalidate();

        
        
        chat_background.repaint();
        chat_background.revalidate();
        

        y2 += 120;

    }
    

       public void send_msg_handler(Message msg) {

        chat_background.repaint();
        chat_background.revalidate();

        JLabel msg_content = new javax.swing.JLabel();
        msg_content.setForeground(new java.awt.Color(255, 255, 255));
        msg_content.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        msg_content.setText("<html>" + msg.getMessage() + "</html>");

        JLabel msg_time = new javax.swing.JLabel();
        msg_time.setForeground(new java.awt.Color(255, 255, 255));
        msg_time.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        msg_time.setText(msg.getDate_time());

        JLabel msg_name = new javax.swing.JLabel();
        msg_name.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        msg_name.setForeground(new java.awt.Color(255, 255, 255));
        msg_name.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        msg_name.setText(msg.getName());

        JLabel msg_dp = new javax.swing.JLabel();
        msg_dp.setBackground(new java.awt.Color(54, 63, 77));

        List data = DBManager.getDBM().get_avatart(msg.getUserid());
        Iterator i = data.iterator();
        if (i.hasNext()) {
            Users user = (Users) i.next();
            ImageIcon iconresized = new ImageIcon(toImageIcon(user.getProfileImage()).getImage().getScaledInstance(35, 35, Image.SCALE_DEFAULT));
            msg_dp.setIcon(iconresized);
        }

        JPanel msg_layer = new javax.swing.JPanel();
        msg_layer.setBackground(new java.awt.Color(42, 50, 61));
        msg_layer.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        msg_layer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        msg_layer.add(msg_content, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 260, 40));
        msg_layer.add(msg_time, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 210, -1));
        msg_layer.add(msg_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 210, -1));
        msg_layer.add(msg_dp, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, 35, 35));

      
        chat_background.add(msg_layer, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, y2, 280, 110));

        JScrollBar sb = msgScrollPane.getVerticalScrollBar();
        sb.setValue(sb.getMaximum());

        chat_background.repaint();
        chat_background.revalidate();

        y2 += 120;
    }
    
    
    
  
    
    
    
    
    
    
    Thread retrivemsg = new Thread() {
        @Override
        public void run() {

            int preiv = 0;

            while (true) {
                try {

                    Message nmsg = chat.broadcast();
                    if (nmsg != null) {
                        if (preiv != nmsg.getMsgid()) {
                            //System.out.println(nmsg.getDate_time() + "\t" + nmsg.getName() + " : " + nmsg.getMessage() + "\n");

                            System.out.println(nmsg.getMsgid() + "-" + me.getId());
                            if (nmsg.getUserid() == me.getId()) {
                                send_msg_handler(nmsg);
                            } else {
                                recive_msg_handler(nmsg);
                            }

                            preiv = nmsg.getMsgid();
                        }
                    }


                    Thread.sleep(100);
                } catch (RemoteException | NullPointerException ex) {
                    System.out.println(ex);
                } catch (InterruptedException ex) {

                }
            }

        }
    }; 
    
    
    
    
    public void start_server(int g_id) {
        try {
        Chat chat = new ChatService(g_id) {};
            Registry reg = LocateRegistry.createRegistry(2123);
            reg.bind("ChatAdmin", chat);

            System.out.println("Chat server is running...");

        } catch (RemoteException | AlreadyBoundException e) {
            System.out.println("Exception ocured : " + e.getMessage());
        }
    }
    
    
    
    
    
    

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AppLayout.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AppLayout.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AppLayout.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AppLayout.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AppLayout().setVisible(true);
            }
        });
    }
    
    
    
    
    
    
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel admin_group_list;
    private javax.swing.JPanel admin_panel;
    private javax.swing.JLabel btn_chat_groups;
    private javax.swing.JLabel btn_chat_groups1;
    private javax.swing.JButton btn_create_group;
    private javax.swing.JButton btnlogin;
    private javax.swing.JButton btnreg;
    private javax.swing.JButton btnreg1;
    private javax.swing.JPanel chat_background;
    private javax.swing.JPanel chat_panel;
    private javax.swing.JPanel client_chat_groups_panel;
    private javax.swing.JLabel close1;
    private javax.swing.JLabel close10;
    private javax.swing.JLabel close2;
    private javax.swing.JLabel close3;
    private javax.swing.JLabel close4;
    private javax.swing.JLabel close5;
    private javax.swing.JLabel close7;
    private javax.swing.JLabel close8;
    private javax.swing.JLabel close9;
    private javax.swing.JPanel create_chat_panel;
    private javax.swing.JLabel create_group;
    private javax.swing.JLabel create_group1;
    private javax.swing.JLabel create_group2;
    private javax.swing.JLabel create_group3;
    private javax.swing.JTextField edit_nickname;
    private javax.swing.JPasswordField edit_password;
    private javax.swing.JLabel edit_profile_image;
    private javax.swing.JLabel edit_profile_link_1;
    private javax.swing.JLabel edit_profile_link_2;
    private javax.swing.JLabel edit_profile_link_3;
    private javax.swing.JPanel edit_profile_panel;
    private javax.swing.JTextField edit_username;
    private javax.swing.JLabel group_create_text;
    private javax.swing.JLabel hide_eyeedit;
    private javax.swing.JLabel img_profile;
    private javax.swing.JLabel img_profile3;
    private javax.swing.JLabel img_profile4;
    private javax.swing.JLabel img_profile5;
    private javax.swing.JLabel img_profile6;
    private javax.swing.JLabel img_profile7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel link_all_users;
    private javax.swing.JLabel link_all_users1;
    private javax.swing.JLabel linkreg;
    private javax.swing.JPanel list_groups_panel;
    private javax.swing.JPanel login_panel;
    private javax.swing.JLabel logout;
    private javax.swing.JLabel logout1;
    private javax.swing.JLabel logout2;
    private javax.swing.JLabel logout3;
    private javax.swing.JLabel logout5;
    private javax.swing.JLabel logout6;
    private javax.swing.JPanel manage_users_panel;
    private javax.swing.JScrollPane msgScrollPane;
    private javax.swing.JTextField msg_typer;
    private javax.swing.JPanel register_panel;
    private javax.swing.JButton remove_user;
    private javax.swing.JButton send_btn;
    private javax.swing.JLabel show;
    private javax.swing.JLabel show_eyeedit;
    private javax.swing.JLabel signup_profile_pic;
    private javax.swing.JLabel text_admin_username;
    private javax.swing.JLabel text_admin_username2;
    private javax.swing.JLabel text_admin_username3;
    private javax.swing.JLabel text_delete;
    private javax.swing.JLabel text_login_errors;
    private javax.swing.JLabel text_reg_error;
    private javax.swing.JLabel text_reg_errors;
    private javax.swing.JLabel text_user_username;
    private javax.swing.JLabel text_user_username1;
    private javax.swing.JLabel text_user_username2;
    private javax.swing.JTextField textgroupdescription;
    private javax.swing.JTextField textgroupname;
    private javax.swing.JPasswordField textpassword;
    private javax.swing.JTextField textregemail;
    private javax.swing.JTextField textregnickname;
    private javax.swing.JTextField textregpassword;
    private javax.swing.JTextField textregusername;
    private javax.swing.JTextField textusername;
    private javax.swing.JLabel titlebar;
    private javax.swing.JLabel update_msg;
    private javax.swing.JComboBox<String> userlist1;
    // End of variables declaration//GEN-END:variables
}
