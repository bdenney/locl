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
package com.bdenney.locl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.bdenney.locl.R;
import com.bdenney.locl.fragment.LightboxFragment;

public class LightboxActivity extends FragmentActivity {
	public static final String EXTRA_URL = "com.bdenney.locl.intent.extra.URL";
	public static final String EXTRA_BLOG_NAME = "com.bdenney.locl.intent.extra.BLOG_NAME";
	public static final String EXTRA_POST_ID = "com.bdenney.locl.intent.extra.POST_ID";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lightbox);

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.lightbox_root, LightboxFragment.create(getUrl(), getBlogName(), getPostId()))
				.commit();
	}

	private String getUrl() {
		final Intent intent = getIntent();
		Bundle extras = null;
		String url = null;

		if (intent != null) {
			extras = intent.getExtras();
		}

		if (extras != null) {
			url = extras.getString(EXTRA_URL);
		}

		return url;
	}

	private String getBlogName() {
		final Intent intent = getIntent();
		Bundle extras = null;
		String blogName = null;

		if (intent != null) {
			extras = intent.getExtras();
		}

		if (extras != null) {
			blogName = extras.getString(EXTRA_BLOG_NAME);
		}

		return blogName;
	}

	private long getPostId() {
		final Intent intent = getIntent();
		Bundle extras = null;
		long postId = 0L;

		if (intent != null) {
			extras = intent.getExtras();
		}

		if (extras != null) {
			postId = extras.getLong(EXTRA_POST_ID);
		}

		return postId;
	}
}
