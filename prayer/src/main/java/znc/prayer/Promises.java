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
            add(new Promise("God's Promise of Hope", "This is a test verse that I'm adding\n\nExodus 4:14", "This is a test prayer\n awi wae oeawoh\naewa ;wa w;weaoi\n; aew;hweag ;ei\nhwg aew;owea ieaw\ngo;hi aweh eagwh eaw;ogi;ow\naego;iheawo;h\nioh;weaoi;awe wea aw ae;waeg ;eawio aweo;he\nio;hg;oaw;g waeoawo;i; eawo;a ih;aw;o weag ;ewaiho aweio;hawe  aew;\n jkzxjkbwa bhlaewjkwaeuhzx wa iauwljbzf i\nuhwauit bhjaet uie atbjlae itweao aelbjkeaw lba wtabewl aejkaewtl eatb"));
        }
    };
}
