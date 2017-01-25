import com.toedter.calendar.JDateChooser;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GYSTApplet extends JApplet implements ActionListener, ItemListener
{
    // GYST Applet components
    private JLabel appletName; // Applet label title
    private JLabel subName; // Applet subname label

    // Applet GUI panels
    private JLeftPanel visualArea; // Visualization panel
    private JRightPanel inputArea; // User input panel

    // JRightPanel components
    private JButton addAssignment; // Add an assignment
    private JButton addCourse; // Add a course
    private JButton submitButton; // Submit button to add assignment/course
    private JComboBox courses; // Courses drop-down menu
    private JDateChooser calendar; // Calendar to select due date
    private JComboBox assignments;
    private JButton markButton;
    private JButton deleteButton;

    // Database
    protected Business bizTier;
    private ArrayList<String> classNames;
    private ArrayList<Course> curCourses;
    private ArrayList<String> assignNames;
    private ArrayList<Assignment> curAssignments;
    // private ArrayList<Assignment>
    private int currCourseID;

    @Override
    public void init()
    {

        try
        {
            bizTier = new Business("jdbc:derby:C:/Users/Anthony/Desktop/Project04/out/production/Project04/GYSTDB");
            /*bizTier = new Business(
                    "jdbc:derby:C:/Users/Anthony/Desktop/Project04/out/production/Project04/GYSTDB");*/
        }
        catch (InstantiationException e)
        {
            // TODO Auto-generated catch block
            System.out.println("DB error, Instantiation Exception");
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            System.out.println("DB error, SQL Exception");
            e.printStackTrace();
        }

        Container contentPane; // GUI Content pane
        SpringLayout layout; // GUI Layout
        JSplitPane bottomPane; // GUI Bottom pane
        JPanel leftPanel; // GUI left panel
        JPanel rightPanel; // GUI right panel

        contentPane = getContentPane();
        layout = new SpringLayout();

        setLayout(layout);

        appletName = new JLabel("G Y S T ");
        appletName.setFont(new Font("SansSerif", 1, 30));
        appletName.setForeground(Color.DARK_GRAY);

        add(appletName);

        layout.putConstraint(SpringLayout.WEST, appletName, 5, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, appletName, 5, SpringLayout.NORTH, contentPane);

        subName = new JLabel("Student Homework Planner.");
        subName.setFont(new Font("Sanserif", Font.ITALIC, 15));
        subName.setForeground(Color.LIGHT_GRAY);

        layout.putConstraint(SpringLayout.WEST, subName, 5, SpringLayout.EAST, appletName);
        layout.putConstraint(SpringLayout.NORTH, subName, 18, SpringLayout.NORTH, contentPane);

        add(subName);

        // Split GUI using pane
        bottomPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        bottomPane.setResizeWeight(0.95);
        bottomPane.setEnabled(false);
        bottomPane.setDividerSize(1);
        add(bottomPane);

        layout.putConstraint(SpringLayout.NORTH, bottomPane, 5, SpringLayout.SOUTH, appletName);
        layout.putConstraint(SpringLayout.WEST, bottomPane, 5, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.EAST, bottomPane, -5, SpringLayout.EAST, contentPane);
        layout.putConstraint(SpringLayout.SOUTH, bottomPane, -5, SpringLayout.SOUTH, contentPane);
        {
            leftPanel = new JPanel();
            leftPanel.setLayout(new GridLayout(1, 1, 1, 1));
            bottomPane.add(leftPanel);
            {
                try
                {
                    visualArea = initJLeftPanel();
                }
                catch (SQLException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                /*
                 * JScrollPane scrollPane = new JScrollPane(visualArea);
                 * scrollPane .setHorizontalScrollBarPolicy(JScrollPane.
                 * HORIZONTAL_SCROLLBAR_AS_NEEDED);
                 */

                leftPanel.add(visualArea);
            }
        }

        // Initialize right panel
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(1, 2, 1, 1));
        bottomPane.add(rightPanel);
        {
            try
            {
                inputArea = initJRightPanel();
            }
            catch (SQLException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            rightPanel.add(inputArea);
        }
    }

    public JLeftPanel initJLeftPanel() throws SQLException
    {
        JLeftPanel leftPanel = new JLeftPanel(bizTier);
        String curAssignment;

        leftPanel.displayAssignments();

        curAssignments = new ArrayList<Assignment>();

        return leftPanel;
    }

    public JRightPanel initJRightPanel() throws SQLException
    {
        final String[] COURSES = { "No Courses" };
        final String[] ASSIGNMENTS = { "-----" };
        String curClass;

        classNames = new ArrayList<String>();
        curCourses = new ArrayList<Course>();
        curCourses = bizTier.getCourses();

        for (Course x : curCourses)
        {
            curClass = x.toString();
            classNames.add(curClass);
        }

        JRightPanel rightPanel;

        addAssignment = new JButton("A s s i g n m e n t");
        addAssignment.setEnabled(false);

        addCourse = new JButton("C o u r s e");
        submitButton = new JButton("[+] Add it.");
        markButton = new JButton("[+] Update it.");
        deleteButton = new JButton("[-] Remove it.");

        assignments = new JComboBox(ASSIGNMENTS);
        courses = new JComboBox(COURSES);
        calendar = new JDateChooser();

        if (!(classNames.isEmpty()))
        {
            courses.removeAllItems();
            for (String s : classNames)
                courses.addItem(s);

            // Get the ID for the first selected Item
            String courseName = String.valueOf(courses.getSelectedItem());
            try
            {
                currCourseID = bizTier.getCourseID(courseName);
                populateAssignmentsDD();
            }
            catch (SQLException ex)
            {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
        }

        rightPanel = new JRightPanel(addAssignment, addCourse, courses, submitButton, assignments,
                markButton, calendar);

        addAssignment.addActionListener(this);
        addCourse.addActionListener(this);
        markButton.addActionListener(this);
        submitButton.addActionListener(this);
        deleteButton.addActionListener(this);
        courses.addItemListener(this);

        // display assignments
        displayAssignments();
        return rightPanel;

    }

    public void actionPerformed(ActionEvent e)
    {

        if (e.getSource() == markButton)
        {
            // updateGrade(int courseID, String assignName, double newGrade)
            String assignName = String.valueOf(assignments.getSelectedItem());
            String grade = inputArea.getLetterGrade();

            if (assignName == null)
            {
                JOptionPane.showMessageDialog(null, "No assignment selected.");
            }
            else
            {
                try
                {
                    bizTier.updateGrade(currCourseID, assignName, grade);
                    populateAssignmentsDD();
                    displayAssignments();
                }
                catch (SQLException ex)
                {
                    // TODO Auto-generated catch block
                    ex.printStackTrace();
                }
            }

        }

        if (e.getSource() == addAssignment)
        {
            inputArea.assignmentWidget(courses, calendar, submitButton, assignments, markButton);
            addAssignment.setEnabled(false);
            addCourse.setEnabled(true);

            displayAssignments();
        }

        if (e.getSource() == addCourse)
        {
            inputArea.courseWidget(submitButton, courses, deleteButton);
            addAssignment.setEnabled(true);
            addCourse.setEnabled(false);
        }

        if (e.getSource() == deleteButton)
        {
            String selection = (String) courses.getSelectedItem();
            System.out.println(selection);
            try
            {
                bizTier.deleteCourse(selection);
                populateCoursesDD();
                populateAssignmentsDD();
                displayAssignments();
            }
            catch (SQLException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        if (e.getSource() == submitButton)
        {
            if (!addAssignment.isEnabled())
            {
                // Adding assignment!
                String assignDesc = inputArea.getDescriptionAW();

                if (assignDesc == null || calendar.getDate() == null)
                {
                    JOptionPane.showMessageDialog(null, "Oops! Make sure to input all fields.");
                }
                else
                {

                    String date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getDate());

                    try
                    {
                        bizTier.insertAssignment(assignDesc, date, currCourseID);
                        populateAssignmentsDD();
                        displayAssignments();
                    }
                    catch (SQLException a)
                    {
                        // TODO Auto-generated catch block
                        a.printStackTrace();
                    }

                    inputArea.clearAssignmentWidget(calendar);
                }

            }
            else if (!addCourse.isEnabled())
            {
                String name = inputArea.getCourseNameCW(); // Course name
                String subject = inputArea.getCourseSubjectCW(); // Subject name
                int courseNum = inputArea.getCourseNumCW(); // Course number

                if (name == null || subject == null || courseNum == -1) // Check
                // for
                // empty
                // fields
                {
                    JOptionPane.showMessageDialog(null, "Oops! Make sure to input all fields.");
                }

                else
                {
                    try
                    {
                        bizTier.insertCourse(name, subject, courseNum);
                        populateCoursesDD();
                        populateAssignmentsDD();
                        displayAssignments();

                    }
                    catch (SQLException ex)
                    {
                        // TODO Auto-generated catch block
                        ex.printStackTrace();
                    }

                    inputArea.clearCourseWidget();
                    JOptionPane.showMessageDialog(null, "Got it. Course " + name + " was created.");
                }
            }
        }
    }

    public void itemStateChanged(ItemEvent e)
    {
        if (e.getSource() == courses)
        {
            if (e.getStateChange() == ItemEvent.SELECTED)
            {
                String courseName = String.valueOf(courses.getSelectedItem());

                try
                {
                    currCourseID = bizTier.getCourseID(courseName);
                    populateAssignmentsDD();
                    displayAssignments();
                }
                catch (SQLException ex)
                {
                    // TODO Auto-generated catch block
                    ex.printStackTrace();
                }
            }
        }
    }

    public void populateCoursesDD() throws SQLException
    {
        String curClass;

        classNames = new ArrayList<String>();
        curCourses = bizTier.getCourses();

        for (Course x : curCourses)
        {
            curClass = x.toString();
            classNames.add(curClass);
        }

        courses.removeAllItems();

        for (String s : classNames)
            courses.addItem(s);
    }

    public void populateAssignmentsDD() throws SQLException
    {
        String curAssign;

        assignNames = new ArrayList<String>();
        curAssignments = bizTier.getAssignments(String.valueOf(courses.getSelectedItem()));

        for (Assignment x : curAssignments)
        {
            curAssign = x.getAssignmentName();
            assignNames.add(curAssign);
        }

        assignments.removeAllItems();

        if (assignNames.isEmpty())
        {
            markButton.setEnabled(false);
        }
        else
        {
            for (String s : assignNames)
                assignments.addItem(s);

            markButton.setEnabled(true);
        }
    }

    public void displayAssignments()
    {

        try
        {
            // display assignments
            visualArea.resetText();
            for (String x : classNames)
            {
                curAssignments = bizTier.getAssignments(x);
                visualArea.updateClassName(x);
                visualArea.updateDisplay(curAssignments);
            }
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}