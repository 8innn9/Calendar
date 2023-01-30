import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ShowSchedule extends JFrame{
    private final JLabel title = new JLabel("제목:");
    private final JLabel date = new JLabel("날짜:");
    private final JLabel Year1 = new JLabel("년");
    private final JLabel Year2 = new JLabel("년");
    private final JLabel month1 = new JLabel("월");
    private final JLabel month2 = new JLabel("월");

    private final JLabel start = new JLabel("일 부터");
    private final JLabel end = new JLabel("일 까지");
    private final JLabel category = new JLabel("카테고리:");
    private final JLabel color = new JLabel("색상:");
    private final JLabel memo = new JLabel("메모:");
    private JTextField jtx; //제목
    private JTextArea jta;  //메모
    private JComboBox<String> combo10;
    private JComboBox<String> combo1; // ~ 부터
    private JComboBox<String> combo12;
    private JComboBox<String> combo20;
    private JComboBox<String> combo2; // ~ 까지
    private JComboBox<String> combo22;
    private JComboBox<String> combo3; // 카테고리
    Color barColor; // 캘린더에서 사용할 바 컬러

    String Year[] = new String[100];
    String Month[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    String Day[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18",
            "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
    String categ[] = {"알고리즘", "자바프로그래밍", "컴퓨터 네트워크", "인공지능"};
    ShowSchedule(sch Sch){
        for(int i=0;i<Year.length;i++){
            Year[i]=Integer.toString(i+2022);
        }

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container ct = getContentPane();
        jtx = new JTextField(30);
        jtx.setText(Sch.getTitle());
        jtx.setEditable(false);

        combo10 = new JComboBox<String>(Year);
        combo10.setSelectedItem(Sch.getYearStart());
        combo10.setEnabled(false);
        combo1 = new JComboBox<String>(Month);
        combo1.setSelectedItem(Sch.getMonthStart());
        combo1.setEnabled(false);
        combo12 = new JComboBox<String>(Day);
        combo12.setSelectedItem(Sch.getDateStart());
        combo12.setEnabled(false);

        combo20=new JComboBox<String>(Year);
        combo20.setSelectedItem(Sch.getYearEnd());
        combo20.setEnabled(false);
        combo2 = new JComboBox<String>(Month);
        combo2.setSelectedItem(Sch.getMonthEnd());
        combo2.setEnabled(false);
        combo22 = new JComboBox<String>(Day);
        combo22.setSelectedItem(Sch.getDateEnd());
        combo22.setEnabled(false);

        combo3 = new JComboBox<String>(categ);
        combo3.setSelectedItem(Sch.getCategory());
        combo3.setEnabled(false);

        JButton ch = new JButton("색상 선택하기");
        ch.setBackground(Sch.getBarcolor());
        ch.setEnabled(false);

        jta = new JTextArea(6, 35); //메모
        jta.setText(Sch.getMemo());
        jta.setEditable(false);

        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        p1.add(title);
        p1.add(jtx);

        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        p2.add(date);
        p2.add(combo10);
        p2.add(Year1);
        p2.add(combo1);
        p2.add(month1);
        p2.add(combo12);
        p2.add(start);
        JPanel p2_2 = new JPanel(new FlowLayout(FlowLayout.CENTER,10,5));
        p2_2.add(combo20);
        p2_2.add(Year2);
        p2_2.add(combo2);
        p2_2.add(month2);
        p2_2.add(combo22);
        p2_2.add(end);


        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        p3.add(category);
        p3.add(combo3);

        JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        p4.add(color);
        p4.add(ch);

        JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        p5.add(memo);
        p5.add(jta);

        ct.setLayout(new BoxLayout(ct, BoxLayout.Y_AXIS));
        ct.add(p1);
        ct.add(p2);
        ct.add(p2_2);
        ct.add(p3);
        ct.add(p4);
        ct.add(p5);

        setSize(400, 500);
        setTitle("일정관리하기");
        setVisible(true);
    }


}
