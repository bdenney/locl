/*
 * Copyright 2014 Brandon Denney
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bdenney.locl.fragment;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import com.bdenney.locl.R;
import com.bdenney.locl.activity.LightboxActivity;
import com.bdenney.locl.adapter.PostAdapter;
import com.bdenney.locl.api.TumblrClient;
import com.bdenney.locl.model.Venue;
import com.etsy.android.grid.StaggeredGridView;
import com.tumblr.jumblr.types.PhotoPost;
import com.tumblr.jumblr.types.Post;

import java.util.List;

public class VenuePostsFragment extends Fragment implements PostAdapter.OnReachedEndListener {
	public static final String ARGS_VENUE = "args_venue";
	private TumblrClient mTumblrClient = new TumblrClient();
	private View mRoot;
	private StaggeredGridView mGrid;
	private TextView mEmptyTextView;
	private PostAdapter mAdapter;

	public static VenuePostsFragment create(final Venue venue) {
		VenuePostsFragment fragment = new VenuePostsFragment();
		final Bundle args = new Bundle();
		args.putParcelable(ARGS_VENUE, venue);
		fragment.setArguments(args);
		return fragment;
	}

	public Venue getVenue() {
		return getArguments().getParcelable(ARGS_VENUE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRoot = inflater.inflate(R.layout.fragment_venue_posts, container, false);
		if (mRoot != null) {
			mGrid = (StaggeredGridView) mRoot.findViewById(R.id.grid);
			mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					PostAdapter adapter = (PostAdapter) mGrid.getAdapter();
					PhotoPost post = (PhotoPost) adapter.getItem(position);

					final Intent intent = new Intent(getActivity(), LightboxActivity.class);
					intent.putExtra(LightboxActivity.EXTRA_URL, post.getPhotos().get(0).getOriginalSize().getUrl());
					intent.putExtra(LightboxActivity.EXTRA_BLOG_NAME, post.getBlogName());
					intent.putExtra(LightboxActivity.EXTRA_POST_ID, post.getId());
					startActivity(intent);
				}
			});
			mEmptyTextView = (TextView) mRoot.findViewById(R.id.posts_empty);

			if (savedInstanceState == null || mAdapter == null) {
				new PostLoadTask(0).execute();
			}

			setActionBarTitle(getVenue().getName());
		}
		return mRoot;
	}

	private void setActionBarTitle(final String title) {
		ActionBar actionBar = getActivity().getActionBar();
		if (actionBar != null) {
			actionBar.setTitle(title);
		}
	}

	private void showEmpty() {
		mEmptyTextView.setVisibility(View.VISIBLE);
		mRoot.setBackgroundResource(R.color.error_red);
		mGrid.setVisibility(View.GONE);
	}

	@Override
	public void onReachedEnd(long timestamp) {
		new PostLoadTask(timestamp).execute();
	}

	private class PostLoadTask extends AsyncTask<Void, Void, List<Post>> {

		private long mBefore;

		public PostLoadTask(final long before) {
			mBefore = before;
		}

		@Override
		protected List<Post> doInBackground(Void... params) {
			return mTumblrClient.getVenuePosts(getVenue(), mBefore);
		}

		@Override
		protected void onPostExecute(List<Post> posts) {
			if (mAdapter == null) {
				mAdapter = new PostAdapter(getActivity(), mGrid, posts, VenuePostsFragment.this);
				mGrid.setAdapter(mAdapter);
			} else {
				mAdapter.addPosts(posts);
			}


			if (mAdapter == null || mAdapter.getCount() == 0) {
				showEmpty();
			}
		}
	}
}
