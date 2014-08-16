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
