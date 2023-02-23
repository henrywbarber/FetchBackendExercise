// Fetch Backend Take Home Test
// Filename: transaction.java
// Name: Henry Barber
// Email: henrywbarber@gmail.com
// Date: 02/22/2023

import java.time.Instant;

/**
 * This class defines an object that outlines a transaction: including a payer, their points, 
 * and the timestamp of the transaction.
 */
public class transaction {
    String payer;
    int points;
    Instant timestamp;

    /**
     * Constructs a transaction object and defines its fields based on arguments
     * 
     * @param payer Name of payer of transaction
     * @param points Amount of points in transaction
     * @param timestamp Instant the transaction took place
     */
    public transaction(String payer, String points, String timestamp) {
        this.payer = payer;
        this.points = Integer.valueOf(points);
        this.timestamp = Instant.parse(timestamp);
    }

    /**
     * Gets the Instance timestamp and returns its value
     * 
     * @return Instant defining timestamp of this transaction
     */
    public Instant getTimestamp() {
        return this.timestamp;
    }

    /**
     * Adds points to points of this transaction instance
     * 
     * @param points amount of points to add
     */
    public void addPoints(int points) {
        this.points = this.points + points;
    }
    /**
     * Gets the name of the transaction payer
     * 
     * @return name of payer
     */
    public String getPayer() {
        return this.payer;
    }
}