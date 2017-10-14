package pharaoh.com.bakingapp.ui.Fragments;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pharaoh.com.bakingapp.R;
import pharaoh.com.bakingapp.data.Models.Step;
import pharaoh.com.bakingapp.ui.FragmentOneListener;

import static android.view.View.GONE;

/**
 * Created by MahmoudAhmed on 9/29/2017.
 */

public class StepDetailsFragment extends android.app.Fragment implements View.OnClickListener {

    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.currentStep)
    TextView current;
    @BindView(R.id.next_button)
    FloatingActionButton next;
    @BindView(R.id.back_button)
    FloatingActionButton back;
    @BindView(R.id.playerView)
    SimpleExoPlayerView playerView;
    @BindView(R.id.empty)
    TextView empty;
    @BindView(R.id.root)
    LinearLayout layout;
    @BindView(R.id.f1)
    FrameLayout f1;
    @BindView(R.id.f2)
    FrameLayout f2;
    @BindView(R.id.image)
    ImageView imageView;

    FragmentOneListener listener;
    ArrayList<Step> steps;
    int currentIndex;
    SimpleExoPlayer player;
    Boolean tablet;

    int width = 0;
    int height = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.details_fragment, container, false);
        ButterKnife.bind(this, root);
        Bundle bundle;
        if(savedInstanceState == null)
        {
            bundle= getArguments();
            steps = bundle.getParcelableArrayList("steps");
            currentIndex = bundle.getInt("current");
            tablet = bundle.getBoolean("tablet");
        }
        else
        {
            steps = savedInstanceState.getParcelableArrayList("steps");
            currentIndex = savedInstanceState.getInt("index");
            tablet = savedInstanceState.getBoolean("tablet");
        }
        show();



        back.setOnClickListener(this);
        next.setOnClickListener(this);

        return root;
    }

    public void show() {
        if (currentIndex <= 0) {
            back.setVisibility(GONE);
        } else {
            back.setVisibility(View.VISIBLE);
        }
        if (listener != null) {
            listener.setCurrent(currentIndex);
        }
        if (currentIndex >= steps.size() - 1) {
            next.setVisibility(GONE);
        } else {
            next.setVisibility(View.VISIBLE);
        }

        releasePlayer();
        if (steps.get(currentIndex).getVideoURL().isEmpty() && steps.get(currentIndex).getThumbnailURL().isEmpty()) {
            playerView.setVisibility(GONE);
            imageView.setVisibility(GONE);
            empty.setVisibility(View.VISIBLE);
        } else if (!steps.get(currentIndex).getVideoURL().isEmpty()) {
            String videoUrl = steps.get(currentIndex).getVideoURL();
            empty.setVisibility(View.GONE);
            imageView.setVisibility(GONE);
            playerView.setVisibility(View.VISIBLE);
            initializePlayer(Uri.parse(videoUrl));

        } else{
            String imageUrl = steps.get(currentIndex).getVideoURL();
            empty.setVisibility(View.GONE);
            playerView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);

            Glide.with(getActivity())
                    .load(imageUrl)
                    .placeholder(R.drawable.details_not_found)
                    .into(imageView);
        }

        hideSystemUi();
        description.setText(steps.get(currentIndex).getDescription());
        current.setText((currentIndex + 1) + "/" + steps.size());
    }

    public void initializePlayer(Uri uri) {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getActivity()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            playerView.setPlayer(player);
            player.setPlayWhenReady(true);
            MediaSource mediaSource = buildMediaSource(uri);
            player.prepare(mediaSource, true, false);
        }
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (tablet)
            return;

        hideSystemUi();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            f1.setLayoutParams(new LinearLayout.LayoutParams(width, height));
            f2.setLayoutParams(new LinearLayout.LayoutParams(width, height));

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            f1.setLayoutParams(new LinearLayout.LayoutParams(height, width / 2));
            f2.setLayoutParams(new LinearLayout.LayoutParams(height, width / 2));
        }

    }

    public void setFragmentListener(FragmentOneListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.back_button) {
            if(currentIndex ==0)
                return;
            currentIndex--;
            show();
        } else if (id == R.id.next_button) {
            if(currentIndex==steps.size()-1)
                return;
            currentIndex++;
            show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (tablet)
            return;

        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);


                int track = getResources().getConfiguration().orientation;
                if (track != 1) {
                    width = layout.getMeasuredWidth();
                    height = layout.getMeasuredHeight();
                    f1.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                    f2.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                } else {
                    height = layout.getMeasuredWidth();
                    width = layout.getMeasuredHeight();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        if(tablet)
        {
            return;
        }
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        empty.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        imageView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("index",currentIndex);
        outState.putParcelableArrayList("steps",steps);
        outState.putBoolean("tablet",tablet);
    }


}
