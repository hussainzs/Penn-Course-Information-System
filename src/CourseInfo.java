import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//This works fine (at least it did in my minimal testing lol)
public class CourseInfo {
    public static ArrayList<Course> getAllCoursesInSubject(String subjCode) {
        Document subjDoc;
        String url = "https://catalog.upenn.edu/courses/" + subjCode.toLowerCase();
        ArrayList<Course> coursesMap = new ArrayList<>();
        try {
            subjDoc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("URL connection failed, given url = " + url);
            return null;
        }

        Pattern getCourseInfo = Pattern.compile("<div class=\"courseblock\">.*");

        Elements divElems = subjDoc.select("div");
        for (Element div : divElems) {
            //the boolean below is because for some reason other div tags
            // were matching, even though they DIDNT start with <div class="courseblock"
            // I have no idea why the regex would allow that but whatever
            boolean classOnly = div.toString().charAt(12) == 'c';
            Matcher isInfo = getCourseInfo.matcher(div.toString());
            if (isInfo.find() && classOnly) {
                Pattern getIdAndTitle = Pattern.compile(".*<strong>(.*)&nbsp;(\\d+) (.*)</strong>.*");
                //selectInfo's group1 should be the courseID and group2 is the title
                Matcher selectIdAndTitle = getIdAndTitle.matcher(div.toString());

                if (selectIdAndTitle.find()) {
                    Elements pElems = div.select("p");
                    String id = selectIdAndTitle.group(1) + " " + selectIdAndTitle.group(2);
                        //the above is to work around the stupid "&nbsp;" in course names in the HTML
                        // i.e. CIS&nbsp;110
                    String title = selectIdAndTitle.group(3);
                    Course c = new Course(id, title);



                    //adding DESCRIPTION to c Course object
                    String description = pElems.get(1).text();
                    c.addDescription(description);

//                    Pattern getDescr = Pattern.compile(".*courseblockextra noindent\">(.*)</p>.*");
//                    Matcher matchDescr = getDescr.matcher(pElems.get(1).toString());
//                    if (matchDescr.find()) {
//                        String description = matchDescr.group(1);
//                        c.addDescription(description);
//                    }

                    //adding PREREQS to c Course object
                    Pattern prereqElement = Pattern.compile(
                            "<p class=\"courseblockextra noindent\">Prerequisite:.*");
                    Pattern getPrereqPElem = Pattern.compile(".*this, '(.+ \\d+)'.*");
                    for (Element p : pElems) {
                        Matcher matchPrereqElem = prereqElement.matcher(p.toString());
                        if (matchPrereqElem.find()) {
                            for (Element a : p.select("a")) {
                                Matcher matchPrereqs = getPrereqPElem.matcher(a.toString());
                                if (matchPrereqs.find()) {
                                    String pre = matchPrereqs.group(1);
                                    c.addPrereq(pre);
                                }
                            }

                        }
                    }

                    //now putting c Course object in the overall Array
                    coursesMap.add(c);
                }
            }
        }

        return coursesMap;
    }

    public static Course getCourseObj(String courseID) {
        courseID = courseID.toUpperCase();
        //First, check that it is in the form [AAA 000]
        Pattern validCourse = Pattern.compile("(\\w+) \\d+");
        Matcher validCourseMatch = validCourse.matcher(courseID);
        if (!validCourseMatch.find()) {
            System.out.println("Invalid course ID");
            return null;
        }
        String subjCode = validCourseMatch.group(1);

        Document subjDoc;
        String url = "https://catalog.upenn.edu/courses/" + subjCode.toLowerCase();
        Course c;
        try {
            subjDoc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("URL connection failed, given url = " + url);
            return null;
        }

        Pattern getCourseInfo = Pattern.compile("<div class=\"courseblock\">.*");

        Elements divElems = subjDoc.select("div");
        for (Element div : divElems) {
            //the boolean below is because for some reason other div tags
            // were matching, even though they DIDNT start with <div class="courseblock"
            // I have no idea why the regex would allow that but whatever
            boolean classOnly = div.toString().charAt(12) == 'c';
            Matcher isInfo = getCourseInfo.matcher(div.toString());
            if (isInfo.find() && classOnly) {
                Pattern getIdAndTitle = Pattern.compile(".*<strong>(.*)&nbsp;(\\d+) (.*)</strong>.*");
                //selectInfo's group1 should be the courseID and group2 is the title
                Matcher selectIdAndTitle = getIdAndTitle.matcher(div.toString());


                if (selectIdAndTitle.find()) {
                    Elements pElems = div.select("p");
                    String id = selectIdAndTitle.group(1) + " " + selectIdAndTitle.group(2);
                    if (id.equals(courseID)) {
                        String title = selectIdAndTitle.group(3);
                        c = new Course(id, title);

                        //TODO: fix bug where if some HTML stuff is in the description
                        // then the description looks all weird. Example: CHEM 242
                        // I could probably fix it by using Element.text() instead of .toString()
                        // but that would require reworking the below code
                        // so i'll get to it later
                        //adding DESCRIPTION to c Course object
                        String description = pElems.get(1).text();
                        c.addDescription(description);

//                        Pattern getDescr = Pattern.compile(".*courseblockextra noindent\">(.*)</p>.*");
//                        Matcher matchDescr = getDescr.matcher(pElems.get(1).toString());
//                        if (matchDescr.find()) {
//                            String description = matchDescr.group(1);
//                            c.addDescription(description);
//                        }

                        //adding PREREQS to c Course object
                        Pattern prereqElement = Pattern.compile(
                                "<p class=\"courseblockextra noindent\">Prerequisite.*");
                        Pattern getPrereqPElem = Pattern.compile(".*this, '(.+ \\d+)'.*");
                        for (Element p : pElems) {
                            Matcher matchPrereqElem = prereqElement.matcher(p.toString());
                            if (matchPrereqElem.find()) {
                                for (Element a : p.select("a")) {
                                    Matcher matchPrereqs = getPrereqPElem.matcher(a.toString());
                                    if (matchPrereqs.find()) {

                                        String pre = matchPrereqs.group(1);
                                        c.addPrereq(pre);
                                    }
                                }

                            }
                        }

                        //now returning c Course
                        return c;
                    }
                }
            }
        }

        //only will exit loop without returning
        // if did not find the course
        System.out.println("Course not found");
        return null;
    }

    //Instead of getting the course title manually from every page which is pain, as we already spent too much time
    //doing that before. This method can easily just return the subject Title for any valid subject code
    public static String getSubjectTitle(String courseCode){
        Document doc ;
        String url = "https://catalog.upenn.edu/courses/" + courseCode.toLowerCase();
        String courseTitle = "";
        try {
            doc = Jsoup.connect(url).get();
            Pattern yesCourseTitle = Pattern.compile("^(.*?)\\s\\(");
            String temp = doc.getElementsByClass("page-title").text();
            Matcher check = yesCourseTitle.matcher(temp);
            if (check.find()){
                courseTitle = check.group(1);
            }
        } catch (IOException e) {
            System.out.println("URL connection failed, given url = " + url);
            return null;
        }
        //System.out.println(courseTitle);
        return courseTitle;
    }

    public static String getCourseCode (String departmentCode, String courseTitle){
        Document doc ;
        String url = "https://catalog.upenn.edu/courses/" + departmentCode.toLowerCase();
        String courseCode = "";
        try{
            doc = Jsoup.connect(url).get();
            Pattern courseNumberPattern = Pattern.compile("[0-9]+");
            String temp = doc.getElementsContainingOwnText(courseTitle).text();
            Matcher check = courseNumberPattern.matcher(temp);
            if (check.find()){
                courseTitle = check.group(0);
                courseCode = departmentCode + " " + courseTitle;
            }
        } catch (IOException e){
            System.out.println("Connection failed with url = " + url);
        }
        System.out.println(courseCode);
        return courseCode.toUpperCase();
    }

    /**
     * Get all courses in a subject subjCode that have the prerequisite courseCode
     * @param subjCode the subject to search through
     * @param courseCode the course to search for
     * Note: if you want to use the course's subject, then can simply not input a subjCode parameter
     * which will use the method below this one
     */
    public static ArrayList<String> getPostreqs(String courseCode, String subjCode) {
        String courseID = courseCode.toUpperCase();

        ArrayList<String> postreqs = new ArrayList<>();
        ArrayList<Course> allCourses = getAllCoursesInSubject(subjCode);

        if (allCourses == null) {
            System.out.println("Error: Cannot find that subject");
            return null;
        }

        for (Course c : allCourses) {
            for (String pre : c.getPrereqs()) {
                if (pre.equals(courseID)) {
                    postreqs.add(c.getID());
                }
            }
        }

        return postreqs;
    }

    public static ArrayList<String> getPostreqs(String courseCode) {
        String courseID = courseCode.toUpperCase();
        Pattern validCourse = Pattern.compile("(\\w+) \\d+");
        Matcher validCourseMatch = validCourse.matcher(courseID);
        if (!validCourseMatch.find()) {
            System.out.println("Invalid course ID");
            return null;
        }
        String subjCode = validCourseMatch.group(1);

        return getPostreqs(courseCode, subjCode);
    }
}
