package de.minecraftadmin.api.exception;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 27.03.13
 * Time: 10:43
 * To change this template use File | Settings | File Templates.
 */
public class WrongBanTypeException extends RuntimeException {

    public WrongBanTypeException() {
        super("Submitted ban ist not a globalban");
    }
}
