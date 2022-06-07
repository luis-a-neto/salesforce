public class ExampleClass {
    // Class that returns the average of a list of positive numbers.
    
    public static double numberAverage(List<double> numbers){
        // Starts up by creating an accumulator and initializing it to zero
        double sum = 0.0;
        
        // For each number in the list...
        for (double current : numbers){
            
            if (current < 0.0) // If we receive a negative number...
                return null;   // ...return null and halt execution.
            else
                sum += current; // Otherwise, adds it to the accumulator.
        }
        
        // Divides the sum by the quantity of numbers (size of the list)
        double average = sum / numbers.size();
        
        // Returns the average.
        return average;
    }
}
