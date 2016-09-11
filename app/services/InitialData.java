package services;


import java.util.*;

import play.libs.*;

import com.avaje.ebean.*;

import javax.inject.*;





@Singleton
public class InitialData {

	@Inject
	public InitialData(){
		
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
}
