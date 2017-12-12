
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Divyanshu_Kapoor
 */
public class Client implements Runnable{
public Socket socket = null;
public ObjectInputStream inputStream = null;
public ObjectOutputStream outputStream = null;
public boolean isConnected = false;
public HashMap<String,Requirement> clientmap;
String name,ip;
int port;
public Vector<String> client_names;
Requirement obj;

public Client(String name)
{
this.name=name;
client_names=new Vector<String>();
}

public Client(String name,String ip,int port)  
{
client_names=new Vector<String>();
this.name=name;
this.ip=ip;
this.port=port;
}

public void communicate()
{
while (!isConnected) {
try {
socket = new Socket(ip,port);
System.out.println("Client Connected");
isConnected = true;
outputStream = new ObjectOutputStream(socket.getOutputStream());
inputStream =new ObjectInputStream(socket.getInputStream());
obj=new Requirement(name,0,0,0);
outputStream.writeObject(obj);
System.out.println("Client wrote "+obj.name);
Thread th=new Thread(this);
th.start();
} 
catch (SocketException se) {
se.printStackTrace();
} catch (IOException e) {
e.printStackTrace();
}
}
}
public Vector getArraylist()
{
    return client_names;
}

    @Override
    public void run() {
     while(true)
     {
      if(inputStream!=null)
      {
          try {
              clientmap=(HashMap)inputStream.readObject();
              Set entrySet=clientmap.entrySet();
              Iterator it=entrySet.iterator();
              while(it.hasNext())
              {
                  System.out.println("sldjflk");
                  Map.Entry me=(Map.Entry)it.next();
                  String s = me.getKey().toString();
                  System.out.println(s);
                  client_names.add(s);
              }
          } catch (IOException ex) {
              Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
          } catch (ClassNotFoundException ex) {
              Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
      }
    }

}
