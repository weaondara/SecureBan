package de.minecraftadmin.api.generated;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 02.01.13
 * Time: 20:17
 * To change this template use File | Settings | File Templates.
 */
public class Version {
    public final static short OLD = 0;
    public final static short SAME = 1;
    public final static short DEV = 2;
    public final static short UNKNOWN = 3;

    public final static String name = "${project.version}-${BUILD_NUMBER}";
}
