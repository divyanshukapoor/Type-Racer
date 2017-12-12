
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Divyanshu_Kapoor
 */
public class Requirement implements Serializable{
    int n;
    String name;
    public Vector <String> user_names;
    int speed,accuracy;
    public Requirement(String name,int n,int speed,int accuracy)
    {
        this.name=name;
        this.n=n;
        this.speed=speed;
        this.accuracy=accuracy;
    }
    
}
