package ca.myApp.model;

public class CourseOffering {
    public int courseOfferingId;
    public String location;
    public String instructors;
    public int year;
    public int semesterCode;
    public String term;


    public CourseOffering(int courseOfferingId, String location, String instructors, int year, int semesterCode, String term) {
        this.courseOfferingId = courseOfferingId;
        this.location = location;
        this.instructors = instructors;
        this.year = year;
        this.semesterCode = semesterCode;
        this.term = term;
    }
}
