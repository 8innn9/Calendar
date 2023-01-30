import java.awt.Button;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class RegisterWindow extends JFrame {
    private JTextField jtf1;
    private JTextField jtf2;
    private JTextField jtf3;
    private JButton jbt1;
    private JButton jbt2;
    private JLabel register = new JLabel("회원가입 창");
    private JLabel name = new JLabel("이름:");
    private JLabel Id = new JLabel("ID:");
    private JLabel Pw = new JLabel("PW:");

    public static void Register(String name, String id, String pw) {
        try {
            File file = new File("member.txt");
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bf = new BufferedWriter(fw);
            bf.write(name + '\t' + id + '\t' + pw + '\n');
            bf.close();

            String schPath = name + "_schedule.csv";
            String todoPath = name + "_todo.csv";
            File schFile = new File(schPath);
            if (!schFile.exists()) {
                schFile.createNewFile();
                bf=new BufferedWriter(new FileWriter(schPath));
                bf.write("start,end,title,category,color,memo" + "\r\n");
                bf.flush();
                bf.close();
            }
            File todoFile = new File(todoPath);
            if (!todoFile.exists()) {
                todoFile.createNewFile();
                bf=new BufferedWriter(new FileWriter(todoFile));
                bf.write("날짜,입력여부,수입,지출"+"\r\n");
                bf.flush();
                bf.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    RegisterWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container ct = getContentPane();
        jtf1 = new JTextField("이름을 입력해주세요.", 30);
        jtf2 = new JTextField("아이디를 입력해주세요.", 30);
        jtf3 = new JTextField("비밀번호를 입력해주세요.", 30);
        jtf1.setEditable(true);
        jtf2.setEditable(true);

        jbt1 = new JButton("취소");

        jbt1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginWindow();
                dispose();
            }
        });

        jbt2 = new JButton("회원가입");
        jbt2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Register(jtf1.getText(), jtf2.getText(), jtf3.getText());
                new LoginWindow();
                dispose();
            }
        });

        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        p1.add(name);
        p1.add(jtf1);
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        p2.add(Id);
        p2.add(jtf2);
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        p3.add(Pw);
        p3.add(jtf3);
        JPanel p4 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        p4.add(jbt1);
        p4.add(jbt2);

        ct.setLayout(new GridLayout(5, 1));
        register.setHorizontalAlignment(JLabel.CENTER); // JLabel 가운데 정렬
        ct.add(register);
        ct.add(p1);
        ct.add(p2);
        ct.add(p3);
        ct.add(p4);

        setTitle("회원가입 창");
        setSize(400, 300);
        setVisible(true);
    }

}
