package eu.kaesebrot.dev.shortener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import eu.kaesebrot.dev.shortener.utils.RandomStringGenerator;

@SpringBootTest
class RandomStringGeneratorTests {
    @Autowired
    private RandomStringGenerator randomStringGenerator;

	@Test
	@DisplayName("Generate correct length String")
	void givenLength_whenGenerate_thenGenerateStringInExpectedLength() {
		int expectedLength = 5;
		var generatedString = randomStringGenerator.generate(expectedLength);
		
		assertEquals(expectedLength, generatedString.length());
		assertTrue(Pattern.matches("^[a-zA-Z0-9]+$", generatedString));
	}

	@Test
	@DisplayName("Generate correct length token")
	void whenGenerateToken_thenGenerateHexTokenInExpectedLength() {
		var generatedString = randomStringGenerator.generateHexToken();
		
		assertEquals(64, generatedString.length());
		assertTrue(Pattern.matches("^[a-f0-9]+$", generatedString));
	}
}
