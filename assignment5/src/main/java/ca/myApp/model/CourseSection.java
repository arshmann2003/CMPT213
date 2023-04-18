package ca.myApp.model;

public class CourseSection {
    public String type;
    public Integer enrollmentTotal;
    public Integer enrollmentCap;

    public CourseSection(String type, Integer enrollmentTotal, Integer enrollmentCap) {
        this.type = type;
        this.enrollmentTotal = enrollmentTotal;
        this.enrollmentCap = enrollmentCap;
    }
}
