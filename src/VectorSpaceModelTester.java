import javax.swing.*;
import java.io.*;
import java.io.FileWriter;
import java.util.*;

/** the tester class.*/
public class VectorSpaceModelTester {

    public void retrieveTopListings (String departmentListing, String result, boolean providedCourse){
        JFrame frame = new JFrame();
        ArrayList<Course> coursesInDepartment = CourseInfo.getAllCoursesInSubject(departmentListing.toLowerCase());
        ArrayList<Document> documentsArrayList = new ArrayList<>();
        ArrayList<String> documentNames = new ArrayList<>();
        //Write the course query (based on the code to a new file)
        try {
            //relative path to write to a file that is not specified
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("./Classes/classDescription.txt"));
            writer.write(result);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document queryCourseInput =  new Document("./Classes/classDescription.txt");
        documentsArrayList.add(queryCourseInput);
        documentNames.add(result);


        //add all the descriptions of the courses in the department
        for (Course c: coursesInDepartment){
            /*only add courses that have a different description (we don't want to recommend courses
            the user has already provided */

            //check through all the document names and make sure they are not the same
            if (!c.getDescription().equals(result)) {

                //Write the course query (based on the code to a new file)
                try {
                    //relative path to write to a file that is not specified
                    BufferedWriter writer = new BufferedWriter(
                            new FileWriter("./Classes/classDescription.txt"));
                    writer.write(c.getDescription());
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Document add = new Document("./Classes/classDescription.txt");
                documentsArrayList.add(add);
                documentNames.add(c.getTitle());
            }

        }

        TreeMap< Double, String> mapFromCourseToSimilarity = new TreeMap<>();

        Corpus corpus = new Corpus(documentsArrayList);
        VectorSpaceModel vectorSpace = new VectorSpaceModel(corpus);
        for(int i = 1; i < documentsArrayList.size(); i++) {
            Document doc = documentsArrayList.get(i);
            String name = documentNames.get(i);
            System.out.println("\nComparing to " + name);
            System.out.println(vectorSpace.cosineSimilarity(queryCourseInput, doc));

            //put the values to the tree map
            mapFromCourseToSimilarity.put(vectorSpace.cosineSimilarity(queryCourseInput, doc), name);
        }

        Set<Double> values = mapFromCourseToSimilarity.keySet();
        ArrayList<Double> valuesToSort = new ArrayList<>();
        for (Double val: values){
            valuesToSort.add(val);
        }
        Collections.sort(valuesToSort);
        Collections.reverse(valuesToSort);

        String displayString = "";
        for (int i = 0; i < 5; i++){
            if (i < valuesToSort.size()) {
                String courseTitle = mapFromCourseToSimilarity.get(valuesToSort.get(i));
                displayString += "\n" + (i + 1) + ". " +
                       CourseInfo.getCourseCode(departmentListing, courseTitle)  +  ": " + courseTitle
                        + ";" + "   Percent Similarity: " +  String.format("%.2f%n",((valuesToSort.get(i)) * 100));
            }
        }

        if (!providedCourse) {
            JOptionPane.showMessageDialog(frame, "Beep Boop.. " +
                    "The Top Courses In The  " + departmentListing.toUpperCase() + " department that" +
                    " are the most similar to the query\n\t\"" + result +
                    "\" \n" + " are: " + "\n" + displayString);
        }
        else{
            JOptionPane.showMessageDialog(frame, "Beep Boop.. " +
                    "The Top Courses In The  " + departmentListing.toUpperCase() + " department that" +
                    " are the most similar to the provided course  "  +
                    "\n" + " are: " + "\n" + displayString);
        }

    }

    public void recommendationQuestion() {

        // CourseInfo courseInfo = new CourseInfo();
        JFrame frame = new JFrame();

        //would you want to filter by course code or course description
        JOptionPane.showMessageDialog(frame, "Welcome to Course Recommender! ");
        String[] optionSet = new String[]{"Filter by Course Code", "Filter by Query"};
        int n = JOptionPane.showOptionDialog(
                frame,
                "How do you want to receive your course recommendations?",
                "Choose your method of recommendation!",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                optionSet,
                optionSet[1]
        );

        if (n == -1){
            JOptionPane.showMessageDialog(frame, "Leaving. Calc-you-later!");
            Main.askQuestions();
        }

        if (n == 0) {

            //Retrieve the department that the user inputs
            JOptionPane.showMessageDialog(frame, "First, please enter the department code that you want to " +
                    "receive recommendations for!");
            String departmentListing = (String) JOptionPane.showInputDialog(
                    frame,
                    "Enter the  Department Category",
                    "Recommendation",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "CIS");
            if (departmentListing == null){
                    JOptionPane.showMessageDialog(frame, "Leaving. Calc-you-later!");
                    Main.askQuestions();
            }
            JOptionPane.showMessageDialog(frame, "You selected the " + departmentListing.toUpperCase()
                    + " department");

            boolean selectedProperCourse = false;
            Course selectedCourse =  null;
            while (!selectedProperCourse) {
                JOptionPane.showMessageDialog(frame, "Please enter the " +
                        " course code that you want to receive similar courses for! ");
                String result = (String) JOptionPane.showInputDialog(
                        frame,
                        "Enter the Course Code",
                        "Recommendation",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        "NETS 1500"
                );

                if (result == null){
                    Main.askQuestions();
                }

                //assume for now that they entered a code that is cis
                 selectedCourse = CourseInfo.getCourseObj(result);
                 if (selectedCourse!= null){
                     selectedProperCourse = true;
                 }
                 else{
                     JOptionPane.showMessageDialog(frame, "Sorry that is an invalid course.  " +
                             "Please try again!");
                 }
            }
            JOptionPane.showMessageDialog(frame, "We will now receive recommendations for: " + " \n" +
                    selectedCourse.getTitle());
            retrieveTopListings(departmentListing, selectedCourse.getDescription(), true);




        } else if (n == 1) {


            //Retrieve the department that the user inputs
            JOptionPane.showMessageDialog(frame, "First, please enter the department code that you want to " +
                    "receive recommendations for!");
            String departmentListing = (String) JOptionPane.showInputDialog(
                    frame,
                    "Enter the  Department Category",
                    "Recommendation",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "CIS");
            if (departmentListing == null){
                JOptionPane.showMessageDialog(frame, "Leaving!");
                Main.askQuestions();
            }
            JOptionPane.showMessageDialog(frame, "You selected the " + departmentListing.toUpperCase()
                    + " department");


            JOptionPane.showMessageDialog(frame, "Now, please enter text that matches your interests");
            String result = (String) JOptionPane.showInputDialog(
                    frame,
                    "Enter the Query of search terms ",
                    "Recommendation by Query",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "artificial intelligence machine computer game graph"
            );
            if (result == null){
                Main.askQuestions();
            }

            retrieveTopListings(departmentListing, result, false);


        }

    }
}

