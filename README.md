# OfflineGestureRecognizer

Offline/Test Recognition
 
a. read in a gesture dataset from files to use for templates and candidates :

-- We had downloaded the xml files present in the location: http://depts.washington.edu/acelab/proj/dollar/xml.zip and placed them in our project source folder. 

-- In the main method, using loadFileTemplates method {Line 370 CanvasBoard.java}, we had loaded these templates and stored in a dictionary.

-- In a list named as 'gestureTemplates_names', we had stored all the template names.


b. connect to your existing $1 pre-processing and recognition methods : 

-- We had placed the previous four steps of the recognition algorithm which are (i)resampling {Line 61 GestureComputing.java}, 
(ii)rotation {Line 109 GestureComputing.java}, (iii)scaling {Line 142 GestureComputing.java}, (iv)translation {Line 170 GestureComputing.java} in  GestureComputing.java.

-- A method named recognizingFile is written {Line 13 GestureComputing.java}, which will call the above mentioned methods for recognizing a particular gesture. 

-- We had written a method called preProcessPoints {Line 160 GestureComputing.java} to preprocess the gesture templates at instantiation.


c. loop over the gesture dataset to systematically configure your recognizer and test it : 

-- We had looped over user from 1 to 10, in which e(no of templates) is looped over 1 to 9 & 100 tests had been conducted.

-- At a particular e, 16 * e is considered as a Training data and 16 templates to be test data.

-- We will send this data to recognizeFile method {Line 13 GestureComputing.java}, to get the best matching template for each test template.

-- We will then calculate the score using scoreCalculator method {Line 38 GestureComputing.java}and then form a N best list based on the increasing order of recognizing scores.


d. output the result of the recognition tests to a log file : 

-- A log file named output.txt has been created using FileWriter object.

-- Using BufferWriter object, we had written the logs into the output file. 

-- This has been then converted to csv file.

-- Data of User, GestureType, RandomIteration, no of Training Examples, TotalSizeOfTrainingSet, TrainingSetContents, Candidate, RecognizedResultGestureType, CorrectIncorrect, 
RecognizedResultScoreRecoResultBestMatch, RecognizedResultNBestSorted, had been added to output file.


Code Execution begins: at main() method of CanvasBoard.Java file. {Line 70 CanvasBoard.java}

