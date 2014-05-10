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

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bdenney.locl.R;
import com.bdenney.locl.api.TumblrClient;
import com.squareup.picasso.Picasso;
import uk.co.senab.photoview.PhotoView;

public class LightboxFragment extends Fragment {

	public static final String ARGS_URL = "args_url";

	public static final String ARGS_BLOG_NAME = "args_blog";

	public static final String ARGS_POST_ID = "args_post_id";

	private PhotoView mPhotoView;
	private TextView mBlogNameView;
	private ImageView mAvatarView;

	private static final String OPEN_BLOG_URI = "http://www.tumblr" +
			".com/download/android?log=0&intent=blog?page=blog&blogName=%s&postID=%s";

	public Uri getBlogUri() {
		return Uri.parse(String.format(OPEN_BLOG_URI, getBlogName(), getPostId()));
	}

	public static LightboxFragment create(final String url, final String blogName, final long postId) {
		final LightboxFragment fragment = new LightboxFragment();
		final Bundle args = new Bundle();
		args.putString(ARGS_URL, url);
		args.putString(ARGS_BLOG_NAME, blogName);
		args.putLong(ARGS_POST_ID, postId);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View root = inflater.inflate(R.layout.fragment_lightbox, container, false);
		if (root != null) {
			mPhotoView = (PhotoView) root.findViewById(R.id.image);
			mBlogNameView = (TextView) root.findViewById(R.id.blog_name);

			if (mBlogNameView != null) {
				mBlogNameView.setText(getBlogName());
			}

			final View attribution = root.findViewById(R.id.attribution_view);
			if (attribution != null) {
				attribution.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						final Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(getBlogUri());
						startActivity(intent);
					}
				});
			}

			mAvatarView = (ImageView) root.findViewById(R.id.avatar);
			new LoadAvatarTask(getBlogName()).execute();
		}
		return root;
	}

	private String getUrl() {
		return getArguments().getString(ARGS_URL);
	}

	private String getBlogName() {
		return getArguments().getString(ARGS_BLOG_NAME);
	}

	private String getPostId() {
		return String.valueOf(getArguments().getLong(ARGS_POST_ID));
	}

	@Override
	public void onStart() {
		super.onStart();

		Picasso.with(getActivity()).load(getUrl()).into(mPhotoView);
	}

	private class LoadAvatarTask extends AsyncTask<Void, Void, String> {

		private String mBlogName;

		public LoadAvatarTask(final String blogName) {
			mBlogName = blogName;
		}

		@Override
		protected String doInBackground(Void... params) {
			return new TumblrClient().getAvatarUrl(mBlogName);
		}

		@Override
		protected void onPostExecute(String avatarUrl) {
			Picasso.with(getActivity()).load(avatarUrl).into(mAvatarView);
		}
	}
}
