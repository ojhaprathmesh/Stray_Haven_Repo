package app.main.model;

import java.text.NumberFormat;
import java.util.Locale;

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

    public int getProgressPercentage() {
        if (amountNeeded == 0) return 0;
        return (int) ((amountRaised / amountNeeded) * 100);
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