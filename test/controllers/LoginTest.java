package controllers;

import org.junit.*;

import static org.junit.Assert.*;

import java.util.*;

import play.mvc.*;
import play.libs.*;
import play.test.*;
import static play.test.Helpers.*;

import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;


public class LoginTest extends WithApplication{
	@Before
	public void setUp(){
		start(fakeApplication(inMemoryDatabase()));
		@SuppressWarnings("unchecked")
		Map<String, List<Object>> all = (Map<String, List<Object>>) Yaml.load("test-data.yml");
		
		for(Object user: all.get("users")){
			//save users
			Ebean.save(user);
		}
		for(Object project: all.get("projects")) {
			//save projects
			Ebean.save(project);
			// Insert the project/user relation
			Ebean.saveManyToManyAssociations(project, "members");
		}
		for(Object task: all.get("tasks")){
			//save tasks
			Ebean.save(task);
		}
	}
	@Test
	public void authenticateSuccess(){
		Result result = callAction(controllers.routes.ref.HomeController.authenticate(),
				fakeRequest().withFormUrlEncodedBody(ImmutableMap.of("email","bob@example.com","password","secret"))
		);
		assertEquals(302, result.status());
		assertEquals("bob@example.com",result.session().get("email"));
	}
}
