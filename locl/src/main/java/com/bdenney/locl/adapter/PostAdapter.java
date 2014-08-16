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
package com.bdenney.locl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.bdenney.locl.R;
import com.etsy.android.grid.StaggeredGridView;
import com.squareup.picasso.Picasso;
import com.tumblr.jumblr.types.Photo;
import com.tumblr.jumblr.types.PhotoPost;
import com.tumblr.jumblr.types.PhotoSize;
import com.tumblr.jumblr.types.Post;

import java.util.*;

public class PostAdapter extends BaseAdapter {
	private List<Post> mPosts = new ArrayList<Post>();
	private Context mContext;
	private StaggeredGridView mGrid;

	private List<PostHolder> mHoldersToSet = new ArrayList<PostHolder>();

	private Set<Long> mRequests = new HashSet<Long>();

	public OnReachedEndListener mListener;

	public interface OnReachedEndListener {
		public void onReachedEnd(final long lastTimestamp);
	}

	public PostAdapter(final Context context, final StaggeredGridView grid, final List<Post> posts,
					   OnReachedEndListener listener) {
		mContext = context;
		mGrid = grid;
		mListener = listener;
		//noinspection ConstantConditions
		mGrid.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				//noinspection ConstantConditions
				mGrid.getViewTreeObserver().removeOnPreDrawListener(this);

				for (PostHolder holder : mHoldersToSet) {
					final Photo photo = holder.post.getPhotos().get(0);
					final PhotoSize size = photo.getSizes().get(0);
					setImageSize(holder, size.getWidth(), size.getHeight());
				}

				mHoldersToSet.clear();

				return true;
			}
		});
		updateData(posts);
	}

	public void updateData(final List<Post> posts) {
		mPosts.clear();
		if (posts != null) {
			mPosts.addAll(posts);
		}
		notifyDataSetChanged();
	}

	public void addPosts(final List<Post> posts) {
		if (posts != null) {
			mPosts.addAll(posts);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mPosts.size();
	}

	@Override
	public Object getItem(int position) {
		return mPosts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = newView(parent);
		}
		final PostHolder holder = (PostHolder) convertView.getTag();

		bindView(position, holder);

		if (position == (mPosts.size() - 1) && !mRequests.contains(holder.post.getTimestamp())) {
			mRequests.add(holder.post.getTimestamp());
			mListener.onReachedEnd(holder.post.getTimestamp());
		}

		return convertView;
	}

	private View newView(final ViewGroup parent) {
		final PostHolder holder = new PostHolder();
		final View root = LayoutInflater.from(mContext).inflate(R.layout.list_item_post, parent, false);
		if (root != null) {
			holder.image = (ImageView) root.findViewById(R.id.post_image);
			root.setTag(holder);
		}
		return root;
	}

	private void bindView(final int position, final PostHolder viewHolder) {
		viewHolder.recycle();
		final PhotoPost post = (PhotoPost) getItem(position);
		final Photo photo = post.getPhotos().get(0);
		final PhotoSize size = photo.getSizes().get(0);
		viewHolder.image.setBackgroundResource(pickRandomColor());
		viewHolder.post = post;

		if (mGrid.getColumnWidth() > 0) {
			setImageSize(viewHolder, size.getWidth(), size.getHeight());
		} else {
			mHoldersToSet.add(viewHolder);
		}

		Picasso.with(mContext).load(photo.getSizes().get(0).getUrl()).into(viewHolder.image);
	}

	private void setImageSize(final PostHolder viewHolder, int width, int height) {
		// Copy the sizes.
		int finalWidth = width;
		int finalHeight = height;

		// Check if we need to adjust.
		if (width < mGrid.getColumnWidth()) {
			// Yep.
			float ratio = mGrid.getColumnWidth() / width;
			finalWidth = mGrid.getColumnWidth();
			finalHeight = (int) (height * ratio);
		}

		final ViewGroup.LayoutParams params = viewHolder.image.getLayoutParams();
		if (params != null) {
			params.width = finalWidth;
			params.height = finalHeight;
			viewHolder.image.setLayoutParams(params);
		}
	}

	private static class PostHolder {
		PhotoPost post;
		public ImageView image;

		public void recycle() {
			image.setImageDrawable(null);
			post = null;
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

