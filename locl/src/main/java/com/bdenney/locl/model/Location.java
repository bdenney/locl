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
package com.bdenney.locl.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/**
 * The location of a foursquare venue.
 *
 * @author brandon
 */
public class Location implements Parcelable {
	@SerializedName("address")
	private String mAddress;

	public String getAddress() {
		return mAddress;
	}

	protected Location(Parcel in) {
		mAddress = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mAddress);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
		@Override
		public Location createFromParcel(Parcel in) {
			return new Location(in);
		}

		@Override
		public Location[] newArray(int size) {
			return new Location[size];
		}
	};
}