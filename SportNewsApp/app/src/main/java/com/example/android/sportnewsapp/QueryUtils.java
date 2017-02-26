package com.example.android.sportnewsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    public static List<News> fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(NewsActivity.LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of news
        List<News> news = extractFeatureFromJson(jsonResponse);

        return news;
    }

    private QueryUtils() {
    }

    private static List<News> extractFeatureFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        List<News> news = new ArrayList<>();

        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            // Extract the JSONArray associated with the key called "results"
            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray resultsArray = response.getJSONArray("results");

            // For each news in the resultsArray, create a news object
            for (int i = 0; i < resultsArray.length(); i++) {

                // Get a single news at position i within the list of news
                JSONObject currentNews = resultsArray.getJSONObject(i);
                String title = currentNews.getString("webTitle");
                String section = currentNews.getString("sectionName");
                String url = currentNews.getString("webUrl");

                //Image Links
                JSONObject newsThumbnailLinks = null;
                try {
                    newsThumbnailLinks = currentNews.getJSONObject("fields");
                } catch (JSONException ignored) {
                }
                // Convert the Image Links to String
                String newsThumbnail = "";
                if ( newsThumbnailLinks == null){
                    newsThumbnail = "null";
                }else{
                    newsThumbnail = newsThumbnailLinks.getString("thumbnail");
                }

                // Create a new news object from the JSON response.
                News allNews = new News(title, section, url, newsThumbnail);

                // Add the new news to the list of news.
                news.add(allNews);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the guardian JSON results", e);
        }

        // Return the list of news
        return news;
    }

    //Returns new URL object from the given string URL.
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(NewsActivity.LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    //Make an HTTP request to the given URL and return a String as the response.
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(NewsActivity.LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(NewsActivity.LOG_TAG, "Problem retrieving the guardian JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}

