package commandline.view;

import java.util.Collection;

public class ListUtility {

    final public static String INDENT_STRING = "    ";

    Collection<?> targetObject;

    ListUtility(Collection<?> targetObject) {
        this.targetObject = targetObject;
    }

    // getEnumeratedList

    public String getEnumeratedList(int indent) {
        String output = "";
        int count = 1;
        for (Object obj : targetObject) {
            output += new ListItem(obj.toString(), indent, Integer.toString(count) + ": ");
            count++;
        }
        return output;
    }

    public String getEnumeratedList() {
        return getEnumeratedList(1);
    }

    // getBulletList

    public String getBulletList(int indent, String bullet) {
        String output = "";
        for (Object obj : targetObject) {
            output += new ListItem(obj.toString(), indent, bullet);
        }
        return output;
    }

    public String getBulletList() {
        return getBulletList(1, "> ");
    }

    // getIndentedList

    public String getIndentedList(int indent) {
        return getBulletList(indent, "");
    }

    public String getIndentedList() {
        return getIndentedList(1);
    }

    /**
     * ListItem
     * 
     * A helper class used to output inner list classes.
     */
    private class ListItem {

        String string;
        int indent;
        String bullet = "";

        public ListItem(String string, int indent, String bullet) {
            this.string = string;
            this.indent = indent;
            this.bullet = bullet;
        }

        @Override
        public String toString() {
            String tabString = "";
            for (int i = 0; i < indent; i++) {
                tabString += INDENT_STRING;
            }
            return tabString + bullet + string + "\n";
        }
    }

}
