package de.minecraftadmin.api.exception;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 29.03.13
 * Time: 08:57
 * To change this template use File | Settings | File Templates.
 */
public class NotAHashedIpException extends RuntimeException {

    public NotAHashedIpException() {
        super("Submitted IP is not a Hash");
    }

}
