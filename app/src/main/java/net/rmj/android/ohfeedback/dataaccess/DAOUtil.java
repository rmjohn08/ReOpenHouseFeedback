package net.rmj.android.ohfeedback.dataaccess;

/**
 * Created by Ronaldo on 9/17/2014.
 */

        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.*;
        import android.util.Log;
        import com.j256.ormlite.android.AndroidConnectionSource;
        import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
        import com.j256.ormlite.dao.Dao;
        import com.j256.ormlite.dao.DaoManager;
        import com.j256.ormlite.support.ConnectionSource;
        import com.j256.ormlite.table.TableUtils;

        import java.sql.SQLException;

        import net.rmj.android.ohfeedback.model.*;

@SuppressWarnings("UnusedDeclaration")
public class DAOUtil {

    private static DAOUtil dao=null;
    private static final int DATABASE_SCHEMA_VERSION = 8;

    private ConnectionSource connectionSource;

    public static DAOUtil getInstance(){

        if (dao==null) {
            dao = new DAOUtil();
        }
        return dao;

    }

    public void open(Context context) {
        SQLiteOpenHelper helper = new OrmLiteSqliteOpenHelper(context, "ohfeedback.db", new SQLiteDatabase.CursorFactory() {
            @Override
            public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query) {
                //noinspection deprecation
                return new SQLiteCursor(db, masterQuery, editTable, query);
            }
        }, DATABASE_SCHEMA_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
                try {
                    TableUtils.createTable(connectionSource, Location.class);
                    TableUtils.createTable(connectionSource, Questionaire.class);
                    TableUtils.createTable(connectionSource, Feedback.class);
                    TableUtils.createTable(connectionSource, LocationQuestion.class);
                    try {
                        Dao<Location, Long> dao = DaoManager.createDao(connectionSource, Location.class);

                        // some initial data
                        dao.executeRaw("INSERT INTO location(zipcode,agent_id,address,city) VALUES('68000',1,'1234 main street','main city');");

                        Dao<Questionaire, Long> daoq = DaoManager.createDao(connectionSource, Questionaire.class);
                        daoq.executeRaw("INSERT INTO questionaire (question, qn_type) VALUES ('Interest','number');");
                        daoq.executeRaw("INSERT INTO questionaire (question, qn_type) VALUES ('Exterior','number');");
                        daoq.executeRaw("INSERT INTO questionaire (question, qn_type) VALUES ('Curb appeal','number');");
                        daoq.executeRaw("INSERT INTO questionaire (question, qn_type) VALUES ('Neighborhood','number');");
                        daoq.executeRaw("INSERT INTO questionaire (question, qn_type) VALUES ('Interior','number');");
                        daoq.executeRaw("INSERT INTO questionaire (question, qn_type) VALUES ('Backyard','number');");
                        daoq.executeRaw("INSERT INTO questionaire (question, qn_type) VALUES ('Street','number');");

                    } catch(Exception ex) {
                        Log.e(getClass().getName(), "error populating tables", ex);
                    }

                } catch (SQLException e) {
                    Log.e(getClass().getName(), "error creating tables", e);
                }
            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {

            }
        };

        this.connectionSource = new AndroidConnectionSource(helper);
    }

    public ConnectionSource getConnectionSource() {
        if (connectionSource == null) {
            throw new IllegalStateException("You must call open() before attempting to get the connection source");
        }

        return connectionSource;
    }

    public <D extends com.j256.ormlite.dao.Dao<T,?>, T>  D getDAO(Class<T> klass) throws SQLException {
        return DaoManager.createDao(this.getConnectionSource(), klass);
    }

    public boolean isOpen() {
        return connectionSource != null;
    }

    public void close() {
        try {
            connectionSource.close();
            connectionSource = null;
        } catch (SQLException e) {
            Log.e(getClass().getName(), "error closing database", e);
        }
    }
}

