package eu.kaesebrot.dev.shortener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import eu.kaesebrot.dev.shortener.utils.RandomStringGenerator;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(profiles = "dev")
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
	void whenGenerateTokenWithoutLength_thenGenerateHexTokenInExpectedLength() {
		var generatedString = randomStringGenerator.generateHexToken();

		assertEquals(64, generatedString.length());
		assertTrue(Pattern.matches("^[a-f0-9]+$", generatedString));
	}

	@Test
	@DisplayName("Generate correct length token")
	void whenGenerateTokenWithLength_thenGenerateHexTokenInExpectedLength() {
		int expectedLength = 127;
		var generatedString = randomStringGenerator.generateHexToken(127);

		assertEquals(expectedLength, generatedString.length());
		assertTrue(Pattern.matches("^[a-f0-9]+$", generatedString));
	}
}
