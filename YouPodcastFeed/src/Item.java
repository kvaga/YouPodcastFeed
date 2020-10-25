
public class Item {
	String title;
	String link;
	String description;
	String enclosureUrl;
	String enclosureType="audio/mp4";
	String pubDate;
	String author;
	String guid;
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<item>\n");
		sb.append("		<title>"+title+"</title>\n");
		sb.append("		<link>"+link+"</link>\n");
		sb.append("		<description>"+description+"</description>\n");
		sb.append("		<enclosure url=\""+enclosureUrl+"\" type=\""+enclosureType+"\"/>\n");
		sb.append("		<pubDate>"+pubDate+"</pubDate>\n");
		sb.append("		<author>"+author+"</author>\n");
		sb.append("		<guid>"+guid+"</guid>\n");
	sb.append("</item>\n");


		
		return sb.toString();
	}
}
