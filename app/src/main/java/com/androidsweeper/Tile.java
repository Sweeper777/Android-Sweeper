package com.androidsweeper;

import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;

public class Tile {
	private State state;
	private boolean mine;
	private ImageView image;

	private int x;
	private int y;

	private static boolean checkWin () {

		if (Game.getInstance ().isOver ())
			return false;

		for (int x = 0 ; x < 9 ; x++) {
			for (int y = 0 ; y < 9 ; y++) {
				Tile tile = Game.getInstance ().getTileMatrix ()[x][y];
				if (tile.getState () == State.FLAG && !tile.hasMine ()) {
					return false;
				}

				if (!tile.hasMine () &&
						(tile.getState () == State.COVERED ||
								tile.getState () == State.SELECTED)) {
					return false;
				}
			}
		}
		return true;
	}

	private void uncover() {
		if (getState () != State.COVERED) {
			return;
		}

		if (hasMine ()) {
			Game.getInstance ().gameOver (false);
			return;
		}

		int numberOfMines = getMinesNearby ();
		setState (State.values ()[numberOfMines + 2]);

		if (numberOfMines == 0) {
			int[] xOffSet = {-1, 0, 1, -1, 1, -1, 0, 1};
			int[] yOffSet = {-1, -1, -1, 0, 0, 1, 1, 1};

			for (int i = 0; i < 8; i++) {
				try {
					Tile tile = Game.getInstance ().getTileMatrix ()[x + xOffSet[i]][y + yOffSet[i]];
					tile.uncover ();
				} catch (IndexOutOfBoundsException e) {
					//continue;
				}
			}
		}
	}

	private int getMinesNearby() {
		int numberOfMines = 0;
		int[] xOffSet = {-1, 0, 1, -1, 1, -1, 0, 1};
		int[] yOffSet = {-1, -1, -1, 0, 0, 1, 1, 1};

		for (int i = 0; i < 8; i++) {
			try {
				Tile tile = Game.getInstance ().getTileMatrix ()[x + xOffSet[i]][y + yOffSet[i]];
				if (tile.hasMine ()) {
					numberOfMines++;
				}
			} catch (IndexOutOfBoundsException e) {
				//continue;
			}
		}

		return numberOfMines;
	}

	public void setState(State state) {
		this.state = state;
		image.setImageResource (State.getCorrespondingResources (state));
	}

	@SuppressWarnings("deprecation")
	public Tile(int x, int y, final GameActivity activity) {
		image = new ImageView (activity);
		int lengthInPx = getLengthInPixels (activity);
		AbsoluteLayout.LayoutParams params =
				new AbsoluteLayout.LayoutParams (lengthInPx,
						lengthInPx, x * lengthInPx, y * lengthInPx);

		image.setLayoutParams (params);
		this.x = x;
		this.y = y;
		activity.gameFrame.addView (image);
		View.OnClickListener imageOnClick = new View.OnClickListener () {
			@Override
			public void onClick(View v) {
				State stateCache = getState ();

				for (int x = 0; x < 9; x++) {
					for (int y = 0; y < 9; y++) {
						Tile tile = Game.getInstance ().getTileMatrix ()[x][y];
						if (tile.getState () == State.SELECTED) {
							tile.setState (State.COVERED);
						}
					}
				}

				if (stateCache == State.COVERED) {
					setState (State.SELECTED);
				} else if (stateCache == State.SELECTED) {
					if (Game.getInstance ().isFirstMove ()) {
						Game.getInstance ().onFirstMove (Tile.this.x, Tile.this.y);
					}
					uncover ();
				} else if (stateCache == State.FLAG) {
					setState (State.COVERED);
					int minesLeft = Game.getInstance ().getMinesLeft ();
					Game.getInstance ().setMinesLeft (minesLeft + 1);
				}

				if (checkWin ()) {
					Game.getInstance ().gameOver (true);
				}
			}
		};
		image.setOnClickListener (imageOnClick);
		View.OnLongClickListener imageOnHold = new View.OnLongClickListener () {
			@Override
			public boolean onLongClick(View v) {
				if (Game.getInstance ().isFirstMove ()) {
					Game.getInstance ().onFirstMove (Tile.this.x, Tile.this.y);
				}

				if (getState () == State.SELECTED) {
					setState (State.FLAG);
					int minesLeft = Game.getInstance ().getMinesLeft ();
					Game.getInstance ().setMinesLeft (minesLeft - 1);
					return true;
				}
				return false;
			}
		};
		image.setOnLongClickListener (imageOnHold);

		setState (State.COVERED);
	}

	public State getState() {
		return state;
	}

	public ImageView getImage () {return image;}

	public boolean hasMine() {
		return mine;
	}

	public void setMine () {
		mine = true;
	}

	public int getLengthInPixels (GameActivity activity) {
		return (int)(30 * activity.getScale () + 0.5F);
	}


}
