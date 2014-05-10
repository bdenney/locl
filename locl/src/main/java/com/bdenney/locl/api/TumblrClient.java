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

import com.bdenney.locl.BuildConfig;
import com.bdenney.locl.model.Venue;
import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Post;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Facade to the Tumblr client library.
 *
 * @author brandon
 */
public class TumblrClient {
	private static final String TUMBLR_CLIENT_ID = BuildConfig.TUMBLR_CLIENT_ID;
	private static final String TUMBLR_SECRET = BuildConfig.TUMBLR_SECRET;

	private JumblrClient mApi = new JumblrClient(TUMBLR_CLIENT_ID, TUMBLR_SECRET);

	public List<Post> getVenuePosts(final Venue venue, final long timestamp) {
		final Map<String, String> options = new HashMap<String, String>();
		options.put("type", "photo");
		if (timestamp > 0) {
			options.put("before", String.valueOf(timestamp));
		}
		final List<Post> posts = mApi.tagged(venue.getName(), options);
		final Iterator<Post> iter = posts.iterator();
		while(iter.hasNext()) {
			final Post post = iter.next();
			if (!post.getType().equals("photo")) {
				iter.remove();
			}
		}
		return posts;
	}

	public String getAvatarUrl(final String blogName) {
		return mApi.blogAvatar(blogName);
	}
}
