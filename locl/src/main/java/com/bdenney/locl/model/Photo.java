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

import com.google.gson.annotations.SerializedName;

/**
 * Foursquare photo object.
 *
 * @author brandon
 */
public class Photo {
	@SerializedName("id")
	private String mId;

	@SerializedName("prefix")
	private String mPrefix;

	@SerializedName("suffix")
	private String mSuffix;

	public String getUrl() {
		return String.format("%s%s%s", mPrefix, "500x300", mSuffix);
	}
}