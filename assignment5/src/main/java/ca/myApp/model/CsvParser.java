package ca.myApp.model;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CsvParser {
    private final String filePath;
    private final String csvSplit;
    private List<String[]> data;
    public List<Map.Entry<String, List<String[]>>> formatedData;


    public CsvParser(String filePath, String csvSplit) {
        this.filePath = filePath;
        this.csvSplit = csvSplit;
    }

    public void parseFile(){
        String line = "";
        data = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(csvSplit);
                formatLine(values);
                data.add(values);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Invalid file path: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        formatData();
    }

    private void formatLine(String[] values){
        for(int i=0; i<values.length; i++){
            values[i] = values[i].strip();
            values[i] = values[i].replaceAll("\"", "").replaceAll("\\s", "");
        }
    }

    private void formatData() {
        HashMap<String, List<String[]>> map = new HashMap<String, List<String[]>>();

        for(String[] line : data){
            if(!line[0].equals("SEMESTER")){
                String key = line[1] + line[2];
                if(map.containsKey(key)){
                    map.get(key).add(line);
                } else {
                    map.put(key, new ArrayList<>());
                    map.get(key).add(line);
                }
            }
        }
        List<Map.Entry<String, List<String[]>>> list = new ArrayList<>(map.entrySet());
        list.sort(new Comparator<Map.Entry<String, List<String[]>>>() {
            @Override
            public int compare(Map.Entry<String, List<String[]>> o1, Map.Entry<String, List<String[]>> o2) {
                String x = o1.getKey();
                String y = o2.getKey();

                if (x.compareTo(y) > 0) {
                    return 1;
                } else if (x.compareTo(y) < 0) {
                    return -1;
                }
                return 0;
            }
        });
        formatedData = list;
        sortData();
    }
    public void sortData(){
        for(int i=0; i<formatedData.size(); i++) {

            List<String[]> line = formatedData.get(i).getValue();
            line.sort(new Comparator<String[]>() {
                @Override
                public int compare(String[] o1, String[] o2) {
                    String x = o1[0];
                    String y = o2[0];

                    if (x.compareTo(y) > 0) {
                        return 1;
                    } else if (x.compareTo(y) < 0) {
                        return -1;
                    }
                    return 0;
                }
            });
        }
    }

    public void displayData(){
        for(int i=0; i<formatedData.size(); i++){

            List<String> courses = new ArrayList<>();
            List<String[]> line = formatedData.get(i).getValue();
            System.out.println(formatedData.get(i).getKey());

            for(int j=0; j<line.size(); j++){

                String courseKey = line.get(j)[0]+line.get(j)[1]+line.get(j)[2]+line.get(j)[3];
                if(!courses.contains(courseKey)) {
                    courses.add(courseKey);

                    System.out.print("\t" + line.get(j)[0] + " in " + line.get(j)[3] + " by ");
                    for (int k = 6; k < line.get(j).length - 1; k++) {  // print instructor names
                        System.out.print(line.get(j)[k]);
                        if (k < line.get(j).length - 2)
                            System.out.print(", ");
                    }
                    System.out.print("\n");
                }
                System.out.print("\t\t" + "Type=" + line.get(j)[line.get(j).length-1]);
                System.out.print(", Enrollment=" + line.get(j)[5] + "/" + line.get(j)[4]);
                System.out.println();
            }
        }
    }
}
