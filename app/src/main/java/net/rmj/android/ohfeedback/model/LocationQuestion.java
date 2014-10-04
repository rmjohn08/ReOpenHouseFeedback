package net.rmj.android.ohfeedback.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ronaldo on 9/17/2014.
 */
@DatabaseTable(tableName="location_question")
public class LocationQuestion {

    @DatabaseField(generatedId=true)
    private long id;

    @DatabaseField(foreign = true, canBeNull = false,foreignAutoRefresh = true)
    private Questionaire question;

    @DatabaseField(foreign = true, canBeNull = false,foreignAutoRefresh = true)
    private Location location;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Questionaire getQuestion() {
        return question;
    }

    public void setQuestion(Questionaire question) {
        this.question = question;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


}
