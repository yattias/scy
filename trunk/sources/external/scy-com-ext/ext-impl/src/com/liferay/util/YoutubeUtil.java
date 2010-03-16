package com.liferay.util;

import com.liferay.portlet.bookmarks.EntryURLException;

/**
 * Util class to add youtube video by youtube url in webcontent tool and as
 * extern callByValue link.
 * 
 * @author Daniel
 * 
 */

public class YoutubeUtil {

	private static String YOUTUBE_URL = "http://www.youtube.com/v/";
	private static String YOUTUBE_WATCH_URL = "http://www.youtube.com/watch?";

	private static String YOUTUBE_WEBCONTENTSTART = "<p><embed type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" src=\"";
	private static String YOUTUBE_WEBCONTENTEND = "\" play=\"true\" loop=\"true\" menu=\"true\"></embed></p>";

	/**
	 * Check if content contains normal youtube video url with watch in name. If
	 * it exist then the content converted to real file url from youtube video.
	 * 
	 * @param content
	 *            the journal source code as string
	 * @param maxYoutubeCount
	 *            how much youtube videos convert to file url
	 * @return content string
	 */

	public static String checkYoutube(String content, int maxYoutubeCount) {
		int actualCount = 0;
		while (content.contains("http://www.macromedia.com/go/getflashplayer") && content.contains("src=\"http://www.youtube.com/watch?")
				&& maxYoutubeCount > actualCount) {

			String contentStart;
			String contentEnd;
			String youtubeFilename;

			String[] startURLStringArray = content.split(YOUTUBE_WATCH_URL, 2);
			String[] endURLStringArray;
			contentStart = startURLStringArray[0];

			// check if some additional content is in url
			if (startURLStringArray[1].contains("&")) {
				String[] middle = startURLStringArray[1].split("&", 2);
				endURLStringArray = middle[1].split("\"", 2);
				contentEnd = endURLStringArray[1];
				youtubeFilename = middle[0].replaceAll("\\?v=", "");
			} else {
				endURLStringArray = startURLStringArray[1].split("\"", 2);
				contentEnd = endURLStringArray[1];
				youtubeFilename = endURLStringArray[0].replaceAll("\\?v=", "");
			}

			// build new source code for webcontent
			StringBuffer sb = new StringBuffer();
			sb.append(contentStart);
			sb.append(YOUTUBE_URL);
			sb.append(youtubeFilename);
			sb.append("\"");
			sb.append(contentEnd);
			content = sb.toString();
			actualCount++;
		}
		return content;
	}

	/**
	 * Build source code for webcontent to show youtube video from youtube url.
	 * 
	 * @param urlEntry
	 *            user entered url for extern callByValue link
	 * @return builded youtube webcontent source code
	 * @throws EntryURLException
	 */
	public static String getYoutubeContent(String urlEntry) throws EntryURLException {
		String youtubeFileName = getYoutubeFilenameFromURL(urlEntry);

		StringBuffer urlSb = new StringBuffer();
		urlSb.append(YOUTUBE_WEBCONTENTSTART);
		urlSb.append(YOUTUBE_URL);
		urlSb.append(youtubeFileName);
		urlSb.append(YOUTUBE_WEBCONTENTEND);

		return urlSb.toString();
	}

	/**
	 * Extract youtube filenmame from YOUTUBE_WATCH_URL.
	 * 
	 * @param urlEntry
	 *            user entered url
	 * @return extracted youtube filename
	 * @throws EntryURLException
	 *             throw this exception if youtube url is not a
	 *             YOUTUBE_WATCH_URL
	 */
	private static String getYoutubeFilenameFromURL(String urlEntry) throws EntryURLException {
		String youtubeFilename;

		// allow urls that start with www
		urlEntry = allowWWWStart(urlEntry);

		if (urlEntry.contains(YOUTUBE_WATCH_URL)) {
			String[] startURLStringArray = urlEntry.split(YOUTUBE_WATCH_URL, 2);
			String[] endURLStringArray;

			// check if some additional content is in url
			if (startURLStringArray[1].contains("&")) {
				String[] middle = startURLStringArray[1].split("&", 2);
				youtubeFilename = middle[0].replaceAll("\\?v=", "");
			} else {
				endURLStringArray = startURLStringArray[1].split("\"", 2);
				youtubeFilename = endURLStringArray[0].replaceAll("\\?v=", "");
			}

			return youtubeFilename;
		} else {
			throw new EntryURLException();

		}
	}

	/**
	 * If url start with www add http://
	 * 
	 * @param urlEntry
	 *            user entered url
	 * @return complete url
	 */
	private static String allowWWWStart(String urlEntry) {
		if (urlEntry.startsWith("www")) {
			String http = "http://";
			String givenUrl = urlEntry;
			urlEntry = http + givenUrl;
		}
		return urlEntry;
	}

}
