package com.kdillo.simple.entities;

import com.kdillo.simple.SimpleApp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.management.InvalidAttributeValueException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

/**
 * POJO User
 */
public class User {
    private static final Logger LOGGER = LogManager.getLogger(User.class);

    private static final int SALT_LENGTH = 32;

    private UUID uid;
    private String first_name;
    private String last_name;
    private String email;
    private Date created;
    private Date updated;
    private String password;

    //don't allow direct writing of the hash/salt, these will be generated by app.
    private String pass_hash;
    private String pass_salt;

    public User() {
        this.uid = null;
        this.first_name = null;
        this.last_name = null;
        this.email = null;
        this.created = null;
        this.updated = null;
        this.password = null;
    }

    /**
     * constructor for user creation (no id, timestamps, or pass hash/salt)
     */
    public User(String first_name, String last_name, String email, String password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
    }

    /**
     * constructor for retrieval from DB: populate fields from columns.
     */
    public User(UUID uid, String first_name, String last_name, String email, Date created, Date updated, String pass_hash, String pass_salt) {
        this.uid = uid;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.created = created;
        this.updated = updated;
        this.pass_hash = pass_hash;
        this.pass_salt = pass_salt;
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public boolean hasPassword() { return !this.password.isEmpty(); }

    public String getPassHash() {
        return pass_hash;
    }

    public String getPassSalt() {
        return pass_salt;
    }

    public void generateNewSalt() {
        this.pass_salt = SimpleApp.randomString(SALT_LENGTH);
    }

    /**
     * Digest the password with the salt on the User object and set the password hash.
     * <p></p>
     * This is for validating the User and saving a new user's password hash.
     */
    private void digestPasswordWithSHA512() throws InvalidAttributeValueException {

        if (this.password == null || this.pass_salt == null) {
            throw new InvalidAttributeValueException("Can't digest password without it existing on the object.");
        }

        try {
            MessageDigest md = MessageDigest.getInstance("SHA512");

            //combine the password and salt in some way, for now just append at end.
            String message = this.password + this.pass_salt;

            byte[] messageDigest = md.digest(message.getBytes());

            //convert byte into signum representation? seems like this is for proper char byte size conversion.
            //converts the very large value into string simply
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashTextSb = new StringBuilder(no.toString(16));

            //if the hash is too small, fill with 0's
            while(hashTextSb.length() < 32) hashTextSb.insert(0, '0');

            this.pass_hash = hashTextSb.toString();

        } catch (NoSuchAlgorithmException ex) {
            System.err.print("Couldn't find algorithm for SHA512");
            throw new RuntimeException(ex);
        } catch (Exception ex) {
            // unknown
        }
    }

    /**
     * Calculate the password hash and assign the pass_hash and pass_salt with new values
     * dependent on its password field.
     */
    public void calculatePassHashWithNewSalt() {

        if (!this.hasPassword()) {
            return;
        }

        this.generateNewSalt();

        try {
            this.digestPasswordWithSHA512();
        } catch (Exception ex) {
            LOGGER.debug("Unable to digest properly, check");
            ex.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", pass_hash='" + pass_hash + '\'' +
                ", pass_salt='" + pass_salt + '\'' +
                '}';
    }

}
