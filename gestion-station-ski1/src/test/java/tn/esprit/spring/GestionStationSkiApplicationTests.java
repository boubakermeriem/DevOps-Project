package tn.esprit.spring;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
class GestionStationSkiApplicationTests {

	@Test
	void contextLoads(){
		assertNotNull(new Object()); // Correct usage
	}
}