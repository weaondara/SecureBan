package de.minecraftadmin.api.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 29.03.13
 * Time: 17:04
 * To change this template use File | Settings | File Templates.
 */
public class IPValidator {

    private final static Pattern REGEX = Pattern.compile("^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

    public static boolean isValid(String ip) {
        Matcher m = REGEX.matcher(ip);
        return m.matches();
    }
}
