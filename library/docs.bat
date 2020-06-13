REM Generate the javadoc pages
set JAVA_HOME=C:\Program Files (x86)\Java\jdk1.6.0_45\bin
set path=C:\Program Files (x86)\Java\jdk1.6.0_45\bin
javadoc -classpath %CP% -d ../reference ../src/*.java
