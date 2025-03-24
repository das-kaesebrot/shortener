package eu.kaesebrot.dev.shortener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import eu.kaesebrot.dev.shortener.utils.HexStringGenerator;

@SpringBootTest
class ShortenerApplicationTests {
    @Autowired
    private HexStringGenerator hexStringGenerator;

	@Test
	@DisplayName("Generate correct length hex String")
	void givenLength_whenGenerate_thenGenerateHexStringInExpectedLength() {
		int expectedLength = 5;
		var generatedString = hexStringGenerator.generate(expectedLength);
		
		assertEquals(expectedLength, generatedString.length());
		assertTrue(Pattern.matches("^[a-f0-9]+$", generatedString));
	}

	@Test
	@DisplayName("Generate correct length token")
	void whenGenerateToken_thenGenerateTokenInExpectedLength() {
		var generatedString = hexStringGenerator.generateToken();
		
		assertEquals(64, generatedString.length());
		assertTrue(Pattern.matches("^[a-f0-9]+$", generatedString));
	}
}
