/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Tumblr
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
