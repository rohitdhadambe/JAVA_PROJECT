import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.*;

class Task extends JPanel {

    JLabel index;
    JTextField taskName;
    JButton done;

    Color bgColor = new Color(36, 37, 42);
    Color textColor = new Color(230, 230, 230);
    Color doneBg = new Color(100, 200, 100); // Less vibrant light green
    boolean checked = true;

    Task() {
        this.setPreferredSize(new Dimension(350, 40));
        this.setBackground(bgColor);
        this.setLayout(new BorderLayout(10, 0));
        this.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, new Color(55, 55, 65)),
            new EmptyBorder(5, 10, 5, 10)
        ));

        index = new JLabel("");
        index.setPreferredSize(new Dimension(30, 30));
        index.setHorizontalAlignment(JLabel.CENTER);
        index.setForeground(textColor);
        index.setFont(new Font("Ubuntu", Font.BOLD, 14));
        this.add(index, BorderLayout.WEST);

        taskName = new JTextField("Write something...");
        taskName.setForeground(Color.GRAY);
        taskName.setFont(new Font("Ubuntu", Font.PLAIN, 14));
        taskName.setBorder(null);
        taskName.setBackground(bgColor);
        taskName.setCaretColor(Color.ORANGE);

        taskName.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (taskName.getText().equals("Write something...")) {
                    taskName.setText("");
                    taskName.setForeground(textColor);
                }
            }

            public void focusLost(FocusEvent e) {
                if (taskName.getText().trim().isEmpty()) {
                    taskName.setText("Write something...");
                    taskName.setForeground(Color.GRAY);
                }
            }
        });

        this.add(taskName, BorderLayout.CENTER);

        done = new JButton("✔");
        done.setPreferredSize(new Dimension(45, 30));
        done.setFocusPainted(true);
        done.setBackground(new Color(255, 87, 34));
        done.setForeground(Color.WHITE);
        done.setFont(new Font("Sans-serif", Font.BOLD, 16));
        done.setCursor(new Cursor(Cursor.HAND_CURSOR));
        done.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));

        this.add(done, BorderLayout.EAST);
    }

    public void changeIndex(int num) {
        this.index.setText(num + ".");
        this.revalidate();
    }

    public JButton getDone() {
        return done;
    }

    public boolean getState() {
        return checked;
    }

    public void changeState() {
        this.setBackground(doneBg);
        taskName.setBackground(doneBg);
        taskName.setForeground(Color.WHITE);
        checked = true;
        revalidate();
    }
}

class ListPanel extends JPanel {

    ListPanel() {
        GridLayout layout = new GridLayout(0, 1, 0, 8);
        this.setLayout(layout);
        this.setBackground(new Color(24, 26, 28));
        this.setBorder(new EmptyBorder(10, 20, 10, 20));
    }

    public void updateNumbers() {
        Component[] listItems = this.getComponents();
        for (int i = 0; i < listItems.length; i++) {
            if (listItems[i] instanceof Task) {
                ((Task) listItems[i]).changeIndex(i + 1);
            }
        }
    }

    public void removeCompletedTasks() {
        Component[] listItems = this.getComponents();
        for (Component c : listItems) {
            if (c instanceof Task && ((Task) c).getState()) {
                this.remove(c);
            }
        }
        updateNumbers();
        revalidate();
        repaint();
    }
}

class TitleBar extends JPanel {

    JLabel dateLabel;

    TitleBar() {
        this.setPreferredSize(new Dimension(400, 60));
        this.setBackground(new Color(28, 29, 34));
        this.setLayout(new BorderLayout());

        JLabel title = new JLabel("📝 TO-DO LIST");
        title.setFont(new Font("Ubuntu", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(10, 20, 10, 10));

        LocalDate today = LocalDate.now();
        String formatted = today.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                           + ", " + today.getDayOfMonth() + " "
                           + today.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

        dateLabel = new JLabel("📅 " + formatted);
        dateLabel.setFont(new Font("Ubuntu", Font.PLAIN, 14));
        dateLabel.setForeground(Color.LIGHT_GRAY);
        dateLabel.setBorder(new EmptyBorder(10, 0, 10, 20));
        dateLabel.setHorizontalAlignment(JLabel.RIGHT);

        this.add(title, BorderLayout.WEST);
        this.add(dateLabel, BorderLayout.EAST);
    }
}

class Footer extends JPanel {

    JButton addTask;
    JButton clear;

    Footer() {
        this.setPreferredSize(new Dimension(400, 60));
        this.setBackground(new Color(28, 29, 34));
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        addTask = createStyledButton("➕ Add", new Color(255, 87, 34));
        clear = createStyledButton("🗑 Clear", new Color(51, 51, 51));

        this.add(addTask);
        this.add(clear);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Ubuntu", Font.BOLD, 14));
        btn.setFocusPainted(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return btn;
    }

    public JButton getNewTask() {
        return addTask;
    }

    public JButton getClear() {
        return clear;
    }
}

class AppFrame extends JFrame {

    TitleBar title;
    Footer footer;
    ListPanel list;
    JButton newTask;
    JButton clear;

    AppFrame() {
        this.setTitle("📝 Stylish Dark To-Do App");
        this.setSize(420, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.getContentPane().setBackground(new Color(24, 26, 28));

        title = new TitleBar();
        footer = new Footer();
        list = new ListPanel();

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(new Color(24, 26, 28));

        this.add(title, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(footer, BorderLayout.SOUTH);

        newTask = footer.getNewTask();
        clear = footer.getClear();

        addListeners();

        this.setVisible(true);
    }

    public void addListeners() {
        newTask.addActionListener(e -> {
            Task task = new Task();
            list.add(task);
            list.updateNumbers();
            revalidate();
        
            task.getDone().addActionListener(doneEvent -> {
                task.changeState();
                // Insert task into PostgreSQL
                DBManager.insertTask(task.taskName.getText());
            });
        });
        

        clear.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to clear all tasks from the list and database?",
                "Confirm Clear",
                JOptionPane.YES_NO_OPTION
            );
        
            if (result == JOptionPane.YES_OPTION) {
                DBManager.clearTasks(); // Clear all tasks from DB
                list.removeAll();       // Remove all tasks from UI
                list.revalidate();
                list.repaint();
            }
        });
        
    }
}

public class test {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppFrame::new);
    }
}