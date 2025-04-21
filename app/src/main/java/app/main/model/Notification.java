package app.main.model;

public class Notification {
    private final int userImage;
    private final String message;
    private final String timestamp;
    private final boolean isRead;
    private boolean isLiked;

    public Notification(int userImage, String message, String timestamp) {
        this.userImage = userImage;
        this.message = message;
        this.timestamp = timestamp;
        this.isRead = false;
        this.isLiked = false;
    }

    public int getUserImage() {
        return userImage;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
} 