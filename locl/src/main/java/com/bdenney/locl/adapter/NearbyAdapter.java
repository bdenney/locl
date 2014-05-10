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
package com.bdenney.locl.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bdenney.locl.R;
import com.bdenney.locl.api.FoursquareClient;
import com.bdenney.locl.model.Venue;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Adapter for nearby venues.
 *
 * @author brandon
 */
public class NearbyAdapter extends BaseAdapter {
	/**
	 * Adapter foursquare client.
	 */
	private FoursquareClient mFoursquareClient = new FoursquareClient();
	/**
	 * Context to use.
	 */
	private Context mContext;

	/**
	 * Collection of venues backing this adapter.
	 */
	private List<Venue> mVenues = new ArrayList<Venue>();

	public NearbyAdapter(final Context context, final List<Venue> venues) {
		updateData(venues);
		mContext = context;
	}

	/**
	 * Updates the dataset.
	 *
	 * @param venues
	 * 		the new list of venues.
	 */
	public void updateData(final List<Venue> venues) {
		mVenues.clear();

		if (venues != null) {
			mVenues.addAll(venues);
		}

		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mVenues.size();
	}

	@Override
	public Object getItem(int position) {
		return mVenues.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Create the view if it doesn't exist.
		if (convertView == null) {
			convertView = newView(parent);
		}

		// Get the view holder.
		final VenueHolder holder = (VenueHolder) convertView.getTag();

		// Bind the view.
		bindView(position, holder);

		return convertView;
	}

	private View newView(final ViewGroup parent) {
		// Inflate a new view.
		final View root = LayoutInflater.from(mContext).inflate(R.layout.list_item_venue, parent, false);

		if (root != null) {
			// Create the tag.
			final VenueHolder venueHolder = new VenueHolder();

			// Find all views.
			venueHolder.root = (RelativeLayout) root;
			venueHolder.image = (ImageView) root.findViewById(R.id.venue_image);
			venueHolder.title = (TextView) root.findViewById(R.id.venue_name);
			venueHolder.location = (TextView) root.findViewById(R.id.venue_location);

			// Set the tag.
			root.setTag(venueHolder);
		}

		return root;
	}

	private void bindView(final int position, final VenueHolder viewHolder) {
		viewHolder.recycle();

		final Venue venue = (Venue) getItem(position);
		viewHolder.title.setText(venue.getName());
		viewHolder.location.setText(venue.getLocation().getAddress());
		viewHolder.image.setBackgroundResource(pickRandomColor());
		viewHolder.imagetask = new VenueImageRequestTask(venue, viewHolder.image);
		viewHolder.imagetask.execute();
	}

	/**
	 * View holder for the venue layout.
	 */
	private static class VenueHolder {
		public RelativeLayout root;
		public ImageView image;
		public TextView title;
		public TextView location;
		public VenueImageRequestTask imagetask;

		public void recycle() {
			image.setImageDrawable(null);
			title.setText("");
			location.setText("");
			if (imagetask != null) {
				imagetask.cancel(true);
				imagetask = null;
			}
		}
	}

	private class VenueImageRequestTask extends AsyncTask<Void, Void, String> {

		private Venue mVenue;
		private ImageView mImageView;

		public VenueImageRequestTask(final Venue venue, final ImageView imageView) {
			mVenue = venue;
			mImageView = imageView;
		}

		@Override
		protected String doInBackground(Void... params) {
			return mFoursquareClient.getVenueImageUrl(mVenue);
		}

		@Override
		protected void onPostExecute(String url) {
			if (!TextUtils.isEmpty(url)) {
				Picasso.with(mContext).load(url).into(mImageView);
			}
		}
	}

	private int pickRandomColor() {
		final Random random = new Random(System.currentTimeMillis());
		final int nextInt = random.nextInt(5);
		int color;
		switch (nextInt) {
		case 0:
			color = R.color.random_1;
			break;
		case 1:
			color = R.color.random_2;
			break;
		case 2:
			color = R.color.random_3;
			break;
		case 3:
			color = R.color.random_4;
			break;
		default:
		case 4:
			color = R.color.random_5;
			break;
		}
		return color;
	}
}
