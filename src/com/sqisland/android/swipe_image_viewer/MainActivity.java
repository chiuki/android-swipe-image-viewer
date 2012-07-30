package com.sqisland.android.swipe_image_viewer;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

public class MainActivity extends Activity {
  private static final String CURRENT_POSITION = "current_position";

  private ImageSwitcher mImageSwitcher;
  private View mOverscrollLeft;
  private View mOverscrollRight;

  private GestureDetector mGestureDetector;

  private int[] mImages = new int[] {
      R.drawable.chiang_mai,
      R.drawable.himeji,
      R.drawable.petronas_twin_tower,
      R.drawable.ulm
  };
  private int mCurrentPosition = 0;

  private Animation mSlideInLeft;
  private Animation mSlideOutRight;
  private Animation mSlideInRight;
  private Animation mSlideOutLeft;
  private Animation mOverscrollLeftFadeOut;
  private Animation mOverscrollRightFadeOut;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (savedInstanceState != null) {
      mCurrentPosition = savedInstanceState.getInt(CURRENT_POSITION, 0);
    }

    // Views
    mImageSwitcher = (ImageSwitcher) findViewById(R.id.image);
    mOverscrollLeft = findViewById(R.id.overscroll_left);
    mOverscrollRight = findViewById(R.id.overscroll_right);

    // Animations
    mSlideInLeft = AnimationUtils.loadAnimation(this,
        android.R.anim.slide_in_left);
    mSlideOutRight = AnimationUtils.loadAnimation(this,
        android.R.anim.slide_out_right);
    mSlideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
    mSlideOutLeft = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
    mOverscrollLeftFadeOut = AnimationUtils
        .loadAnimation(this, R.anim.fade_out);
    mOverscrollRightFadeOut = AnimationUtils.loadAnimation(this,
        R.anim.fade_out);

    // ImageSwitcher
    mImageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
      @Override
      public View makeView() {
        ImageView view = new ImageView(MainActivity.this);
        view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        view.setLayoutParams(new ImageSwitcher.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
      }
    });
    mImageSwitcher.setImageResource(mImages[mCurrentPosition]);
    mGestureDetector = new GestureDetector(this, new SwipeListener());
    mImageSwitcher.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
      }
    });
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (outState == null) {
      return;
    }
    outState.putInt(CURRENT_POSITION, mCurrentPosition);
  }

  private void moveNextOrPrevious(int delta) {
    int nextImagePos = mCurrentPosition + delta;
    if (nextImagePos < 0) {
      mOverscrollLeft.setVisibility(View.VISIBLE);
      mOverscrollLeft.startAnimation(mOverscrollLeftFadeOut);
      return;
    }
    if (nextImagePos >= mImages.length) {
      mOverscrollRight.setVisibility(View.VISIBLE);
      mOverscrollRight.startAnimation(mOverscrollRightFadeOut);
      return;
    }

    mImageSwitcher.setInAnimation(delta > 0 ? mSlideInRight : mSlideInLeft);
    mImageSwitcher.setOutAnimation(delta > 0 ? mSlideOutLeft : mSlideOutRight);

    mCurrentPosition = nextImagePos;
    mImageSwitcher.setImageResource(mImages[mCurrentPosition]);
  }

  private class SwipeListener extends SimpleOnGestureListener {
    private static final int SWIPE_MIN_DISTANCE = 75;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
        float velocityY) {
      try {
        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
          return false;
        // right to left swipe
        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
            && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
          moveNextOrPrevious(1);
        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
            && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
          moveNextOrPrevious(-1);
        }
      } catch (Exception e) {
        // nothing
      }
      return false;
    }
  }
}