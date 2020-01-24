package commandline.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class ListUtilityTest {

    private final String[] stringArray = {"List Item 1", "List Item 2", "List Item 3"};
    private final Collection<String> iterableStringArray = Arrays.asList(stringArray);

    @DisplayName("Works on empty iterator")
    @Test
    public void worksWithEmptyIterator() {
        final Collection<String> emptyList = new ArrayList<String>();
        ListUtility list = new ListUtility(emptyList);
        assertEquals("", list.getIndentedList());
        assertEquals("", list.getBulletList());
        assertEquals("", list.getEnumeratedList());
    }

    @DisplayName("getEnumeratedList()")
    @Nested
    public class GetEnumeratedList {
        @Test
        @DisplayName("Returns correctly formatted enumerated list")
        public void returnsEnumeratedList() {
            String tab = ListUtility.INDENT_STRING;

            String expectedOutput =
                    tab + "1: List Item 1\n" + tab + "2: List Item 2\n" + tab + "3: List Item 3\n";

            ListUtility list = new ListUtility(iterableStringArray);
            assertEquals(expectedOutput, list.getEnumeratedList());
        }
    }

    @DisplayName("getBulletList()")
    @Nested
    public class GetBulletList {
        @Test
        @DisplayName("Returns correctly formatted bulletpoint list")
        public void returnsBulletPointList() {
            String tab = ListUtility.INDENT_STRING;

            String expectedOutput =
                    tab + "> List Item 1\n" + tab + "> List Item 2\n" + tab + "> List Item 3\n";

            ListUtility list = new ListUtility(iterableStringArray);
            assertEquals(expectedOutput, list.getBulletList());
        }
    }

    @DisplayName("getIndentedList()")
    @Nested
    public class GetIndentedList {
        @Test
        @DisplayName("Returns correctly formatted indented list")
        public void returnsIndentedList() {
            String tab = ListUtility.INDENT_STRING;

            String expectedOutput =
                    tab + "List Item 1\n" + tab + "List Item 2\n" + tab + "List Item 3\n";

            ListUtility list = new ListUtility(iterableStringArray);
            assertEquals(expectedOutput, list.getIndentedList());
        }
    }



}
