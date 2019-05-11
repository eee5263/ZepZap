package com.zepzap.zepzap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameMainActivity extends AppCompatActivity {
    private TextView topScoreText;
    private TextView gameRuleText;
    private String token;
    private Button startGameBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);
        gameRuleText = (TextView) findViewById(R.id.gameRuleText);
        topScoreText = (TextView) findViewById(R.id.topScoreText);
        token = new Token().getToken();

        startGameBtn = (Button) findViewById(R.id.startGameBtn);
        startGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameMainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        //receive new top score if available
    }


}
