package com.android.bakingapp.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.bakingapp.R;
import com.android.bakingapp.model.objects.Recipe;
import com.android.bakingapp.model.objects.Step;
import com.android.bakingapp.ui.fragment.DetailFragment;
import com.android.bakingapp.ui.fragment.StepFragment;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import static com.android.bakingapp.ui.fragment.DetailFragment.setData;
import static com.android.bakingapp.utils.Constants.CURRENT_INDEX;
import static com.android.bakingapp.utils.Constants.RECIPE;
import static com.android.bakingapp.utils.Constants.STEP;

public class DetailActivity extends BaseActivity implements StepFragment.OnSelectedListener {

    private Step step;
    private Recipe recipe;
    private Integer currentIndex;
    private SimpleExoPlayer simpleExoPlayer;
    private PlayerView playerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState != null) {
            step = savedInstanceState.getParcelable(STEP);
            recipe = savedInstanceState.getParcelable(RECIPE);
            currentIndex = savedInstanceState.getInt(CURRENT_INDEX);
        }

        if (getIntent() != null) {
            step = getIntent().getParcelableExtra(STEP);
            recipe = getIntent().getParcelableExtra(RECIPE);
        }

        if (getResources().getBoolean(R.bool.isTablet)) {
            if (step == null) {
                StepFragment stepFragment;
                DetailFragment detailFragment;

                FragmentManager fragmentManager = getSupportFragmentManager();

                Bundle bundle = new Bundle();
                bundle.putParcelable(RECIPE, recipe);

                stepFragment = (StepFragment) fragmentManager.findFragmentByTag("StepFragment");
                if (stepFragment == null) {
                    stepFragment = new StepFragment();
                    stepFragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .add(R.id.step_fragment, stepFragment, "StepFragment")
                            .commit();
                }
                detailFragment = (DetailFragment) fragmentManager.findFragmentByTag("DetailFragment");
                if (detailFragment == null) {
                    detailFragment = new DetailFragment();
                    detailFragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .add(R.id.detail_fragment, detailFragment, "DetailFragment")
                            .commit();
                }
            }
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            DetailFragment detailFragment;

            detailFragment = (DetailFragment) fragmentManager.findFragmentByTag("DetailFragment");
            if (detailFragment == null) {
                detailFragment = new DetailFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable(STEP, step);
                bundle.putParcelable(RECIPE, recipe);
                detailFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .add(R.id.detail_fragment, detailFragment, "DetailFragment")
                        .commit();
            }
        }

        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory();
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeVideoPlayer(Step step) {
        if (simpleExoPlayer == null) {
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory();
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        }
        playerView.setPlayer(simpleExoPlayer);
        simpleExoPlayer.setPlayWhenReady(true);

        Uri videoUri = Uri.parse(step.getVideoURL());

        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(
                        this,
                        Util.getUserAgent(this, "BakingApp")
                );
        MediaSource videoSource =
                new ExtractorMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(videoUri);

        simpleExoPlayer.prepare(videoSource);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof StepFragment) {
            StepFragment stepFragment = (StepFragment) fragment;
            stepFragment.setOnSelectedListener(this);
        } else if (fragment instanceof DetailFragment) {
            DetailFragment detailFragment = (DetailFragment) fragment;
            detailFragment.setOnSelectedListener(this);
        }
    }

    @Override
    public void onItemSelected(Step step, DetailFragment detailFragment) {
        playerView = this.findViewById(R.id.recipe_video_player);

        initializeVideoPlayer(step);

        TextView descriptionTextView = this.findViewById(R.id.recipe_description_text_view);
        descriptionTextView.setText(String.format("%s", step.getDescription()));

        setData(step.getId(), step);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (step != null) {
            outState.putParcelable(STEP, step);
        }

        if (currentIndex != null) {
            outState.putInt(CURRENT_INDEX, currentIndex);
        }

        if (recipe != null) {
            outState.putParcelable(RECIPE, recipe);
        }
    }
}
