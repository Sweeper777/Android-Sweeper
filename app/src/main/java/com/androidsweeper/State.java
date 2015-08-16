package com.androidsweeper;

public enum State{
	COVERED,
	SELECTED,
	ZERO,
	ONE,
	TWO,
	THREE,
	FOUR,
	FIVE,
	SIX,
	SEVEN,
	EIGHT,
	FLAG,
	ANDROID;

	public static int getCorrespondingResources (State state) {
		switch (state) {
			case COVERED:
				return R.drawable.tilecovered;
			case SELECTED:
				return R.drawable.tileselected;
			case ZERO:
				return R.drawable.tile0;
			case ONE:
				return R.drawable.tile1;
			case TWO:
				return R.drawable.tile2;
			case THREE:
				return R.drawable.tile3;
			case FOUR:
				return R.drawable.tile4;
			case FIVE:
				return R.drawable.tile5;
			case SIX:
				return R.drawable.tile6;
			case SEVEN:
				return R.drawable.tile7;
			case EIGHT:
				return R.drawable.tile8;
			case FLAG:
				return R.drawable.tileflag;
			case ANDROID:
				return R.drawable.tileandroid;
			default:
				return Integer.MIN_VALUE;
		}
	}
}
