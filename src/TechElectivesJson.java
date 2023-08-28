import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * This class is used to parse the JSON file containing the CIS tech electives.
 * JSON has data in following form (example):
 *  {   
 *      "course4d": "ENGL 0999",
        "course3d": "ENGL 299",
        "title": "Independent Study in Language and Literature",
        "status": "no"
    },
    {
        "course4d": "ENGR 2120",
        "course3d": "ENGR 212",
        "title": "Concepts in Micro- and Nanotechnology",
        "status": "yes"
    },
 * This is a new update since Penn changed their website!!.
 */
public class TechElectivesJson {
    /**
     * HashMap with `HashMap<String, ArrayList<Course>>` where:
     * - `key` is the department
     * - `value` is an ArrayList of valid tech electives for CIS in that department.
     */
    private HashMap<String, ArrayList<Course>> elecMap;

    public TechElectivesJson() {
        elecMap = new HashMap<>();
        try {
            // ObjectMapper is a class from the Jackson library that allows us to parse JSON files
            ObjectMapper mapper = new ObjectMapper();
            // URL is a class from the java.net library that allows us to read from a URL
            URL url = new URL ("https://advising.cis.upenn.edu/assets/json/37cu_csci_tech_elective_list.json");
            // Read the JSON data and parse it into a JsonNode object
            JsonNode jsonData = mapper.readTree(url);

            // Iterate through each JSON object in the array
            for (JsonNode dataElement : jsonData) {
                // if it is a valid tech elective (i.e. "status": "yes")
                // then 
                // if the department (key) is already in the map, add the course to its arraylist (value)
                // else create new (key) department and create new (value) arraylist then add the course to it
                if (dataElement.get("status").asText().equals("yes")) {
                    String department = dataElement.get("course4d").asText().substring(0, 4);
                    Course course = new Course(dataElement.get("course4d").asText(), dataElement.get("title").asText());
                    if (elecMap.containsKey(department)) {
                        elecMap.get(department).add(course);
                    } else {
                        ArrayList<Course> courses = new ArrayList<>();
                        courses.add(course);
                        elecMap.put(department, courses);
                    }
                }
            }
    } catch (IOException e) {
        System.out.println("URL connection failed extracting tech electives data. Check link.");
    }

    }//end constructor

    /**
     * This method returns the hashmap of tech electives
     * @return HashMap<String, ArrayList<Course>> elecMap
     */
    public HashMap<String, ArrayList<Course>> getElecMap() {
        return this.elecMap;
    }

    /**
     * This method returns an arraylist of courses in a given department
     * @param department
     * @return
     */
    public ArrayList<Course> coursesInDept(String department) {
        return elecMap.get(department);
    }
}
