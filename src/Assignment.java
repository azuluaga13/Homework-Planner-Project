// Programmers: Wes Clark, Guillermo Martinez, Ronnypetson Souza da Silva, Anthony Zuluaga
// Assignment:  Final Project
// Date:        December 3, 2015
// Description: This class represents the assignment entity, that is present in the project logic.

import java.text.SimpleDateFormat;
import java.util.Date;

public class Assignment
{
    private int assignmentID; // The identification of the assignement
    private String assignmentName; // The name of the assigment
    private Date dueDate; // The date due
    private double grade; // The student's grade
    private int courseID; // The course ID of the corresponding course
    private String letterGrade;

    public Assignment(int assignmentID, String assignmentName, Date dueDate, int courseID, String lg)
    // PRE: assnID > 0, courseID > 0, assnName and dueDate are initialized
    // POST: A new Assignment object is created with the class members assigned to
    // the respective parameters.
    {
        this.assignmentID = assignmentID;
        this.assignmentName = assignmentName;
        this.dueDate = dueDate;
        this.courseID = courseID;
        this.grade = 0.0;
        this.letterGrade = lg;
    }

    public int getAssignmentID()
    // POST: FCTVAL == this.assignmentID;
    {
        return this.assignmentID;
    }

    public String getAssignmentName()
    // POST: FCTVAL == this.assignmentName
    {
        return this.assignmentName;
    }

    public Date getDueDate()
    // POST: FCTVAL == this.dueDate
    {
        return this.dueDate;
    }

    public double getGrade()
    // POST: FCTVAL == this.grade
    {
        return this.grade;
    }

    public double getCourseID()
    // POST: FCTVAL == this.courseID
    {
        return this.courseID;
    }

    public void setAssignmentID(int id)
    // PRE: id > 0
    // POST: this.assignmentID is assigned to id
    {
        this.assignmentID = id;
    }

    public void setAssignmentName(String name)
    // PRE: name is initialized
    // POST: this.assignmentName is assigned to name
    {
        this.assignmentName = name;
    }

    public void setDueDate(Date dueDate)
    // PRE: dueDate is initialized
    // POST: this.dueDate is assigned to dueDate
    {
        this.dueDate = dueDate;
    }

    public void setGrade(double grade)
    // PRE: grade > 0
    // POST: this.grade is assigned to grade
    {
        this.grade = grade;
    }

    public void setCourseID(int courseID)
    // PRE: courseID > 0
    // POST: this.courseID is assigned to courseID
    {
        this.courseID = courseID;
    }

    public String toString()
    //POST: FCTVAL == a String of the object's assignmentName, dueDate, and letterGrade members.
    {
        String simple = new SimpleDateFormat("dd/MM/yyyy").format(dueDate);

        if (letterGrade == null)
            return "[ ] " + assignmentName + " |  Due Date: " + simple ;

        return "[x] " + assignmentName + " | Grade: " + letterGrade;
    }
}
