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
package com.bdenney.locl.api;

import android.text.TextUtils;
import android.util.Log;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.bdenney.locl.BuildConfig;
import com.bdenney.locl.LoclApplication;
import com.bdenney.locl.model.Photo;
import com.bdenney.locl.model.Venue;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Foursquare client library facade.
 *
 * @author brandon
 */
public class FoursquareClient {
	/**
	 * Logging tag.
	 */
	private static final String TAG = FoursquareClient.class.getSimpleName();
	/**
	 * GSON object.
	 */
	private Gson mGson = new Gson();
	/**
	 * Foursquare API client ID.
	 */
	private static final String FOURSQUARE_CLIENT_ID = BuildConfig.FOURSQUARE_CLIENT_ID;
	/**
	 * Foursquare API client secret.
	 */
	private static final String FOURSQUARE_SECRET = BuildConfig.FOURSQUARE_SECRET;

	public List<Venue> getNearbyVenues(final String latLong, final String query) {
		final List<Venue> venues = new ArrayList<Venue>();
		try {
			String requestUrl;
			if (TextUtils.isEmpty(query)) {
				requestUrl = String.format("https://api.foursquare" +
								".com/v2/venues/trending?client_id=%s&client_secret=%s&v=20140128&ll=%s&query=%s",
						FOURSQUARE_CLIENT_ID,
						FOURSQUARE_SECRET,
						latLong,
						query
				);
			} else {
				requestUrl = String.format("https://api.foursquare" +
								".com/v2/venues/search?client_id=%s&client_secret=%s&v=20140128&ll=%s&query=%s",
						FOURSQUARE_CLIENT_ID,
						FOURSQUARE_SECRET,
						latLong,
						query
				);
			}


			final RequestFuture<JSONObject> future = RequestFuture.newFuture();
			final JsonObjectRequest request = new JsonObjectRequest(requestUrl, null, future, future);
			LoclApplication.getRequestQueue().add(request);
			final JSONObject response = future.get();
			final JSONObject responseObj = response.getJSONObject("response");
			final JSONArray venuesObj = responseObj.getJSONArray("venues");
			for (int i = 0; i < venuesObj.length(); i++) {
				venues.add(mGson.fromJson(venuesObj.get(i).toString(), Venue.class));
			}
		} catch (Exception e) {
			Log.e(TAG, "Nearby venues call failed.", e);
		}
		return venues;
	}

	public String getVenueImageUrl(final Venue venue) {
		String photoUrl = "";
		try {
			final String requestUrl = String.format("https://api.foursquare" +
							".com/v2/venues/%s/photos?client_id=%s&client_secret=%s&limit=1&offset=0&v=20140128",
					venue.getId(),
					FOURSQUARE_CLIENT_ID,
					FOURSQUARE_SECRET
			);
			final RequestFuture<JSONObject> future = RequestFuture.newFuture();
			final JsonObjectRequest request = new JsonObjectRequest(requestUrl, null, future, future);
			LoclApplication.getRequestQueue().add(request);
			final JSONObject response = future.get();
			final JSONObject responseObj = response.getJSONObject("response");
			final JSONObject photosObj = responseObj.getJSONObject("photos");
			final JSONArray itemsObj = photosObj.getJSONArray("items");
			photoUrl = mGson.fromJson(itemsObj.get(0).toString(), Photo.class).getUrl();
		} catch (Throwable e) {
			Log.e(TAG, "Venue image call failed.", e);
		}
		return photoUrl;
	}
}
