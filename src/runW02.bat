@ECHO OFF
javac ./com/w02/*.java
jar -cvf w02.jar ./com/w02/*.class
DEL .\com\w02\*.class
java -classpath w02.jar com.w02.FunctionClass
DEL w02.jar