
import static com.sun.glass.ui.Cursor.setVisible;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hp
 */
public class User {
    String name;
    String headpath=new Provide_path().getHeadpath();
    User(String name)
    {
        this.name=name;
        File f=new File(headpath+"\\user\\"+name+"_speed.txt");
        File f1=new File(headpath+"\\user\\"+name+"_accuracy.txt");
        try {
            f.createNewFile();
            f1.createNewFile();
            String s="Welcome "+name;
            System.out.println(name);
            JOptionPane.showMessageDialog(null,s);
        } catch (IOException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        new Arena("user",name).setVisible(true);
    }
}
