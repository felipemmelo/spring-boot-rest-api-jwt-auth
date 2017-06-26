import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

public class Test {

	public static void main(String[] args) {
		
		PasswordEncoder encoder = new StandardPasswordEncoder("our_little_secret");

		System.out.println("pass1, " + encoder.encode("pass1"));
		System.out.println("pass2, " + encoder.encode("pass2"));
		
	}
}
