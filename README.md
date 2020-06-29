# Bridges

This is a JavaFX based implementation of the Japanese puzzle game [Bridges](https://en.wikipedia.org/wiki/Hashiwokakero) (aka. Hashiwokakero).
I implemented it for a practical programming course in my master's studies.

If you wonder why I didn't use any libraries: The implementation was rated after the course, therefore I was not allowed to use any libraries except JUnit.

# System Requirements
This application requires the Java Runtime Environment version 11.

# Building and running the application
This will build a runtime image, so you don't need the any JRE installed on your machine, to run the application.
```
./gradlew jlink
build/image/bin/Bridges
```
