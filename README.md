# Training_Spring_React

How to run the App:
1.	Download file and open through IntelliJ IDEA or other IDE
2.	Generate React project: npm install
3.	Install Bootstrap, React Router, and reactstrap in the frontend directory: npm install --save bootstrap@5.1 react-cookie@4.1.1 react-router-dom@5.3.0 reactstrap@8.10.0
4.	Run the following command: yarn upgrade react-router-dom@5.2.0
5.	In package.json file change "react-router-dom" version to 5.2.0
6.	Create database “testdb4”, if you want to add database with other name, change the name also in application.properties file
7.	Change password to yours in  application.properties file
8.	Run backend and frontend (npm start)

Additional info for successful run of the app:
1.	In order to use Login functionality, please, add user in table ‘user’ in “testdb4”  database
2.	Run the following SQL command: INSERT INTO clients (id, dob, email, name) VALUES (0, '2000-01-01', 'no@parent.com', 'NO PARENT');
Description: We need it as id ‘0’ will be used for clients with no parent
3.	Please note that link to other user is like the link to the lower level, so if you have a link from A to B, don’t put the link from B to A. Example of successful link input:
A=>B,  B=>C,  C=>D, D=>E .... 
4.	Don’t forget to input mandatory field birth date (it is used also for age calculation)
5.	Please note that youngest clients are detected by age, not by their birth date
