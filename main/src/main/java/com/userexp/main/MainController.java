package com.userexp.main;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.userexp.main.models.EmpActivities;
import com.userexp.main.models.DataRepository;
import com.userexp.main.models.UserRepository;
import com.userexp.main.models.UserTable;

import jsonmodels.Activities;
import jsonmodels.ActivityRespJson;
import jsonmodels.JsonData;
import jsonmodels.ResponseJson;
import jsonmodels.Stats;
import jsonmodels.TodaysActivityJson;

@Controller // This means that this class is a Controller
//@RequestMapping(path="/demo") // This means URL's start with /demo (after Application path)
public class MainController {
	@Autowired // This means to get the bean called userRepository
	     // Which is auto-generated by Spring, we will use it to handle the data
	private UserRepository userRepository;
	@Autowired // This means to get the bean called dataRepository
	private DataRepository employee;

		@PostMapping(path="/add") // Map ONLY POST Requests
	public @ResponseBody String addNewUser (@RequestParam String name
	  , @RequestParam String email) {
	// @ResponseBody means the returned String is the response, not a view name
	// @RequestParam means it is a parameter from the GET or POST request
	UserTable n = new UserTable();
	n.setName(name);
	n.setEmail(email);
	userRepository.save(n);
	return "Saved";
	}
		
		
		
	
	@GetMapping(path="/get-data")
	public @ResponseBody ResponseJson getData() {
		final Session session = HibernateUtil.getHibernateSession();
		EntityManager entity = session.getEntityManagerFactory().createEntityManager();
		Transaction tx = session.beginTransaction();
		
		//getting last 7 days stats
		String statsSQL = "select name, count(name) as occur from EmpActivities where start_time > :date group by name";
		List<Object[]> statsList = entity.createQuery(statsSQL).setParameter("date", Stats.getManupulatedDateDate(-7)).
				getResultList();
		List<Stats> statistics = new ArrayList<>();
		Iterator statsit = statsList.iterator();
		while(statsit.hasNext()){
			Stats stats = new Stats();
		    Object[] line = (Object[]) statsit.next();
		    stats.activity_name = (String) line[0];
		    stats.occurrences  = (long) line[1];
		    statistics.add(stats);
		}

		//Get unique ids 
		String empListSQL = "select distinct(employee_id) from EmpActivities where start_time between :pasday and :nextday";
		List<Object[]> empIdList = entity.createQuery(empListSQL).
				setParameter("pasday", Stats.getManupulatedDateDate(-1)).
				setParameter("nextday", Stats.getManupulatedDateDate(+1)).getResultList();
		Iterator empIt = empIdList.iterator();
		List<TodaysActivityJson> todays_activities = new ArrayList<TodaysActivityJson>();
		while(empIt.hasNext()){
			//Employeed id loop
		    long empId = (long) empIt.next();
			TodaysActivityJson activityJson = new TodaysActivityJson();
			List<ActivityRespJson> EmployeeActivities = new ArrayList<>();

		    String activitySQL = "select name, start_time from EmpActivities where employee_id = :empid and"
					+ " start_time between :pasday and :nextday";
		    List<Object[]> empActByDay = entity.createQuery(activitySQL).
					setParameter("empid", empId).
					setParameter("pasday", Stats.getManupulatedDateDate(-1)).
					setParameter("nextday", Stats.getManupulatedDateDate(+1)).getResultList();
		    Iterator empActByDayIt = empActByDay.iterator();
			while(empActByDayIt.hasNext()){
				ActivityRespJson activityRespJson = new ActivityRespJson();
			    Object[] line = (Object[]) empActByDayIt.next();
			    activityRespJson.name = (String) line[0];
			    activityRespJson.start_time  = (Timestamp) line[1];
			    EmployeeActivities.add(activityRespJson);
			}
			activityJson.activities = EmployeeActivities;
			activityJson.employee_id = empId;
			todays_activities.add(activityJson);
		}
		ResponseJson response = new ResponseJson(statistics, todays_activities);
		tx.commit();
		session.close();
		return response;
	}	
	
	
	@GetMapping(path="/insert-data")
	public @ResponseBody String insertData() throws JsonParseException, JsonMappingException, IOException {
		
		final Session session = HibernateUtil.getHibernateSession();
		EntityManager entity = session.getEntityManagerFactory().createEntityManager();
		Transaction tx = session.beginTransaction();
		File[] files = new File("D:\\Projects\\Java\\EmployeeActivitiesToBeProcessed").listFiles();
	    ObjectMapper objectMapper = new ObjectMapper();
		List<String> allowed_activities = Arrays.asList("teabreak", "lunchbreak", "login", "logout", "gamemood","naptime");
		
		for( File file: files) {
			FileReader reader = new FileReader(file);
			JsonData empJson = objectMapper.readValue(reader, JsonData.class);
			//getEmployyed id from file to check
			long empIdFromFile = empJson.employee_id;
			List<Object[]> objList = new ArrayList<>();
			for (Activities activity: empJson.activities) {
				//starttime from file to check
				Timestamp start_timeFile = activity.time;
				long empIdfromDB = 0; Timestamp start_timefromDB = null;
				//Query to check if data already exist
				String selectHql = "select employee_id, start_time from EmpActivities where employee_id = :empid"
						+ " and start_time = (:st)";
				objList = entity.createQuery(selectHql).setParameter( "empid", empIdFromFile)
						 .setParameter( "st"   , start_timeFile).getResultList();
				
				Iterator it = objList.iterator();
				while(it.hasNext()){
				     Object[] line = (Object[]) it.next();
				     empIdfromDB = (long) line[0];
				     start_timefromDB  = (Timestamp) line[1];
				}
				//Check if data exists
				if( empIdfromDB==0 && start_timefromDB == null && allowed_activities.contains(activity.name)) {
					EmpActivities emp = new EmpActivities(empIdFromFile, activity.name, activity.time, activity.duration );
					employee.save(emp);
				}

			}
		}
		tx.commit();
		session.close();
		return "Saved";
	}
}
