package de.minecraftadmin.secureban.system;

import com.avaje.ebean.config.MatchingNamingConvention;
import com.avaje.ebean.config.NamingConvention;
import com.avaje.ebean.config.TableName;
import com.avaje.ebean.config.dbplatform.DatabasePlatform;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 10.01.13
 * Time: 01:57
 * To change this template use File | Settings | File Templates.
 */
public class NameSpace implements NamingConvention {

    private final String nameSpace;

    public NameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    @Override
    public void setDatabasePlatform(DatabasePlatform databasePlatform) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TableName getTableName(Class<?> aClass) {
        return new TableName(nameSpace + "_" + aClass.getSimpleName());
    }

    @Override
    public TableName getM2MJoinTableName(TableName tableName, TableName tableName2) {
        return new MatchingNamingConvention().getM2MJoinTableName(tableName, tableName2);
    }

    @Override
    public String getColumnFromProperty(Class<?> aClass, String s) {
        return new MatchingNamingConvention().getColumnFromProperty(aClass, s);
    }

    @Override
    public String getPropertyFromColumn(Class<?> aClass, String s) {
        return new MatchingNamingConvention().getPropertyFromColumn(aClass, s);
    }

    @Override
    public String getSequenceName(String s, String s2) {
        return new MatchingNamingConvention().getSequenceName(s, s2);
    }

    @Override
    public boolean isUseForeignKeyPrefix() {
        return true;
    }
}
