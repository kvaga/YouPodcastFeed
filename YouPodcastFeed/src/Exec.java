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

	public static void main(String[] args) throws Exception {

		// for comit 222
		String youtubeUrl = "https://www.youtube.com/user/AcademeG/videos";

		String responseHtmlBody = getURLContent(youtubeUrl);
		Channel channel = new Channel();
		channel.link = youtubeUrl;
		getChannelContent(channel, responseHtmlBody);
		int count = 0;
		for (String str : responseHtmlBody.split("\"title\":\\{\"runs\":\\[\\{\"text")) {
			Item item = getItemFromText("\"title\":{\"runs\":[{\"text" + str);
			if (item != null) {
				if (item.link.contains("/redirect?")) {
					// WARN: потенциально снижение производительности
					continue;
				}
				item.link = "https://www.youtube.com" + item.link;
				item.enclosureUrl = item.link;
				item.guid = getGUID(item.link);
				String[] descriptionAndPubdate = getDescriptionAndPubDate(item.link);
				item.description = descriptionAndPubdate[0];
				item.pubDate = descriptionAndPubdate[1];

				channel.items.add(item);
			}

		}
//		System.err.println(responseHtmlBody);

		System.out.println(channel);
	}

	static String getURLContent(String urlText) throws YouPodcastException {
		try {
			URL url = new URL(urlText);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String s;
			StringBuilder sb = new StringBuilder();
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			return sb.toString();
		} catch (Exception e) {
			throw new YouPodcastException.GetUrlContentException("Can't get an URL content for the URL: " + urlText);
		}
	}

	static String[] getDescriptionAndPubDate(String url) throws YouPodcastException {
		String regex = "\"title\":\\{\"simpleText\":\".*\"},\"description\":\\{\"simpleText\":\"(?<description>.*)\"},\"lengthSeconds\".*,\"publishDate\":\"(?<pubDate>.*)\",.*,\"uploadDate\".*";
		String text = getURLContent(url);
		Pattern pattern = Pattern.compile(regex);
		String stringToBeMatched = text;
		Matcher matcher = pattern.matcher(stringToBeMatched);
//        System.err.println(text);
		if (matcher.find()) {
//                    System.out.println("Description: "+matcher.group("description")); 
//                    System.out.println("PubDate: "+matcher.group("pubDate")); 

			String[] mas = { matcher.group("description"), matcher.group("pubDate") };
			return mas;
		} else {
			System.err.println(url + ": Ничего не найдено");
		}
		return null;
	}

	static Item getItemFromText(String text) {
		Item item = new Item();
		String regex = "\"title\":\\{\"runs\":\\[\\{\"text\":\"(?<text>.*)\"}],\"accessibility\".*webCommandMetadata\":\\{\"url\":\"(?<url>.*)\",\"webPageType";
		String regex2 = "\"title\":\\{\"runs\":\\[\\{\"text\":\"(?<text>.*)\"}],\"accessibility\":\\{\"accessibilityData\":.*";
		Pattern pattern = Pattern.compile(regex);
		String stringToBeMatched = text;
		Matcher matcher = pattern.matcher(stringToBeMatched);
//        System.err.println(text);
		if (matcher.find()) {
			// Get the group matched using group() method
//                    System.out.println("Title: "+matcher.group("text")); 
//                    System.out.println("URL: "+matcher.group("url")); 
			item.link = matcher.group("url");
			item.title = matcher.group("text");

		} else {
//                	System.err.println("Ничего не найдено");
			return null;
		}
		return item;
	}

	static void getChannelContent(Channel channel, String stringToBeMatched) {
		String regex = "<title>AcademeG - YouTube</title>.*" + "<meta name=\"title\" content=\"(?<title>.*)\">.*"
				+ "<meta name=\"description\" content=\"(?<description>.*)\">.*" + "<meta name=\"keywords\"";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(stringToBeMatched);
		while (matcher.find()) {
			String title = matcher.group("title");
			String description = matcher.group("description");
//	            System.out.println("title: "+title); 
			channel.title = title;
//	            System.out.println("description: "+description); 
			channel.description = description;

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
