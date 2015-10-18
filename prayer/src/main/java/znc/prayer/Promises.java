package znc.prayer;

import java.util.ArrayList;

/**
 * Created by isaac on 10/16/15.
 */
public class Promises {
    public static class Promise {
        public final String title;
        public final String verse;
        public final String prayer;

        Promise(String title, String verse, String prayer) {
            this.title = title;
            this.verse = verse;
            this.prayer = prayer;
        }
    }

    public static final ArrayList<Promise> PROMISES = new ArrayList<Promise>() {
        {
            add(new Promise("God's Promise of Hope", "This is a test verse that I'm adding\n\nExodus 4:14", "This is a test prayer"));
        }
    };
}
