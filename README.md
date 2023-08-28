# Penn Course Information System

Welcome to the Penn Course Information System (PCIS) repository! This project was developed by Hussain Zaidi, Ethan Eisenberg, and Charlie Gottlieb.
The Penn Course Information System is designed to provide students with a comprehensive tool to navigate and explore the courses offered at the University of Pennsylvania. 
It offers several features to simplify the course selection process and help students make informed decisions.

## Getting Started
To get started with PCIS, follow these steps:

1. Clone this repository to your local machine.
2. Make sure you have Java and Gradle installed. If you don't have Gradle install it from (https://gradle.org/releases/)
3. Navigate to the project directory and run the following command in terminal to build the project: gradle build
4. Once the project is built, run Main.java


## Features

### 1. Acronym Lookup

Penn has a wide range of subjects with various acronyms (like OIDD, ANEL, YDSH, HCIN, etc.), which can be confusing. The Acronym Lookup feature allows users to enter an acronym and the system provides the corresponding subject name. 
This feature helps students quickly understand the different subject codes used at Penn without having to search on Google everytime you run into one of them.

### 2. Course Information

The Course Information feature allows users to search for a specific course by entering its course code, such as "CIS 1210" or "NETS 1500". 
The system collects dynamically updated data from Penn's website and provides a detailed description of the course, including any prerequisites. 
Personal Fav: Additionally, it highlights other courses that use the selected course as a prerequisite. This feature is incredibly useful for students to understand the course's relevance and plan their academic journey effectively.

### 3. Technical Elective Finder (CIS Majors)

This feature is specifically designed for CIS BSE majors. Finding qualifying technical electives for CIS majors at Penn can be a hassle. The Technical Electives feature simplifies this process by offering two sub-features:

- **Course Code Check**: Users can enter a course code to check if that course counts as a technical elective for CIS majors.
- **Subject-based Search**: Users can enter a subject code, such as CIS, NETS, MEAM, ESE, NURS, etc., to find out which courses in those subjects count as technical electives for CIS majors. This information is collected and analyzed using updated data.

### 4. Social Science and Humanities Elective Finder for SEAS

This feature helps all engineering students at Penn decide on their Social Science (SS) and Humanities (H) electives. Penn has specific rules regarding which courses count towards SS and H requirements. 
The Social Science and Humanities Electives feature offers four sub-features:

- **Print Subjects**: Users can view a list of subjects whose courses count as Social Science or Humanities electives.
- **Restricted Subjects**: Users can see which subjects have a limited number of SS or H electives. Not all courses from some subjects count towards SS or H electives.
- **Subject-based Search**: Users can find valid SS or H courses within a specific subject, such as CIS, NETS, PHIL, ECON, OIDD, etc.
- **Course Code Check**: Users can enter a course code, such as "EESC 3003", to check if it counts towards SS or H requirements.

### 5. Course Recommendation

The Course Recommendation feature utilizes Vector Space Model and Text Analysis algorithms to recommend courses based on user interests. 
1. Users can enter a custom query to find courses relevant to their interests.
2. Users can search for courses similar to a specific course, such as find courses similar to "CIS 1210".

## Technical Details

- **Data Collection**: We utilized JSOUP and Regex to extract and filter current data from Penn's website, ensuring users access the latest information. 
- **Adaptation to System Changes:**: PCIS successfully adapted to significant changes in Penn's course code system. When the university introduced new course codes and updated its tech elective website, we re-implemented feature 3 from scratch and fixed issues in other features.
- **New JSON data collection**: Data collection for feature 3 was streamlined through the use of (publicly available) Penn's JSON API endpoint for CIS tech electives.
- **Course Recommendation**: The project incorporates a customized Vector Space Model to handle user queries and course recommendations.
- **Build Optimization**: To address constant dependencies and efficiency issues, incorporated Gradle into the project for smoother building and execution.

**Developers Note**: Web scraping dependency - We do not have access to Penn's API so please be aware that the Penn Course Information System (PCIS) utilizes web scraping to collect data from the University of Pennsylvania's website. 
While we've designed PCIS to be adaptable, significant changes to Penn's website structure could potentially affect certain features in the future. We want to acknowledge this inherent limitation of web scraping. 

Disclaimer: This README provides an overview of the Penn Course Information System project's features and technical aspects. It is intended for informational purposes only and does not represent official statements from the developers or the University of Pennsylvania.
