package jsonmodels;

import java.util.List;

public class ResponseJson {

	public List<Stats> all_employees_last_7_days_statistics;
	public List<TodaysActivityJson> todays_activites;
	public ResponseJson(List<Stats> all_employees_last_7_days_statistics, List<TodaysActivityJson> todays_activities) {
		super();
		this.all_employees_last_7_days_statistics = all_employees_last_7_days_statistics;
		this.todays_activites = todays_activities;
	}
}