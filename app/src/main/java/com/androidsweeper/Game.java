package com.androidsweeper;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.Random;

public class Game {
	private static Game instance;
	private GameActivity activity;
	private Tile[][] tileMatrix;

	private Timer timer;

	private boolean firstMove;
	private boolean over;

	private int minesLeft;

	private Game (GameActivity activity) {
		this.activity = activity;
		tileMatrix = new Tile[9][9];
		for (int x = 0 ; x < 9 ; x++) {
			for (int y = 0 ; y < 9 ; y++) {
				tileMatrix[x][y] = new Tile (x, y, activity);
			}
		}

		timer = new Timer (activity.timerValue);
		firstMove = true;
		SharedPreferences prefs = activity.getPreferences (Context.MODE_PRIVATE);
		activity.bestValue.setText (prefs.getString ("bestTime", "0:00"));

		setMinesLeft (10);
	}

	public static void initializeGame (GameActivity activity) {
		instance = new Game (activity);
	}

	public static Game getInstance () {
		return instance;
	}

	public Tile[][] getTileMatrix () {
		return tileMatrix;
	}

	public GameActivity getActivity () {
		return activity;
	}

	public boolean isOver() {
		return over;
	}

	public boolean isFirstMove() {
		return firstMove;
	}

	public int getMinesLeft() {
		return minesLeft;
	}

	public void setMinesLeft(int minesLeft) {
		this.minesLeft = minesLeft;
		activity.androidLeft.setText (activity.MINES_LEFT_SUFFIX + minesLeft);
	}

	private void setFirstMove(boolean firstMove) {
		this.firstMove = firstMove;
	}

	public void gameOver (boolean hasWon) {
		timer.stopTimer ();

		for (int x = 0 ; x < 9 ; x++) {
			for (int y = 0 ; y < 9 ; y++) {
				Tile tile = tileMatrix[x][y];
				tile.getImage ().setOnClickListener (null);
				tile.getImage ().setOnLongClickListener (null);
				if (tile.hasMine () && !hasWon) {
					tile.setState (State.ANDROID);
				}
			}
		}

		if (hasWon) {
			Toast.makeText (activity, "You win!", Toast.LENGTH_LONG).show ();

			SharedPreferences prefs = activity.getPreferences (Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit ();
			Timer bestTime = new Timer (activity.bestValue, prefs.getString ("bestTime", "0:00"));

			if (bestTime.compareTo (timer) > 0 || bestTime.toString ().equals ("0:00")) {
				editor.putString ("bestTime", timer.toString ()).apply ();
				activity.bestValue.setText (timer.toString ());
			}
		} else {
			Toast.makeText (activity, "You lose!", Toast.LENGTH_LONG).show ();
		}
		over = true;
	}

	public void onFirstMove (int moveX, int moveY) {
		setFirstMove (false);
		Random r = new Random ();

		for (int i = 0 ; i < 10 ; i++) {
			int x;
			int y;
			do {
				x = r.nextInt (9);
				y = r.nextInt (9);
			} while (tileMatrix[x][y].hasMine () ||
					(moveX == x && moveY == y));

			tileMatrix[x][y].setMine ();
		}

		timer.startTimer ();
	}

	public Timer getTimer() {
		return timer;
	}
}
