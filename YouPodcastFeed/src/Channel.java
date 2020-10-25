import java.util.LinkedList;

public class Channel {
	String title;
	String link;
	String description;
	String language;
	String pubDate;
	LinkedList<Item> items = new LinkedList<Item>();
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<channel>\n");
		sb.append("  <title>"+title+"</title>\n");
		sb.append("  <link>"+link+"</link>\n");
		sb.append("  <description>"+description+"</description>\n");
		sb.append("  <language>"+language+"</language>\n");
		sb.append("  <pubDate>"+pubDate+"</pubDate>\n");
		for(Item item : items) {
			sb.append(item);
		}
		sb.append("</channel>");
		return sb.toString();
	}
}
