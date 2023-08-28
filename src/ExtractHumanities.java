import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ExtractHumanities {

    Map<String, String> humanities;
    // k = String subject Code [i.e. ANTH, GSWS etc]
    // v = String of allowed classes in this subject or any exceptions mentioned
    //If v is empty then all classes in the subject are allowed
    //Also: Humanities are not differentiated between SS or H because of the inconsistencies of the webpage.
    //Example queries that can be answered with this code are
        /*
        Q. Which subjects count towards valid humanities and social science breath?
            --> Print a list of all K's in the map.

        Q.Which subjects have limited number of classes that count towards social science and humanities?
            --> For this we can traverse through the map and check which k's have non-empty v's
                since each non-empty v represents some limitation. Print each of those K's (i.e. CIS, EAS, PHIL etc)
                Subjects where all classes count towards humanities have empty v's

        Q.Given a specific subject code i.e. ANTH, can we find valid humanities in that subject?
            --> i.e. for ANTH we can get the v of k=ANTH and return the v. In this case v is empty so all anthropology classes are valid SS or H
                However, for DSGN print its v: 233, 234, 235, 236, 238, 245, 247, 264, 266, 268, and 269 as valid humanities
                Or for PHIL print it's v: (except 005, 006, and all other logic courses)

        Q. Given a class code i.e DSGN 245 , is this a valid SS or H for engineers?
            --> Search for k=DSGN, check its v. If v is empty, return "YES, DSGN 245 counts"
                if v is non-empty check its v, if it contains 245 then say YES otherwise NO its not valid
                This might be less accurate and will need a few hardcoded edge cases because of the inconsistency of information
                given on the humanities page.
                i.e. when k=ECON, its v= (except statistics, probability, and math courses) [ECON 104 is not allowed]
                It's hard to process this information because if user asks for ECON 002, this v does not tell us
                if that counts or no. Therefore, for such edge cases, our software will say "Sorry, I don't know the answer" just like Siri
            **However, this still works for quite a few of the subjects so its worth's implementing.
         */


    String websiteLink = "https://ugrad.seas.upenn.edu/student-handbook/courses-requirements/social-sciences-and-humanities-breadth/";
    Document doc;

    public ExtractHumanities(){
        humanities = new TreeMap<>();
        try {
            this.doc = Jsoup.connect(this.websiteLink).get();
            //call the method to fill the humanities list
            fillHumanities();

        } catch (IOException e) {
            System.out.println("URL connection failed, given url = " + websiteLink);
        }

    }

    /**
     * Method to web-scrape the humanities webpage and fill the humanities map
     */
    private void fillHumanities(){
        try {
            //If a subject title contains a dash I used this regex pattern to extract the subject code
            Pattern dashPattern = Pattern.compile("([^\\s^–]+)");

            //In some cases, the title contained the subject code inside ( ) so I used the following regex
            Pattern parenthesisPattern = Pattern.compile("\\((.*?)\\)");

            //All subject codes and their text is contained under li tags
            Elements ss = doc.getElementsByTag("li");

            //I manually filled this because all foreign languages count as SS or H as mentioned on the page.
            humanities.put("Foreign Languages", "");

            for (Element note: ss){
                String courseTitle = note.getElementsByTag("strong").text();

                //This is case 1 where subject title contains '-'
                if (courseTitle.contains("–")){
                    Matcher dashCheck = dashPattern.matcher(courseTitle);
                    if (dashCheck.find()){
                        if (dashCheck.group(0).equals("CINE/FILM")){
                            humanities.put("CIMS", "");
                        } else {
                            humanities.put(dashCheck.group(0), note.ownText());
                        }
                    }

                    //This is case 2 where subject title is between ( )
                } else if (note.getElementsByTag("strong").text().contains("(")){
                    Matcher parenthesisCheck = parenthesisPattern.matcher(courseTitle);
                    if (parenthesisCheck.find()){
                        humanities.put(parenthesisCheck.group(1), note.ownText());
                    }
                } else {
                    //This is the third case where there is neither '-' or ( ) or the previous code was
                    //not working on these few cases. So I hardcoded these in.
                    //It's the most time efficient way to handle these few edge cases
                    switch (courseTitle) {
                        case "Legal Studies":
                            humanities.put("LGST", "100, 101, 210, 215, 220");
                            break;
                        case "PSYC -Psychology":
                            humanities.put("PSYC", "except statistics, probability, and math courses");
                            break;
                        case "STSC -Science, Technology and Society ":
                            humanities.put("STSC", "");
                            break;
                    }
                }
            }
        } catch (Exception e){
            System.out.println("Error occurred extracting humanities");
        }

    }

    /**
     * Print method for humanities map, for testing purposes
     */
    public void printHumanitiesMap(){
        for (Map.Entry<String,String> e : humanities.entrySet()){
            System.out.println(e.getKey() + "[" + e.getValue() + "]");
        }
    }

    /**
     * Prints any given list, useful Testing
     * @param s List to be printed
     * @return String of result
     */
    public String printGivenList(List<String> s){
        int counter = 0;
        String result = "";
        for (String e: s){
            if (counter%5==0){
                result += "\n\n";
            }
               result += counter+1 + "-" + e + ", ";
            counter++;
        }
        return result;
    }

    /**
     * Method to answer Query: Give me all subjects that have valid humanity classes for seas students
     * @return List of all subjects on Humanity page
     */
    public List<String> getAllValidHumanities(){
        List<String> answer = new LinkedList<>();
        for (Map.Entry<String,String> e : humanities.entrySet()){
            answer.add(e.getKey());
        }
        return answer;
    }

    /**
     * Method to answer Query: Which subjects have restrictions on the courses that count towards Humanities?
     * @return List of resticted Humanities
     */
    public List<String> getRestrictedHumanities(){
        List<String> answer = new LinkedList<>();
        for (Map.Entry<String,String> e : humanities.entrySet()){
            if (!e.getValue().isEmpty()){
                answer.add(e.getKey());
            }
        }
        return answer;
    }

    /**
     * Method to answer Query: Which courses in a given subject count towards valid humanity courses?
     * @param givenSubject Humanity subject to be tested
     * @return String of restrictions or information provided for the course by SEAS website
     */
    public String getValidHumanityGivenSubject(String givenSubject) {
        String answer = "";
        String subjectProvided = givenSubject.toUpperCase();
        for (Map.Entry<String,String> e : humanities.entrySet()) {
            if (e.getKey().equals(subjectProvided)){
                if (e.getValue().equals("")){
                    answer = "All classes in " + subjectProvided + " are valid humanities";
                } else {
                    answer = subjectProvided +" courses: " + e.getValue();
                }
            }
        }
        if (answer.equals("")){
            answer = "Sorry, this subject does not have any valid SS or H courses. Please enter a valid " +
                    "humanity subject code \n i.e. ANTH, EAS, DSGN, LGST, FOLK etc";
        }
        return answer;
    }

    /**
     * Method to answer Query: Given a course, is that a valid Humanity course?
     * @param subjectCode Subject code i.e ANTH, CIS, EAS, DSGN
     * @param courseCode Course code i.e. 101, 005, 203
     * @return answer determined by the method
     */
    public String isHumanityValid (String subjectCode, String courseCode){
        String answer = "";
        if (getAllValidHumanities().contains(subjectCode)){
            switch (subjectCode) {
                case "ECON":
                    if (courseCode.equals("104")) {
                        answer = "No, " + subjectCode + " " + courseCode + " is not a valid humanity course for seas students";
                    } else {
                        answer = "Yes, " + subjectCode + " " + courseCode + " is a valid humanity course for seas students" +
                                "\n **IF it's not a statistics, probability or Math course**";
                    }
                    break;
                case "VLST":
                    if (courseCode.equals("209")) {
                        answer = "No, " + subjectCode + " " + courseCode + " is not a valid humanity course for seas students";
                    } else {
                        answer = "Yes, " + subjectCode + " " + courseCode + " is a valid humanity course for seas students";
                    }
                    break;
                case "CIS":
                    if (courseCode.equals("106")) {
                        answer = "Yes, " + subjectCode + " " + courseCode + " is a valid humanity course for seas students";
                    } else {
                        answer = "No, " + subjectCode + " " + courseCode + " is not a valid humanity course for seas students";
                    }
                    break;
                case "PHIL":
                    if (courseCode.equals("005") || courseCode.equals("006")) {
                        answer = "No, " + subjectCode + " " + courseCode + " is not a valid humanity course for seas students";
                    } else {
                        answer = "Yes, " + subjectCode + " " + courseCode + " is a valid humanity course for seas students" +
                                "\n **IF it's not a LOGIC course**";
                    }
                    break;
                default:
                    for (Map.Entry<String, String> e : humanities.entrySet()) {
                        if (e.getKey().equals(subjectCode)) {
                            if (e.getValue().contains(courseCode)) {
                                answer = "Yes, " + subjectCode + " " + courseCode + " is a valid humanity course for seas students";
                            } else if (e.getValue().isEmpty()) {
                                answer = "Yes, " + subjectCode + " " + courseCode + " is valid, since all courses in this subject are valid";
                            } else {
                                answer = "Maybe Yes, Maybe Not, Following courses are valid for "+ subjectCode + " according to SEAS humanities website "
                                        + getValidHumanityGivenSubject(subjectCode);
                            }
                        }
                    }
                    break;
            }
            if (answer.equals("")){
                answer = "Sorry, this is not a valid humanity subject code. Please enter a valid " +
                        "subject code \n i.e. ANTH, EAS, DSGN etc";
            }

        } else {
            answer = "Unfortunately, " + subjectCode + " has no valid SS or H courses for SEAS students";
        }

        return answer;
    }

    /**
     * Given a course code i.e CIS 110, splits them into CIS, and 110.
     * @param combinedString course code
     * @return array of subject code and course code separated
     */
    public String[] getCourseCodeAndSubjectCode(String combinedString){
        return combinedString.split(" ");
    }

}
