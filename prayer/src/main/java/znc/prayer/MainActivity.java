package znc.prayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private KenBurnsView mBackgroundView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBackgroundView = (KenBurnsView) findViewById(R.id.background);
        Picasso.with(this).load(R.drawable.forest).fit().centerCrop().into(mBackgroundView);
    }
}
