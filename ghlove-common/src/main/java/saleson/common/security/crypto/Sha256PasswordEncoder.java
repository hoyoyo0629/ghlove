package saleson.common.security.crypto;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Sha256PasswordEncoder implements PasswordEncoder {
	@Override
	public String encode(CharSequence rawPassword) {
		String encrypted = Hashing.sha256().hashString(rawPassword.toString(), Charsets.UTF_8).toString();
		return encrypted;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encode(rawPassword).equals(encodedPassword);
	}
}
