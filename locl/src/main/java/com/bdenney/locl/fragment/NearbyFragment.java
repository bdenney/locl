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
package com.bdenney.locl.fragment;

import android.app.ActionBar;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.bdenney.locl.R;
import com.bdenney.locl.activity.VenuePostsActivity;
import com.bdenney.locl.adapter.NearbyAdapter;
import com.bdenney.locl.api.FoursquareClient;
import com.bdenney.locl.model.Venue;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.common.base.Strings;

import java.util.List;

/**
 * Fragment for displaying nearby venues from foursquare.
 *
 * @author brandon
 */
public class NearbyFragment extends Fragment implements GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, SwipeRefreshLayout.OnRefreshListener {
	/**
	 * Query arguments.
	 */
	public static final String ARGS_QUERY = "args_query";

	private View mRoot;
	private TextView mEmptyTextView;

	/**
	 * The list view holding the nearby venues.
	 */
	private ListView mList;
	/**
	 * Class {@link com.bdenney.locl.api.FoursquareClient}.
	 */
	private FoursquareClient mFoursquare = new FoursquareClient();
	/**
	 * Class location client.
	 */
	private LocationClient mLocationClient;
	/**
	 * Indicates whether the location client is connected.
	 */
	private boolean mLocationClientConnected;

	private SwipeRefreshLayout mRefreshLayout;

	public static NearbyFragment create(final String query) {
		final NearbyFragment fragment = new NearbyFragment();
		final Bundle args = new Bundle();
		args.putString(ARGS_QUERY, query);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRoot = inflater.inflate(R.layout.fragment_nearby, container, false);
		if (mRoot != null) {
			mRefreshLayout = (SwipeRefreshLayout) mRoot.findViewById(R.id.swipe_refresh_layout);
			mRefreshLayout.setOnRefreshListener(this);
			mList = (ListView) mRoot.findViewById(R.id.list_nearby_venues);
			mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if (mList == null) {
						return;
					}

					final NearbyAdapter adapter = ((NearbyAdapter) mList.getAdapter());
					final Venue venue = (Venue) adapter.getItem(position);

					final Intent intent = new Intent(getActivity(), VenuePostsActivity.class);
					intent.putExtra(VenuePostsActivity.EXTRA_VENUE, venue);
					startActivity(intent);
				}
			});

			mLocationClient = new LocationClient(getActivity(), this, this);
			mEmptyTextView = (TextView) mRoot.findViewById(R.id.nearby_empty);
			setActionBarTitle(TextUtils.isEmpty(getQuery()) ? getString(R.string.trending) : getQuery());
		}
		return mRoot;
	}

	private void setActionBarTitle(final String title) {
		ActionBar actionBar = getActivity().getActionBar();
		if (actionBar != null) {
			actionBar.setTitle(title);
		}
	}

	private String getQuery() {
		return Strings.nullToEmpty(getArguments().getString(ARGS_QUERY));
	}

	@Override
	public void onStart() {
		super.onStart();
		mLocationClient.connect();
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mLocationClientConnected) {
			mLocationClient.disconnect();
		}
	}

	private void showEmpty() {
		mEmptyTextView.setVisibility(View.VISIBLE);
		mRoot.setBackgroundResource(R.color.error_red);
		mList.setVisibility(View.GONE);
	}

	/**
	 * Notifies the user that a location error has occurred.
	 */
	private void showLocationError() {
		Toast.makeText(getActivity(), R.string.location_error, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onConnected(Bundle bundle) {
		mLocationClientConnected = true;
		if (mList != null && (mList.getAdapter() == null || mList.getAdapter().getCount() == 0)) {
			new NearbyTask(buildLatLongString(mLocationClient.getLastLocation())).execute();
		}
	}

	@Override
	public void onDisconnected() {
		mLocationClientConnected = false;
	}

	/**
	 * Builds a lat/long string from a {@link android.location.Location}.
	 *
	 * @param location
	 * 		the {@link android.location.Location} to build the string from.
	 * @return the lat/long string.
	 */
	private static String buildLatLongString(final Location location) {
		return String.format("%s,%s", location.getLatitude(), location.getLongitude());
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		showLocationError();
	}

	@Override
	public void onRefresh() {
		if (mLocationClientConnected) {
			new NearbyTask(buildLatLongString(mLocationClient.getLastLocation())).execute();
		}
	}

	/**
	 * Searches foursquare for venues near the given lat/long.
	 *
	 * @author brandon
	 */
	private class NearbyTask extends AsyncTask<Void, Void, List<Venue>> {
		/**
		 * The lat/long to search for.
		 */
		private String mLatLong;

		/**
		 * Constructor.
		 *
		 * @param latLong
		 * 		the lat/long to search venues for.
		 */
		public NearbyTask(final String latLong) {
			mLatLong = latLong;
		}

		@Override
		protected List<Venue> doInBackground(Void... params) {
			return mFoursquare.getNearbyVenues(mLatLong, getQuery());
		}

		@Override
		protected void onPostExecute(List<Venue> venues) {
			mRefreshLayout.setRefreshing(false);
			if (mList != null) {
				mList.setAdapter(new NearbyAdapter(getActivity(), venues));
			}

			if (venues == null || venues.size() == 0) {
				showEmpty();
			}
		}
	}
}
