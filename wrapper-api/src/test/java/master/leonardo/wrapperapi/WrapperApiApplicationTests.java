package master.leonardo.wrapperapi;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import master.leonardo.wrapperapi.DTO.PersonDTO;
import master.leonardo.wrapperapi.services.MessageCoder;

@SpringBootTest
class WrapperApiApplicationTests {
	
	private final MessageCoder coder;
	
	@Autowired
	public WrapperApiApplicationTests(MessageCoder messageCoder) {
		this.coder = messageCoder;
		
	}

    @Test
    void contextLoads() {
    }
    
    @Test
    void messageCoderTest() {
    	PersonDTO testPerson = new PersonDTO(619,"France","Female",42,2,0,1,1,1,101348.88,1);
    	String signature = coder.code(testPerson).getSignature();
    	System.out.println();
    	Assertions.assertEquals(Arrays.toString(Base64.getDecoder().decode(signature)), "[120, 69, -42, -35, -41, 5, -80, -10, 71, 25, -59, -99, 122, -2, -85, -1, 60, -30, -110, 10, -106, 38, 63, -63, -82, -73, -21, 7, -17, 17, 85, -48, 12, -26, 112, -117, -59, 47, -101, 26, -12, 95, 65, 94, 123, 123, -50, 102, 84, -25, 61, 127, 8, -114, -44, 107, 32, 97, -103, -85, 47, 115, -91, 90, 107, -65, -21, 93, 98, -72, 22, 55, 82, 4, 26, -87, 70, 31, 122, -95, 123, -117, -15, 52, 46, 5, 110, 35, -111, 26, 101, 55, -78, -45, 2, -14, 118, -17, -35, -101, 111, -114, -34, 105, -107, 67, -117, -66, -115, -108, -27, -111, -76, -76, 57, 42, 22, 10, -11, -25, 29, -114, -36, -84, -126, -105, 4, 65, 18, 30, -125, -116, -14, 108, 71, 75, -59, 96, -65, 42, -114, -31, -21, 72, -52, -109, 83, -4, -8, -51, -106, 78, -86, 48, 14, 41, -88, 36, -113, -31, -35, 48, -47, -80, 55, -64, 7, 4, -76, 82, 32, 127, 68, 33, -3, -52, 70, 113, 79, -16, 67, -84, 89, -50, -101, -10, -84, -68, -110, 0, 57, -81, -59, 127, 99, -18, -101, 94, -106, 109, 47, -29, 95, -26, -117, 15, 49, 77, 84, 3, -52, -69, 81, 72, -117, 2, -30, -93, 1, -112, -14, 112, 48, 26, -19, 18, 5, -8, 122, -32, 120, -2, 16, 96, -111, -113, 6, -33, -82, -27, 62, 1, -15, 63, -12, -65, 122, -123, -95, 117, -122, 51, -73, 86, -83, -99]",
    			"");
    	
    	
    	
    }

}
