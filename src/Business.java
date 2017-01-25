// Programmers: Wes Clark, Guillermo Martinez, Ronnypetson Souza da Silva, Anthony Zuluaga
// Assignment:  Final Project
// Date:        December 3, 2015
// Description: This class makes a high-level abstraction of the database operations
//              needed for the project.

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Business
{
    private Connection businessConnection; // The connection with the database
    private String databaseURL; // The URL of the database

    public Business(String databaseURL) throws InstantiationException, SQLException
    // PRE: databaseURL must be initialized
    // POST: a new Business object is created with this.databaseURL = databaseURL
    // and a new connection is established with the database if all goes right
    {
        this.databaseURL = databaseURL;
        try
        {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
        }
        catch (IllegalAccessException ex)
        {
            System.out.println("DB error, illegal access");
            Logger.getLogger(Business.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ClassNotFoundException ex)
        {
            System.out.println("DB error, class not found");
        }
    }

    public void insertCourse(String className, String subject, int courseNumber)
            throws SQLException
    // PRE: className and subject must be initialized. courseNumber > 0;
    // POST: a new course is inserted into the database table COURSES
    {
        String sqlcmd = "INSERT INTO COURSES (CLASS_NAME,SUBJECT,COURSE_NUMBER)" + " VALUES ('"
                + className + "', '" + subject + "', " + courseNumber + ")";
        this.businessConnection = DriverManager.getConnection(this.databaseURL, "user", "user");
        Statement stmt = this.businessConnection.createStatement();
        stmt.execute(sqlcmd);
        stmt.close();
        this.businessConnection.close();
    }

    public ArrayList<Course> getCourses() throws SQLException
    //POST: FCTVAL == an ArrayList of Course objects == all courses in COURSES table.
    {
        ArrayList<Course> curCourses = new ArrayList<Course>();
        Course c;
        String sqlcmd = "SELECT * FROM COURSES";
        this.businessConnection = DriverManager.getConnection(this.databaseURL, "user", "user");
        Statement stmt = this.businessConnection.createStatement();
        ResultSet result = stmt.executeQuery(sqlcmd);

        while (result.next())
        {
            c = new Course(result.getInt("ID"), result.getString("CLASS_NAME"),
                    result.getString("SUBJECT"), result.getInt("COURSE_NUMBER"));
            curCourses.add(c);
        }

        stmt.close();
        this.businessConnection.close();

        return curCourses;
    }

    public ArrayList<Assignment> getAssignments(String s) throws SQLException
    //PRE: s is a valid CLASS_NAME from ASSIGNMENTS table.
    //POST: FCTVAL == an ArrayList of Assignment objects == all ASSIGNMENTS from CLASS_NAME s.
    {
        ArrayList<Assignment> curAssignment = new ArrayList<Assignment>();
        Assignment a;
        int id = getCourseID(s);
        String sqlcmd = "SELECT * FROM ASSIGNMENTS WHERE ID = " + id;
        this.businessConnection = DriverManager.getConnection(this.databaseURL, "user", "user");
        Statement stmt = this.businessConnection.createStatement();
        ResultSet result = stmt.executeQuery(sqlcmd);

        while (result.next())
        {
            a = new Assignment(result.getInt("A_ID"), result.getString("ASSN_NAME"),
                    result.getDate("DUE_DATE"), result.getInt("ID"),
                    result.getString("LETTER_GRADE"));

            curAssignment.add(a);
        }

        stmt.close();
        this.businessConnection.close();

        return curAssignment;
    }

    public int getCourseID(String s) throws SQLException
    //PRE: s is a valid CLASS_NAME from ASSIGNMENTS table.
    //POST: FCTVAL == Primary Key ID from COURSES table.
    {
        int c_id = 0;
        String sqlcmd = "SELECT ID FROM COURSES WHERE CLASS_NAME = '" + s + "'";
        this.businessConnection = DriverManager.getConnection(this.databaseURL, "user", "user");
        Statement stmt = this.businessConnection.createStatement();
        ResultSet result = stmt.executeQuery(sqlcmd);
        if (result != null)
        {
            result.next();
            c_id = result.getInt("ID");
        }
        stmt.close();
        this.businessConnection.close();

        return c_id;
    }

    public void insertAssignment(String assignName, String dueDate, int courseID)
            throws SQLException
    // PRE: assignName and dueDate must be initialized. assignID > 0;
    // POST: a new assignment is inserted into the database table ASSIGNMENTS
    {
        String sqlcmd = "INSERT INTO ASSIGNMENTS (ASSN_NAME,DUE_DATE,ID)" + " VALUES ('"
                + assignName + "', '" + dueDate.toString() + "', " + courseID + ")";
        this.businessConnection = DriverManager.getConnection(this.databaseURL, "user", "user");
        Statement stmt = this.businessConnection.createStatement();
        stmt.execute(sqlcmd);
        stmt.close();
        this.businessConnection.close();
    }

    public void deleteCourse(String s) throws SQLException
    // PRE: s is a valid COURSE_NAME in COURSES table.
    // POST: the course with id = ID is deleted from the table COURSES.
    {
        int id = getCourseID(s);
        String sqlcmd1 = "DELETE FROM ASSIGNMENTS WHERE ID = " + id;
        String sqlcmd2 = "DELETE FROM COURSES WHERE ID = " + id;
        this.businessConnection = DriverManager.getConnection(this.databaseURL, "user", "user");
        Statement stmt = this.businessConnection.createStatement();
        stmt.execute(sqlcmd1);
        stmt.execute(sqlcmd2);
        stmt.close();
        this.businessConnection.close();
    }

    public void deleteAssignment(int courseID, String assignName) throws SQLException
    // PRE: assignID > 0
    // POST: The assignment with id = assignID is delete from the table
    // ASSIGNMENTS
    {
        String sqlcmd = "DELETE FROM ASSIGNMENTS WHERE ID = " + courseID + "AND ASSN_NAME = '"
                + assignName + "'";
        this.businessConnection = DriverManager.getConnection(this.databaseURL, "user", "user");
        Statement stmt = this.businessConnection.createStatement();
        stmt.execute(sqlcmd);
        stmt.close();
        this.businessConnection.close();
    }

    public void updateGrade(int assignID, double newGrade) throws SQLException
    // PRE: assignID > 0, newGrade > 0.0
    // POST: The assignment with id = assignID has its grade updated.
    {
        String sqlcmd = "UPDATE ASSIGNMENTS SET GRADE = " + newGrade + " WHERE A_ID = " + assignID;
        this.businessConnection = DriverManager.getConnection(this.databaseURL, "user", "user");
        Statement stmt = this.businessConnection.createStatement();
        stmt.execute(sqlcmd);
        stmt.close();
        this.businessConnection.close();
    }

    public void updateGrade(int courseID, String assignName, String newGrade) throws SQLException
    // PRE: courseID > 0, newGrade > 0.0, and assignName is initialized.
    // POST: The assignment with course_id = courseID and name = assignName has
    // its grade updated.
    {
        String sqlcmd = "UPDATE ASSIGNMENTS SET LETTER_GRADE = '" + newGrade
                + "' WHERE ASSN_NAME = '" + assignName + "' AND ID = " + courseID;
        this.businessConnection = DriverManager.getConnection(this.databaseURL, "user", "user");
        Statement stmt = this.businessConnection.createStatement();
        stmt.execute(sqlcmd);
        stmt.close();
        this.businessConnection.close();
    }

    public void updateDueDate(int courseID, String assignName, Date newDueDate) throws SQLException
    // PRE: courseID > 0, newDueDate and assignName are initialized.
    // POST: The assignment with course_id = courseID and name = assignName
    // has its due date updated.
    {
        String sqlcmd = "UPDATE ASSIGNMENTS SET GRADE = " + newDueDate.toString()
                + " WHERE ASSN_NAME = '" + assignName + "' AND ID = " + courseID;
        this.businessConnection = DriverManager.getConnection(this.databaseURL, "user", "user");
        Statement stmt = this.businessConnection.createStatement();
        stmt.execute(sqlcmd);
        stmt.close();
        this.businessConnection.close();
    }

    public double getAverageGrade(int courseID) throws SQLException
    // PRE: courseID > 0
    // POST: FCTVAL == the average grade for the course with id = courseID.
    {
        String sqlcmd = "SELECT AVG(GRADE) AS Average FROM ASSIGNMENTS WHERE ID = " + courseID;
        this.businessConnection = DriverManager.getConnection(this.databaseURL, "user", "user");
        Statement stmt = this.businessConnection.createStatement();
        ResultSet result = stmt.executeQuery(sqlcmd);
        stmt.close();
        this.businessConnection.close();
        return result.getDouble("Average");
    }

    public ArrayList<Assignment> getPastDueNonGradedAssignments() throws SQLException
    // POST: FCTVAL == an ArrayList of assignments that are past due and aren't
    // graded
    {
        ArrayList<Assignment> assigns = new ArrayList<Assignment>();
        Assignment assign;
        String sqlcmd = "SELECT * FROM ASSIGNMENTS WHERE"
                + " DUE_DATE <= CURRENT DATE AND GRADE IS NULL";
        this.businessConnection = DriverManager.getConnection(this.databaseURL, "user", "user");
        Statement stmt = this.businessConnection.createStatement();
        ResultSet result = stmt.executeQuery(sqlcmd);

        while (result.next())
        {
            assign = new Assignment(result.getInt("A_ID"), result.getString("ASSN_NAME"),
                    result.getDate("DUE_DATE"), result.getInt("ID"),
                    result.getString("LETTER_GRADE"));
            assigns.add(assign);
        }

        stmt.close();
        this.businessConnection.close();

        return assigns;
    }

    public ArrayList<Assignment> getUpcomingAssignments(String text) throws SQLException
    // PRE: upperLimitDate must be initialized.
    // POST: FCTVAL == a ArrayList of assignments such that their due dates are
    // upcoming.
    {
        ArrayList<Assignment> assigns = new ArrayList<Assignment>();
        Assignment assign;
        String sqlcmd = "SELECT * FROM ASSIGNMENTS WHERE"
                + " DUE_DATE >= CURRENT DATE AND DUE_DATE <= '" + text + "'";
        this.businessConnection = DriverManager.getConnection(this.databaseURL, "user", "user");
        Statement stmt = this.businessConnection.createStatement();
        ResultSet result = stmt.executeQuery(sqlcmd);

        while (result.next())
        {
            assign = new Assignment(result.getInt("A_ID"), result.getString("ASSN_NAME"),
                    result.getDate("DUE_DATE"), result.getInt("ID"),
                    result.getString("LETTER_GRADE"));
            assigns.add(assign);
        }

        stmt.close();
        this.businessConnection.close();

        return assigns;
    }
}
