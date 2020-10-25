import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.UUID;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Exec {

	public static void main(String[] args) throws IOException {
		
		// for comit 222
		String youtubeUrl="https://www.youtube.com/user/AcademeG/videos";

		String responseHtmlBody=getURLContent(youtubeUrl);
		Channel channel = new Channel();
		channel.link=youtubeUrl;
		getChannelContent(channel, responseHtmlBody);
		int count=0;
		for(String str:		responseHtmlBody.split("\"title\":\\{\"runs\":\\[\\{\"text")) {
//			System.err.println("\"title\":{\"runs\":[{\"text" + str);
			Item item = getItemFromText("\"title\":{\"runs\":[{\"text" + str);
			if(item!=null) {
				if(item.link.contains("/redirect?")) {
					// WARN: потенциально снижение производительности
					continue;
				}
				item.link="https://www.youtube.com"+item.link;
				item.enclosureUrl=item.link;
				item.guid=getGUID(item.link);
				String[] descriptionAndPubdate = getDescriptionAndPubDate(item.link);
				item.description=descriptionAndPubdate[0];
				item.pubDate=descriptionAndPubdate[1];
				
				channel.items.add(item);
			}
			
		}
//		System.err.println(responseHtmlBody);
		
//		String text="\"title\":{\"runs\":[{\"text\":\"Взял новую Audi e-tron. Первый тюнинг.\"}],\"accessibility\":"
//				+ "{\"accessibilityData\":{\"label\":\"Взял новую Audi e-tron. Первый тюнинг. Автор: AcademeG 2 недели назад 32 минуты 1 633 066 просмотров\"}}},"
//				+ "\"publishedTimeText\":{\"simpleText\":\"2 недели назад\"},"
//				+ "\"viewCountText\":{\"simpleText\":\"1 633 066 просмотров\"},"
//				+ "\"navigationEndpoint\":{\"clickTrackingParams\":\"CJwBEJQ1GAIiEwj224Kbh83sAhXYJZsKHeORBzEyCmctaGlnaC1jcnZaGFVDMGxUOUs4V2Z1YzFLUHFtNllqUmYxQZoBBRDyOBhm\","
//				+ "\"commandMetadata\":{\"webCommandMetadata\":"
//				+ "{\"url\":\"/watch?v=5bq1ybArDho\\u0026t=888s\",\"webPageType\":\"WEB_PAGE_TYPE_WATCH\",\"rootVe\":3832}},"
//				+ "\"watchEndpoint\":{\"videoId\":\"5bq1ybArDho\",\"startTimeSeconds\":888}},"
//				+ "\"trackingParams\":\"CJwBEJQ1GAIiEwj224Kbh83sAhXYJZsKHeORBzFAmpysgZu5rd3lAQ==\","
//				+ "\"shortViewCountText\":{\"simpleText\":\"1,6 млн просмотров\"},"
//				+ "\"menu\":{\"menuRenderer\":{\"items\":[{\"menuServiceItemRenderer\":{\"text\":{\"runs\":[{\"text\":\"Добавить в очередь\"}]},"
//				+ "\"icon\":{\"iconType\":\"ADD_TO_QUEUE_TAIL\"},\"serviceEndpoint\":"
//				+ "{\"clickTrackingParams\":\"CJ8BEP6YBBgFIhMI9tuCm4fN7AIV2CWbCh3jkQcx\","
//				+ "\"commandMetadata\":{\"webCommandMetadata\":{\"url\":\"/service_ajax\",\"sendPost\":true}},\"signalServiceEndpoint\":"
//				+ "{\"signal\":\"CLIENT_SIGNAL\",\"actions\":[{\"clickTrackingParams\":\"CJ8BEP6YBBgFIhMI9tuCm4fN7AIV2CWbCh3jkQcx\","
//				+ "\"addToPlaylistCommand\":{\"openMiniplayer\":true,\"videoId\":\"5bq1ybArDho\",\"listType\":\"PLAYLIST_EDIT_LIST_TYPE_QUEUE\","
//				+ "\"onCreateListCommand\":{\"clickTrackingParams\":\"CJ8BEP6YBBgFIhMI9tuCm4fN7AIV2CWbCh3jkQcx\",\"commandMetadata\":{\"webCommandMetadata\":"
//				+ "{\"url\":\"/service_ajax\",\"sendPost\":true,\"apiUrl\":\"/youtubei/v1/playlist/create\"}},\"createPlaylistServiceEndpoint\":"
//				+ "{\"videoIds\":[\"5bq1ybArDho\"],\"params\":\"CAQ%3D\"}},\"videoIds\":[\"5bq1ybArDho\"]}}]}},"
//				+ "\"trackingParams\":\"CJ8BEP6YBBgFIhMI9tuCm4fN7AIV2CWbCh3jkQcx\"}},{\"menuServiceItemRenderer\":{\"text\":{\"runs\":[{"
//				+ "\"text\":\"Добавить в плейлист \\\"Смотреть позже\\\"\"}]},\"icon\":{\"iconType\":\"WATCH_LATER\"},"
//				+ "\"serviceEndpoint\":{\"clickTrackingParams\":\"CJwBEJQ1GAIiEwj224Kbh83sAhXYJZsKHeORBzE=\",\"commandMetadata\":{\"webCommandMetadata\":"
//				+ "{\"url\":\"/service_ajax\",\"sendPost\":true,\"apiUrl\":\"/youtubei/v1/browse/edit_playlist\"}},\"playlistEditEndpoint\":"
//				+ "{\"playlistId\":\"WL\",\"actions\":[{\"addedVideoId\":\"5bq1ybArDho\",\"action\":\"ACTION_ADD_VIDEO\"}]}},"
//				+ "\"trackingParams\":\"CJwBEJQ1GAIiEwj224Kbh83sAhXYJZsKHeORBzE=\"}},{\"menuServiceItemRenderer\":{\"text\":{\"runs\":["
//				+ "{\"text\":\"Добавить в плейлист\"}]},\"icon\":{\"iconType\":\"PLAYLIST_ADD\"},\"serviceEndpoint\":"
//				+ "{\"clickTrackingParams\":\"CJwBEJQ1GAIiEwj224Kbh83sAhXYJZsKHeORBzE=\",\"commandMetadata\":"
//				+ "{\"webCommandMetadata\":{\"url\":\"/service_ajax\",\"sendPost\":true,\"apiUrl\":\"/youtubei/v1/playlist/get_add_to_playlist\"}},"
//				+ "\"addToPlaylistServiceEndpoint\":{\"videoId\":\"5bq1ybArDho\"}},\"trackingParams\":\"CJwBEJQ1GAIiEwj224Kbh83sAhXYJZsKHeORBzE=\","
//				+ "\"hasSeparator\":true}}],\"trackingParams\":\"CJwBEJQ1GAIiEwj224Kbh83sAhXYJZsKHeORBzE=\",\"accessibility\":{\"accessibilityData\":"
//				+ "{\"label\":\"Меню действий\"}}}},\"thumbnailOverlays\":[{\"thumbnailOverlayResumePlaybackRenderer\":{\"percentDurationWatched\":45}},"
//				+ "{\"thumbnailOverlayTimeStatusRenderer\":{\"text\":{\"accessibility\":{\"accessibilityData\":{\"label\":\"32 минуты 33 секунды\"}},"
//				+ "\"simpleText\":\"32:33\"},\"style\":\"DEFAULT\"}},{\"thumbnailOverlayToggleButtonRenderer\":{\"isToggled\":false,\"untoggledIcon\":{\"iconType\":\"WATCH_LATER\"},\"toggledIcon\":{\"iconType\":\"CHECK\"},\"untoggledTooltip\":\"Смотреть позже\",\"toggledTooltip\":\"Добавлено в плейлист \\\"Посмотреть позже\\\"\",\"untoggledServiceEndpoint\":{\"clickTrackingParams\":\"CJ4BEPnnAxgCIhMI9tuCm4fN7AIV2CWbCh3jkQcx\",\"commandMetadata\":{\"webCommandMetadata\":{\"url\":\"/service_ajax\",\"sendPost\":true,\"apiUrl\":\"/youtubei/v1/browse/edit_playlist\"}},\"playlistEditEndpoint\":{\"playlistId\":\"WL\",\"actions\":[{\"addedVideoId\":\"5bq1ybArDho\",\"action\":\"ACTION_ADD_VIDEO\"}]}},\"toggledServiceEndpoint\":{\"clickTrackingParams\":\"CJ4BEPnnAxgCIhMI9tuCm4fN7AIV2CWbCh3jkQcx\",\"commandMetadata\":{\"webCommandMetadata\":{\"url\":\"/service_ajax\",\"sendPost\":true,\"apiUrl\":\"/youtubei/v1/browse/edit_playlist\"}},\"playlistEditEndpoint\":{\"playlistId\":\"WL\",\"actions\":[{\"action\":\"ACTION_REMOVE_VIDEO_BY_VIDEO_ID\",\"removedVideoId\":\"5bq1ybArDho\"}]}},\"untoggledAccessibility\":{\"accessibilityData\":{\"label\":\"Смотреть позже\"}},\"toggledAccessibility\":{\"accessibilityData\":{\"label\":\"Добавлено в плейлист \\\"Посмотреть позже\\\"\"}},\"trackingParams\":\"CJ4BEPnnAxgCIhMI9tuCm4fN7AIV2CWbCh3jkQcx\"}},{\"thumbnailOverlayToggleButtonRenderer\":{\"untoggledIcon\":{\"iconType\":\"ADD_TO_QUEUE_TAIL\"},\"toggledIcon\":{\"iconType\":\"PLAYLIST_ADD_CHECK\"},\"untoggledTooltip\":\"Добавить в очередь\",\"toggledTooltip\":\"Добавлено в плейлист \\\"Посмотреть позже\\\"\",\"untoggledServiceEndpoint\":{\"clickTrackingParams\":\"CJ0BEMfsBBgDIhMI9tuCm4fN7AIV2CWbCh3jkQcx\",\"commandMetadata\":{\"webCommandMetadata\":{\"url\":\"/service_ajax\",\"sendPost\":true}},\"signalServiceEndpoint\":{\"signal\":\"CLIENT_SIGNAL\",\"actions\":[{\"clickTrackingParams\":\"CJ0BEMfsBBgDIhMI9tuCm4fN7AIV2CWbCh3jkQcx\",\"addToPlaylistCommand\":{\"openMiniplayer\":true,\"videoId\":\"5bq1ybArDho\",\"listType\":\"PLAYLIST_EDIT_LIST_TYPE_QUEUE\",\"onCreateListCommand\":{\"clickTrackingParams\":\"CJ0BEMfsBBgDIhMI9tuCm4fN7AIV2CWbCh3jkQcx\",\"commandMetadata\":{\"webCommandMetadata\":{\"url\":\"/service_ajax\",\"sendPost\":true,\"apiUrl\":\"/youtubei/v1/playlist/create\"}},\"createPlaylistServiceEndpoint\":{\"videoIds\":[\"5bq1ybArDho\"],\"params\":\"CAQ%3D\"}},\"videoIds\":[\"5bq1ybArDho\"]}}]}},\"untoggledAccessibility\":{\"accessibilityData\":{\"label\":\"Добавить в очередь\"}},\"toggledAccessibility\":{\"accessibilityData\":{\"label\":\"Добавлено в плейлист \\\"Посмотреть позже\\\"\"}},\"trackingParams\":\"CJ0BEMfsBBgDIhMI9tuCm4fN7AIV2CWbCh3jkQcx\"}},{\"thumbnailOverlayNowPlayingRenderer\":{\"text\":{\"runs\":[{\"text\":\"Текущее видео\"}]}}}],\"richThumbnail\":{\"movingThumbnailRenderer\":{\"movingThumbnailDetails\":{\"thumbnails\":[{\"url\":\"https://i.ytimg.com/an_webp/5bq1ybArDho/mqdefault_6s.webp?du=3000\\u0026sqp=CPDjz_wF\\u0026rs=AOn4CLDghMEEx_qT5K-09Jk-Izg7chdy8Q\",\"width\":320,\"height\":180}],\"logAsMovingThumbnail\":true},\"enableHoveredLogging\":true,\"enableOverlay\":true}}}},{\"gridVideoRenderer\":{\"videoId\":\"KEgJBM4gH5w\",\"thumbnail\":{\"thumbnails\":[{\"url\":\"https://i.ytimg.com/vi/KEgJBM4gH5w/hqdefault.jpg?sqp=-oaymwEYCKgBEF5IVfKriqkDCwgBFQAAiEIYAXAB\\u0026rs=AOn4CLC-s7IeOJ5fdfbRF9Rs9eBEHzriSw\",\"width\":168,\"height\":94},{\"url\":\"https://i.ytimg.com/vi/KEgJBM4gH5w/hqdefault.jpg?sqp=-oaymwEYCMQBEG5IVfKriqkDCwgBFQAAiEIYAXAB\\u0026rs=AOn4CLAQjXnPNYUDKAElS92L5MVNyzYCNQ\",\"width\":196,\"height\":110},{\"url\":\"https://i.ytimg.com/vi/KEgJBM4gH5w/hqdefault.jpg?sqp=-oaymwEZCPYBEIoBSFXyq4qpAwsIARUAAIhCGAFwAQ==\\u0026rs=AOn4CLAV5rW1xQJh2dqA_azAKuBhEx990w\",\"width\":246,\"height\":138},{\"url\":\"https://i.ytimg.com/vi/KEgJBM4gH5w/hqdefault.jpg?sqp=-oaymwEZCNACELwBSFXyq4qpAwsIARUAAIhCGAFwAQ==\\u0026rs=AOn4CLAWxI4SfRpiAY-Ngn94WIVBZDP6Dg\",\"width\":336,\"height\":188}]},\r\n" + 
//				"";
		System.out.println(channel);

		
//        System.out.println("Current Matcher: "                         + result); 
  
        
    }
	
	static String getURLContent(String urlText) throws IOException{
//		String youtubeUrl="https://www.youtube.com/user/AcademeG/videos";
		URL url = new URL(urlText);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String s;
		StringBuilder sb = new StringBuilder();
		while((s=br.readLine())!=null) {
			sb.append(s);
		}
		return sb.toString();
	}
	static String[] getDescriptionAndPubDate(String url) throws IOException {
//		String text = "\"title\":{\"simpleText\":\"Конференция \\\"Структурные продукты\\\"\"},\"description\":{\"simpleText\":\"Организаторы конференции Мосбиржа и НАУФОР\"},";
		String regex="\"title\":\\{\"simpleText\":\".*\"},\"description\":\\{\"simpleText\":\"(?<description>.*)\"},\"lengthSeconds\".*,\"publishDate\":\"(?<pubDate>.*)\",.*,\"uploadDate\".*";
		String text=getURLContent(url);
		Pattern pattern = Pattern.compile(regex); 
        String stringToBeMatched = text; 
        Matcher matcher  = pattern .matcher(stringToBeMatched); 
//        System.err.println(text);
        		if (matcher.find()) { 
                    // Get the group matched using group() method 
//                    System.out.println("Description: "+matcher.group("description")); 
//                    System.out.println("PubDate: "+matcher.group("pubDate")); 

//                    item.link=matcher.group("url");
//                    item.title=matcher.group("text");
                    String[] mas = {matcher.group("description"),matcher.group("pubDate")};
                    return mas;
                } else {
                	System.err.println(url+": Ничего не найдено");
                }
        		return null;
	}
	static Item getItemFromText(String text){
		Item item = new Item();
		String regex = "\"title\":\\{\"runs\":\\[\\{\"text\":\"(?<text>.*)\"}],\"accessibility\".*webCommandMetadata\":\\{\"url\":\"(?<url>.*)\",\"webPageType"; 
        String regex2= "\"title\":\\{\"runs\":\\[\\{\"text\":\"(?<text>.*)\"}],\"accessibility\":\\{\"accessibilityData\":.*";
		Pattern pattern = Pattern.compile(regex); 
        String stringToBeMatched = text; 
        Matcher matcher  = pattern .matcher(stringToBeMatched); 
//        System.err.println(text);
        		if (matcher.find()) { 
                    // Get the group matched using group() method 
//                    System.out.println("Title: "+matcher.group("text")); 
//                    System.out.println("URL: "+matcher.group("url")); 
                    item.link=matcher.group("url");
                    item.title=matcher.group("text");
                    
                } else {
//                	System.err.println("Ничего не найдено");
                	return null;
                }
        return item;
	}
	static void getChannelContent(Channel channel, String stringToBeMatched) {
		 String regex = "<title>AcademeG - YouTube</title>.*" + 
		 		"<meta name=\"title\" content=\"(?<title>.*)\">.*" + 
		 		"<meta name=\"description\" content=\"(?<description>.*)\">.*" + 
		 		"<meta name=\"keywords\""; 
		 Pattern pattern = Pattern.compile(regex); 
	     Matcher matcher  = pattern .matcher(stringToBeMatched); 
        while (matcher.find()) { 
        	String title=matcher.group("title");
        	String description = matcher.group("description");
//	            System.out.println("title: "+title); 
	            channel.title=title;
//	            System.out.println("description: "+description); 
	            channel.description=description;

	        } 
	}
	static String getGUID(String str) {
		 return UUID.nameUUIDFromBytes(str.getBytes()).toString();
	}
}

//<item>
//<title>Конференция "Структурные продукты"</title>
//<link>https://www.youtube.com/watch?v=qjouBf4BcGQ</link>
//<description>Организаторы конференции Мосбиржа и НАУФОР</description>
//<enclosure url="https://cdn.listenbox.app/a/lpgEsePHIYq.m4a?show_id=NlmC3rJgLNBg" type="audio/mp4"/>
//<pubDate>Tue, 06 Oct 2020 06:23:17 GMT</pubDate>
//<author>Moscow Exchange</author>
//<guid>NlmC3rJgLNBg:lpgEsePHIYq</guid>
//<itunes:duration>02:52:15</itunes:duration>
//<itunes:order>1</itunes:order>
//<itunes:image href="https://i.ytimg.com/vi/qjouBf4BcGQ/maxresdefault.jpg"/>
//<itunes:keywords/>
//<itunes:subtitle>Организаторы конференции Мосбиржа и НАУФОР</itunes:subtitle>
//</item>


