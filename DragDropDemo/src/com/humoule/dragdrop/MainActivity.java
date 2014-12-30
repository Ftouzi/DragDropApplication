package com.humoule.dragdrop;

import android.app.Activity;
import android.content.ClipData;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnDragListener, OnTouchListener
{

	private static final String TAG = MainActivity.class.getSimpleName();

	private final String IMAGE_DETERMINED_TAG = "Dterminde Face";
	private final String IMAGE_LAUGHING_TAG = "Laughing Face";

	private ImageView dragViewDetermined;
	private ImageView dragViewLaghing;
	private ImageView dropView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dragViewDetermined = (ImageView) findViewById(R.id.determinedImage);
		dragViewLaghing = (ImageView) findViewById(R.id.laghingImage);
		dropView = (ImageView) findViewById(R.id.dropImage);

		dragViewDetermined.setTag(IMAGE_DETERMINED_TAG);
		dragViewLaghing.setTag(IMAGE_LAUGHING_TAG);

		dragViewDetermined.setOnTouchListener(this);
		dragViewLaghing.setOnTouchListener(this);

		// Set OnDragListener on the view where to drop the dragged image
		dropView.setOnDragListener(this);

	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		String tag = v.getTag().toString();

		// Instantiates the drag shadow builder
		View.DragShadowBuilder mShadow;

		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				ClipData data = ClipData.newPlainText("some label", tag);

				if (tag.equals(IMAGE_DETERMINED_TAG))
				{
					dropView.setImageResource(R.drawable.determined_face);
					mShadow = new View.DragShadowBuilder(dragViewDetermined);
				}
				else
				{
					dropView.setImageResource(R.drawable.laughing_face);
					mShadow = new View.DragShadowBuilder(dragViewLaghing);
				}

				v.startDrag(data, mShadow, null, 0);
			break;

			case MotionEvent.ACTION_UP:
				v.performClick();

			break;

			default:
			break;
		}
		return false;
	}

	@Override
	public boolean onDrag(View v, DragEvent event)
	{
		Log.d(TAG, "onDrag");

		// Store the action type for the incoming event
		final int action = event.getAction();

		// Handles each of the expected events
		switch (action)
		{
			case DragEvent.ACTION_DRAG_STARTED:

				// In order to inform user that drag has started, we apply yellow tint to view
				((ImageView) v).setColorFilter(Color.YELLOW);

				// Invalidate the view to force a redraw in the new tint
				v.invalidate();

				// Returns true to indicate that the View can accept the
				// dragged data.
				return true;

			case DragEvent.ACTION_DRAG_ENTERED:

				// Apply a gray tint to the View
				((ImageView) v).setColorFilter(Color.LTGRAY);

				// Invalidate the view to force a redraw in the new tint
				v.invalidate();

				return true;

			case DragEvent.ACTION_DRAG_LOCATION:

				// Ignore the event
				return true;

			case DragEvent.ACTION_DRAG_EXITED:

				// Re-sets the color tint to yellow
				((ImageView) v).setColorFilter(Color.YELLOW);

				// Invalidate the view to force a redraw in the new tint
				v.invalidate();

				return true;

			case DragEvent.ACTION_DROP:

				// Gets the item containing the dragged data
				ClipData dragData = event.getClipData();

				// Gets the text data from the item.
				final String tag = dragData.getItemAt(0).getText().toString();

				// Displays a message containing the dragged data.
				Toast.makeText(MainActivity.this, "The dragged image is " + tag, Toast.LENGTH_SHORT).show();

				// Turns off any color tints
				((ImageView) v).clearColorFilter();

				// Invalidates the view to force a redraw
				v.invalidate();

				return true;

			case DragEvent.ACTION_DRAG_ENDED:

				// Turns off any color tinting
				((ImageView) v).clearColorFilter();

				// Invalidates the view to force a redraw
				v.invalidate();

				// Check for result
				if (event.getResult())
				{
					Toast.makeText(MainActivity.this, "Bingo !", Toast.LENGTH_SHORT).show();

				}
				else
				{
					Toast.makeText(MainActivity.this, "Oups, try again !", Toast.LENGTH_SHORT).show();
					dropView.setImageBitmap(null);
				}

				return true;

			default:
			break;
		}

		return false;
	}

}
