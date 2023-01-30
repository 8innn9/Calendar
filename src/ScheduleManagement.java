import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.Charset;

class ScheduleManagement extends JFrame {
    static String userName;
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
    private JButton jbt1;   //취소
    private JButton jbt2;   //저장
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

    //저장 버튼 클릭시 csv파일에 저장하는 메소드
    void writeCSV() {
        File csv = new File(userName+"_schedule.csv");
        BufferedWriter bw = null;
        String[] content = new String[6];
        try {
            if (!csv.exists()) {
                csv.createNewFile();
                bw = new BufferedWriter(new FileWriter(csv));
                bw.write("start,end,title,category,color,memo" + "\r\n");
                bw.flush();
                bw.close();
            }
            bw = new BufferedWriter(new FileWriter(csv, true));
            Charset.forName("UTF-8");
            content[0] = combo10.getSelectedItem()+"-"+combo1.getSelectedItem() + "-" + combo12.getSelectedItem();
            content[1] = combo20.getSelectedItem()+"-"+combo2.getSelectedItem() + "-" + combo22.getSelectedItem();
            content[2] = jtx.getText();
            content[3] = (String) combo3.getSelectedItem();
            content[4] = Integer.toString(barColor.getRGB());
            content[5] = jta.getText();

            for (int i = 0; i < content.length - 1; i++) {
                bw.write(content[i] + ",");
            }
            bw.write(content[content.length - 1] + "\r\n");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.flush();
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    ScheduleManagement(String Name) {
        for(int i=0;i<Year.length;i++){
            Year[i]=Integer.toString(i+2022);
        }
        userName = Name;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container ct = getContentPane();
        jtx = new JTextField(30);
        jtx.setEditable(true);

        combo10 = new JComboBox<String>(Year);
        combo1 = new JComboBox<String>(Month);
        combo12 = new JComboBox<String>(Day);

        combo20=new JComboBox<String>(Year);
        combo2 = new JComboBox<String>(Month);
        combo22 = new JComboBox<String>(Day);

        combo3 = new JComboBox<String>(categ);

        JButton ch = new JButton("색상 선택하기");
        //색상 선택하기 버튼 클릭 시 컬러 다이얼로그 화면 표시 후
        //선택한 컬러를 저장하고 버튼 색을 변화
        ch.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ColorChooserGUI chg = new ColorChooserGUI();
                barColor = chg.getSelectColor();
                ch.setBackground(barColor);
            }
        });

        jta = new JTextArea(6, 35); //메모
        jta.setEditable(true);

        jbt1 = new JButton("취소");
        jbt1. addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CalendarWindow(userName);
                dispose();
            }
        });

        jbt2 = new JButton("저장");
        //저장 버튼을 누르면 CSV 파일에 저장.
        jbt2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writeCSV();
                new CalendarWindow(userName);
                dispose();
            }
        });

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
//        p4.add(jcb);

        JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        p5.add(memo);
        p5.add(jta);

        JPanel p6 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        p6.add(jbt1);
        p6.add(jbt2);

        ct.setLayout(new BoxLayout(ct, BoxLayout.Y_AXIS));
        ct.add(p1);
        ct.add(p2);
        ct.add(p2_2);
        ct.add(p3);
        ct.add(p4);
        ct.add(p5);
        ct.add(p6);

        setSize(400, 500);
        setTitle("일정관리하기");
        setVisible(true);
    }


}

//컬러 다이얼로그
class ColorChooserGUI extends JFrame {

    private JColorChooser jch;
    private Color SelectColor;

    ColorChooserGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Color selectedColor = jch.showDialog(null, "Color", Color.YELLOW);
        if (selectedColor != null)
            SelectColor = selectedColor;
    }

    Color getSelectColor() {
        return SelectColor;
    }
}