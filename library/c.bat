REM ---------------------------------------------------------------
REM
REM v04/13/2016: Processing 3.x compatible version
REM
REM ---------------------------------------------------------------

ECHO OFF

CLS

REM set JAVA_HOME=C:\Program Files (x86)\Java\jdk1.6.0_45\bin
set JAVA_HOME=C:\Program Files (x86)\Java\jdk1.8.0_151

REM set path=C:\Program Files (x86)\Java\jdk1.6.0_45\bin
set path=C:\Program Files (x86)\Java\jdk1.8.0_151\bin

REM set CP=zxingcore.jar;zxingjavase.jar;C:\processing-3.1.1-32\core\library\core.jar;C:\processing-3.1.1-32\lib\pde.jar
set CP=zxingcore_3.3.2.jar;zxingjavase_3.3.2.jar;D:\processing-2.2.1-32\core\library\core.jar;D:\processing-2.2.1-32\lib\pde.jar
REM set CP=zxingcore_3.3.2.jar;zxingjavase_3.3.2.jar;D:\processing-3.3.7-32\core\library\core.jar;D:\processing-3.3.7-32\lib\pde.jar

REM set CP=core-3.2.1.jar;javase-3.2.1.jar;C:\processing-3.1.1-32\core\library\core.jar;C:\processing-3.1.1-32\lib\pde.jar

javac -cp %CP% -d . ../src/*.java

jar cvf zxing4p3.jar com\cage\zxing4p3\*.class

copy *.jar C:\Users\rolfasus\Documents\Processing\libraries\zxing4p3\library

REM copy *.jar ..\examples\decodeImageCam\code
REM copy *.jar ..\examples\decodeMultipleCodes\code
REM copy *.jar ..\examples\decodePImage\code
REM copy *.jar ..\examples\generateQRCode\code

REM use docs.bat for javadoc
REM javadoc -classpath %CP% -d ../reference ../src/*.java

