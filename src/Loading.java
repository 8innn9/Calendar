import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.SwingUtilities;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

class Loading extends JFrame implements ActionListener, Runnable {
    // 0~100을 세어주는 프로그래스바 생성
    private JProgressBar jpb = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
    private JLabel jl1 = new JLabel("Now Loading...");
    private JButton bt1 = new JButton("로딩 시작하기");
    static String userName;

    // 생성자 호출
    public Loading(String name) {
        userName = name;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container con = getContentPane();
        con.setLayout(new BorderLayout());
        JPanel jp1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 150));
        jp1.setBorder(new LineBorder(Color.blue, 5));
        jp1.setBackground(Color.white);
        Font font = new Font("Aharoni 굵게", Font.ITALIC, 30);
        jl1.setFont(font);
        jl1.setForeground(Color.blue);
        jp1.add(jl1);
        jp1.add(jpb);

        // JProgressBar 셋팅
        jpb.setStringPainted(true);
        jpb.setString("0%");
        // 화면에 올리기
        JPanel jp2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        jp2.add(bt1);
        bt1.addActionListener(this);
        con.add(jp1, BorderLayout.CENTER);
        con.add(jp2, BorderLayout.SOUTH);
        setTitle(userName + "님 로딩중입니다.");
        setSize(350, 500);
        setVisible(true);
    }

    private static int ii;

    // 스레드 오버라이드 메소드
    @Override
    public void run() {
        if (ii == 100) {
            ii = 0;
        }
        for (int i = ii; i <= 100; i++) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ee) {
            }
            jpb.setValue(i);
            ii = i;
            jpb.setString(i + "%");
            if (i == 100) {
                new CalendarWindow(userName);
                dispose();
            }

        }
    }

    // ActionListener 오버라이드 메소드
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bt1) { // "시작" 버튼 클릭
            new Thread(this).start();
        }
    }
}
