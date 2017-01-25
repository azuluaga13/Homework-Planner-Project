// Programmers: Wes Clark, Guillermo Martinez, Ronnypetson Souza da Silva, Anthony Zuluaga
// Assignment:  Final Project
// Date:        December 3, 2015
// Description: The class for the applet's right panel.

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.Format;
import java.util.Date;
import java.util.Locale;

public class JRightPanel extends JPanel
{
    private final static Color COLOR = Color.DARK_GRAY;
    private final static String[] GRADES = { "A", "B", "C", "D", "F" };

    // JPanels
    private JPanel top; // Top Panel
    private JPanel center; // Center Panel
    private JPanel bottom; // Bottom Panel

    // Assignment Widget components
    private JLabel addAsignLabel;
    private JLabel courseLabel; // Course selection label
    private JLabel descriptionLabel; // Assignment description label
    private JLabel dueDateLabel; // Due Date label
    private JTextField descriptionBox; // Assignment description text area
    private JLabel markLabel;
    private JLabel assignmentLabel;
    private JComboBox gradeBox;

    // Course Widget components
    private JLabel addCourseLabel;
    private JLabel subject;
    private JLabel courseNumber;
    private JLabel courseName;
    private JLabel removeLabel;
    private JTextField subjectBox;
    private JTextField courseNumberBox;
    private JTextField courseNameBox;

    public JRightPanel(JButton assignment, JButton course, JComboBox courses, JButton submit,
                       JComboBox assignments, JButton markButton, JDateChooser calendar)
    {
        super();

        // Initialize top and bottom panels.
        top = new JPanel();
        center = new JPanel();
        bottom = new JPanel();

        setLayout(new BorderLayout());

        // Customize top and bottom panels.
        top.setLayout(new GridLayout(1, 2));
        center.setLayout(new GridLayout(13, 2));
        bottom.setLayout(new GridLayout(4, 1));

        // Initialize components.
        initAssignment();
        initCourse();

        // Add components to top and bottom panels.
        top.add(assignment);
        top.add(course);
        bottom.add(submit);

        assignmentWidget(courses, calendar, submit, assignments, markButton);

        // Add top and bottom panels to JRightPanel.
        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private void initAssignment()
    {
        addAsignLabel = new JLabel("Add an Assignment");
        addAsignLabel.setFont(new Font("SansSerif", 1, 20));
        addAsignLabel.setForeground(Color.DARK_GRAY);

        courseLabel = new JLabel("Select a Course:");
        courseLabel.setFont(new Font("SansSerif", Font.ROMAN_BASELINE, 15));
        courseLabel.setForeground(COLOR);

        descriptionLabel = new JLabel("Assignment Description:");
        descriptionLabel.setFont(new Font("SansSerif", Font.ROMAN_BASELINE, 15));
        descriptionLabel.setForeground(COLOR);

        dueDateLabel = new JLabel("Due Date:");
        dueDateLabel.setFont(new Font("SansSerif", Font.ROMAN_BASELINE, 15));
        dueDateLabel.setForeground(COLOR);

        markLabel = new JLabel("Update a Graded Assignment");
        markLabel.setFont(new Font("SansSerif", 1, 20));
        markLabel.setForeground(Color.DARK_GRAY);

        assignmentLabel = new JLabel("Select an Assignment:");
        assignmentLabel.setFont(new Font("SansSerif", Font.ROMAN_BASELINE, 15));
        assignmentLabel.setForeground(COLOR);

        gradeBox = new JComboBox(GRADES);

        descriptionBox = new JTextField();
    }

    private void initCourse()
    {
        addCourseLabel = new JLabel("Create New Course");
        addCourseLabel.setFont(new Font("SansSerif", 1, 20));
        addCourseLabel.setForeground(Color.DARK_GRAY);

        subject = new JLabel("Subject:");
        subject.setFont(new Font("SansSerif", Font.ROMAN_BASELINE, 15));
        subject.setForeground(COLOR);

        courseNumber = new JLabel("Course Number:");
        courseNumber.setFont(new Font("SansSerif", Font.ROMAN_BASELINE, 15));
        courseNumber.setForeground(COLOR);

        courseName = new JLabel("Course Name:");
        courseName.setFont(new Font("SansSerif", Font.ROMAN_BASELINE, 15));
        courseName.setForeground(COLOR);

        removeLabel = new JLabel("Remove Course");
        removeLabel.setFont(new Font("SansSerif", 1, 20));
        removeLabel.setForeground(Color.DARK_GRAY);

        subjectBox = new JTextField();
        courseNumberBox = new JTextField();
        courseNameBox = new JTextField();
    }

    public void assignmentWidget(JComboBox courses, JDateChooser calendar, JButton submitButton,
                                 JComboBox assignments, JButton markButton)
    {
        center.removeAll();
        center.updateUI();

        Format fullUSDate = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
        JFormattedTextField test = new JFormattedTextField(fullUSDate);
        test.setValue(new Date());

        center.add(addAsignLabel);
        center.add(courseLabel);
        center.add(courses);
        center.add(descriptionLabel);
        center.add(descriptionBox);
        center.add(dueDateLabel);
        center.add(calendar);
        center.add(submitButton);

        center.revalidate();

        bottom.removeAll();
        bottom.updateUI();

        bottom.add(markLabel);
        bottom.add(assignments);
        bottom.add(gradeBox);
        bottom.add(markButton);

        bottom.revalidate();

        top.setBackground(new Color(255, 204, 204));
        center.setBackground(new Color(255, 204, 204));
        bottom.setBackground(new Color(255, 204, 204));

        updatePanel();
    }

    public void courseWidget(JButton submitButton, JComboBox courses, JButton deleteButton)
    {
        bottom.removeAll();
        bottom.updateUI();

        center.removeAll();
        center.updateUI();

        center.add(addCourseLabel);
        center.add(subject);
        center.add(subjectBox);
        center.add(courseNumber);
        center.add(courseNumberBox);
        center.add(courseName);
        center.add(courseNameBox);
        center.add(submitButton, BorderLayout.SOUTH);

        center.revalidate();

        bottom.add(removeLabel);
        bottom.add(courses);
        bottom.add(deleteButton);

        bottom.revalidate();

        top.setBackground(new Color(255, 255, 153));
        center.setBackground(new Color(255, 255, 153));
        bottom.setBackground(new Color(255, 255, 153));

        updatePanel();
    }

    public void updatePanel()
    // POST: Updates the panel to display updated graphs.
    {
        this.updateUI();
        this.revalidate();
        this.repaint();
    }

    public String getDescriptionAW()
    // POST: FCTVAL == the text input by the user.
    {
        if (descriptionBox.getText().isEmpty())
            return null;

        return descriptionBox.getText();
    }

    public String getCourseNameCW()
    // POST: FCTVAL == the text input by the user.
    {
        if (courseNameBox.getText().isEmpty())
            return null;

        return courseNameBox.getText();
    }

    public String getCourseSubjectCW()
    // POST: FCTVAL == the text input by the user.
    {
        if (subjectBox.getText().isEmpty())
            return null;

        return subjectBox.getText();
    }

    public int getCourseNumCW()
    // POST: FCTVAL == the text (converted to an integer) input by the user.
    {
        if (courseNumberBox.getText().isEmpty())
            return -1;

        return Integer.parseInt(courseNumberBox.getText());
    }

    public String getLetterGrade()
    // POST: FCTVAL == the selection made from JComboBox gradeBox.
    {
        return String.valueOf(gradeBox.getSelectedItem());
    }

    public void clearCourseWidget()
    // POST: All input boxes in courseWidget are reset to blank.
    {
        courseNameBox.setText("");
        subjectBox.setText("");
        courseNameBox.setText("");
    }

    public void clearAssignmentWidget(JDateChooser calendar)
    // POST: All input boxes in assignmentWidget are reset to blank.
    {
        descriptionBox.setText("");
        calendar.setCalendar(null);
    }
}
