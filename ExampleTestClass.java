@isTest public class ExampleTestClass {
	// Example test class. Of course.
    
    @isTest static void tryNumberAverage(){
        List<double> listNumbers = new List<double>();
        
        listNumbers.add(4.4);
        listNumbers.add(6.6);
        
        double result = ExampleClass.numberAverage(listNumbers);
        
        System.assert(result == 5.5, 'Average calculation incorrect.');
    }r
    
    @isTest static void tryNegativeNumber(){
        List<double> listNumbers = new List<double>();
        
        listNumbers.add(-2.2);
        
        double result = ExampleClass.numberAverage(listNumbers);
        
        System.assert(result == null, 'Should return null.');
    }
}
