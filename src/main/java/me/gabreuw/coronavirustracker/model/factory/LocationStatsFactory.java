package me.gabreuw.coronavirustracker.model.factory;

import me.gabreuw.coronavirustracker.model.entities.LocationStats;
import org.apache.commons.csv.CSVRecord;

public class LocationStatsFactory {

    public static LocationStats createFrom(CSVRecord record) {
        LocationStats locationStats = new LocationStats();

        int latestCases = Integer.parseInt(record.get(record.size() - 1));
        int prevDayCases = Integer.parseInt(record.get(record.size() - 2));

        locationStats.setState(record.get("Province/State"));
        locationStats.setCountry(record.get("Country/Region"));
        locationStats.setLatestTotalCases(latestCases);
        locationStats.setDiffFromPrevDay(latestCases - prevDayCases);

        return locationStats;
    }

}
