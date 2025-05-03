package app.main.model;

public class NGO {
    String name, location, pets, contact, tagline;

    public NGO(String name, String location, String pets, String contact, String tagline) {
        this.name = name;
        this.location = location;
        this.pets = pets;
        this.contact = contact;
        this.tagline = tagline;
    }

    // Getters
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getPets() { return pets; }
    public String getContact() { return contact; }
    public String getTagline() { return tagline; }
} 