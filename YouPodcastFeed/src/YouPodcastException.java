
public abstract class YouPodcastException extends Exception{
	private YouPodcastException(String message){
		super(message);
	}
	
	public static class GetUrlContentException extends YouPodcastException{
		public GetUrlContentException(String message) {
			super(message);
		}
	}
}
