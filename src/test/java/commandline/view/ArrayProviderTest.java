package commandline.view;

import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

/**
 * A helper class to provide arrays to ParameterizedTests (which cannot otherwise be done) Used in
 * {@link CommandLineViewTest.getUserSelection#displaysErrorOnInvalidInput}
 */
public class ArrayProviderTest {

    /**
     * Provides a steam of string arrays of various sizes (guaranteed to be at least length 1) for
     * use with parametized tests.
     * 
     * @return a stream of <Arguments> which are string arrays
     */
    public static Stream<Arguments> stringArrayProvider() {
        return Stream.of(Arguments.of((Object) new String[] {"tomato", "onion"}),
                Arguments.of((Object) new String[] {"1", "2", "3"}),
                Arguments.of((Object) new String[] {"1", "2", "3", "5", "twenty", "blah"}));
    }
}
