public class AgeUtils{
    public static void compareAge(Integer age){
        if (age < 18){
        System.debug(‘Ask for parental permission!’);
        }
        else if (age >= 65){
            System.debug(‘Elderly people are priority.’);
        }
        else {
            System.debug(‘Permission not needed.’);
        }
    System.debug(‘End of execution.’);
    }
}
