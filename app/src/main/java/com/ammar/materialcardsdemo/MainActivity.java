package com.ammar.materialcardsdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.graphics.Outline;
import android.widget.TextView;
import android.view.ViewAnimationUtils;

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

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            final FrameLayout frameLayout = (FrameLayout)rootView;
            RecyclerView mRecyclerView = (RecyclerView) frameLayout.findViewById(R.id.recyclerView);
            final TextView hiddenTextView = (TextView) frameLayout.findViewById(R.id.hiddenFloatText);

            ImageButton fab = (ImageButton) frameLayout.findViewById(R.id.fab);
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
                        view.setImageResource(R.drawable.ic_action_accept);

                        // get the center for the clipping circle
                        //int cx = (hiddenTextView .getLeft() + hiddenTextView .getRight()) / 2;
                        //int cy = (hiddenTextView .getTop() + hiddenTextView .getBottom()) / 2;

                        //Log.d("TextView", cx + " " + cy);

                        // get the final radius for the clipping circle
                        int finalRadius = (int) Math.sqrt((hiddenTextView.getWidth() * hiddenTextView.getWidth()) + (hiddenTextView.getHeight() * hiddenTextView.getHeight()));

                        Log.d("TextView", "Radius: " + finalRadius);

                        // create and start the animator for this view
                        // (the start radius is zero)
                        hiddenTextView.setVisibility(View.VISIBLE);
                        ValueAnimator anim = ViewAnimationUtils.createCircularReveal(hiddenTextView , 0, 0, 0, finalRadius);
                        anim.start();
                    }
                    else {
                        view.setSelected(false);
                        view.setImageResource(R.drawable.ic_action_new);

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

            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(this.getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);

            String[] myDataset = {"Testing", "Testing"};

            // specify an adapter (see also next example)
            mAdapter = new MyAdapter(myDataset);
            mRecyclerView.setAdapter(mAdapter);
            return rootView;
        }
    }
}
