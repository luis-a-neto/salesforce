public class HelloWorld{
	// Hello World class.
	public static String buildMessage(String name){
		String message;
		message = 'Hello ' + name;
		return message;
	}
	
	public static void doHelloWorld(String name){
		System.debug(buildMessage(name));
	}
}
