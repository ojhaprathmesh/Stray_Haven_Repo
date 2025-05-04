package app.main.model;

import java.text.NumberFormat;
import java.util.Locale;

public class PetDonation {
    private int imageResourceId;
    private String description;
    private long amountRaised;
    private long amountNeeded;
    private int daysLeft;

    public PetDonation(int imageResourceId, String description, long amountRaised, long amountNeeded, int daysLeft) {
        this.imageResourceId = imageResourceId;
        this.description = description;
        this.amountRaised = amountRaised;
        this.amountNeeded = amountNeeded;
        this.daysLeft = daysLeft;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getDescription() {
        return description;
    }

    public long getAmountRaised() {
        return amountRaised;
    }

    public long getAmountNeeded() {
        return amountNeeded;
    }

    public int getDaysLeft() {
        return daysLeft;
    }
    
    public int getProgressPercentage() {
        long totalAmount = amountRaised + amountNeeded;
        if (totalAmount == 0) return 0;
        return (int) ((amountRaised * 100) / totalAmount);
    }

    public String getAmountRaisedFormatted() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        return currencyFormat.format(amountRaised) + " raised";
    }

    public String getAmountNeededFormatted() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        return currencyFormat.format(amountNeeded) + " needed";
    }

    public String getDaysLeftFormatted() {
        NumberFormat numberFormat = NumberFormat.getIntegerInstance(Locale.getDefault());
        return numberFormat.format(daysLeft) + " Days Left";
    }
} 