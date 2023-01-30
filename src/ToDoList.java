import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.nio.charset.Charset;
import java.io.*;
import java.util.List;

public class ToDoList extends JFrame implements WindowListener {
    JTextField jtf, inT, outT;
    JCheckBox completeCb, modifyCb, deleteCb;
    JScrollPane jsp;
    JPanel list, money, edit, edit_modify, edit_delete, under, mainPanel, deletePanel, modifyPanel, panel1 = new JPanel();
    JButton inCheck, outCheck, deleteNo, deleteYes, modifyNo, modifyYes;
    ArrayList<JCheckBox> completeCbL = new ArrayList<>();
    ArrayList<JCheckBox> modifyCbL = new ArrayList<>();
    ArrayList<JCheckBox> deleteCbL = new ArrayList<>();
    ArrayList<JTextField> jtfL = new ArrayList<>();
    static String userName;
    String year = Integer.toString(CalendarWindow.getYear());
    String month = Integer.toString(CalendarWindow.getMonth());
    String day = Integer.toString(CalendarWindow.getDay());
    String date = year + "-" + month + "-" + day;

    public ToDoList(String name) throws IOException {
        userName = name;
        Container ct = getContentPane();
        ct.setLayout(new BorderLayout());

        JLabel title = new JLabel("TODO LIST");
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0)); // label 여백 설정
        title.setHorizontalAlignment(JLabel.CENTER); // label 기본: 왼쪽 정렬 -> 가운데 정렬하기 위해 설정
        ct.add(title, BorderLayout.NORTH);

        ct.add(panel1, BorderLayout.CENTER);

        int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
        int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER; // 수평 방향 스크롤바는 사용x
        jsp = new JScrollPane(panel1, v, h);
        ct.add(jsp, BorderLayout.CENTER);

        mainPanel = new JPanel();
        deletePanel = new JPanel();
        modifyPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        deletePanel.setLayout(new BoxLayout(deletePanel, BoxLayout.Y_AXIS));
        modifyPanel.setLayout(new BoxLayout(modifyPanel, BoxLayout.Y_AXIS));

        panel1.add(mainPanel);
        panel1.add(deletePanel);
        panel1.add(modifyPanel);

        deletePanel.setVisible(false);
        modifyPanel.setVisible(false);

        // <수입 관련 label, textXfield, 확인 버튼 만들고 panel(in)에 붙임>
        JLabel inL = new JLabel("수입");
        inT = new JTextField(7);
        inCheck = new JButton("확인");
        JPanel in = new JPanel();
        in.add(inL);
        in.add(inT);
        in.add(inCheck);

        // <지출 관련 label, textfield, 확인 버튼 만들고 panel(out)에 붙임>
        JLabel outL = new JLabel("지출");
        outT = new JTextField(7);
        outCheck = new JButton("확인");
        JPanel out = new JPanel();
        out.add(outL);
        out.add(outT);
        out.add(outCheck);

        // in, out 패널을 또 money 패널에 붙임
        money = new JPanel();
        money.setLayout(new GridLayout(2,1));
        money.add(in);
        money.add(out);

        inCheck.addActionListener(new Account());
        outCheck.addActionListener(new Account());

        moneyReturn(); // 저장된 수입, 지출 금액을 화면에 나타내도록 하는 메소드

        // 수정하기, 삭제하기 버튼 만들고 panel(edit)에 붙임
        JButton modify = new JButton("수정하기");
        JButton delete = new JButton("삭제하기");
        edit = new JPanel();
        edit.setLayout(new GridLayout(1, 2));
        edit.add(modify);
        edit.add(delete);

        modify.addActionListener(new Edit());
        delete.addActionListener(new Edit());

        // 수정 화면에서 나올 버튼들(취소/완료)을 만들고 panel(edit_modify)에 붙임
        modifyNo = new JButton("취소");
        modifyYes = new JButton("완료");
        edit_modify = new JPanel();
        edit_modify.setLayout(new GridLayout(1, 2));
        edit_modify.add(modifyNo);
        edit_modify.add(modifyYes);

        modifyNo.addActionListener(new Cancel());
        modifyYes.addActionListener(new Modify());

        // 삭제 화면에서 나올 버튼들(취소/삭제)을 만들고 panel(edit_delete)에 붙임
        deleteNo = new JButton("취소");
        deleteYes = new JButton("삭제");
        edit_delete = new JPanel();
        edit_delete.setLayout(new GridLayout(1, 2));
        edit_delete.add(deleteNo);
        edit_delete.add(deleteYes);

        deleteNo.addActionListener(new Cancel());
        deleteYes.addActionListener(new Delete());

        // 아래부분에 위치하는 panel(under)
        under = new JPanel();
        under.setLayout(new BoxLayout(under, BoxLayout.Y_AXIS));

        // under는 수정, 삭제 관련 버튼들과 수입, 지출에 관련된 panel(money)를 포함하는 패널
        // 화면에 따라 버튼 관련 패널이 바뀌며(edit, edit_modify, edit_delete) money 패널은 고정
        under.add(edit);
        edit.setVisible(true);
        under.add(edit_modify);
        edit_modify.setVisible(false);
        under.add(edit_delete);
        edit_delete.setVisible(false);
        under.add(money);
        ct.add(under, BorderLayout.SOUTH);

        panel_Main(); // 메인 화면을 구성하는 메소드

        addWindowListener(this);
        setTitle(userName+"의 TODO LIST");
        setSize(300,400);
        setVisible(true);
    }

    void tdlist(int n) { // 리스트(체크박스+텍스트필드)를 만들고 각각의 panel에 붙이는 메소드
        jtf = new JTextField(20);
        jtf.setEditable(true);

        if(n == 1) { // n = 1이면 완료 여부를 체크하는 체크박스
            completeCb = new JCheckBox();
            completeCbL.add(completeCb); // (완료 여부 체크박스)arrayList에 추가

            list = new JPanel();
            list.add(completeCb);
            list.add(jtf);

            mainPanel.add(list); // 메인패널에 붙이기

            jtf.requestFocus();
            completeCb.addItemListener(new Check());
            jtf.addKeyListener(new Input());
        } else if(n == 2) { // n = 2이면 삭제 여부를 체크하는 체크박스
            deleteCb = new JCheckBox();
            deleteCbL.add(deleteCb); // (삭제 여부 체크박스)arrayList에 추가

            list = new JPanel();
            list.add(deleteCb);
            list.add(jtf);

            deletePanel.add(list); // 삭제패널에 붙이기
        } else if(n == 3) { // n = 3이면 수정 여부를 체크하는 체크박스
            jtfL.add(jtf); // (텍스트핃드)arrayList에 추가

            modifyCb = new JCheckBox();
            modifyCbL.add(modifyCb); // (수정 여부 체크박스)arrayList에 추가

            list = new JPanel();
            list.add(modifyCb);
            list.add(jtf);

            modifyPanel.add(list); // 수정패널에 붙이기
        }
    }

    void panel_Main() throws IOException { // 메인 화면을 구성하는 메소드(메인패널 + 저장된 내용 불러오기(체크박스 상태, 입력한 내용) + 수정하기/삭제하기 버튼)
        try {
            File dateCsv = new File(userName + "_" + date + ".csv");
            BufferedReader br = new BufferedReader(new FileReader(dateCsv));
            Charset.forName("UTF-8"); // 파일에서 한글 깨지지 않도록 하기 위해
            String str = br.readLine();

            while ((str = br.readLine()) != null) {
                String[] data = str.split(",");
                String cCbState = data[0]; // 완료 여부 체크박스의 상태는 csv 파일의 첫 번째 행에 저장되어 있음
                String content = data[1]; // 내용은 csv 파일의 두 번째 행에 저장되어 있음
                tdlist(1); // 완료 여부를 체크하는 체크박스와 텍스트필드로 구성된 리스트 만들기
                boolean jcs = false;
                if(cCbState.equals("TRUE")) { // 파일에 저장된 체크박스의 상태가 true이면 jcs를 true로 변경
                    jcs = true;
                }
                completeCb.setSelected(jcs); // jcs가 true냐 false냐에 따라 체크 표시
                jtf.setText(content); // 파일에 저장되어 있는 내용을 텍스트필드에 출력
            }

            if(str == null) { // 파일에 저장된 내용을 다 불러왔으면 다시 내용을 입력할 수 있도록 새로운 리스트를 만듦
                tdlist(1);
            }
            br.close();
        } catch(FileNotFoundException e) { // 파일이 존재하지 않으면 저장된 내용이 없다는 뜻이므로, 투두를 입력할 수 있도록 리스트를 나타냄
            tdlist(1);
        } finally {
            edit_delete.setVisible(false);
            edit_modify.setVisible(false);
            edit.setVisible(true); // 수정하기/ 삭제하기 버튼을 나타냄
        }
    }

    void panel_Delete() throws IOException { // 삭제 화면을 구성하는 메소드(삭제 패널 + 저장된 내용 불러오기(입력한 내용) + 취소/삭제 버튼)
        try {
            File dateCsv = new File(userName + "_" + date + ".csv");
            BufferedReader br = new BufferedReader(new FileReader(dateCsv));
            Charset.forName("UTF-8"); // 파일에서 한글 깨지지 않도록 하기 위해
            String str = br.readLine();

            while ((str = br.readLine()) != null) {
                String[] data = str.split(",");
                String content = data[1];
                tdlist(2); // 삭제 여부 체크하는 체크박스와 텍스트필드로 구성된 리스트
                jtf.setText(content); // 파일에 저장되어 있는 내용을 텍스트필드에 출력
            }
            br.close();
        } catch(FileNotFoundException e) {
        } finally {
            edit.setVisible(false);
            edit_modify.setVisible(false);
            edit_delete.setVisible(true); // 취소/삭제 버튼을 나타냄
        }
    }

    void panel_Modify() throws IOException { // 수정 화면을 구성하는 메소드(수정 패널 + 저장된 내용 불러오기(입력한 내용) + 취소/완료 버튼)
        try {
            File dateCsv = new File(userName + "_" + date + ".csv");
            BufferedReader br = new BufferedReader(new FileReader(dateCsv));
            Charset.forName("UTF-8"); // 파일에서 한글 깨지지 않도록 하기 위해
            String str = br.readLine();

            while ((str = br.readLine()) != null) {
                String[] data = str.split(",");
                String content = data[1];
                tdlist(3); // 수정 여부 체크하는 체크박스와 텍스트필드로 구성된 메소드
                jtf.setText(content); // 파일에 저장되어 있는 내용을 텍스트 필드에 출력
            }
            br.close();
        } catch(FileNotFoundException e) {
        } finally {
            edit.setVisible(false);
            edit_delete.setVisible(false);
            edit_modify.setVisible(true); // 취소/완료 버튼을 나타냄
        }
    }

    void moneyReturn() { // 파일에 저장되어 있는 수입, 지출 금액을 화면에 나타내는 메소드
        try {
            File todoCsv = new File(userName + "_todo.csv");
            BufferedReader br = new BufferedReader(new FileReader(todoCsv));
            boolean existence = false; // 파일에 내가 찾는 날짜가 있는지 없는지 구분하기 위한 변수
            String str = "";

            while((str = br.readLine()) != null) { // 파일을 한 줄씩 읽음
                String[] data = str.split(",");
                if(data[0].equals(date)) { // 해당 날짜가 파일에 저장되어 있으면
                    existence = true;
                    inT.setText(data[2]); // 수입 텍스트필드에 저장된 값 출력
                    outT.setText(data[3]); // 지출 텍스트필드에 저장된 값 출력
                    break;
                }
            }

            if(existence == false) { // 파일에 내가 찾는 날짜가 저장되어 있지 않으면 입력한 내용이 없다는 뜻이므로 0원으로 나타냄
                inT.setText("0");
                outT.setText("0");
            }
            br.close();
        } catch (IOException e) {

        }
    }

    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {
        checkTodo();
        dispose();
        new CalendarWindow(userName);
    }

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}

    void checkTodo() { // 완료 여부 체크박스가 전부 체크되었으면 _todo.csv 파일의 입력 여부를 X로 변경하는 메소드
        try {
            File dateCsv = new File(userName + "_" + date + ".csv");
            File todoCsv = new File(userName + "_todo.csv");
            BufferedReader dtBr;
            BufferedReader tdBr = new BufferedReader(new FileReader(todoCsv));
            Charset.forName("UTF-8");

            if(dateCsv.exists()) {
                dtBr = new BufferedReader(new FileReader(dateCsv));
                Charset.forName("UTF-8");

                String str = "";
                int count = 0;
                while ((str = dtBr.readLine()) != null) { // _date(yyyy-mm-dd).csv 파일을 한 줄씩 읽음
                    String[] data = str.split(",");
                    if (data[0].equals("TRUE")) {
                        count++;
                    }
                }

                String change = "";
                if (count == (completeCbL.size()) - 1) { // 모든 리스트의 체크박스가 체크되었으면 _todo.csv의 입력 여부를 X로 바꿈
                    while ((str = tdBr.readLine()) != null) { // _todo.csv 파일을 한줄 씩 읽음
                        String[] data = str.split(",");
                        if (data[0].equals(date)) { // 해당 날짜의 입력 여부를 X로 바꿈
                            change += data[0] + ",X," + data[2] + "," + data[3] + "\r\n";
                        } else { // 다른 날짜들의 내용은 그대로
                            change += data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "\r\n";
                        }
                    }
                } else { // 체크되지 않은 체크박스가 하나라도 있으면 _todo.csv의 입력여부는 O로 표시
                    while ((str = tdBr.readLine()) != null) {
                        String[] data = str.split(",");
                        if (data[0].equals(date)) {
                            change += data[0] + ",O," + data[2] + "," + data[3] + "\r\n";
                        } else {
                            change += data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "\r\n";
                        }
                    }
                }
                BufferedWriter tdBw = new BufferedWriter(new FileWriter(todoCsv, false));
                Charset.forName("UTF-8");
                tdBw.write(change); // 파일에 덮어쓰기
                tdBw.flush();
                tdBw.close();
                dtBr.close();
                tdBr.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Input implements KeyListener { // 리스트에 내용을 입력했을 경우 파일에 저장하는 클래스

        @Override
        public void keyTyped(KeyEvent ke) {}

        @Override
        public void keyPressed(KeyEvent ke) {
            int txtLength= jtf.getText().length();
            if(txtLength >= 1 && ke.getKeyCode() == KeyEvent.VK_ENTER) { // 텍스트필드에 한 글자 이상 입력하고 엔터를 눌러야 파일에 저장되도록 함
                File dateCsv = new File(userName + "_" + date + ".csv");
                File todoCsv = new File(userName + "_todo.csv");
                BufferedWriter dtBw = null;
                boolean existence = false;
                try {
                    if(!dateCsv.exists()) { // date 파일이 존재하지 않으면
                        dateCsv.createNewFile(); // 파일을 새로 만듦
                        dtBw = new BufferedWriter(new FileWriter(dateCsv));
                        Charset.forName("UTF-8"); // 파일에서 한글 깨지지 않게 하기 위함
                        dtBw.write("완료여부,내용" + "\r\n");
                        dtBw.flush();
                        dtBw.close();
                    }

                    dtBw = new BufferedWriter(new FileWriter(dateCsv, true));
                    Charset.forName("UTF-8"); // 파일에서 한글 깨지지 않게 하기 위함
                    String inputContent = jtf.getText();
                    String data = "FALSE," + inputContent;
                    dtBw.write(data + "\r\n");
                    dtBw.flush();
                    dtBw.close();

                    BufferedReader tdBr = new BufferedReader(new FileReader(todoCsv));
                    Charset.forName("UTF-8"); // 파일에서 한글 깨지지 않게 하기 위함

                    String str = "", change = "";
                    while((str = tdBr.readLine())!= null) {
                        String[] s = str.split(",");
                        if(s[0].equals(date)) { // _todo.csv 파일에 해당 날짜가 입력되어 있으면 입력 여부 O로 저장
                            existence = true;
                            change += s[0] + ",O," + s[2] + "," + s[3] + "\r\n";
                        } else { // 나머지 날짜들은 원래 내용 그대로 다시 저장
                            change += s[0] + "," + s[1] + "," + s[2] + "," + s[3] + "\r\n";
                        }
                    }

                    BufferedWriter tdBw = new BufferedWriter(new FileWriter(todoCsv, false)); // 덮어쓰기
                    Charset.forName("UTF-8");
                    tdBw.write(change);
                    tdBw.flush();
                    tdBw.close();
                    tdBr.close();

                    if(existence == false) { // _todo.csv 파일에 날짜가 입력되어 있지 않은 경우
                        BufferedWriter tdBw2 = new BufferedWriter(new FileWriter(todoCsv, true)); // 이어쓰기
                        Charset.forName("UTF-8");
                        tdBw2.write(date + ",O,0,0\r\n"); // _todo.csv 파일에 해당 날짜 추가
                        tdBw2.flush();
                        tdBw2.close();
                    }
                } catch(IOException e) {
                    e.printStackTrace();
                } finally {
                    tdlist(1); // 또 입력할 수 있도록 새로운 리스트 만들기
                    jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum()); // 자동 스크롤
                    panel1.revalidate(); // 컴포넌트 재배치 (새로 만들어진 리스트가 화면에 잘 나타나도록 해줌)
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent ke) {}
    }

    class Check implements ItemListener { // 완료 여부 체크박스를 선택 또는 해제했을 경우 파일에 그 상태를 저장하는 클래스
        @Override
        public void itemStateChanged(ItemEvent ie) {
            if (ie.getStateChange() == ItemEvent.SELECTED) { // 체크박스 선택했을 경우
                for (int index = 0; index < completeCbL.size(); index++) {
                    if (ie.getItem() == completeCbL.get(index)) { // arrayList를 이용해 선택된 체크박스의 인덱스를 구함
                        try {
                            File dateCsv = new File(userName + "_" + date + ".csv");
                            BufferedReader br = new BufferedReader(new FileReader(dateCsv));
                            Charset.forName("UTF-8"); // 파일에서 한글 깨지지 않게 하기 위함

                            String change = "", str = "";
                            for (int i = 0; i < (index + 1); i++) { // index + 1인 이유: 파일에는 제목이 포함되어 있기 때문
                                str = br.readLine();
                                change += (str + "\r\n"); // 한 줄씩 읽고 change 변수에 그대로 저장
                            }

                            String check = br.readLine(); // 체크박스 선택한 부분
                            String[] checkData = check.split(",");
                            change += "TRUE," + checkData[1] + "\r\n"; // 체크박스 상태를 true로 바꿔서 저장

                            while ((str = br.readLine()) != null) {
                                change += (str + "\r\n"); // 마지막줄까지는 원래 내용 그대로 change 변수에 저장
                            }

                            BufferedWriter bw = new BufferedWriter(new FileWriter(dateCsv, false)); // 덮어쓰기
                            Charset.forName("UTF-8");

                            bw.write(change); // change 변수에 저장된 내용을 date.csv 파일에 쓰기
                            bw.flush();
                            bw.close();
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (ie.getStateChange() == ItemEvent.DESELECTED) { // 체크박스 선택 해제했을 경우
                for (int ind = 0; ind < completeCbL.size(); ind++) {
                    if (ie.getItem() == completeCbL.get(ind)) { //arrayList를 이용해 해제된 체크박스의 인덱스를 구함
                        try {
                            File dateCsv = new File(userName + "_" + date + ".csv");
                            BufferedReader br = new BufferedReader(new FileReader(dateCsv));
                            Charset.forName("UTF-8");
                            String change = "", str = "";

                            for (int i = 0; i < (ind + 1); i++) { // 체크박스 선택 해제한 부분 전의 내용은 모두 그대로 change 변수에 저장
                                str = br.readLine();
                                change += (str + "\r\n");
                            }

                            String check = br.readLine();
                            String[] checkData = check.split(",");
                            if (checkData[0].equals("TRUE")) { // 파일에 저장되어 있는 체크박스의 상태가 true인 경우 false로 바꿔서 change 변수에 저장
                                change += "FALSE," + checkData[1] + "\r\n";
                            } else {
                                change += checkData[0] + "," + checkData[1] + "\r\n";
                            }

                            while ((str = br.readLine()) != null) { // 마지막 줄까지의 내용은 전부 그대로 change 변수에 저장
                                change += (str + "\r\n");
                            }

                            BufferedWriter bw = new BufferedWriter(new FileWriter(dateCsv, false)); // 덮어쓰기
                            Charset.forName("UTF-8");

                            bw.write(change); // change 변수에 저장된 내용을 date.csv 파일에 쓰기
                            bw.flush();
                            bw.close();
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    class Edit implements ActionListener { // 메인 화면에서 삭제하기 or 수정하기 버튼을 눌렀을 때 삭제 화면 or 수정화면 나타냄

        @Override
        public void actionPerformed(ActionEvent ae) {
            if(completeCbL.size() != 1 && (ae.getActionCommand() == "삭제하기" || ae.getActionCommand() == "수정하기")) {
                try {
                    completeCbL.clear(); // 완료 여부 체크박스 arraylist 데이터 전부 삭제(리스트를 매번 새로 그리기 때문에 지워주지 않으면
                    // 다음에 그릴 때 체크박스가 누적되어 오류가 발생함)
                    mainPanel.setVisible(false);
                    if(ae.getActionCommand() == "삭제하기") {
                        deletePanel.removeAll(); // 파일에 업데이트된 내용을 반영하기 위해 매번 지우고 다시 그림
                        panel_Delete(); // 삭제 화면 (취소/삭제 버튼까지 포함)
                        deletePanel.setVisible(true);
                    } else {
                        modifyPanel.removeAll(); // 패널 지우기
                        panel_Modify();  // 수정 화면(취소/ 완료 버튼까지 포함)
                        modifyPanel.setVisible(true);
                    }
                } catch(IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    class Delete implements ActionListener { // 삭제 버튼을 눌렀을 경우 파일에서 그 내용을 삭제하는 클래스

        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<Integer> indexArrayList = new ArrayList<>(); // 선택한 삭제 체크박스 인덱스들을 저장하는 arrayList (한 번에 여러 개 삭제 할 수 있도록 하기 위함)
            boolean isSelected = false; //
            for(int i = 0; i < deleteCbL.size(); i++) {
                JCheckBox dCb = deleteCbL.get(i);
                if(dCb.isSelected()) {
                    indexArrayList.add(i); // 선택한 체크박스의 인덱스를 indexArrayList에 저장
                    Collections.sort(indexArrayList);
                    isSelected = true;
                }
            }
            if(isSelected == true && e.getActionCommand() == "삭제") { // 체크박스를 선택하고 삭제 버튼을 눌러야 삭제가 되도록 함
                try {
                    File dateCsv = new File(userName + "_" + date + ".csv");
                    BufferedReader br = new BufferedReader(new FileReader(dateCsv));
                    Charset.forName("UTF-8");

                    String change = "", str = "";
                    int start = 0;
                    for(int i = 0; i < indexArrayList.size(); i++ ) { // 인덱스의 개수만큼 반복
                        for(int j = start; j < (indexArrayList.get(i) + 1); j++) { // 파일에는 제목이 포함되어 있으므로 +1
                            str = br.readLine();
                            change += (str + "\r\n");
                        }
                        String deleteData = br.readLine(); // 삭제할 부분
                        start = indexArrayList.get(i) + 2;  // 제목 때문에 +1, 인덱스 다음 내용부터 읽어야 하므로 +1 => +2
                    }

                    while((str = br.readLine()) != null) {
                        change += (str + "\r\n");
                    }


                    BufferedWriter bw = new BufferedWriter(new FileWriter(dateCsv, false));
                    Charset.forName("UTF-8");
                    bw.write(change);
                    bw.flush();
                    bw.close();
                    br.close();

                    // 이 아래로는 내용을 다 삭제한 경우 _todo.csv 파일의 입력여부를 X로 변경하기 위한 코드
                    BufferedReader dtBr = new BufferedReader(new FileReader(dateCsv));
                    Charset.forName("UTF-8");
                    File todoCsv = new File(userName + "_todo.csv");
                    String change2 = "", str2 = "";

                    int count = 0;
                    while((str2 = dtBr.readLine()) != null) {
                        count++; // 한 줄 읽을 때마다 count 증가
                    }

                    if(count == 1) { // count가 1이라는 말은 파일에 제목밖에 남지 않았다는 말(즉, 파일은 존재하나 내용은 없다(내용을 다 삭제했다))
                        BufferedReader tdBr = new BufferedReader(new FileReader(todoCsv));
                        Charset.forName("UTF-8");
                        while((str2 = tdBr.readLine()) != null) {
                            String[] data = str2.split(",");
                            if(data[0].equals(date)) { // 해당되는 날짜를 찾으면 그 열의 입력여부를 X로 변경해서 change 변수에 저장
                                change2 += data[0] + ",X," + data[2] + "," + data[3] + "\r\n";
                            } else { // 나머지는 원래 내용 그대로 change 변수에 저장
                                change2 += data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "\r\n";
                            }
                        }

                        BufferedWriter tdBw = new BufferedWriter(new FileWriter(todoCsv, false)); // 덮어쓰기
                        Charset.forName("UTF-8");

                        tdBw.write(change2);
                        tdBw.flush();
                        tdBw.close();
                        tdBr.close();
                    }
                    dtBr.close();
                } catch(IOException e2) {
                    e2.printStackTrace();
                } finally {
                    try {
                        deleteCbL.clear(); // 삭제 여부 체크박스 arrayList 데이터 전부 삭제
                        deletePanel.setVisible(false);
                        mainPanel.removeAll();
                        panel_Main();
                        mainPanel.setVisible(true); // 메인 화면으로 변경
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    class Modify implements ActionListener { // 완료 버튼을 눌렀을 때 파일에 저장되어 있는 내용을 수정하기 위한 클래스

        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<Integer> indexArrayList = new ArrayList<>(); // 선택한 수정 체크박스 인덱스들을 저장하는 arrayList
            boolean isSelected = false; //
            for(int i = 0; i < modifyCbL.size(); i++) {
                JCheckBox mCb = (JCheckBox) modifyCbL.get(i);
                if(mCb.isSelected()) {
                    indexArrayList.add(i);
                    Collections.sort(indexArrayList);
                    isSelected = true; // 선택된 체크박스가 있으면 true로 바뀜
                }
            }
            if(isSelected == true && e.getActionCommand() == "완료") { // 체크박스 선택하고 완료 버튼을 눌러야 수정되도록 함
                try {
                    File dateCsv = new File(userName + "_" + date + ".csv");
                    BufferedReader br = new BufferedReader(new FileReader(dateCsv));
                    Charset.forName("UTF-8");

                    String change = "", str = "", content = "";
                    int start = 0;
                    for(int i = 0; i < indexArrayList.size(); i++ ) {
                        for(int j = start; j < (indexArrayList.get(i) + 1); j++) { // 파일에는 제목이 있으므로 +1
                            str = br.readLine();
                            change += (str + "\r\n");
                        }
                        for(int k = 0; k < jtfL.size(); k++) {
                            JTextField modification = jtfL.get(k);
                            if(k == indexArrayList.get(i)) { // 선택한 수정 체크박스 인덱스와 같은 인덱스 값을 갖는 텍스트필드에 입력된 내용 저장
                                content = modification.getText();
                            }
                        }
                        str = br.readLine();
                        String[] data = str.split(",");
                        change += data[0] + "," + content + "\r\n"; // 변경된 내용 저장
                        start = indexArrayList.get(i) + 2; // 제목 +1, 인덱스 다음 내용부터 읽어야 하므로 +1 => +2
                    }

                    while((str = br.readLine()) != null) {
                        change += (str + "\r\n");
                    }

                    BufferedWriter bw = new BufferedWriter(new FileWriter(dateCsv, false));
                    Charset.forName("UTF-8");
                    bw.write(change);
                    bw.flush();
                    bw.close();
                    br.close();
                } catch(IOException e2) {
                    e2.printStackTrace();
                } finally {
                    try {
                        modifyCbL.clear(); // 수정 여부 체크박스 arrayList 데이터 전부 삭제
                        jtfL.clear();
                        modifyPanel.setVisible(false);
                        mainPanel.removeAll();
                        panel_Main();
                        mainPanel.setVisible(true); // 메인화면으로 돌아옴
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    class Cancel implements ActionListener { // 삭제 화면 또는 수정 화면에서 취소버튼을 눌렀을 경우 메인 화면으로 돌아가기 위한 클래스

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == deleteNo || e.getSource() == modifyNo) {
                try {
                    if(e.getSource() == deleteNo) {
                        deleteCbL.clear(); // 삭제 여부 체크박스 arrayList 데이터 전부 삭제
                        deletePanel.setVisible(false);
                    } else {
                        modifyCbL.clear(); // 수정 여부 체크박스 arrayList 데이터 전부 삭제
                        modifyPanel.setVisible(false);
                    }
                    mainPanel.removeAll();
                    panel_Main();
                    mainPanel.setVisible(true); // 메인 화면으로 돌아옴
                } catch(IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    class Account implements ActionListener { // 수입, 지출 입력 시 파일에 저장하기 위한 클래스

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == inCheck || e.getSource() == outCheck) {
                String in, out, change = "", str = "";
                boolean existence = false;
                File todoCsv = new File(userName + "_todo.csv");
                try {
                    BufferedReader br = new BufferedReader(new FileReader(todoCsv));
                    while((str = br.readLine()) != null) {
                        String[] data = str.split(",");
                        if(data[0].equals(date)) { // 파일에서 날짜를 찾으면
                            existence = true;
                            if(e.getSource() == inCheck) { // 수입 입력 후 확인 버튼을 눌렀을 경우
                                in = inT.getText(); // 입력한 금액
                                out = data[3]; // 원래 저장되어 있던 금액
                            } else { // 지출 입력 후 확인 버튼을 눌렀을 경우
                                in = data[2]; // 원래 저장되어 있던 금액
                                out = outT.getText(); // 텍스트필드에 입력한 금액
                            }
                            change += data[0]+ "," + data[1] + "," + in + "," + out + "\r\n"; // change 변수에 저장
                        } else { // 찾는 날짜가 아닌 경우 원래 있던 내용 그대로 change 변수에 저장
                            change += data[0]+ "," + data[1] + "," + data[2] + "," + data[3] + "\r\n";
                        }
                    }
                    BufferedWriter bw = new BufferedWriter(new FileWriter(todoCsv, false)); // 덮어쓰기
                    Charset.forName("UTF-8");

                    bw.write(change);

                    if(existence == false) { // todo 파일에 날짜가 저장되어 있지 않은 경우(todo를 입력하지 않은 경우)
                        if(e.getSource() == inCheck) {
                            in = inT.getText();
                            out = "0";
                        } else {
                            in = "0";
                            out = outT.getText();
                        }
                        String add = date + ",X," + in + "," + out + "\r\n"; // todo 파일에 날짜와 입력한 금액 저장(todo 파일에 날짜가 저장되어 있지 않은 건 투두리스트를 입력하지 않은 경우이므로 입력 여부는 X)
                        bw.write(add);
                    }
                    bw.flush();
                    bw.close();
                    br.close();
                } catch(IOException e1) {

                }
            }
        }
    }
}
