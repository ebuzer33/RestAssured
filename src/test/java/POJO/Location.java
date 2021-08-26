package POJO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Location {
    private String postcode;
    private String country;
    private String countryabbreviation;
    private List<Place> places;

    public String getPostcode() {
        return postcode;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryabbreviation() {
        return countryabbreviation;
    }

    public List<Place> getPlaces() {
        return places;
    }

    @JsonProperty("post code")
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("country abbreviation")
    public void setCountryabbreviation(String countryabbreviation) {
        this.countryabbreviation = countryabbreviation;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    @Override
    public String toString() {
        return "Location{" +
                "postcode='" + postcode + '\'' +
                ", country='" + country + '\'' +
                ", countryabbreviation='" + countryabbreviation + '\'' +
                ", places=" + places +
                '}';
    }
}
