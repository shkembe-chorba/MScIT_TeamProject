package commandline.utils;

import java.util.List;
import java.util.function.IntFunction;

/**
 * A utility for returning formatted string lists from Collections. Created because a lot of array
 * outputs will follow a list format, either with bullet points or enumeration.
 * 
 * @author Shkembe Chorba
 */
public class ListUtility {

    public static final String INDENT_STRING = "    ";
    public static final String BULLET_STRING = "> ";
    public static final String SELECTED_STRING = " <--";

    private List<?> list;

    /**
     * Takes a list item and creates formatted lists for the .toString() form of its items.
     * 
     * @param list the target list item
     */
    public ListUtility(List<?> list) {
        this.list = list;
    }

    /**
     * Returns an enumerated list in the form: <code> {@code
     *      1: item1 
     *      2: item2 
     *      3: item3 <--
     * }<code> Where <-- denotes the item given by the selection index.
     * 
     * @param indent    the indentation amount
     * @param selection the index of the selected item
     * @return a string formatted as an enumerated list
     */
    public String getEnumeratedList(int indent, int selectionIndex) {
        return getList(indent, x -> Integer.toString(x + 1) + ": ", selectionIndex);
    }

    public String getEnumeratedList(int selectionIndex) {
        return getEnumeratedList(1, selectionIndex);
    }

    public String getEnumeratedList() {
        return getEnumeratedList(-1);
    }

    /**
     * Returns an bullet list in the form: <code> {@code
    *      > item1 
    *      > item2 
    *      > item3 <--
    * }<code> Where <-- denotes the item given by the selection index.
     * 
     * @param indent    the indentation amount
     * @param selection the index of the selected item
     * @return a string formatted as a bullet list
     */
    public String getBulletList(int indent, int selectionIndex) {
        return getList(indent, x -> BULLET_STRING, selectionIndex);
    }

    public String getBulletList(int selectionIndex) {
        return getBulletList(1, selectionIndex);
    }

    public String getBulletList() {
        return getBulletList(-1);
    }

    /**
     * Returns an indented list in the form: <code> {@code
    *      item1 
    *      item2 
    *      item3 <--
    * }<code> Where <-- denotes the item given by the selection index.
     * 
     * @param indent    the indentation amount
     * @param selection the index of the selected item
     * @return a string formatted as an indented list
     */
    public String getIndentedList(int indent, int selection) {
        return getList(indent, x -> "", selection);
    }

    public String getIndentedList(int selectionIndex) {
        return getIndentedList(1, selectionIndex);
    }

    public String getIndentedList() {
        return getIndentedList(-1);
    }

    /**
     * Returns a String with a formatted list. in the form: <code> {@code
     *      item1 
     *      item2 
     *      item3 <--
     * }<code> Where <-- denotes the item given by the selection index.
     * 
     * Each item may be preceded with a string which is given from the indexBulletFunction. The
     * function is passed an integer which allows for formatting said string based on index.
     * 
     * @param indent              the indentation amount
     * @param indexBulletFunction a function which takes the item integer and returns the desired
     *                            string preceding each list item
     * @param selectionIndex      the index of the selected item
     * @return a string formatted as a list
     */
    public String getList(int indent, IntFunction<String> indexBulletFunction, int selectionIndex) {
        String output = "";

        for (int i = 0; i < list.size(); i++) {
            String bullet = indexBulletFunction.apply(i);
            String itemString = list.get(i).toString();
            boolean isSelected = selectionIndex == i;
            output += new ListItem(itemString, indent, bullet, isSelected);
        }

        return output;
    }

    /**
     * A helper class used to output inner list Strings.
     */
    private class ListItem {
        private int indent = 0;
        private String listItemString = "";
        private final String text;
        private boolean isSelected = false;

        public ListItem(String text, int indent, String listItemString, boolean isSelected) {
            this.text = text;
            this.indent = indent;
            this.listItemString = listItemString;
            this.isSelected = isSelected;
        }

        @Override
        public String toString() {
            String tabString = "";
            String selectedString = isSelected ? SELECTED_STRING : "";
            for (int i = 0; i < indent; i++) {
                tabString += INDENT_STRING;
            }
            return tabString + listItemString + text + selectedString + "\n";
        }
    }

}
