package com.android.bakingapp.ui.fragment;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.bakingapp.R;
import com.android.bakingapp.databinding.FragmentRecipeDetailBinding;
import com.android.bakingapp.model.objects.Recipe;
import com.android.bakingapp.model.objects.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import static com.android.bakingapp.utils.Constants.CURRENT_INDEX;
import static com.android.bakingapp.utils.Constants.RECIPE;
import static com.android.bakingapp.utils.Constants.STEP;

public class DetailFragment extends Fragment {

    private static Step step;
    private static Integer currentIndex = 0;

    private Recipe recipe;
    private SimpleExoPlayer simpleExoPlayer;
    private Uri videoUri;

    private FragmentRecipeDetailBinding binding;

    private StepFragment.OnSelectedListener callback;

    public void setOnSelectedListener(StepFragment.OnSelectedListener callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            step = savedInstanceState.getParcelable(STEP);
            recipe = savedInstanceState.getParcelable(RECIPE);
            currentIndex = savedInstanceState.getInt(CURRENT_INDEX);
        } else {
            currentIndex = 0;
        }

        if (getArguments() != null) {
            step = getArguments().getParcelable(STEP);
            recipe = getArguments().getParcelable(RECIPE);
        }

        if (step != null) {
            currentIndex = step.getId();
        }

        if (step == null) {
            if (recipe != null) {
                step = recipe.getSteps().get(0);
            }
        }

        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory();
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_detail, container, false);
        View view = binding.getRoot();

        initializeVideoPlayer();

        if (!getResources().getBoolean(R.bool.isTablet)) {
            if (getActivity() != null) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    hideSystemUI(getActivity());

                    binding.recipeVideoPlayer.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
                    binding.recipeDescriptionCard.setVisibility(View.GONE);
                    binding.recipeDetailLinearLayout.setVisibility(View.GONE);
                }
            }
        }

        if (step != null) {
            binding.recipeDescriptionTextView.setText(String.format("%s", step.getDescription()));
        } else {
            try {
                binding.recipeDescriptionTextView.setText(String.format("%s", recipe.getSteps().get(0).getDescription()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        binding.backButton.setOnClickListener(view1 -> {
            if (getActivity() != null) {
                if (currentIndex == 0) {
                    getActivity().onBackPressed();
                } else {
                    if (currentIndex < recipe.getSteps().size()) {
                        callback.onItemSelected(recipe.getSteps().get(step.getId() - 1), this);
                    }
                }
            }
        });

        binding.forwardButton.setOnClickListener(view2 -> {
            if (getActivity() != null) {
                if (currentIndex == recipe.getSteps().size() - 1) {
                    getActivity().onBackPressed();
                } else {
                    if (currentIndex < recipe.getSteps().size()) {
                        callback.onItemSelected(recipe.getSteps().get(step.getId() + 1), this);
                    }
                }
            }
        });

        return view;
    }

    private void initializeVideoPlayer() {
        binding.recipeVideoPlayer.setPlayer(simpleExoPlayer);
        simpleExoPlayer.setPlayWhenReady(true);

        if (step != null) {
            videoUri = Uri.parse(step.getVideoURL());
        } else {
            try {
                videoUri = Uri.parse(recipe.getSteps().get(0).getVideoURL());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (getContext() != null) {
            DataSource.Factory dataSourceFactory =
                    new DefaultDataSourceFactory(
                            getContext(),
                            Util.getUserAgent(getContext(), "BakingApp")
                    );
            MediaSource videoSource =
                    new ExtractorMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(videoUri);

            simpleExoPlayer.prepare(videoSource);
        }
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (!getResources().getBoolean(R.bool.isTablet)) {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (getActivity() != null) {
                    hideSystemUI(getActivity());
                }

                binding.recipeVideoPlayer.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
                binding.recipeDescriptionCard.setVisibility(View.GONE);
                binding.recipeDetailLinearLayout.setVisibility(View.GONE);
            } else {
                if (getActivity() != null) {
                    showSystemUI(getActivity());
                }
                binding.recipeVideoPlayer.getLayoutParams().height = 1200;
                binding.recipeVideoPlayer.setVisibility(View.VISIBLE);
                binding.recipeDescriptionCard.setVisibility(View.VISIBLE);
                binding.recipeDetailLinearLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void hideSystemUI(FragmentActivity fragmentActivity) {
        View decorView = fragmentActivity.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the nav bar and status bar
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    private void showSystemUI(FragmentActivity fragmentActivity) {
        View decorView = fragmentActivity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    }

    public static void setData(Integer index, Step currentStep) {
        currentIndex = index;
        step = currentStep;
    }
}
