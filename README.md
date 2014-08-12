Locl
====
This was my Tumblr May 2014 Hackday project. It's an Android application that allows a user to look at Tumblr posts associated with Foursquare venues.

##Building
To get this application working correctly, you must sign up for API keys from both [Tumblr](https://www.tumblr.com/oauth/apps) and [Foursquare](https://foursquare.com/developers/apps).

Create a `gradle.properties` file in the root of the project with these values:

    tumblrClientId=""
    tumblrSecret=""
    foursquareClientId=""
    foursquareSecret=""

##Libraries
* [Guava](https://code.google.com/p/guava-libraries/)
* [Volley](https://android.googlesource.com/platform/frameworks/volley/)
* [Gson](https://code.google.com/p/google-gson/)
* [Picasso](http://square.github.io/picasso/)
* [Jumblr](https://github.com/tumblr/jumblr)
* [AndroidStaggeredGrid](https://github.com/etsy/AndroidStaggeredGrid)
* [PhotoView](https://github.com/chrisbanes/PhotoView)

##License
    The MIT License (MIT)

    Copyright (c) 2014 Tumblr

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
