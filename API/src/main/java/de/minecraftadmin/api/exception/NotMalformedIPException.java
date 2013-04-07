package de.minecraftadmin.api.exception;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 29.03.13
 * Time: 16:57
 * To change this template use File | Settings | File Templates.
 */
public class NotMalformedIPException extends Exception {

    public NotMalformedIPException(String ip) {
        super(ip + " is not a malformed IP Address");
    }
}
