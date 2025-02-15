This README file provides information on setting up the Library Management System, starting the Docker container, and executing integration tests.



To run the Library Management System using Docker, follow these steps:

1. Navigate to the Project Directory:
   Open a terminal and navigate to the root directory of your library project.

2. Build the Docker Image:
   If this is your first time running the project, build the Docker image using the provided Dockerfile.

   docker build -t library-system .


3. Run the Docker Container:
   Start the Docker container to launch the library management system.

   docker run -d --name library-container -p 8080:8080 library-system


   - The `-d` flag runs the container in detached mode.
   - The `--name` flag assigns a name to the container.
   - The `-p` flag maps port 8080 from the container to your local machine.

4. Verify the Container is Running:
   Verify that the container is up and running using:

   docker ps


   You should see `library-container` listed as one of the running containers.


To execute integration tests for the Library Management System, follow these steps:

1. Navigate to the Test Directory:
   Open a terminal and navigate to the directory containing the test scripts.

   cd /path/to/your/library-project/tests

2. Run the Tests Using JUnit:
   Use the JUnit framework to run your integration tests. Ensure your test classes are properly set up in your IDE (such as IntelliJ IDEA or Eclipse) or via the command line with Maven/Gradle.

   For Maven:


   mvn test


  *For Gradle:*

   ./gradlew test

3. Review Test Results:
   Once tests are executed, review the test results for any failures or errors that need addressing. Logs will be available in your console or through your build tool's reports.



- Docker Issues: If the Docker container fails to start, check that Docker is running on your system and review any error messages in the terminal.

- Test Failures: If integration tests fail, ensure the Docker container is running and check the test logs for specific error messages. Address any issues in your code or environment configuration.
