package net.rmj.android.ohfeedback.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Location Model
 */
@DatabaseTable(tableName="location")
public class Location {
	@DatabaseField(generatedId=true, columnName = "_id")
    private long id;

    private long locationId;

    @DatabaseField(columnName = "address")
	private String address;
    @DatabaseField(columnName = "city")
    private String city;
    @DatabaseField(columnName = "state")
	private String state;
    @DatabaseField(columnName = "lat")
	private double lat;
    @DatabaseField(columnName = "lon")
	private double lon;
    @DatabaseField(columnName = "pin_no")
	private String pinNo;
    @DatabaseField(columnName = "agent_id")
	private String agenId;
    @DatabaseField(columnName = "seller_email")
	private String sellerEmail;
    @DatabaseField(columnName = "zipcode")
	private String zipcode;

    @ForeignCollectionField(eager=true)
    private Collection<LocationQuestion> questions;


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public long getLocationId() {
		return locationId;
	}
	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public String getPinNo() {
		return pinNo;
	}
	public void setPinNo(String pinNo) {
		this.pinNo = pinNo;
	}
	public String getAgentId() {
		return agenId;
	}
	public void setAgentId(String agenId) {
		this.agenId = agenId;
	}
	public String getSellerEmail() {
		return sellerEmail;
	}
	public void setSellerEmail(String sellerEmail) {
		this.sellerEmail = sellerEmail;
	}

    public void setLocationQuestions(ForeignCollection<LocationQuestion> questions) {
        this.questions = questions;
    }

    public List<LocationQuestion> getLocationQuestions() {
        ArrayList<LocationQuestion> questionList = new ArrayList<LocationQuestion>();
        for(LocationQuestion l : questions) {
            questionList.add(l);
        }

        return questionList;
    }


}
