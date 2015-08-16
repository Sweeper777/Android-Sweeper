package com.androidsweeper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
	AbsoluteLayout gameFrame;
	TextView timerValue;
	TextView bestValue;
	TextView androidLeft;

	final String MINES_LEFT_SUFFIX = "Androids Left:";

	public float getScale () {
		return getResources().getDisplayMetrics().density;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		setContentView (R.layout.activity_game);
		gameFrame = (AbsoluteLayout) findViewById (R.id.game_frame);
		timerValue = (TextView)findViewById (R.id.timer_value);
		bestValue = (TextView) findViewById (R.id.best_value);
		androidLeft = (TextView) findViewById (R.id.mines_left_label);

		Game.initializeGame (this);
	}

	public void restartClick (View view) {
		Game.getInstance ().getTimer ().resetTimer ();
		gameFrame.removeAllViews ();
		Game.initializeGame (this);
	}
}
