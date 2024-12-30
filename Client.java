import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Client implements ActionListener{
    JTextField text;
    static JPanel textarea;
    static Box vertical = Box.createVerticalBox();
    static JFrame f1;
    static DataOutputStream dout;
    Client(){
        f1 = new JFrame("Basic Chat Application");
        f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f1.setLayout(null);

        JPanel p1 = new JPanel();
        p1.setBackground(new Color(7,94,84));
        p1.setBounds(0,0,450,70);
        p1.setLayout(null);
        f1.add(p1);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image img1 = i1.getImage().getScaledInstance(25,25,Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(img1);
        JLabel back = new JLabel(i2);
        back.setBounds(5,20,25,25);
        p1.add(back);
        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        ImageIcon i3 = new ImageIcon(ClassLoader.getSystemResource("icons/2.png"));
        Image img2 = i3.getImage().getScaledInstance(60,60,Image.SCALE_DEFAULT);
        ImageIcon i4 = new ImageIcon(img2);
        JLabel profile = new JLabel(i4);
        profile.setBounds(40,8,50,50);
        p1.add(profile);

        ImageIcon i5 = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
        Image img3 = i5.getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(img3);
        JLabel video = new JLabel(i6);
        video.setBounds(300,20,30,30);
        p1.add(video);

        ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
        Image img4 = i7.getImage().getScaledInstance(35,30,Image.SCALE_DEFAULT);
        ImageIcon i8 = new ImageIcon(img4);
        JLabel call = new JLabel(i8);
        call.setBounds(360,20,30,30);
        p1.add(call);

        ImageIcon i9 = new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png"));
        Image img5 = i9.getImage().getScaledInstance(10,25,Image.SCALE_DEFAULT);
        ImageIcon i10 = new ImageIcon(img5);
        JLabel more = new JLabel(i10);
        more.setBounds(420,20,10,25);
        p1.add(more);

        JLabel name = new JLabel("John");
        name.setBounds(110,15,100,18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font(Font.SANS_SERIF,Font.BOLD,18 ));
        p1.add(name);

        JLabel status = new JLabel("Active Now");
        status.setBounds(110,35,100,18);
        status.setForeground(Color.WHITE);
        status.setFont(new Font(Font.SANS_SERIF,Font.BOLD,10));
        p1.add(status);

        textarea = new JPanel();
        textarea.setBounds(3,70,444,530);
        f1.add(textarea);

        text = new JTextField();
        text.setBounds(0,590,370,80);
        text.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,16));
        f1.add(text);
        try {
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
        } catch (Exception e) {
            e.printStackTrace();
        }
        JButton send = new JButton("Send");
        send.setBounds(350,590,100,80);
        send.setBackground(new Color(7,94,84));
//        send.setOpaque(true);
//        send.setBorderPainted(false);
        send.setForeground(Color.WHITE);
        f1.add(send);

        send.addActionListener(this);

        f1.setSize(450,650);
        f1.setLocation(800,100);
        f1.setUndecorated(true);
        f1.getContentPane().setBackground(Color.LIGHT_GRAY);


        f1.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        try{
            String out = text.getText();

//        JLabel output = new JLabel(out);
            JPanel p2 = formatLabel(out);
//        p2.add(output);

            textarea.setLayout(new BorderLayout());

            JPanel right = new JPanel(new BorderLayout());
            right.add(p2, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));

            textarea.add(vertical,BorderLayout.PAGE_START);

            dout.writeUTF(out);

            text.setText("");
            f1.repaint();
            f1.invalidate();
            f1.validate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    public static JPanel formatLabel(String out){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


        JLabel output = new JLabel("<html><p style=\"width: 150px\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(new Color(37, 211, 102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15, 15, 15, 50));
        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));

        panel.add(time);

        return panel;
    }
    public static void main(String[] args) {

        new Client();
        try{
            Socket s = new Socket("127.0.0.1",6001) ;
            DataInputStream din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());
            while(true){
                textarea.setLayout(new BorderLayout());
                String msg = din.readUTF();
                JPanel panel = formatLabel(msg);

                JPanel left = new JPanel(new BorderLayout());
                left.add(panel,BorderLayout.LINE_START);
                vertical.add(left);

                vertical.add(Box.createVerticalStrut(15));
                textarea.add(vertical,BorderLayout.PAGE_START);

                f1.validate();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
