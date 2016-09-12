package services;


import java.util.*;

import play.libs.*;

import com.avaje.ebean.*;

import javax.inject.*;


@Singleton
public class InitialData {
	
	public static int initialDataInit = 0;

	@Inject
	public InitialData(){
			
		if(initialDataInit == 0){	
			initialDataInit = 1;
		
			@SuppressWarnings("unchecked")
			Map<String,List<Object>> all = (Map<String,List<Object>>) Yaml.load("initial-data.yml");

			for(Object user: all.get("users")){
				Ebean.save(user);
			}
			for(Object project: all.get("projects")){
				Ebean.save(project);
				Ebean.saveManyToManyAssociations(project,"members");
			}
			for(Object task: all.get("tasks")){
				Ebean.save(task);
			}
		}
		else{}
	}
}
