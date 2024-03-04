<p>
<img src="https://repository-images.githubusercontent.com/54369221/b098cf00-afb4-11ea-89fb-fb4d2c505130" alt="jqwik" height="290"/>  
<img src="https://www.kloia.com/hubfs/java-1.png" alt="Java" height="300"/>
</p>

Property based testing in java with jqwik
=============
[![Java21](https://img.shields.io/badge/java-21-brightgreen.svg)](https://www.python.org/)
[![Junit5](https://img.shields.io/badge/junit-5.10.2-yellow.svg)](https://docs.pytest.org/en/latest/)
[![AssertJ3](https://img.shields.io/badge/AssertJ-3.25.3-blue.svg)](https://hypothesis.readthedocs.io/en/latest/)
[![Maven3](https://img.shields.io/badge/Maven-3.3.0-orange.svg)](https://pypi.org/project/pytest-xdist/)


## Introduction

This project is dedicated to testing and validating various scenarious using property based testing in Java. It
showcases different algorithms and their usage, providing a practical insight into property based testing with jqwik.

## Examples

* [SHA256Hashing.java](src%2Fmain%2Fjava%2Fcom%2Fwtech%2Fcore%2Fhashing%2FSHA256Hashing.java): Utilizes the SHA-256
  algorithm for hashing passwords. It's a part of the SHA-2 family of cryptographic hash functions but is generally not
  recommended for password hashing due to its speed and susceptibility to brute-force attacks.
* [SHA512Hashing.java](src%2Fmain%2Fjava%2Fcom%2Fwtech%2Fcore%2Fhashing%2FSHA512Hashing.java): Employs the SHA-512
  algorithm. While more secure than SHA-256 due to a larger hash size, it shares similar limitations for password
  security.

## Prerequisites

* Java Development Kit (JDK) - Version 21 or higher.
* Maven - For managing dependencies and running the project.
* An IDE like IntelliJ IDEA, Eclipse, or VSCode for editing and running the Java
  files.
* Or simply run it in command line.

## How to Run

1. Clone the repository:

```bash
git clone git@github.com:wallaceespindola/java-property-based-testing-jqwik.git

cd java-property-based-testing-jqwik
```

2. Build the project with Maven:

```bash
mvn clean install
```

3. Running the main examples:

```bash
java -jar ./target/password-hashing-security.jar
```

4. This project is using Java 21, if you want to run it with a previous version, modify the pom.xml and compile to your preferred version:

```bash
<maven.compiler.source>21</maven.compiler.source>
<maven.compiler.target>21</maven.compiler.target>
```

5. You can also run the unit tests for each algorithm on your IDE at the path 'src/test/java'.

## Author

* Wallace Espindola, Sr. Software Engineer / Java & Python Dev
* E-mail: wallace.espindola@gmail.com
* LinkedIn: https://www.linkedin.com/in/wallaceespindola/
* Website: https://wtechitsolutions.com/

## Article published

* Dzone, December/2023: https://dzone.com/articles/secure-password-hashing-in-java

## License

* This project is released under the Apache 2.0 License. For more details, see [LICENSE](LICENSE).
* Copyright © 2024 [Wallace Espindola](https://github.com/wallaceespindola/).
