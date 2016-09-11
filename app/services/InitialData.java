package services;

import java.time.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import play.libs.*;

import com.avaje.ebean.*;

import javax.inject.*;

import play.inject.ApplicationLifecycle;



@Singleton
public class InitialData {
	private final Clock clock;
	private final Instant start;
	private final ApplicationLifecycle appLifecycle;
	@Inject
	public InitialData(Clock clock,ApplicationLifecycle appLifecycle){
		this.clock = clock;
		this.appLifecycle = appLifecycle;
		start = clock.instant();
		
		appLifecycle.addStopHook(() -> {
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
			
            return CompletableFuture.completedFuture(null);
		});
	}
}
