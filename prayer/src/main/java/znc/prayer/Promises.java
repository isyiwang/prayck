package znc.prayer;

import java.util.ArrayList;

/**
 * Created by isaac on 10/16/15.
 */
public class Promises {
    public static class Promise {
        public String verse;
        public String prayer;

        Promise(String verse, String prayer) {
            this.verse = verse;
            this.prayer = prayer;
        }
    }

    public static final ArrayList<Promise> PROMISES = new ArrayList<Promise>() {
        {
            add(new Promise("This is a test verse that I'm adding\n\nExodus 4:14", "This is a test prayer"));
        }
    };
}
