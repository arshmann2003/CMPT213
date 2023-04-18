package ca.myApp.model;

import java.util.ArrayList;

public class Watcher {
    /**
    Each watcher object returned by this endpoint has:
    id: Watcher’s ID, as assigned by the back-end.
    department: JSON object for the department of the course being watched. Expected subfields are deptId and name.
    course: JSON object for the course being watched. Expected sub-fields are courseId and
    catalogNumber.
    events: Array of strings, showing the history of events it has observed.
    Expected format of each event should be similar to the template:
            [date]: Added section [type] with enrollment ([total]/[cap]) to offering [term] [year]
    For example:
    Sun Mar 25 21:41:35 PDT 2018: Added section LEC with enrollment (89 / 90)
    to offering Spring 2019
            [total] and [cap] should be the new amount added by this change, rather than the total
    number. i.e., if you are adding an extra tutorial holding 23 students with a cap of 30 to an
    existing set of tutorials for a course offering, the event should show (23/30), not the much
    greater total of all tutorials for this section.
     **/

    public long id;
    public Department department;
    public Course course;
    public ArrayList<String> events;

    public Watcher() {
    }
}
