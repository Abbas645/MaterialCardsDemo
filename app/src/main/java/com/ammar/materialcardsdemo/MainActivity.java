package com.ammar.materialcardsdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Outline;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private RecyclerView mRecyclerView;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView.LayoutManager mLayoutManager;
        private String[] myDataset;
        private ImageButton fab;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            final FrameLayout frameLayout = (FrameLayout)rootView;

            RecyclerView mRecyclerView = (RecyclerView) frameLayout.findViewById(R.id.recyclerView);
            final TextView hiddenTextView = (TextView) frameLayout.findViewById(R.id.hiddenFloatText);

            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(this.getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            myDataset = new String[]{"Testing", "Testing"};
            mAdapter = new MyAdapter(myDataset);
            mRecyclerView.setAdapter(mAdapter);

            SwipeDismissRecyclerViewTouchListener touchListener =
                    new SwipeDismissRecyclerViewTouchListener(
                            mRecyclerView,
                            new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
                                @Override
                                public boolean canDismiss(int position) {
                                    return true;
                                }

                                @Override
                                public void onDismiss(RecyclerView recyclerView, int[] reverseSortedPositions) {

                                    for (int position : reverseSortedPositions) {
                                        recyclerView.removeViewAt(position);
                                    }
                                }
                            });

            mRecyclerView.setOnTouchListener(touchListener);
            // Setting this scroll listener is required to ensure that during ListView scrolling,
            // we don't look for swipes.
            mRecyclerView.setOnScrollListener(touchListener.makeScrollListener());

            fab = (ImageButton) frameLayout.findViewById(R.id.fab);
            Outline mOutlineCircle = new Outline();
            int shapeSize = getResources().getDimensionPixelSize(R.dimen.priamry_fab_height);
            mOutlineCircle.setRoundRect(0, 0, shapeSize, shapeSize, shapeSize / 2);
            fab.setOutline(mOutlineCircle);
            fab.setClipToOutline(true);

            fab.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    ImageButton view = (ImageButton) v;

                    if (!view.isSelected()) {

                        view.setSelected(true);
                        view.setImageResource(R.drawable.ic_fab_accept);

                        int cx = 0;
                        int cy = 0;
                        int finalRadius = 0;

                        if (myDataset.length == 0)
                        {
                            //FAB is in the center of screen so moving to bottom right corner
                            Log.d("hiddentTextView", hiddenTextView.getX() + " " + hiddenTextView.getY() + " " + hiddenTextView.getTop() + " " + hiddenTextView.getBottom());
                            cx = hiddenTextView.getLeft() + hiddenTextView.getWidth()/2;
                            cy = hiddenTextView.getTop() + hiddenTextView.getHeight() / 2;

                            Log.d("Values", cx + " " + cy);

                            int x = rootView.getWidth()/2 - fab.getWidth()/2 - Utils.dpToPx(getActivity(), 16);
                            int y = rootView.getHeight()/2 - fab.getHeight()/2 - Utils.dpToPx(getActivity(), 16);

                            ObjectAnimator translateX= ObjectAnimator.ofFloat(fab, "translationX", 0, x);
                            ObjectAnimator translateY= ObjectAnimator.ofFloat(fab, "translationY", 0, y);
                            AnimatorSet animatorSet = new AnimatorSet();
                            animatorSet.playTogether(translateX, translateY);
                            animatorSet.start();

                            finalRadius = (hiddenTextView.getWidth()>hiddenTextView.getHeight())?hiddenTextView.getWidth():hiddenTextView.getHeight();
                        }
                        else
                        {
                            finalRadius = (int) Math.sqrt((hiddenTextView.getWidth() * hiddenTextView.getWidth()) + (hiddenTextView.getHeight() * hiddenTextView.getHeight()));
                        }

                        // get the center for the clipping circle
                        //int cx = (hiddenTextView .getLeft() + hiddenTextView .getRight()) / 2;
                        //int cy = (hiddenTextView .getTop() + hiddenTextView .getBottom()) / 2;

                        hiddenTextView.setVisibility(View.VISIBLE);
                        ValueAnimator anim = ViewAnimationUtils.createCircularReveal(hiddenTextView , cx, cy, 0, finalRadius);
                        anim.start();
                    }
                    else {
                        view.setSelected(false);
                        view.setImageResource(R.drawable.ic_fab_new);

                        // get the center for the clipping circle
                        int cx = hiddenTextView.getRight();
                        int cy = hiddenTextView.getBottom();

                        // get the initial radius for the clipping circle
                        int initialRadius = (int) Math.sqrt((hiddenTextView.getWidth() * hiddenTextView.getWidth()) + (hiddenTextView.getHeight() * hiddenTextView.getHeight()));

                        // create the animation (the final radius is zero)
                        ValueAnimator anim =
                                ViewAnimationUtils.createCircularReveal(hiddenTextView, 0, 0, initialRadius, 0);

                        // make the view invisible when the animation is done
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                hiddenTextView.setVisibility(View.INVISIBLE);
                            }
                        });

                        // start the animation
                        anim.start();
                    }
                }
            });

            ViewTreeObserver vto = rootView.getViewTreeObserver();

            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) fab.getLayoutParams();

                    if (myDataset.length == 0)
                    {
                        //Moving FAB to center
                        int x = -(rootView.getWidth() / 2 - fab.getWidth() / 2);
                        int y = -(rootView.getHeight() / 2 - fab.getHeight() / 2);
                        layoutParams.setMargins(-x, -y, -x, -y);
                        fab.setLayoutParams(layoutParams);
                    }
                    else
                    {
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                        layoutParams.setMargins(0, 0, Utils.dpToPx(getActivity(), 16), Utils.dpToPx(getActivity(), 16));
                        fab.setLayoutParams(layoutParams);
                    }

                    ViewTreeObserver obs = rootView.getViewTreeObserver();
                    obs.removeOnGlobalLayoutListener(this);
                }
            });

            return rootView;
        }
    }
}
