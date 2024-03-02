package project;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DateInfoTest {
    private DateInfo dateInfo;
    
    @BeforeEach
    void setUp() throws Exception {
        dateInfo = new DateInfo(1, 12, 2024);
    }
    
    @Test
    void testDateInfoConstructor() {
        assertEquals(1, dateInfo.getStartMonth());
        assertEquals(12, dateInfo.getEndMonth());
        assertEquals(2024, dateInfo.getStartYear());
    }
    
    @Test
    void testToString() {
        String expectedString = "\n- Date: 1\\2024 - 12\\2024";
        assertEquals(expectedString, dateInfo.toString());
    }
}
