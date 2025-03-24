package eu.kaesebrot.dev.shortener;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import eu.kaesebrot.dev.shortener.utils.ShortUriGenerator;

@SpringBootTest
class ShortenerApplicationTests {
    @Autowired
    private ShortUriGenerator shortUriGenerator;

	@Test
	void testHexStringLength() {
		var generatedString = shortUriGenerator.generate(5);
		
		assertEquals(5, generatedString.length());
	}
}
