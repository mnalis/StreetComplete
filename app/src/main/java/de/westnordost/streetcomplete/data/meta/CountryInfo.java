package de.westnordost.streetcomplete.data.meta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CountryInfo implements Serializable, Cloneable
{
	public static final long serialVersionUID = 1L;

	// this value is not defined in the yaml file but it is the ISO language code part of the file name!
	// i.e. US for US-TX.yml
	String countryCode;

	List<String> lengthUnits;
	List<String> speedUnits;
	List<String> weightLimitUnits;
	List<String> popularSports;
	List<String> popularReligions;
	String firstDayOfWorkweek;
	Integer regularShoppingDays;
	Integer workweekDays;
	String additionalValidHousenumberRegex;
	List<String> officialLanguages;
	List<String> additionalStreetsignLanguages;
	Boolean isSlowZoneKnown;
	String slowZoneLabelText;
	String slowZoneLabelPosition;
	Boolean isLivingStreetKnown;
	List<String> orchardProduces;
	Boolean isAdvisorySpeedLimitKnown;
	Boolean isLeftHandTraffic;
	Integer mobileCountryCode;
	List<String> chargingStationOperators;
	List<String> clothesContainerOperators;
	List<String> atmOperators;
	Boolean isUsuallyAnyGlassRecycleableInContainers;
	Boolean isCenterLeftTurnLaneKnown;
	String centerLine;
	String shoulderLine;
	Boolean roofsAreUsuallyFlat;

	public Boolean isRoofsAreUsuallyFlat() { return roofsAreUsuallyFlat; }

	public String getCenterLine() { return centerLine; }
	public String getShoulderLine() { return shoulderLine; }

	public Boolean isCenterLeftTurnLaneKnown() {
		return isCenterLeftTurnLaneKnown;
	}

	public Boolean isUsuallyAnyGlassRecycleableInContainers() {
		return isUsuallyAnyGlassRecycleableInContainers;
	}

	public List<String> getChargingStationOperators() {
		return chargingStationOperators;
	}

	public List<String> getClothesContainerOperators() {
		return clothesContainerOperators;
	}

	public List<String> getAtmOperators() {
		return atmOperators;
	}

	public List<String> getLengthUnits()
	{
		return lengthUnits;
	}

	public List<String> getSpeedUnits()
	{
		return speedUnits;
	}

	public List<String> getWeightLimitUnits()
	{
		return weightLimitUnits;
	}

	public List<String> getPopularSports()
	{
		if(popularSports == null) return new ArrayList<>(1);
		return Collections.unmodifiableList(popularSports);
	}

	public List<String> getPopularReligions()
	{
		if(popularReligions == null) return new ArrayList<>(1);
		return Collections.unmodifiableList(popularReligions);
	}

	public String getFirstDayOfWorkweek()
	{
		return firstDayOfWorkweek;
	}

	public Integer getRegularShoppingDays()
	{
		return regularShoppingDays;
	}

	public Integer getWorkweekDays()
	{
		return workweekDays;
	}

	public boolean isSlowZoneKnown()
	{
		return isSlowZoneKnown;
	}

	public String getSlowZoneLabelText()
	{
		return slowZoneLabelText;
	}

	public String getSlowZoneLabelPosition()
	{
		return slowZoneLabelPosition;
	}

	public String getAdditionalValidHousenumberRegex()
	{
		return additionalValidHousenumberRegex;
	}

	public List<String> getOfficialLanguages()
	{
		if(officialLanguages == null) return new ArrayList<>(1);
		return Collections.unmodifiableList(officialLanguages);
	}

	public List<String> getAdditionalStreetsignLanguages()
	{
		if(additionalStreetsignLanguages == null) return new ArrayList<>(1);
		return Collections.unmodifiableList(additionalStreetsignLanguages);
	}

	public String getCountryCode()
	{
		return countryCode;
	}

	public Locale getLocale()
	{
		List<String> languages = getOfficialLanguages();
		if (!languages.isEmpty())
		{
			return new Locale(languages.get(0), countryCode);
		}
		return Locale.getDefault();
	}

	public boolean isLivingStreetKnown()
	{
		return isLivingStreetKnown;
	}

	public List<String> getOrchardProduces()
	{
		if(orchardProduces == null) return new ArrayList<>(1);
		return Collections.unmodifiableList(orchardProduces);
	}

	public boolean isAdvisorySpeedLimitKnown()
	{
		return isAdvisorySpeedLimitKnown;
	}

	public boolean isLeftHandTraffic()
	{
		return isLeftHandTraffic;
	}

	public Integer getMobileCountryCode()
	{
		return mobileCountryCode;
	}
}
