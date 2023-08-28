/**
 * UPDATE (08/28/2023): This class does not work anymore after Penn's website update. 
 * Refer to `TechElectivesJson.java` for the new implementation.
 */

// import org.jsoup.Jsoup;
// import java.io.IOException;
// import java.util.regex.Pattern;
// import org.jsoup.nodes.Document;
// import org.jsoup.nodes.Element;
// import org.jsoup.select.Elements;
// import java.util.*;
// import java.util.regex.Matcher;

public class CisElectives {

    // private String url;
    // private Document currentDoc;
    // private List<String>electivesDescriptions;
    // private List<String> electives;
    // private HashMap<String, ArrayList<Course>> elecMap;
    // //this is a map that connects a department string
    // //with an array of all the acceptable courses in it

    // /**
    //  * Constructor for CisElectives
    //  * Automatically calls fillElectiveList() method
    //  * Assumes the cis url
    //  */
    // public CisElectives(){
    //     url = "https://advising.cis.upenn.edu/tech-electives/";
    //     electivesDescriptions = new LinkedList<>();
    //     electives = new LinkedList<>();
    //     elecMap = new HashMap<>();

    //     try {
    //         this.currentDoc = Jsoup.connect(this.url).get();
    //         fillElectiveList();
    //     } catch (IOException e) {
    //         System.out.println("URL connection failed, given url = " + url);
    //     }
    // }

    // private void fillElectiveList(){
    //     try{
    //         Document electiveDocument = Jsoup.connect(url).get();
    //         Elements contentForElectives = electiveDocument.getElementsByTag("td");
    //         Pattern yesPattern = Pattern.compile("<td bgcolor=\"green\">YES</td>");
    //         Pattern coursePattern = Pattern.compile(".*<span class=\"tooltiptext\">formerly (.+) (\\d+)</span></span>");

    //         Pattern courseDescription = Pattern.compile("</span> (.*?)<br> ", Pattern.CASE_INSENSITIVE);

    //         int index = 0;
    //         for (int i = 0; i < contentForElectives.size(); i++) {
    //             Element tdCurrent = contentForElectives.get(i);
    //             Matcher checkYes = yesPattern.matcher(tdCurrent.toString());
    //             if (checkYes.find()) { //the i'th <td> elem is a green YES
    //                 Element tdCourses = contentForElectives.get(i + 1);
    //                 Elements spans = tdCourses.select("span");

    //                 //this matches for the course name inside the td tag.
    //                 //course name is not inside the span tag
    //                 Matcher check1 = courseDescription.matcher(tdCourses.toString());
    //                 while (check1.find()){
    //                     electivesDescriptions.add(check1.group(1));
    //                 }

    //                 //this loop gets only the <span> elements with a course code
    //                 // and extracts the code itself
    //                 //it also fills the electives list with course codes and names
    //                 for (Element span : spans) {
    //                     Matcher getCourseCode = coursePattern.matcher(span.toString());
    //                     if (getCourseCode.find()) {
    //                         /*
    //                             Charlie: I changed the Pattern very slightly
    //                             so that I could extract the department (i.e. CBE, MEAM, STAT, etc)
    //                             in addition to the id itself (i.e. 1210, 4100, etc).
    //                             I combined them in 'courseCode' below to make the standard (like MEAM 1001)
    //                             and then I also made a 'Course' object with the information of the courseCode
    //                             and the description (thanks Hussain) and put that in a HashMap
    //                             that has keys of the deparment and values of an array with all
    //                             elective courses in that department
    //                          */

    //                         String department = getCourseCode.group(1);
    //                         String courseCode = department + " " + getCourseCode.group(2);

    //                         electives.add(courseCode+"-"+electivesDescriptions.get(index));

    //                         Course elec = new Course(courseCode, electivesDescriptions.get(index));

    //                         //this loop is to rule out those annoying edge cases
    //                         // where a course is labeled as
    //                         // "previously X, Y" intead of the normal "previously X"
    //                         // i do it by just checking if there is a comma
    //                         boolean hasComma = false;
    //                         for (int idx = 0; idx < elec.getID().length(); idx++) {
    //                             if (elec.getID().charAt(idx) == ',') {
    //                                 hasComma = true;
    //                                 break;
    //                             }
    //                         }

    //                         System.out.println("Department: " + department);
    //                         System.out.println("Course Code: " + courseCode);
    //                         System.out.println("Course Description: " + electivesDescriptions.get(index));
    //                         if (elecMap.containsKey(department)) {
    //                             elecMap.get(department).add(elec);
    //                         } else if (!hasComma) {
    //                             elecMap.put(department, new ArrayList<>());
    //                             elecMap.get(department).add(elec);
    //                         }
                            
    //                         index++;
    //                     }
    //                 }

    //             }//end big if statement
    //         }

    //     } catch (IOException e){
    //         System.out.println("Something went wrong extracting electives from url");
    //     }
    // }

    // public ArrayList<Course> coursesInDept(String department) {
    //     return elecMap.get(department);
    // }

    // public void printElectiveList(){
    //     int i=1;
    //     for (String e: electives){
    //         System.out.println(i + ": " + e);
    //         i++;
    //     }
    // }

    // public HashMap<String, ArrayList<Course>> getElecMap() {
    //     System.out.println(elecMap);
    //     return this.elecMap;
    // }

}
