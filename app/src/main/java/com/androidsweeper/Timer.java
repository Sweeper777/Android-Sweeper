package com.androidsweeper;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.TextView;

public class Timer implements Comparable<Timer> {
	private Handler handler;
	private boolean paused;
	private TextView text;

	private int minutes;
	private int seconds;

	private final Runnable timerTask = new Runnable () {
		@Override
		public void run() {
			if (!paused) {
				seconds++;
				if (seconds >= 60) {
					seconds = 0;
					minutes++;
				}

				text.setText (Timer.this.toString ());
				Timer.this.handler.postDelayed (this, 1000);
			}
		}
	};

	@Override
	public String toString () {
		if (Integer.toString (seconds).length () == 1) {
			return minutes + ":0" + seconds;
		} else {
			return minutes + ":" + seconds;
		}
	}

	public void startTimer () {
		paused = false;
		handler.postDelayed (timerTask, 1000);
	}

	public void stopTimer () {
		paused = true;
	}

	public void resetTimer () {
		stopTimer ();
		minutes = 0;
		seconds = 0;
		text.setText (toString ());
	}

	public Timer (TextView text) {
		this.text = text;
		handler = new Handler ();
	}

	public Timer (TextView text, String parseString) {
		this (text);
		String[] splitString = parseString.split (":");
		minutes = Integer.parseInt (splitString[0]);
		seconds = Integer.parseInt (splitString[1]);
	}

	@Override
	public int compareTo(@NonNull Timer another) {
		int numberOfSeconds = seconds + minutes * 60;
		int anotherNumberOfSeconds = another.seconds + another.minutes * 60;

		return ((Integer)numberOfSeconds).compareTo (anotherNumberOfSeconds);
	}
}
