package app.main.activity;

public class Pet {
    String name;
    int image;

    public Pet(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }
}