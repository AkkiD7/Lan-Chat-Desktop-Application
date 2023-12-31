import java.util.StringTokenizer;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
class Client extends JFrame implements ActionListener
{
    JTextField address;
    JButton recieve;
    JLabel status,title;
    int screenX,screenY,myX,myY;
    JPanel lowerBorder;
    JProgressBar jpb;
    String location; // represents location where downloaded file is to be saved
    private void setFont(Font f,JComponent...c)
    {
        for(JComponent C:c)
            C.setFont(f);
    }
    private void add(JComponent parent,JComponent component,int gridx, int gridy, int gridwidth, int gridheight,int posInCell,Insets insets)
    {
        parent.add(component, new GridBagConstraints(gridx, gridy,gridwidth, gridheight, 1.0, 1.0, posInCell, GridBagConstraints.NONE, insets, 0,0));
    }
    Client()
    {
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png"))); //for setting icon
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE); //for closing window
		setTitle("LAN chatting");	//Set Title to window


        // set Windows Look and Feel for Windows Systems
        try
        {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,"Using Swing Default Look and Feel, as you a using a NON-WINDOWS System","Changing look and feel",0);
        }
        
        JPanel upperBorder,panel,flowPanel;
        JFileChooser jfc;
        final JLabel defaultLocation = new JLabel("Default location is : "+(location=defLoc()));
        final JLabel changeLocation = new JLabel("Change Location where downloaded file is saved ");
		defaultLocation.setForeground(new Color(25,22,69));
		changeLocation.setForeground(new Color(25,22,69));

        address   = new JTextField("Enter Server IP Address");
        (recieve   = new JButton("Recieve File")).setForeground(new Color(25,22,69));
       
        (status   = new JLabel("Enter Server Adress and press 'Recieve File' to Continue")).setForeground(Color.WHITE);
        (title    = new JLabel("  Smart File Transfer (Client)")).setForeground(new Color(229,229,229));
        jfc       = new JFileChooser();
		panel = new JPanel();
        panel.setBackground(new Color(0,204,204));

        jpb = new JProgressBar();
        jpb.setStringPainted(true);
        jpb.setFont(new Font("Times new roman",Font.BOLD,20));
        jpb.setForeground(new Color(170,111,236));
        jpb.setBackground(Color.BLACK);

        panel     = new JPanel();
        panel.setBackground(new Color(0,204,204));
		
        (upperBorder = new JPanel()).setBackground(new Color(25,22,69));
        (lowerBorder = new JPanel()).setBackground(new Color(41,143,143));
        (flowPanel   = new JPanel()).setBackground(new Color(25,22,69));

        setFont(new Font("Times new roman",Font.PLAIN,20),status,recieve,title);
        setFont(new Font("Times new roman",Font.PLAIN,26),address);

        panel.setLayout(new GridBagLayout());
        upperBorder.setLayout(new BorderLayout());
        lowerBorder.setLayout(new FlowLayout(FlowLayout.CENTER));

        add(panel,address,0,0,1,1,GridBagConstraints.CENTER,new Insets(10,10,10,10));
        add(panel,recieve,0,1,1,1,GridBagConstraints.CENTER,new Insets(10,10,10,10));
        add(panel,defaultLocation,0,2,3,1,GridBagConstraints.WEST,new Insets(0,0,0,0));
        add(panel,changeLocation,0,3,3,1,GridBagConstraints.WEST,new Insets(0,0,0,0));

        
        upperBorder.add(flowPanel,BorderLayout.EAST);
        upperBorder.add(title); // not centered, towards left.
        lowerBorder.add(status);
        add(panel);
        add(upperBorder,BorderLayout.NORTH);
        add(lowerBorder,BorderLayout.SOUTH);
        final JTextField temp=address;
        addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent me)
            {
                screenX = me.getXOnScreen();
                screenY = me.getYOnScreen();
                myX     = getX();
                myY     = getY();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter()
        {
            public void mouseDragged(MouseEvent me)
            {
                int deltaX = me.getXOnScreen() - screenX;
                int deltaY = me.getYOnScreen() - screenY;
                setLocation(myX + deltaX , myY + deltaY);
            }
        });
        address.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent me)
            {
                temp.setText("");
                temp.removeMouseListener(this);
            }
        });
        changeLocation.addMouseListener(new MouseAdapter()
        {
            public void mouseEntered(MouseEvent me)
            {
                changeLocation.setForeground(Color.WHITE);
            }
            public void mouseExited(MouseEvent me)
            {
                changeLocation.setForeground(Color.BLACK);
            }
            public void mouseClicked(MouseEvent me)
            {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                jfc.setCurrentDirectory(new File(location));
                int ret = jfc.showSaveDialog(null);
                if(ret == JFileChooser.APPROVE_OPTION)
                {
                    try
                    {
                        FileWriter fw = new FileWriter("c:\\sft\\log.bin");
                        fw.write(jfc.getSelectedFile().toString());
                        fw.close();
                        defaultLocation.setText("Default Location is : "+(location=jfc.getSelectedFile().toString()));
                    }
                    catch(Exception e)
                    {
                         JOptionPane.showMessageDialog(null,"Error in changing path, The application will now exit","Fatal Error",0);
                    }
                }
            }
        });
        recieve.addActionListener(this);
       
        setResizable(false);
       
        setSize(600,300);
        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(0,204,204),5));
        getRootPane().setDefaultButton(recieve);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        requestFocus();
    }
    private String defLoc()
    {
        String ret="";
        try
        {
            BufferedReader br  = new BufferedReader(new FileReader("c:\\sft\\log.bin"));
            String s = br.readLine();
            ret=s;
            File f = new File(s);
            if(!f.isDirectory())
                new FileReader(s);
        }
        catch(Exception e)
        {
            if(e instanceof FileNotFoundException || e instanceof NullPointerException)
            {
                try
                {
                    FileWriter fw = new FileWriter ("c:\\sft\\log.bin");
                    ret = System.getProperty("user.home")+"\\"+"Desktop"+"\\";
                    fw.write(ret);
                    fw.close();
                }
                catch(Exception ee)
                {
          JOptionPane.showMessageDialog(null,"Critical Error, The application will now exit","Critical Error",0);
                }
            }
        }
        return ret;
    }
    public void actionPerformed(ActionEvent ae)
    {
        String com=ae.getActionCommand();

        if (com.equals("Recieve File"))
        {
            recieve.setEnabled(false);
            int add[] = new int[4];
            int counter=0;
            StringTokenizer st = new StringTokenizer(address.getText(),".");
            while(st.hasMoreTokens())
            {
                try
                {
                    if(counter>=4)
                        new Integer("g");
                    add[counter++]=new Integer(st.nextToken());
                }
                catch(NumberFormatException nfe)
                {
                    address.setText("");
                    recieve.setEnabled(true);
                    JOptionPane.showMessageDialog(null,"IP address has only no.s seperated by dots (.)nIt is of the form \"xxx.xxx.xxx.xxx\" nwhere xxx is a no. less than 256","Integer Data Expected",0);
                    return;
                }
            }
            if(  !(counter == 4) || add[0] > 255 || add[1] > 255 || add[2] > 255 || add[3] > 255 )
            {
                address.setText("");
                recieve.setEnabled(true);
                JOptionPane.showMessageDialog(null,"Incorrect IP Address nIt is of the form \"xxx.xxx.xxx.xxx\" nwhere xxx is a no. less than 256","Invalid IP",0);
                return;
            }
            new Thread(new Runnable()
            {
                public void run()
                {
                    recieveFile();
                }
            }).start();
        }
    }
    private void recieveFile()
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                status.setText("Searching for server...Please wait");
                address.setEnabled(false);
            }
        });
        byte b[] = new byte[100000];
        // array to retrieve data from server and send to client

        String sizeName[] = new String[2];
        // stores size and name of file as recieved from character streams

        double done=0,length;
        // done is used to count the percentage

        int read=0,i=0;
        // read counts the bytes read (within 4 bytes integer range) in WHILE loop

        // constructing streams
        BufferedReader br=null;
        // to read String and long data via Socket

        PrintWriter pw=null;
        // to write String and long data via Socket

        BufferedInputStream bis=null;
        //to write file contents (byte stream) via Socket

        BufferedOutputStream bos=null;
        //to read byte data via Socket

        FileOutputStream fos=null;
        // to read actual file using byte stream

        Socket s=null;
        // this will serve a local port for a client

        // now allocating memory to objects and starting main logic

        try
        {
            s = new Socket(address.getText(),4000);
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            pw = new PrintWriter(s.getOutputStream());
            StringTokenizer st = new StringTokenizer(br.readLine(),"/");
            status.setText("Connection Established, about to begin download");
            while(st.hasMoreTokens())
                System.out.println(sizeName[i++]=st.nextToken() + " ");

            pw.println("Recieved");
		
            pw.flush();
            length = new Double(sizeName[1]);
            bis = new BufferedInputStream  (s.getInputStream());
            bos = new BufferedOutputStream (s.getOutputStream());
            fos = new FileOutputStream     (location+"\\"+sizeName[0]);

            lowerBorder.remove(status);
            remove(lowerBorder);
            add(jpb,BorderLayout.SOUTH);
            repaint();
            revalidate();
            while(true)
            {
                done+=read;
                if(done>=length)
                    break;
                read=bis.read(b);
                ClientSwingWorker csw = new ClientSwingWorker (done,length,read,b,fos,jpb);
                csw.execute();
                while(!(csw.isDone())) {}
            }
            fos.flush();

            address.setEnabled(true);
            recieve.setEnabled(true);
            remove(jpb);
            lowerBorder.add(status);
            add(lowerBorder,BorderLayout.SOUTH);
            status.setText("Recieved 100%");
            repaint();
            revalidate();

            double time        = new Double(br.readLine());
            String speedString = br.readLine();

            bis.close();
            bos.close();
            fos.close();
            pw.close();
            br.close();
            s.close();

            JOptionPane.showMessageDialog(null,"Time taken is "+time+" Speed is "+speedString+" MBPS","File Sent (Client)",3);
            status.setText("Enter Server Adress and press 'Recieve File' to Continue");
        }
        catch(Exception e)
        {
            if ( e instanceof ConnectException)
            {
                
                status.setText ("Enter Server Adress and press 'Connect and Start' button to Continue");
                address.setEnabled(true);
                recieve.setEnabled(true);
                JOptionPane.showMessageDialog(null,"No Running Server found on specified address [ "+address.getText()+" ]","Server Not Found",0);
                final JTextField temp=address;
                address.addMouseListener(new MouseAdapter()
                {
                    public void mouseClicked(MouseEvent me)
                    {
                        temp.setText("");
                        temp.removeMouseListener(this);
                    }
                });
                return;
            }
            else if (e instanceof FileNotFoundException)
            {
                JOptionPane.showMessageDialog(null,"Failed in saving file,nLocation : "+location+" required administrative rights to save or it was an invalid pathnSelect some other location for downloaded filesnThe Program will now Exit and default location would be reset","Error ["+location+"]",0);
                try
                {
                    FileWriter fw = new FileWriter("c:\\sft\\log.bin");
                    fw.close();
                }
                catch(IOException ee)
                {
                }
                System.exit(1);
            }
            e.printStackTrace();
        }
    }
    public static void main(String...args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new Client();
            }
        });
    }
}
class ClientSwingWorker extends SwingWorker<Void, Void>
{
    JProgressBar jpb;
    final double done,size;
    byte b[] = new byte[100000];
    final int read;
    FileOutputStream fos;
    ClientSwingWorker(double done,double size,int read,byte b[],FileOutputStream fos,JProgressBar jpb)
    {
        this.done = done;
        this.size = size;
        this.read = read;
        this.b    = b;
        this.jpb  = jpb ;
        this.fos  = fos;
    }
    protected Void doInBackground() throws Exception
    {
        fos.write(b,0,read);
        return null;
    }
}