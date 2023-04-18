package ca.myApp.controllers;

import ca.myApp.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for POST, GET, and DELETE requests made by application
 */

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
class Controller {
    private final CsvParser parser;
    private final List<Department> departments = new ArrayList<>();
    private final List<Map.Entry<String, List<String[]>>> data; // ["CMPT213", [ [...],[...] ] ]
    private final List<String> deptNames = new ArrayList<>(); // []
    private List<Course> courses = new ArrayList<>();
    private Map<String, List<Integer>> y = new HashMap<>();
    private List<CourseOffering> offerings = new ArrayList<>();
    private final List<Watcher> watchers = new ArrayList<>();
    private final List<String> allWatcherKeys = new ArrayList<>();

    public Controller(){
        parser = new CsvParser("data/course_data_2018.csv", ",");
        parser.parseFile();
        data = parser.formatedData;
    }

    @GetMapping("/api/about")
    public AboutInfo getAboutInfo(){
        return new AboutInfo("Arsh Mann", "The Course Planner");
    }

    @GetMapping("/api/dump-model")
    public void displayAllCourses(){
        parser.displayData();
    }

    @GetMapping("/api/departments")
    public List<Department> allDepartments(){
        String name = "";
        for(int i=0; i<data.size(); i++){
            name = data.get(i).getValue().get(0)[1];
            if(!deptNames.contains(name)){
                deptNames.add(name);
                departments.add(new Department(i, name));
            }
        }
        return departments;
    }

    @GetMapping("/api/departments/{DEPT}/courses")
    public List<Course> getCourses(@PathVariable long DEPT){
        String faculty = "";
        courses = new ArrayList<>();
        for (Department department : departments) {
            if (DEPT == department.deptId) {
                faculty = department.name;
            }
        }
        int i=0;
        for (Map.Entry<String, List<String[]>> datum : data) {
            List<String[]> x = datum.getValue();
            String courseNumber = x.get(0)[2];
            String dept = x.get(0)[1];
            if(Objects.equals(dept, faculty))
                courses.add(new Course( i++, courseNumber));
        }

        return courses;
    }

    @GetMapping("/api/departments/{deptId}/courses/{courseId}/offerings")
    public List<CourseOffering> getCourseOfferings(@PathVariable int deptId, @PathVariable int courseId){
        offerings = new ArrayList<>();
        y = new HashMap<>();
        StringBuilder key = new StringBuilder();
        for (Department department : departments) {
            if (deptId == department.deptId)
                key = new StringBuilder(department.name);
        }
        for(Course course : courses) {
            if (courseId == course.courseId)
                key.append(course.catalogNumber);
        }
        List<String> sections = new ArrayList<>();
        int i=0;
        for (Map.Entry<String, List<String[]>> datum : data) {
            if(datum.getKey().equals(key.toString())){
                List<String[]> x = datum.getValue();
                for (String[] strings : x) {
                    String semester = getSemester(strings[0]);
                    int year = getYear(strings[0]);
                    int semesterCode = Integer.parseInt(strings[0]);
                    String catalogNumber = strings[2];
                    String subject = strings[1];
                    String location = strings[3];
                    String instructors = strings[6];
                    String enrollmentCap = strings[4];
                    String enrollmentTotal = strings[5];

                    String courseKey = subject+catalogNumber+location+strings[0]+strings[7]; //CMPT120SURREY1187LEC
                    String sectionKey = subject+catalogNumber+location+strings[0];
                    if(y.containsKey(courseKey) ){
                        List<Integer> section = y.get(courseKey);
                        section.set(0, section.get(0) + Integer.parseInt(enrollmentCap));
                        section.set(1, section.get(1)+Integer.parseInt(enrollmentTotal));
                    } else {
                        List<Integer> list = new ArrayList<>();
                        list.add(Integer.valueOf(enrollmentCap));
                        list.add(Integer.valueOf(enrollmentTotal));
                        y.put(courseKey, list);
                        if(!sections.contains(sectionKey)){
                            offerings.add(new CourseOffering(i++, location, instructors, year, semesterCode, semester));
                            sections.add(sectionKey);
                        }
                    }
                }
            }
        }
        return offerings;
    }
//        -X GET ${SERVER}/api/departments/${DEPT}/courses/${COURSE}/offerings/99999
    @GetMapping("/api/departments/{deptId}/courses/{courseId}/offerings/{courseOfferingId}")
    public List<CourseSection> getSectionInformation(@PathVariable int deptId, @PathVariable int courseId,
                                                     @PathVariable int courseOfferingId){
        List<CourseSection> courseSections = new ArrayList<>();
        StringBuilder key = new StringBuilder();  // CMPT120
        String selectedCatalogNumber = "";
        String selectedDepartment = "";
        String selectedLocation = "";
        String selectedSemesterCode = "";
        for (Department department : departments) {
            if (deptId == department.deptId){
                key = new StringBuilder(department.name);
                selectedDepartment = department.name;
            }
        }
        for(Course course : courses) {
            if (courseId == course.courseId){
                key.append(course.catalogNumber);
                selectedCatalogNumber = course.catalogNumber;
            }
        }
        for(CourseOffering offering : offerings){
            if(courseOfferingId==offering.courseOfferingId){
                selectedLocation = (offering.location);
                selectedSemesterCode = String.valueOf((offering.semesterCode));
            }
        }
        for (Map.Entry<String, List<String[]>> datum : data) {
            if(datum.getKey().equals(key.toString())){
                List<String[]> x = datum.getValue();
                List<String> components = new ArrayList<>();
                for (String[] strings : x) {
                    String semester = getSemester(strings[0]);
                    int year = getYear(strings[0]);
                    int semesterCode = Integer.parseInt(strings[0]);
                    String catalogNumber = strings[2];
                    String subject = strings[1];
                    String location = strings[3];
                    String courseKey = subject+catalogNumber+location+strings[0]+strings[7]; //CMPT120SURREY1187LEC
                    if(location.equals(selectedLocation) && selectedDepartment.equals(subject) &&
                            selectedCatalogNumber.equals(catalogNumber) && selectedSemesterCode.equals(Integer.toString(semesterCode))){
                        List<Integer> enroll = y.get(courseKey);
                        if(!components.contains(strings[7])){
                            courseSections.add(new CourseSection(strings[7], enroll.get(1), enroll.get(0)));
                            components.add(strings[7]);
                        }
                    }
                }
            }
        }
        return courseSections;
    }

    @PostMapping("/api/addoffering")
    public ResponseEntity<NewSection> addOffering(@RequestBody NewSection section)  {
        String key = section.subjectName + section.catalogNumber;
        String[] newCourse = new String[8];
        newCourse[0] = section.semester;
        newCourse[1] = section.subjectName;
        newCourse[2] = section.catalogNumber;
        newCourse[3] = section.location;
        newCourse[4] = section.enrollmentCap;
        newCourse[5] = section.enrollmentTotal;
        newCourse[6] = section.instructor;
        newCourse[7] = section.component;

        String mainKey = section.semester+section.subjectName+section.catalogNumber+section.instructor+section.component+section.location;
        if(allWatcherKeys.contains(mainKey))
            return ResponseEntity.status(HttpStatus.CREATED).body(section);
        else
            allWatcherKeys.add(mainKey);

        if(!(section.enrollmentTotal.matches("\\d+")&&section.enrollmentCap.matches("\\d+")
                && section.semester.matches("\\d+"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(section);
        }
        for (Map.Entry<String, List<String[]>> datum : data) {
            if (datum.getKey().equals(key)){
                List<String[]> values = datum.getValue();
                values.add(newCourse);
            }
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Vancouver"));
        String formattedDate = sdf.format(date);

        String newEvent = "";
        newEvent += formattedDate;
        newEvent += ": Added section ";
        newEvent += section.component + " with enrollment (" + section.enrollmentTotal+"/"+section.enrollmentCap+")";
        newEvent += " to offering " + getSemester(section.semester) + " " + getYear(section.semester);

        for (Watcher watcher : watchers) {
            String watcherKey = watcher.department.name+watcher.course.catalogNumber;
            if(watcherKey.equals(key))
                watcher.events.add(newEvent);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(section);
    }

    @PostMapping("/api/watchers")
    public void addWatcher(@RequestBody WatcherBody watcherBody ){
        StringBuilder key = new StringBuilder(); //CMPT130
        long deptId = watcherBody.deptId;
        String deptName = "";
        long courseId = watcherBody.courseId;
        String catalogNumber = "";

        for (Department department : departments) {
            if (deptId == department.deptId){
                key = new StringBuilder(department.name);
                deptName = department.name;
            }
        }
        for(Course course : courses) {
            if (courseId == course.courseId){
                key.append(course.catalogNumber);
                catalogNumber = course.catalogNumber;
            }
        }
        Watcher watcher = new Watcher();
        if(watcher.events==null)
            watcher.events = new ArrayList<>();
        watcher.department = new Department();
        watcher.course = new Course();
        watcher.department.deptId = deptId;
        watcher.department.name = deptName;
        watcher.course.courseId = (int) courseId;
        watcher.course.catalogNumber = catalogNumber;
        watcher.id = watchers.size()+1;
        watchers.add(watcher);
    }

    @GetMapping("/api/watchers")
    public List<Watcher> getWatchers(){
        return watchers;
    }

    @GetMapping("/api/watchers/{id}")
    public ArrayList<String> getWatcherEvents(@PathVariable int id){
        for(Watcher watcher : watchers){
            if(watcher.id == id)
                return watcher.events;
        }
        throw new RuntimeException("Invalid watcher id");
    }

    @DeleteMapping("/api/watchers/{id}")
    public void deleteWatcher(@PathVariable int id){
        for(Watcher watcher : watchers){
            if(watcher.id == id)
                watchers.remove(watcher);
            return;
        }
        throw new RuntimeException("Invalid watcher id");
    }

    @GetMapping("/api/stats/students-per-semester?deptId={id}")
    public void displayGraph(@PathVariable int deptId){

    }


    private int getYear(String s) {
        String digit = "20";
        digit+=String.valueOf(String.valueOf(s.charAt(1)+String.valueOf(s.charAt(2))));
        return Integer.parseInt(digit);
    }

    private String getSemester(String s) {
        char digit = s.charAt(s.length()-1);
        if(digit=='7')
            return "Fall";
        else if(digit=='4')
            return "Summer";
        else
            return "Spring";
    }
}
