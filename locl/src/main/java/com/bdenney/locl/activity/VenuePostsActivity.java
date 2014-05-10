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
import com.bdenney.locl.fragment.VenuePostsFragment;
import com.bdenney.locl.model.Venue;

public class VenuePostsActivity extends FragmentActivity {

	public static final String EXTRA_VENUE = "com.bdenney.locl.intent.extra.VENUE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_venue_posts);

		if (savedInstanceState == null) {
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.venue_posts_root, VenuePostsFragment.create(getVenue()))
					.commit();
		}
	}

	private Venue getVenue() {
		final Intent intent = getIntent();
		Bundle extras = null;
		Venue venue = null;

		if (intent != null) {
			extras = intent.getExtras();
		}

		if (extras != null) {
			venue = extras.getParcelable(EXTRA_VENUE);
		}

		return venue;
	}
}
