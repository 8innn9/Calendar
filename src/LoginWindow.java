import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.*;

class LoginWindow extends JFrame {
    private JTextField jtf1;
    private JTextField jtf2;
    private JButton jbt1;
    private JButton jbt2;
    private JLabel login = new JLabel("로그인 창");
    private JLabel Id = new JLabel("ID:");
    private JLabel Pw = new JLabel("PW:");
    static String userName;

    class userInfo{
        String name;
        String password;
        userInfo(String name, String password){
            this.name=name;
            this.password=password;
        }
    }

    public boolean LogIn(String id, String pw) throws IOException {
        File file = new File("member.txt");
        BufferedReader bf = new BufferedReader(new FileReader(file));
        StringTokenizer st;
        HashMap<String,userInfo> loginHash = new HashMap<>(); //key : Id, value : PW , userName

        String userId;
        String userPw;
        String name;
        String line;
        boolean result = false;

        while ((line = bf.readLine()) != null) {
            st = new StringTokenizer(line);

            name = st.nextToken();
            userId = st.nextToken();
            userPw = st.nextToken();
            loginHash.put(userId,new userInfo(name,userPw));

        }
        if(loginHash.containsKey(id)){
            if(loginHash.get(id).password.equals(pw)){
                result = true;
                userName = loginHash.get(id).name;
            }
        }
        return result;

    }


    LoginWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container ct = getContentPane();
        jtf1 = new JTextField("아이디를 입력해주세요.", 30);
        jtf2 = new JTextField("비밀번호를 입력해주세요.", 30);
        jtf1.setEditable(true);
        jtf2.setEditable(true);

        jbt1 = new JButton("로그인");
        jbt1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (LogIn(jtf1.getText(), jtf2.getText())) {
                        try{
                            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                            new Loading(userName);
                        }catch(Exception ex){
                        }
                        dispose();
                    } else {
                        new ErrorWindow();
                        dispose();
                    }
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });

        jbt2 = new JButton("회원가입");

        jbt2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterWindow();
                dispose();
            }
        });

        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        p1.add(Id);
        p1.add(jtf1);
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        p2.add(Pw);
        p2.add(jtf2);
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        p3.add(jbt1);
        p3.add(jbt2);

        ct.setLayout(new GridLayout(4, 1));
        login.setHorizontalAlignment(JLabel.CENTER); // JLabel 가운데 정렬
        ct.add(login);
        ct.add(p1);
        ct.add(p2);
        ct.add(p3);

        setTitle("로그인 창");
        setSize(400, 300);
        setVisible(true);
    }
}

class ErrorWindow extends JFrame {
    ErrorWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel info = new JLabel("아이디 또는 비밀번호가 잘못 입력 되었습니다.");
        JButton bt = new JButton("다시 시도");
        bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginWindow();
                dispose();
            }
        });
        Container ct = getContentPane();
        ct.setLayout(new FlowLayout());
        ct.add(info);
        ct.add(bt);

        setSize(300, 100);
        setTitle("로그인 실패");
        setVisible(true);

    }
}
