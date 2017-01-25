import javax.swing.*;

import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Created by Memo on 11/30/15.
 */
public class JLeftPanel extends JPanel
{
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    Business bizTier;

    String status;
    private JTextArea statusArea; // Text area to display assignments
    private JScrollPane scrollPanel; // Scroll bar for statusArea

    public JLeftPanel(Business b) throws SQLException
    {
        super();

        this.setBackground(Color.WHITE);

        this.setLayout(new GridLayout(1, 1));

        this.bizTier = b;
    }

    //The code I submitted has the two methods below removed since we never actually
    //call them. --Wes C.
    /*public void displayCourse(String c)
    {
        JLabel course;

        course = new JLabel(c);
        course.setFont(new Font("SansSerif", Font.ROMAN_BASELINE, 20));

        this.add(course);
        updatePanel();
    }

    public void displayUpcomingAssignments() throws SQLException
    {
        ArrayList<Assignment> list = new ArrayList<Assignment>();
        LocalDateTime date = LocalDateTime.now();
        date = date.plusWeeks(1);
        String text = date.format(formatter);
        list = bizTier.getUpcomingAssignments(text);

        for (Assignment x : list)
        {
            System.out.println(x.toString());
        }
    }*/

    public void displayAssignments()
    {
        setLayout(new GridLayout(1, 1));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        status = "";

        statusArea = new JTextArea();
        statusArea.setBackground(Color.WHITE);

        scrollPanel = new JScrollPane(statusArea);

        add(scrollPanel);
    }

    public void resetText()
    {
        statusArea.setFont(new Font("SansSerif", Font.BOLD, 14));
        statusArea.setText("");
        statusArea.append("         U P C O M I N G    A S S I G N M E N T S\n");

    }

    public void updateClassName(String className)
    {
        statusArea.append(className + "\n");
    }

    public void updateDisplay(ArrayList<Assignment> assignments)
    // POST: Updates the panel to display updated assignments.
    {
        for (Assignment x : assignments)
        {
            statusArea.append("      " + x.toString() + "\n\n");
        }
    }

    public void updatePanel()
    {
        this.updateUI();
        this.revalidate();
        this.repaint();
    }
}
