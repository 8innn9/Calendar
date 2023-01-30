import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Timer;

import javax.swing.*;

class CalendarWindow extends JFrame implements ActionListener, MouseListener {
    static String userName;
    Calendar today;
    Calendar cal;
    String[] StoSdays = { "일", "월", "화", "수", "목", "금", "토" };
    static int year, month, day;
    int hour, minute, second, firstDay, dayOfWeek, todays;
    boolean isNoted = false;
    JPanel Head, accountBook, ptimeandmanage, PrintDay;
    JButton btnBefore, btnAfter;
    JButton btnmanagement;// 일정관리 버튼
    JButton btnCal[] = new JButton[49];

    JLabel Ltitle;

    JTextField income, spending;// 가계부
    JTextField txtYear, txtMonth;
    Timer timer;
    JLabel lClock;// 현재시간 시계
    Font f;
    Container ct = null;
    static ArrayList<String> todoDate = new ArrayList<>(); // 사용자가 보고 있는 달에 todo가 입력되어 있는 날짜가 있는 날짜를 저장할 배열
    static ArrayList<sch> schDate = new ArrayList<>(); // 사용자가 보고 있는 달에 일정이 입력되어 있는 날짜를 저장할 배열

    public CalendarWindow(String name) {
        userName = name;
        today = Calendar.getInstance();
        cal = new GregorianCalendar();
        year = today.get(Calendar.YEAR);
        month = today.get(Calendar.MONTH) + 1;// 1월의 값이 0이다.
        Head = new JPanel();// 윗부분
        Head.setLayout(new GridLayout(1, 3));
        accountBook = new JPanel(new GridLayout(2, 1));

        JPanel incomePanel = new JPanel();
        JLabel incomeLabel = new JLabel("이달의 총수입");
        incomePanel.add(incomeLabel);
        incomePanel.add(income = new JTextField(7));
        JPanel spendingPanel = new JPanel();
        JLabel spendingLabel = new JLabel("이달의 총지출");
        spendingPanel.add(spendingLabel);
        spendingPanel.add(spending = new JTextField(7));// 가계부 연동시켜야 한다.
        accountBook.add(incomePanel);
        accountBook.add(spendingPanel);
        Head.add(accountBook);
        findTodo();
        findSch();

        JPanel yearAndMonth = new JPanel();
        yearAndMonth.add(btnBefore = new JButton("◀"));
        yearAndMonth.add(txtYear = new JTextField(year + "년"));
        yearAndMonth.add(txtMonth = new JTextField(month + "월", 3));
        txtYear.setEnabled(false);
        txtMonth.setEnabled(false);// 임의로 입력 못하게
        yearAndMonth.add(btnAfter = new JButton("▶"));
        f = new Font("Sherif", Font.BOLD, 20);
        txtYear.setFont(f);
        txtMonth.setFont(f);
        Head.add(yearAndMonth);

        lClock = new JLabel();
        ptimeandmanage = new JPanel(new GridLayout(2, 1));
        timer = new Timer();
        timer.schedule(new timeset(), 0, 1000);// 호출객제(내부클래스로작성), 초기지연시간,호출간격
        ptimeandmanage.add(lClock);

        btnmanagement = new JButton("일정관리버튼");
        ptimeandmanage.add(btnmanagement);
        Head.add(ptimeandmanage);
        add(Head, "North");
        btnmanagement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ScheduleManagement(userName);
                dispose();
            }
        });

        PrintDay = new JPanel(new GridLayout(7, 7)); // "일"파트
        f = new Font("Sherif", Font.BOLD, 15);
        gridInit();
        calSet();
        hideInit();
        add(PrintDay, "Center");

        btnBefore.addActionListener(this);
        btnAfter.addActionListener(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(userName + "님의 캘린더 화면");
        setSize(900, 550);
        setVisible(true);
    }

    public void calSet() {
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, (month - 1));// 아까 +1한거 다시 되돌림
        cal.set(Calendar.DATE, 1);
        firstDay = cal.getFirstDayOfWeek();
        dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);// 월요일(1)~일요일(7)
        int hopping = 0;
        btnCal[0].setForeground(new Color(255, 0, 0));// 일요일(red)
        btnCal[6].setForeground(new Color(0, 0, 255));// 토요일(blue)

        // 빈칸 세팅용이다.일요일~그 달의 시작 요일까지 빈칸으로 세팅한다.
        for (int i = firstDay; i < dayOfWeek; i++) {
            hopping++;
        }
        for (int i = 0; i < hopping; i++) {
            btnCal[i + 7].setText("");
        }

        // 날짜를 배치한다 일요일:Red. 토요일:Blue
        for (int i = cal.getMinimum(Calendar.DAY_OF_MONTH); i <= cal.getMaximum(Calendar.DAY_OF_MONTH); i++) {
            cal.set(Calendar.DATE, i);
            if (cal.get(Calendar.MONTH) != month - 1) {
                break;
            }
            todays = i;
            btnCal[i + 6 + hopping].setForeground(new Color(0, 0, 0));// 일단 투명으로 하고 나서 색 지정을 할 수 있음
            if ((i + hopping - 1) % 7 == 0) {// 일요일인 경우
                btnCal[i + 6 + hopping].setForeground(new Color(255, 0, 0));
            }
            if ((i + hopping) % 7 == 0) {// 토요일인 경우
                btnCal[i + 6 + hopping].setForeground(new Color(0, 0, 255));
            }
            if (isNoted == true) {
                for (int j = 0; j < todoDate.size(); j++) {
                    int date = Integer.parseInt(todoDate.get(j));
                    btnCal[6 + hopping + date].setForeground(new Color(255, 0, 255));
                }
            }

            /*
             * 요일을 찍은 다음부터 계산해야 하니 요일을 찍은 버튼의 갯수를 더하고 인덱스가 0부터 시작이니 -1을 해준 값으로 연산을 해주고 버튼의
             * 색깔을 변경해준다.
             */
            btnCal[i + 6 + hopping].setBorder(BorderFactory.createEmptyBorder(2, 4, 0, 2));
            btnCal[i + 6 + hopping].setLayout(new GridLayout(6, 1, 0, 1));// 일정 한 날에 6-2(공백제어)=4개씩 입력 가능
            btnCal[i + 6 + hopping].setText((i) + "");
            btnCal[i + 6 + hopping].setHorizontalAlignment(SwingConstants.LEFT);
            btnCal[i + 6 + hopping].setVerticalAlignment(SwingConstants.TOP);
            btnCal[i + 6 + hopping].add(new JLabel(""));// 칸 채우기
            btnCal[i + 6 + hopping].add(new JLabel(""));// 칸 채우기
            LsampleCheck(btnCal[i + 6 + hopping]);// 바 추가
        }

    }

    public void hideInit() {
        for (int i = 0; i < btnCal.length; i++) {
            if (btnCal[i].getText().equals("")) {
                btnCal[i].setEnabled(false);// 비어있으면 비활성화(클릭X)
            }
        }
    }

    public void gridInit() {
        for (int i = 0; i < StoSdays.length; i++)
            PrintDay.add(btnCal[i] = new JButton(StoSdays[i]));

        for (int i = StoSdays.length; i < 49; i++) {
            PrintDay.add(btnCal[i] = new JButton(""));
            btnCal[i].addActionListener(this);
        }
    }

    public void panelInit() {
        GridLayout Lgrid = new GridLayout(7, 7);
        PrintDay.setLayout(Lgrid);
    }

    public void calInput(int hop) {
        if (hop == -1 || hop == 1) {
            month += (hop);
            if (month <= 0) {
                month = 12;
                year = year - 1;
            } else if (month >= 13) {
                month = 1;
                year = year + 1;
            }
        } else if (hop == 12) {
            year++;
        } else if (hop == -12) {
            year--;
        }
    }
    public void actionPerformed(ActionEvent click) {
        if (click.getSource() == btnBefore) {// 과거로 go~
            this.PrintDay.removeAll();// 전부지워~
            calInput(-1); // 달을 하나 빼준다
            gridInit();
            panelInit();
            findTodo();
            findSch();
            calSet();
            hideInit();
            this.txtYear.setText(year + "년");
            this.txtMonth.setText(month + "월");
        } else if (click.getSource() == btnAfter) { // 다음 달로 가기위한 소스부
            this.PrintDay.removeAll();
            calInput(1); // 달을 하나 더해준다.
            gridInit();
            panelInit();
            findTodo();
            findSch();
            calSet();
            hideInit();
            this.txtYear.setText(year + "년");
            this.txtMonth.setText(month + "월");
        } else if (Integer.parseInt(click.getActionCommand()) >= 1
                && Integer.parseInt(click.getActionCommand()) <= 31) {
            day = Integer.parseInt(click.getActionCommand());
            // 버튼의 밸류 즉 1,2,3.... 문자를 정수형으로 변환하여 클릭한 날짜를 바꿔준다.
            // todolis가 작성되었음 = isNoted가 true로 바뀌는 코드 작성해야
            try {
                new ToDoList(userName);
                dispose();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            findTodo();
            findSch();
            calSet();
        }
        //else if()
    }

    static int getYear() {
        return year;
    }

    static int getMonth() {
        return month;
    }

    static int getDay() {
        return day;
    }

    // TODOList가 입력된 날짜를 저장할 메소드
    void findTodo() {
        isNoted = false;
        try {
            File file = new File(userName + "_todo.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            Charset.forName("UTF-8");

            ArrayList<Tdl> tmp = new ArrayList<>();
            Tdl data;
            String str = br.readLine();
            while ((str = br.readLine()) != null) {
                String[] line = str.split(",");
                // line[0] : yyyy-mm-dd , line[1] : boolean, line[2] : 수입, line[3] : 지출
                String[] yyyymmdd = line[0].split("-");
                data = new Tdl(yyyymmdd[0], yyyymmdd[1], yyyymmdd[2], line[1], line[2], line[3]);
                tmp.add(data);

            }
            todoDate.clear();
            int incomeM = 0, spendingM = 0;
            String todoYear;
            String todoMonth;
            String todoischecked;
            for (int i = 0; i < tmp.size(); i++) {
                todoYear = tmp.get(i).getYear();
                todoMonth = tmp.get(i).getMonth();
                todoischecked = tmp.get(i).getIschecked();
                if (todoYear.equals(Integer.toString(getYear())) && todoMonth.equals(Integer.toString(getMonth()))) {
                    if (todoischecked.equals("O")) {
                        todoDate.add(tmp.get(i).getDay());
                    }
                    incomeM += Integer.parseInt(tmp.get(i).getIncome());
                    spendingM += Integer.parseInt(tmp.get(i).getOutcome());
                }

            }
            income.setText(Integer.toString(incomeM)); // 수입 텍스트필드에 나타냄
            spending.setText(Integer.toString(spendingM)); // 지출 텍스트필드에 나타냄
            Collections.sort(todoDate);
            if (todoDate.isEmpty() == false)
                isNoted = true;
            br.close();

        } catch (IOException e) {
            income.setText("0");
            spending.setText("0");
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    class Tdl {
        String year;
        String month;
        String day;
        String ischecked;
        String income;
        String outcome;

        Tdl(String year, String month, String day, String ischecked, String income, String outcome) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.ischecked = ischecked;
            this.income = income;
            this.outcome = outcome;
        }

        String getYear() {
            return year;
        }

        String getMonth() {
            return month;
        }

        String getDay() {
            return day;
        }

        String getIschecked() {
            return ischecked;
        }

        String getIncome() {
            return income;
        }

        String getOutcome() {
            return outcome;
        }

    }

    // 일정이 입력된 날짜를 판단할 메소드
    void findSch() {
        try {
            File schcsv = new File(userName + "_schedule.csv");
            BufferedReader br = new BufferedReader(new FileReader(schcsv));
            Charset.forName("UTF-8");

            String str = br.readLine(); // 파일을 한 줄 씩 읽음 => 파일의 첫 줄은 제목이므로 쓸모 x
            ArrayList<sch> tmp = new ArrayList<>();
            sch data;
            while ((str = br.readLine()) != null) {
                String[] splitstr = str.split(",");
                String[] splitStartDay = splitstr[0].split("-");
                String[] splitEndDay = splitstr[1].split("-");
                data = new sch(splitStartDay[0], splitStartDay[1], splitStartDay[2], splitEndDay[0], splitEndDay[1],
                        splitEndDay[2], splitstr[2], splitstr[3], splitstr[4], splitstr[5]);

                tmp.add(data);
            }
            schDate.clear();
            String todoYearStart;
            String todoMonthStart;
            String todoYearEnd;
            String todoMonthEnd;
            for (int i = 0; i < tmp.size(); i++) {
                todoYearStart = tmp.get(i).getYearStart();
                todoMonthStart = tmp.get(i).getMonthStart();
                todoYearEnd = tmp.get(i).getYearEnd();
                todoMonthEnd = tmp.get(i).getMonthEnd();
                // 일정의 시작하는 달이 사용자의 화면과 같거나 , 일정의 끝나는 달이 사용자의 화면과 같다면 schDate에 저장
                if ((todoYearStart.equals(Integer.toString(getYear()))
                        && todoMonthStart.equals(Integer.toString(getMonth()))
                        || (todoYearEnd.equals(Integer.toString(getYear()))
                        && todoMonthEnd.equals(Integer.toString(getMonth()))))) {
                    schDate.add(tmp.get(i));
                }
            }
            // 시작 날짜를 기준으로 정렬
            Collections.sort(schDate, new Comparator<sch>() {
                @Override
                public int compare(sch c1, sch c2) {
                    return Integer.parseInt(c1.getDateStart()) - Integer.parseInt(c2.getDateStart());
                }
            });

            br.close();
        } catch (IOException e) {
        }
    }

    public void LsampleCheck(JButton btnCal) {

        for (int j = 0; j < schDate.size(); j++) {// 모든 schDate 확인
            sch checksch = schDate.get(j);

            if (checksch.getMonthStart().equals(checksch.getMonthEnd())) { // 시작달==마감달인 경우
                if ((Integer.parseInt(btnCal.getText()) >= Integer.parseInt(checksch.getDateStart()))
                        && (Integer.parseInt(btnCal.getText()) <= Integer.parseInt(checksch.getDateEnd()))) { // 시작일~마감일
                    // 사이면
                    Ltitle = new JLabel(checksch.getTitle());
                    Ltitle.setOpaque(true);
                    Ltitle.setBackground(checksch.getBarcolor());
                    Ltitle.setPreferredSize(new Dimension(127, 15));
                    Ltitle.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            new ShowSchedule(checksch);
                        }
                    });
                    btnCal.add(Ltitle);
                }
            } else { // 시작달!=마감달인 경우
                // 시작달 부분
                if (cal.get(Calendar.MONTH) + 1 == Integer.parseInt(checksch.getMonthStart())) {// 지금 보는 달이 시작달이면
                    if ((Integer.parseInt(btnCal.getText()) >= Integer.parseInt(checksch.getDateStart()))
                            && (Integer.parseInt(btnCal.getText()) <= cal.getMaximum(Calendar.DAY_OF_MONTH))) {
                        Ltitle = new JLabel(checksch.getTitle());
                        Ltitle.setOpaque(true);
                        Ltitle.setBackground(checksch.getBarcolor());
                        Ltitle.setPreferredSize(new Dimension(127, 15));
                        Ltitle.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                new ShowSchedule(checksch);
                            }
                        });
                        btnCal.add(Ltitle);
                    }
                } else if (cal.get(Calendar.MONTH) + 1 == Integer.parseInt(checksch.getMonthEnd())) { // 지금 보는 달이 마감달이면
                    if ((Integer.parseInt(btnCal.getText()) >= cal.getMinimum(Calendar.DAY_OF_MONTH))
                            && (Integer.parseInt(btnCal.getText()) <= Integer.parseInt(checksch.getDateEnd()))) {
                        Ltitle = new JLabel(checksch.getTitle());
                        Ltitle.setOpaque(true);
                        Ltitle.setBackground(checksch.getBarcolor());
                        Ltitle.setPreferredSize(new Dimension(127, 15));
                        Ltitle.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                new ShowSchedule(checksch);
                            }
                        });
                        btnCal.add(Ltitle);
                    }
                }
            }
        }
    }

    class timeset extends TimerTask {
        @Override
        public void run() {
            Calendar cal = Calendar.getInstance();
            hour = cal.get(Calendar.HOUR_OF_DAY);
            minute = cal.get(Calendar.MINUTE);
            second = cal.get(Calendar.SECOND);
            lClock.setText(hour + "시 " + minute + "분 " + second + "초를 지나고 있습니다..");
            f = new Font("Sherif", Font.BOLD, 18);
            lClock.setFont(f);
        }
    }
}
class sch {
    String yearStart;
    String yearEnd;
    String monthStart;
    String monthEnd;
    String dateStart;
    String dateEnd;
    String title;
    String category;
    Color barcolor;
    String memo;

    sch(String yearStart, String monthStart, String dateStart, String yearEnd, String monthEnd, String dateEnd,
        String title, String category, String barcolor,String memo) {
        this.yearStart = yearStart;
        this.monthStart = monthStart;
        this.dateStart = dateStart;
        this.yearEnd = yearEnd;
        this.monthEnd = monthEnd;
        this.dateEnd = dateEnd;
        this.title = title;
        this.category = category;
        this.barcolor = Color.decode(barcolor);
        this.memo = memo;
    }

    String getYearStart() {
        return yearStart;
    }

    String getMonthStart() {
        return monthStart;
    }

    String getDateStart() {
        return dateStart;
    }

    String getYearEnd() {
        return yearEnd;
    }

    String getMonthEnd() {
        return monthEnd;
    }

    String getDateEnd() {
        return dateEnd;
    }

    String getTitle() {
        return title;
    }

    String getCategory() {
        return category;
    }


    String getMemo() {
        return memo;
    }

    Color getBarcolor() {
        return barcolor;
    }

}