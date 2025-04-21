package app.main.model;

public class PetDonation {
    private final int imageResId;
    private final String description;
    private final double amountRaised;
    private final double amountNeeded;
    private final int daysLeft;

    public PetDonation(int imageResId, String description, double amountRaised, double amountNeeded, int daysLeft) {
        this.imageResId = imageResId;
        this.description = description;
        this.amountRaised = amountRaised;
        this.amountNeeded = amountNeeded;
        this.daysLeft = daysLeft;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getDescription() {
        return description;
    }

    public double getAmountRaised() {
        return amountRaised;
    }

    public double getAmountNeeded() {
        return amountNeeded;
    }

    public int getDaysLeft() {
        return daysLeft;
    }

    public int getProgressPercentage() {
        if (amountNeeded == 0) return 0;
        return (int) ((amountRaised / amountNeeded) * 100);
    }

    public String getAmountRaisedFormatted() {
        return String.format("₹%.2f raised", amountRaised);
    }

    public String getAmountNeededFormatted() {
        return String.format("₹%.2f needed", amountNeeded);
    }

    public String getDaysLeftFormatted() {
        return String.format("%d Days Left", daysLeft);
    }
} 