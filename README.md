Set UP Yota
Precondition:
Project can be setUp on Windows 10 system
Download Intellig IDE https://www.jetbrains.com/ru-ru/idea/



1.Execute project from archive
2.Open project with IDE
3.Ran testService.jar to set up server

Run
1. Execute mvn clean command in terminal to clean test data
2. Run testing.xml file to run in parallel all tests(some requests can have 500 error due to the large number of requests on short time)
3. Tests can be runned one by one or by classes: src/test/java/tests/open any test class - and you can run any test by click on ran button;)
 


Open Report
1. Maven/Plugins/Allure/allure:serve
 


