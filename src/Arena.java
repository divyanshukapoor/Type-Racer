
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hp
 */
public class Arena extends javax.swing.JFrame {

    /**
     * Creates new form Arena
     */
    Timer t;
    String mode,name;
    int num_of_files;
    
    String headpath=new Provide_path().getHeadpath();
    String path_to_folder=headpath+"\\Files";
    Document doc;
    String txt;
    int wcount;
    int cws,cwe; // current word start end
    int length;
    int i;
    Clip clip;
    int curr;
    SimpleAttributeSet set;
    // a construct to load text feild with a file
    public void initialize()
    {
      min=0;
      sec=0;
      set_timer();
      set_it_def();
      this.cwe=0;
      this.cws=0;
      this.i=0;
      this.curr=0;
      this.set= new SimpleAttributeSet();
      String rf=random_file();
      //txt=get_file_as_string(rf);
      this.txt=get_file_as_string(rf);
      jTextPane1.setEditable(false);
      jTextPane1.requestFocus();
      this.length=txt.length();
      int ind=0;
      while(txt.charAt(ind)!=' '&& ind<length)
      {
          ind++;
      }
      if(ind!=0)
      this.cwe=ind--;
      jTextPane1.setText(txt);
      this.doc = jTextPane1.getStyledDocument();
        try {
            String st=doc.getText(0,length);
      doc.remove(0,length);
      StyleConstants.setForeground(set, Color.BLUE);
            doc.putProperty(set, st);
            doc.insertString(0,st, set);
        } catch (BadLocationException ex) {
            Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
        }
      //StyleConstants.setForeground(set,new Color(0,102,204));
      jTextPane1.setDocument(doc);
      change_current_word_color(0,cwe);
      jProgressBar1.setMaximum(0);  
      arr=new int[length+1];
    }
    //
    
    public void accuracy_to_file(Drilldata obj,String mode,String path)
    {
        BufferedWriter br=null;
        FileWriter fr=null;
        File f=new File(path);
        try {
            fr=new FileWriter(f.getAbsoluteFile(),true);
            br=new BufferedWriter(fr);
            String s=obj.accuracy+"\n";
            br.write(s);
            br.close();
            fr.close();
        } catch (IOException ex) {
            Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void speed_to_file(Drilldata obj,String mode,String path)
    {
        BufferedWriter br=null;
        FileWriter fr=null;
        File f=new File(path);
        try {
            fr=new FileWriter(f.getAbsoluteFile(),true);
            br=new BufferedWriter(fr);
            String s=obj.speed+"\n";
            br.write(s);
            br.close();
            fr.close();
        } catch (IOException ex) {
            Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    int min=0,sec=0;
    public void set_timer()
    {
      tm.setText("0"+min+":"+"0"+sec);
      t=new Timer(1000,new Timerclass());
      t.start(); 
    }
    class Timerclass implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) {
           sec++;
           if(sec>=60){
               sec=00;
               min++;
           }
           if(sec/10 ==0)
               tm.setText("0"+min+":"+"0"+sec);
           else 
               tm.setText("0"+min+":"+sec);
        }
        
    }
    
    public Arena()
    {
      initComponents();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
        }
        SwingUtilities.updateComponentTreeUI(this);
      initialize();
      jProgressBar1.setMaximum(0);
      jProgressBar1.setMaximum(length);
      this.wcount=0;
    }
    public Arena(String mode,String name) 
    {
      initComponents();
      this.mode=mode;
      this.name=name;
      lrating.setVisible(false);
      if(mode.equals("user"))
      {
       lrating.setVisible(true);
       int n=0;
       File fs=new File(headpath+"\\user\\"+name+"_speed.txt");
       File fa=new File(headpath+"\\user\\"+name+"_accuracy.txt"); 
       BufferedReader bs=null,ba=null;
        try {
            bs= new BufferedReader(new FileReader(fs.getAbsolutePath()));
            ba=new BufferedReader(new FileReader(fa.getAbsolutePath()));
            String s;
            int acc=0,sp=0;
            while((s= bs.readLine()) != null){
             sp=sp+Integer.parseInt(s);
             n++;
            }
            while((s=ba.readLine())!=null)
            {
               acc+=Integer.parseInt(s);
            }
             System.out.println(sp+" "+acc);
            if(n!=0){
            sp=sp/n;
            acc=acc/n;
            }
           
            int ans=0;
            ans= (int) (((0.6*sp)+(0.4*acc)));
            int f=0;
            if(ans>=10 && ans <14)
                f=1;
            else if(ans>=14 && ans<29)
                f=2;
            else if(ans>=29 && ans<43)
                f=3;
            else if(ans>=43 && ans<57)
                f=4;
            else if(ans>=57 && ans<71)
                f=5;
            else if(ans>=71 && ans<85)
                f=6;
            else if(ans>=85)
                f=7;
            String in=headpath+"\\Resources\\s"+f+".png";
            System.out.println(in);
            ImageIcon ic=new ImageIcon(in);
            System.out.println(lrating);
            lrating.setIcon(ic);
            bs.close();
            ba.close();
         }
        catch (FileNotFoundException ex) {
            Logger.getLogger(UserForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UserForm.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
        }
        SwingUtilities.updateComponentTreeUI(this);
      initialize();
      jProgressBar1.setMaximum(0);
      jProgressBar1.setMaximum(length);
      this.wcount=0;
    }
    
    void set_it_def()
    {
        _1.setVisible(false);
        _2.setVisible(false);
        _3.setVisible(false);
        _4.setVisible(false);
        _5.setVisible(false);
        _6.setVisible(false);
        _7.setVisible(false);
        _8.setVisible(false);
        _9.setVisible(false);
        _10.setVisible(false);
    }
    void set_it(char c)
    {
            if(c=='q' || c=='1'|| c=='a'|| c=='z'){set_it_def();_1.setVisible(true);}
            if(c=='2' || c=='w'|| c=='s'|| c=='x')//left ring
            {set_it_def();_2.setVisible(true);}
            if(c=='3' || c=='e'|| c=='d'|| c=='c')//left middle
                {set_it_def();_3.setVisible(true);}
            if(c=='4' || c=='r'|| c=='f'|| c=='v' || c=='5' || c=='t'|| c=='g'|| c=='b')//left index
                {set_it_def();_4.setVisible(true);}
            if(c=='0' || c=='-'|| c=='='|| c=='p' || c=='[' || c==']'|| c==';'|| c=='\'' || c == '\\')//right tiny
                {set_it_def();_10.setVisible(true);}
            if(c=='9' || c=='o'|| c=='l'|| c=='.')//right ring
                {set_it_def();_9.setVisible(true);}
            if(c=='8' || c=='i'|| c=='k'|| c==',')//right middle
                {set_it_def();_8.setVisible(true);}
            if(c=='6' || c=='y'|| c=='h'|| c=='n' || c=='7' || c=='u'|| c=='j'|| c=='m')//right index
                {set_it_def();_7.setVisible(true);}
            if(c=='Q' || c=='!'|| c=='A'|| c=='Z')//left tiny && right tiny
                {set_it_def();_10.setVisible(true);_1.setVisible(true);}
            if(c=='@' || c=='W'|| c=='S'|| c=='X')//left ring && right tiny
                {set_it_def();_10.setVisible(true);_2.setVisible(true);}
            if(c=='#' || c=='E'|| c=='D'|| c=='C')//left middle && right tiny
                {set_it_def();_10.setVisible(true);_3.setVisible(true);}
            if(c=='$' || c=='R'|| c=='F'|| c=='V' || c=='%' || c=='T'|| c=='G'|| c=='B')//left index && right tiny
                {set_it_def();_10.setVisible(true);_4.setVisible(true);}
            if(c==')' || c=='_'|| c=='+'|| c=='P' || c=='{' || c=='}'|| c==':'|| c=='\"' || c == '|')//right tiny && left tiny
                {set_it_def();_10.setVisible(true);_1.setVisible(true);}
            if(c=='(' || c=='O'|| c=='L'|| c=='>')//right ring && left tiny
                {set_it_def();_1.setVisible(true);_9.setVisible(true);}
            if(c=='*' || c=='I'|| c=='K'|| c=='<')//right middle && left tiny
                {set_it_def();_1.setVisible(true);_8.setVisible(true);}
            if(c=='^' || c=='Y'|| c=='H'|| c=='N' || c=='&' || c=='U'|| c=='J'|| c=='M')
                {set_it_def();_1.setVisible(true);_7.setVisible(true);}
            if(c==' ')
                {set_it_def();_5.setVisible(true);}
    }
    String get_file_as_string(String s) // this is text file name
    {
        String res="";
         try // this is text file name
         {
             BufferedReader br=null;
             br=new BufferedReader(new FileReader(s));
             String tmp=""; // res is string withn copied text
             while((tmp=br.readLine())!=null)
             {
                 res+=tmp;
             }
             System.out.println(res);
         } catch (IOException ex) {
            Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    } 
    
    // select random text file
    String random_file()
    {
      num_of_files= new File(path_to_folder).listFiles().length;
      int fno=new Random().nextInt(num_of_files)+1;
      System.out.println(fno);
      String fpath=path_to_folder+"\\"+fno+".txt";
      System.out.println(fpath);
      return fpath;
    }
    void change_current_word_color(int ss,int ee)
    {
        try {
            String st=doc.getText(ss,(ee-ss+1));
            doc.remove(ss,(ee-ss+1));
            set = new SimpleAttributeSet();
            StyleConstants.setForeground(set, Color.yellow);
            doc.putProperty(set, st);
            doc.insertString(ss,st,set);
        } catch (BadLocationException ex) {
            Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    void change_char_color(int pos,boolean status)
    {
         try {
            String st=doc.getText(pos,1);
            doc.remove(pos,1);
            set = new SimpleAttributeSet();
            if(status==true){
            StyleConstants.setForeground(set, Color.GREEN);
            StyleConstants.setUnderline(set,true);
            }
            else {
            StyleConstants.setForeground(set, Color.RED);
            StyleConstants.setUnderline(set,true);
            }
            doc.putProperty(set, st);
            doc.insertString(pos,st, set);
        } catch (BadLocationException ex) {
            Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     void change_space_char(int pos)
    {
         try {
            String st=doc.getText(pos,1);
            doc.remove(pos,1);
            set = new SimpleAttributeSet();
            StyleConstants.setForeground(set, Color.YELLOW);
            doc.putProperty(set, st);
            doc.insertString(pos,st, set);
        } catch (BadLocationException ex) {
            Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    void change_space_color(int pos,boolean status)
    {
       try {
            String st=doc.getText(pos,1);
            doc.remove(pos,1);
            set = new SimpleAttributeSet();
            if(status==true){
            StyleConstants.setBackground(set, Color.GREEN);
            }
            else {
            StyleConstants.setBackground(set, Color.RED);
            }
            doc.putProperty(set, st);
            doc.insertString(pos,st, set);
        } catch (BadLocationException ex) {
            Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jPanel2 = new javax.swing.JPanel();
        accuracy = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel3 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lrating = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tm = new javax.swing.JLabel();
        speedlabel = new javax.swing.JLabel();
        speed = new javax.swing.JLabel();
        jSpeed = new javax.swing.JButton();
        jAccuracy = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        entered_char = new javax.swing.JLabel();
        _2 = new javax.swing.JLabel();
        _4 = new javax.swing.JLabel();
        _3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        _5 = new javax.swing.JLabel();
        _8 = new javax.swing.JLabel();
        _9 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        _10 = new javax.swing.JLabel();
        _7 = new javax.swing.JLabel();
        _6 = new javax.swing.JLabel();
        _1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTextPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Text", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 14), new java.awt.Color(51, 0, 255))); // NOI18N
        jTextPane1.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        jTextPane1.setForeground(new java.awt.Color(0, 102, 204));
        jTextPane1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextPane1KeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(jTextPane1);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Stats", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 14), new java.awt.Color(51, 51, 255))); // NOI18N

        accuracy.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        accuracy.setForeground(new java.awt.Color(204, 0, 102));
        accuracy.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jProgressBar1.setBackground(new java.awt.Color(0, 153, 0));
        jProgressBar1.setForeground(new java.awt.Color(0, 204, 0));
        jProgressBar1.setRequestFocusEnabled(false);

        jLabel3.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 102));
        jLabel3.setText("Progress");

        jLabel15.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel15.setText("Accuracy");

        lrating.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(240, 240, 240), null, new java.awt.Color(204, 204, 255)));

        jLabel4.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel4.setText("Timer");

        tm.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        tm.setForeground(new java.awt.Color(0, 51, 255));

        speedlabel.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        speedlabel.setText("Speed(WPM)");

        jSpeed.setText("Speed Chart");
        jSpeed.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jSpeedMouseClicked(evt);
            }
        });

        jAccuracy.setText("Accuracy Chart");
        jAccuracy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jAccuracyMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tm, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(speedlabel)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(164, 164, 164)
                        .addComponent(jAccuracy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(accuracy, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(speed, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addComponent(lrating, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(speedlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(speed, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(accuracy, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
                .addGap(27, 27, 27)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tm, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpeed)
                    .addComponent(jAccuracy))
                .addGap(54, 54, 54)
                .addComponent(lrating, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setLayout(null);

        entered_char.setBackground(new java.awt.Color(204, 255, 204));
        entered_char.setFont(new java.awt.Font("Verdana", 1, 22)); // NOI18N
        entered_char.setForeground(new java.awt.Color(0, 51, 255));
        entered_char.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        entered_char.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Typed", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 12), new java.awt.Color(255, 51, 51))); // NOI18N
        jPanel3.add(entered_char);
        entered_char.setBounds(500, 220, 60, 60);

        _2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/2.png"))); // NOI18N
        jPanel3.add(_2);
        _2.setBounds(180, 230, 10, 10);

        _4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/4.png"))); // NOI18N
        jPanel3.add(_4);
        _4.setBounds(230, 220, 10, 10);

        _3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/3.png"))); // NOI18N
        jPanel3.add(_3);
        _3.setBounds(200, 220, 10, 10);

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/blank.png"))); // NOI18N
        jPanel3.add(jLabel5);
        jLabel5.setBounds(130, 240, 30, 20);

        _5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/5.png"))); // NOI18N
        jPanel3.add(_5);
        _5.setBounds(270, 270, 10, 10);

        _8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/8.png"))); // NOI18N
        jPanel3.add(_8);
        _8.setBounds(384, 220, 10, 10);

        _9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/9.png"))); // NOI18N
        jPanel3.add(_9);
        _9.setBounds(400, 230, 10, 10);

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/blank.png"))); // NOI18N
        jPanel3.add(jLabel7);
        jLabel7.setBounds(180, 190, 23, 20);

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/blank.png"))); // NOI18N
        jPanel3.add(jLabel10);
        jLabel10.setBounds(290, 250, 30, 20);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/blank.png"))); // NOI18N
        jPanel3.add(jLabel6);
        jLabel6.setBounds(154, 210, 20, 20);

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/blank.png"))); // NOI18N
        jPanel3.add(jLabel8);
        jLabel8.setBounds(220, 194, 23, 20);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/blank.png"))); // NOI18N
        jPanel3.add(jLabel9);
        jLabel9.setBounds(270, 250, 20, 20);

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/blank.png"))); // NOI18N
        jPanel3.add(jLabel14);
        jLabel14.setBounds(430, 240, 20, 20);

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/blank.png"))); // NOI18N
        jPanel3.add(jLabel13);
        jLabel13.setBounds(410, 210, 20, 20);

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/blank.png"))); // NOI18N
        jPanel3.add(jLabel12);
        jLabel12.setBounds(390, 194, 20, 20);

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/blank.png"))); // NOI18N
        jPanel3.add(jLabel11);
        jLabel11.setBounds(340, 194, 20, 20);

        _10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/10.png"))); // NOI18N
        jPanel3.add(_10);
        _10.setBounds(430, 260, 10, 10);

        _7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/7.png"))); // NOI18N
        jPanel3.add(_7);
        _7.setBounds(350, 220, 10, 10);

        _6.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        _6.setForeground(new java.awt.Color(255, 0, 0));
        _6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/6.png"))); // NOI18N
        _6.setText("I");
        jPanel3.add(_6);
        _6.setBounds(310, 270, 10, 10);

        _1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/1.png"))); // NOI18N
        jPanel3.add(_1);
        _1.setBounds(150, 260, 10, 10);

        jLabel1.setBackground(new java.awt.Color(255, 0, 0));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/keyboard_keys_position.gif"))); // NOI18N
        jPanel3.add(jLabel1);
        jLabel1.setBounds(0, 0, 600, 340);

        jButton1.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(51, 0, 255));
        jButton1.setText("End");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 601, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        setVisible(false);
        
    }//GEN-LAST:event_jButton1ActionPerformed
    
    int []arr;
    int d=1,c=1;
    public void matchpattern(String st,char ch)
    {
        if(ch==' ' && st.charAt(curr)==ch) // space is there and input is  space 
        {
            arr[curr]=1;
            d=1;
            System.out.println(ch+" curr= "+curr);
            change_space_color(curr,true);
            // nothing to do
            if(c==1)
            {
                cws=curr+1;
                c=0;
            }
            else
            cws++;
            cwe=cws;
        }
        else if(ch==st.charAt(curr)) // char is there and input is  same char
        {
            arr[curr]=1;
            c=1;
            if(d==1)
            {
                i=0;
                int j=curr;
                while(st.charAt(j)!=' '&&j<length-1)
                {
                    i++;
                    j++;
                }
                cwe=cws+i-1;
            change_current_word_color(cws,cwe);   
            }
            d=0;         
            change_char_color(curr,true);
            System.out.println(ch+" s= "+cws+" e= "+cwe+"curr = "+curr); 
            
        }
        else if(st.charAt(curr)==' ')// string has space but not input space 
        {
            wcount++;
            System.out.println("curr = "+curr);
            change_space_color(curr,false);
            cws=curr+1;
            d=1;
        }
        else
        {
            wcount++;
            c=1;
            if(d==1)
            {
                i=0;
                int j=curr;
                while(j<length && st.charAt(j)!=' ')
                {
                    i++;
                    j++;
                }
                cwe=cws+i-1;
            change_current_word_color(cws,cwe);
            }
            d=0;
            System.out.println("*"+" s= "+cws+" e= "+cwe+"curr = "+curr); // input is wrong
            change_char_color(curr, false);
        }
        curr++;
    }
    public void fun()
    {
        if(curr>=cws)
        {
            arr[curr]=0;
            curr--;
            change_space_char(curr);
            System.out.println("Backspace "+curr);
        }
    }

    public void accuracy()
    {
        
    }
    private void jTextPane1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextPane1KeyTyped
        // TODO add your handling code here:
        char ch;
        ch=evt.getKeyChar();
        entered_char.setText((ch+""));
        if(curr<length-1)
        set_it(txt.charAt(curr+1));
        System.out.println(ch);
        jProgressBar1.setValue(curr);  
        if(curr<length)
        {
           if(evt.getKeyChar()==KeyEvent.VK_BACK_SPACE)
           {
            System.out.println("Heellllo");
            fun();
            }
           else
            matchpattern(txt,ch);
           if(curr==length-1){
               t.stop();
            int s=0;
            for(int i=0;i<arr.length;i++)
            {
                if(arr[i]==1)
                    s++;
            }
            int acc=(int)Math.ceil((double)((s*100)/length));
            accuracy.setText(acc+" %");
            String []words=txt.split("\\s+");
            System.out.println("Length ="+words.length);
            double time=min+(double)sec/60;
            double timerounded=Math.round(time*100D)/100D;
            double ans=words.length/timerounded;
            speed.setText((int)ans+"");
            Drilldata obj=new Drilldata((int)ans, acc);
            accuracy_to_file(obj,mode,headpath+mode+"\\"+name+"_accuracy.txt");
            speed_to_file(obj,mode,headpath+mode+"\\"+name+"_speed.txt");
           }
        }
        else if(evt.getKeyChar()==KeyEvent.VK_ENTER)
        {
            accuracy.setText("");
            wcount++;
            speed.setText("");
            initialize();
            jProgressBar1.setMinimum(0);
            jProgressBar1.setMaximum(length);
        }
    }//GEN-LAST:event_jTextPane1KeyTyped

    private void jAccuracyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jAccuracyMouseClicked
        // TODO add your handling code here:
        Stats stats=new Stats("Accuracy","Accuray(in %)","accuracy",mode,name);
        stats.setSize(550, 300);
        Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
        int xw=(int)d.getWidth();
        int xh=(int)d.getHeight();
        int w=stats.getWidth();
        int h=stats.getHeight();
        stats.setLocation((xw-w)/2,(xh-h)/2);
        stats.setVisible(true);
        stats.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }//GEN-LAST:event_jAccuracyMouseClicked

    private void jSpeedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSpeedMouseClicked
        // TODO add your handling code here:
        Stats stats=new Stats("Speed","Speed(in WPM)","speed",mode,name);
        stats.setSize(550, 300);
        Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
        int xw=(int)d.getWidth();
        int xh=(int)d.getHeight();
        int w=stats.getWidth();
        int h=stats.getHeight();
        stats.setLocation((xw-w)/2,(xh-h)/2);
        stats.setVisible(true);
        stats.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    }//GEN-LAST:event_jSpeedMouseClicked

    /**
     * @param args the command line arguments
     */
    /*    public static void main(String args[]) {
       
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Arena.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Arena.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Arena.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Arena.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        
        
        
        new Arena();
        
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Arena().setVisible(true);
            }
        });
       */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel _1;
    private javax.swing.JLabel _10;
    private javax.swing.JLabel _2;
    private javax.swing.JLabel _3;
    private javax.swing.JLabel _4;
    private javax.swing.JLabel _5;
    private javax.swing.JLabel _6;
    private javax.swing.JLabel _7;
    private javax.swing.JLabel _8;
    private javax.swing.JLabel _9;
    private javax.swing.JLabel accuracy;
    private javax.swing.JLabel entered_char;
    private javax.swing.JButton jAccuracy;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jSpeed;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JLabel lrating;
    private javax.swing.JLabel speed;
    private javax.swing.JLabel speedlabel;
    private javax.swing.JLabel tm;
    // End of variables declaration//GEN-END:variables

}
