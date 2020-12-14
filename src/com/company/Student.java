package com.company;
import gui.CFrame;
import utils.FileUtils;

import javax.management.StringValueExp;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Student {
    private CFrame frame;
    private JMenuItem increaseBudget;
    private JMenuItem meals;
    private JMenuItem classes;
    private JList<File> directoryList;
    private static final String MEALS_PATH = "./meals/";
    private static final String INFO_PATH = ".\\user pass\\";
    private static final String STUDENTS_PATH = "./students/";
    private static final String CLASSES_PATH = ".\\classes\\";
    private String username;
    private int money;
    JTabbedPane tabbedPane = new JTabbedPane();

    public Student(String user)
    {
        this.username = user;
        frame = new CFrame(user);
        frame.getMainPanel().addPanel("PROFILE",profilePanel());
        addFrameMenu();
        addToTab();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void addFrameMenu()
    {

        meals = frame.addToMenu("Set Meals");
        classes = frame.addToMenu("Classes");
        increaseBudget = frame.addToMenu("Increase budget");
    }

    private JScrollPane initDirectoryList() {
        File[] files = FileUtils.getFilesInDirectory(CLASSES_PATH);
        System.out.println(files.length);
        directoryList = new JList<>(files);
        directoryList = new JList<>();

        directoryList.setBackground(new Color(211, 211, 211));
        directoryList.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        directoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        directoryList.setVisibleRowCount(-1);
        directoryList.setMinimumSize(new Dimension(130, 100));
        directoryList.setMaximumSize(new Dimension(130, 100));
        directoryList.setFixedCellWidth(130);
        directoryList.setCellRenderer(new MyCellRenderer());
        directoryList.addMouseListener(new MyMouseAdapter());
        directoryList.setListData(files);

        JScrollPane panel = new JScrollPane(directoryList);
        return panel;
    }
    private class MyCellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object object, int index, boolean isSelected, boolean cellHasFocus) {
            if (object instanceof File) {
                File file = (File) object;
                setText(file.getName());
                setIcon(FileSystemView.getFileSystemView().getSystemIcon(file));
                if (isSelected) {
                    setBackground(list.getSelectionBackground());
                    setForeground(list.getSelectionForeground());
                } else {
                    setBackground(list.getBackground());
                    setForeground(list.getForeground());
                }
                setEnabled(list.isEnabled());
            }
            return this;
        }
    }
    private class MyMouseAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent eve) {
            // Double-click detected
            if (eve.getClickCount() == 2) {
                int index = directoryList.locationToIndex(eve.getPoint());
//                System.out.println("Item " + index + " is clicked...");
                File curr[] = FileUtils.getFilesInDirectory(CLASSES_PATH);
                File files[] = FileUtils.getFilesInDirectory(curr[index].toString());
                int cnt = 0;
                String content = "class\n";
                while (cnt < files.length)
                {
                    content += FileUtils.fileReader(files[cnt]) + "\n";
                    cnt ++;
                }
                openExistingNote(content, tabbedPane);
            }
        }
    }
    public void openExistingNote(String content, JTabbedPane tabbedPane) {
        JTextArea existPanel = createTextPanel();
        existPanel.setText(content);

//        int tabIndex = tabbedPane.getTabCount() + 1;
        tabbedPane.addTab(FileUtils.getProperFileName(content) , existPanel);
//        tabbedPane.setSelectedIndex(tabIndex - 1);
    }
    private JTextArea createTextPanel() {
        JTextArea textPanel = new JTextArea();
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return textPanel;
    }
    private void addToTab()
    {
        meals.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    frame.getMainPanel().addPanel("Meals",setMeals());
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
            }
        });
        increaseBudget.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getMainPanel().addPanel("Increase budget",changeBudget());
            }
        });

        classes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getMainPanel().addPanel("Choose class",viewClasses());
            }
        });

    }

    public JPanel setMeals() throws FileNotFoundException {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JCheckBox saturday = new JCheckBox("Saturday ");
        JCheckBox sunday = new JCheckBox("Sunday ");
        JCheckBox monday = new JCheckBox("Monday ");
        JCheckBox tuesday = new JCheckBox("Tuesday ");
        JCheckBox wednesday = new JCheckBox("Wednesday ");
        JCheckBox thursday = new JCheckBox("Thursday ");
        String m[] = {null};
        JComboBox saturdayBox = new JComboBox(m);
        JComboBox sundayBox = new JComboBox(m);
        JComboBox mondayBox = new JComboBox(m);
        JComboBox tuesdayBox = new JComboBox(m);
        JComboBox wednesdayBox = new JComboBox(m);
        JComboBox thursdayBox = new JComboBox(m);
        int flag1 = 0;

        JPanel mealsPanel =new JPanel(new GridLayout(6, 1, 5, 5));
        int days = 0;
        File curr[] = FileUtils.getFilesInDirectory(MEALS_PATH);
        while(days < curr.length)
        {
            String food1 = null;
            String food2 = null;
            int cnt = 0;
            Scanner scanner = new Scanner(curr[days]);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(cnt == 1)
                    food1 = line;
                else if(cnt == 2)
                    food1 += "     " + line;
                else if(cnt == 3)
                    food2 = line;
                else if(cnt == 4)
                    food2 += "     " + line;
                cnt++;
            }
            String m1[] = { food1, food2};
            if(curr[days].toString().contains("Saturday"))
            {
                saturdayBox = new JComboBox(m1);
                mealsPanel.add(saturdayBox,0);
                flag1++;
            }
            else if(curr[days].toString().contains("Sunday"))
            {
                sundayBox = new JComboBox(m1);
                mealsPanel.add(sundayBox,1);
            }
            else if(curr[days].toString().contains("Monday"))
            {
                mondayBox = new JComboBox(m1);
                mealsPanel.add(mondayBox,2,0);
            }
            else if(curr[days].toString().contains("Tuesday"))
            {
                tuesdayBox = new JComboBox(m1);
                mealsPanel.add(tuesdayBox,3);
            }
            else if(curr[days].toString().contains("Wednesday"))
            {
                wednesdayBox = new JComboBox(m1);
                mealsPanel.add(wednesdayBox,4);
            }
            else if(curr[days].toString().contains("Thursday"))
            {
                thursdayBox = new JComboBox(m1);
                mealsPanel.add(thursdayBox);
            }
            days++;

        }

        JPanel daysPanel = new JPanel(new GridLayout(6, 1, 5, 5));

        JPanel labels = new JPanel(new BorderLayout(5, 5));

        daysPanel.add(saturday);
        daysPanel.add(sunday);
        daysPanel.add(monday);
        daysPanel.add(tuesday);
        daysPanel.add(wednesday);
        daysPanel.add(thursday);
        labels.add(frame.getMainPanel().setLabel(" Set weekly meal plan ",Color.getHSBColor(122,13,14)),
                BorderLayout.CENTER);
        labels.add(frame.getMainPanel().setLabel("Budget: "+getBudget(), Color.red),BorderLayout.EAST);
        panel.add(labels, BorderLayout.NORTH);
        panel.add(daysPanel, BorderLayout.WEST);
        panel.add(mealsPanel, BorderLayout.CENTER);
        panel.add(frame.getMainPanel().setButtons(), BorderLayout.SOUTH);
        saveMeals(saturday, saturdayBox);
        saveMeals(sunday, sundayBox);
        saveMeals(monday, mondayBox);
        saveMeals(tuesday, tuesdayBox);
        saveMeals(wednesday, wednesdayBox);
        saveMeals(thursday, thursdayBox);
        return panel;
    }

    public JPanel changeBudget()
    {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
//        JLabel username = new JLabel(" username: ");
        JLabel id = new JLabel(" ID: ");
        JLabel pass = new JLabel(" password: ");
        JLabel amount = new JLabel(" amount: ");

//        JTextField usernameF = new JTextField();
        JTextField idF = new JTextField();
        JTextField amountF = new JTextField();
        JPasswordField passF = new JPasswordField();

        JPanel labelPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        JPanel fieldsPanel = new JPanel(new GridLayout(3, 1, 5, 5));

//        labelPanel.add(username);
//        fieldsPanel.add(usernameF);

        labelPanel.add(id);
        fieldsPanel.add(idF);

        labelPanel.add(pass);
        fieldsPanel.add(passF);

        labelPanel.add(amount);
        fieldsPanel.add(amountF);

        panel.add(frame.getMainPanel().setLabel(" Increase budget ",
                Color.green), BorderLayout.NORTH);
        panel.add(fieldsPanel, BorderLayout.CENTER);
        panel.add(labelPanel, BorderLayout.WEST);
        panel.add(frame.getMainPanel().setButtons(), BorderLayout.SOUTH);
        charge(amountF);
        return panel;
    }

    public JPanel profilePanel()
    {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JPanel info = new JPanel(new BorderLayout(5, 5));
        JPanel infoPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        JLabel name = new JLabel(" Username: ");
        JLabel pass = new JLabel(" Password: ");
        JLabel budget = new JLabel(" budget: ");
        JLabel average = new JLabel(" Average: ");
        Border border = BorderFactory.createLineBorder(Color.gray, 2);
        JTextField nameF = new JTextField(username);
        nameF.setBackground(Color.DARK_GRAY);
        nameF.setBorder(border);
        JTextField passF = new JTextField(getPass());
        passF.setBackground(Color.DARK_GRAY);
        passF.setBorder(border);
        JTextField budgetF = new JTextField(String.valueOf(getBudget()));
        budgetF.setBackground(Color.DARK_GRAY);
        budgetF.setBorder(border);
        JTextField averageF = new JTextField(String.valueOf(getAverage()));
        averageF.setBackground(Color.DARK_GRAY);
        averageF.setBorder(border);
        JTextField classesF = new JTextField();

        infoPanel.add(name);
        infoPanel.add(nameF);

        infoPanel.add(pass);
        infoPanel.add(passF);

        infoPanel.add(budget);
        infoPanel.add(budgetF);

        infoPanel.add(average);
        infoPanel.add(averageF);

        infoPanel.setBackground(Color.gray);

        nameF.setEnabled(false);
        passF.setEnabled(false);
        budgetF.setEnabled(false);
        averageF.setEnabled(false);
        classesF.setEnabled(false);

        info.add(infoPanel, BorderLayout.NORTH);
        info.add(classesF, BorderLayout.CENTER);
        panel.add(frame.getMainPanel().setLabel("Your profile",Color.getHSBColor(248, 56, 155)), BorderLayout.NORTH);
        panel.add(info, BorderLayout.CENTER);
        return panel;
    }

    public String getPath(String str)
    {
        String path = STUDENTS_PATH+username+"/"+str+".txt";
        return path;
    }
    public int getBudget()
    {
        int budget = 0;
        File budgetFile = new File(getPath("budget"));
        Scanner scanner = null;
        try {
            scanner = new Scanner(budgetFile);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        int cnt = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(cnt == 1)
                budget = Integer.parseInt(line);
            cnt++;
        }
        return  budget;
    }
    public String getPass()
    {
        String  pass = null;
        File file = new File(INFO_PATH+username+".txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        int cnt = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(cnt == 2)
                pass = line;
            cnt++;
        }
        return  pass;
    }
    public int getAverage()
    {
        int average = 0;
        File averageFile = new File(getPath("average"));
        Scanner scanner = null;
        try {
            scanner = new Scanner(averageFile);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        int cnt = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(cnt == 2)
                average = Integer.parseInt(line);
            cnt++;
        }
        return  average;
    }
    public void charge( JTextField amount)
    {
        frame.getMainPanel().getSubmit().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String amountNote = amount.getText();
                int budget = getBudget();
                budget += Integer.parseInt(amountNote);
                money = budget;
                String note = "budget\n" + budget;
                System.out.println(getPath("budget"));
//                System.out.println(note);
                if (!amountNote.isEmpty()){
                    boolean isSuccessful = new File(getPath("budget")).mkdirs();
                    System.out.println("Creating " + getPath("budget") + " directory is successful: " + isSuccessful);
                    FileUtils.fileWriter(note, STUDENTS_PATH+username+"/");
                    System.out.println(note);
                }
            }
        });
    }

    public JPanel viewClasses()
    {
        JPanel panel = new JPanel(new BorderLayout(1,2));
        panel.add(initDirectoryList(), BorderLayout.WEST);
        panel.add(tabbedPane, BorderLayout.CENTER);
        return panel;
    }
    public void saveMeals(JCheckBox day, JComboBox meals)
    {
        frame.getMainPanel().getSubmit().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String dayNote = day.getText();
                if(day.isSelected())
                {
                    String path = STUDENTS_PATH+username+"/meals/";
                    FileUtils.makeFolder(path);
                    String mealNote = meals.getSelectedItem().toString();
                    String note = dayNote + "\n" + mealNote;
                    FileUtils.fileWriter(note, path);
                    System.out.println(note);
                }
            }
        });
    }
//    public void chooseClass()
//    {
//        JPanel classPanel = new JPanel(new GridLayout(6, 1, 5, 5));
//        JCheckBox class1 = new JCheckBox(" class1: ");
//        JCheckBox class2 = new JCheckBox(" class2: ");
//
//    }
}
