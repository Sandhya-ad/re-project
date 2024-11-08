package com.example.coffee2_app;

<<<<<<< HEAD
import java.util.Map;

=======
/**
 * Represents a Facility with an ID, name, and description
 */
>>>>>>> f77e9d4d48fdcea5d4001088bff08a98922a3343
public class Facility {

    /**
     * ID for the facility
     */
    private String id;

    /**
     * Name of the facility
     */
    private String name;

    /**
     * Description of the facility
     */
    private String description;
    private String imageUrl;

<<<<<<< HEAD
    // Constructor
    public Facility(Map<String, Object> data) {
        this.name = (String) data.get("name");
        this.id = (String) data.get("id");
        this.imageUrl = (String) data.get("imageUrl");
        // Initialize other fields from the map
    }
=======
    /**
     * Constructs a Facility object with the specified ID, name, and description
     *
     * @param id ID for the facility
     * @param name Name of the facility
     * @param description Description of the facility
     */
>>>>>>> f77e9d4d48fdcea5d4001088bff08a98922a3343
    public Facility(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    /**
     * Default constructor for Firebase
     */
    public Facility() {}

    /**
     * Gets the unique identifier of the facility
     *
     * @return The ID of the facility.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the facility
     *
     * @param id The new ID of the facility
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the name of the facility
     *
     * @return The name of the facility
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the facility
     *
     * @param name The new name of the facility
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the facility
     *
     * @return The description of the facility
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the facility
     *
     * @param description The new description of the facility
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}