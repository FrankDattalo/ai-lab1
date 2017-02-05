Frank Dattalo
2/4/2017

There are two main methods within this project:

Part 1: sra.RoverSampleSensor.main

Part 2: mbra.MovingRoverSnesors.main

To run these from Eclipse go to the menu: Run > Run Configurations > Arguments > Program Arguments
and then specify the arguments for the program.

To run these programs from the command line do the following:
Part 1:
$ cd <this directory>
$ javac -d bin -sourcepath src src/sra/RoverSampleSensor.java
$ java -cp bin sra.RoverSampleSensor -file <path to test file>

Part 2:
$ cd <this directory>
$ javac -d bin -sourcepath src src/mbra/MovingRoverSensors.java
$ java -cp bin mbra.MovingRoverSensors -file <path to test file>

Test Plan:

Part 1 - Simple Reflex Agent - was tested with the following files:
hw1-data1.txt
hw1-data6.txt

Part 2 - Model Based Reflex Agent - was tested with the following files:
hw1-data2.txt
hw1-data3.txt
hw1-data4.txt
hw1-data5.txt
hw1-data7.txt

The tests attempt to enumerate all possible ways that the agent would decided to NOOP, ie: all terminal conditions.
Furthermore, the tests aim to explore all edge cases.