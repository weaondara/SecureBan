package de.minecraftadmin.api.utils;

import de.minecraftadmin.api.entity.Note;

import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 25.02.13
 * Time: 19:30
 * To change this template use File | Settings | File Templates.
 */
public class NoteSorter implements Comparator<Note> {
    @Override
    public int compare(Note o1, Note o2) {
        if (o1.getId() < o2.getId()) return 1;
        if (o1.getId() > o2.getId()) return -1;
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
