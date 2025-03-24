package eu.kaesebrot.dev.shortener;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import eu.kaesebrot.dev.shortener.utils.HexStringGenerator;

@SpringBootTest
class ShortenerApplicationTests {
    @Autowired
    private HexStringGenerator hexStringGenerator;

	@Test
	void testHexStringLength() {
		var generatedString = hexStringGenerator.generate(5);
		
		assertEquals(5, generatedString.length());
	}
}
