package hr.fer.takelab.robocijacije;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuActivity extends AppCompatActivity {

    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.new_game)
    Button newGame;
    @BindView(R.id.new_game_card)
    CardView newGameCard;
    @BindView(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_menu);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);

//        newGameCard.setVisibility(View.INVISIBLE);
        YoYo.with(Techniques.FadeIn).duration(1500).playOn(newGameCard);
        YoYo.with(Techniques.FadeInDown).duration(1500).playOn(logo);
        YoYo.with(Techniques.FadeInDown).duration(1500).playOn(title);
    }

    @OnClick(R.id.new_game)
    public void openMath() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
