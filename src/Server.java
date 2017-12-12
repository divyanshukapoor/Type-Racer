
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hp
 */
public class Server{ 
   public ServerSocket serverSocket=null;
   public ObjectInputStream is=null;
   public ObjectOutputStream os=null;
   String name;
   public Vector <String> ser_names;
   public HashMap<String ,ObjectOutputStream> hos;
   public HashMap<String,ObjectInputStream> his;
   public HashMap<String ,Requirement> status_map;
   Thread serverthread ;
   public Server()
   {
       
   }
   public Server(String name)
   {
       ser_names=new Vector<String>();
       Requirement o=new Requirement(name, 0,0,0);
       o.name=name;
       this.name=name;
       ser_names.add(name);
       hos=new HashMap<String ,ObjectOutputStream>();
       his=new HashMap<String ,ObjectInputStream>();
       status_map=new HashMap<String,Requirement>();
       status_map.put(name,o);
       his.put(o.name, is);
       hos.put(o.name,os);
   }
   int k;
   public Vector getArraylist()
   {
       return ser_names;
   }
   int flag;
   public void communicate()
   {
       if(flag==0)
       {
       flag=1;
       try {
           serverSocket=new ServerSocket(5500);
       } catch (IOException ex) {
           Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
       }
       }
       while(true)
       {
           Socket socket = null;
       try {
           socket =serverSocket.accept();
           is=new ObjectInputStream(socket.getInputStream());
           os=new ObjectOutputStream(socket.getOutputStream());
           System.out.println("Server Connected");
           Requirement obj=(Requirement)is.readObject();
           ser_names.add(obj.name);
           status_map.put(obj.name, obj);
           his.put(obj.name, is);
           hos.put(obj.name,os);
           writeToAll(status_map);
           flushall();
           Thread th=new Thread(new MyThread(socket,is,os,obj));
           th.start();
       } catch (IOException ex) {
           Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
       }   catch (ClassNotFoundException ex) {  
               Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
           }  
       }
   }

    private void writeToAll(HashMap<String,Requirement> status_map) {
       for(String s:ser_names)
       {
           if(!s.equals(name)){
           ObjectOutputStream os=hos.get(s);
           System.out.println("write "+s);
           try {
               os.writeObject(status_map);
           } catch (IOException ex) {
               Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
           }
           }
       }
    }

    

    private void flushall() {
        for(String s:ser_names)
       {
           if(!s.equals(name)){
           ObjectOutputStream os=hos.get(s);
           System.out.println("flush "+s);
           try {
               os.flush();
           } catch (IOException ex) {
               Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
           }
           }
       }
       }

    private  class MyThread implements Runnable {
        Socket socket;
        ObjectInputStream is;
        ObjectOutputStream os;
        Requirement obj;
        public MyThread(Socket socket,ObjectInputStream is ,ObjectOutputStream os,Requirement obj) {
            this.socket =socket;
            this.is=is;
            this.os=os;
            this.obj = obj;
        }
        @Override
        public void run() {
            System.out.println(obj.name);
            System.out.println("Object Received "+obj.name);
        }
    }
}
