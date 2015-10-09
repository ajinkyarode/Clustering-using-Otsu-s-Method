public class Histogram {       

// Beginning of method main

public static void main(String[] args) {   
/* Declare array and assign values */

int array[] = {19, 3, 15, 7, 11, 9, 13, 5, 17, 1};
String output = "Element\tValue\tHistogram";
/* Format histogram */

// For each array element, output a bar in histogram

for ( int counter = 0; counter < array.length; counter++ ) {

output += "\n" + counter + "\t" + array[ counter ] + "\t";

 

// Print bar of asterisks                              

for ( int stars = 0; stars < array[ counter ]; stars++ ) {

output += "*";  

}

}

System.out.println(output);
} // End of method main
}