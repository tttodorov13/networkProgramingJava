@ECHO OFF
javac ./com/w03/*.java
jar -cvf w03.jar ./com/w03/*.class
DEL .\com\w03\*.class
java -classpath w03.jar com.w03.FunctionClass
DEL w03.jar
DEL *.txt