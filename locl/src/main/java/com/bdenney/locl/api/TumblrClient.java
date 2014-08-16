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
