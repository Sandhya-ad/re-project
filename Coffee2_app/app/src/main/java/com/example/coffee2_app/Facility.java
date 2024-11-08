package com.example.coffee2_app;

/**
 * Represents a Facility with an ID, name, and description
 */
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

    /**
     * Constructs a Facility object with the specified ID, name, and description
     *
     * @param id ID for the facility
     * @param name Name of the facility
     * @param description Description of the facility
     */
    public Facility(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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
}