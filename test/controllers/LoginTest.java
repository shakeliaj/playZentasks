package controllers;

import org.junit.*;

import static org.junit.Assert.*;

import java.util.*;

import play.mvc.*;
import play.libs.*;
import play.test.*;
import static play.test.Helpers.*;
import play.data.FormFactory;
import play.data.Form;

import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;
import controllers.HomeController.Login;
import javax.inject.*;


public class LoginTest extends WithApplication{
	@Inject
	FormFactory formFactory;
	
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
		Form<HomeController.Login> info = new Form<HomeController.Login>();
		info.put("email","bob@example.com");
		info.put("password","secret");
		Result result = callAction(
			controllers.routes.HomeController.authenticate(),
			fakeRequest().withFormUrlEncodedBody(formFactory.form(Login.class).bind(info))
		);
		assertEquals(302, result.status());
		assertEquals("bob@example.com",result.session().get("email"));
	}
	@Test
	public void authenticateFailure(){
		Result result = callAction(
			controllers.routes.HomeController.authenticate(),
			fakeRequest().withFormUrlBodyEncodedBody(ImmutableMap.of("email","bob@example.com","password","badpassword"))
		);
		assertEquals(400,result.status());
		assertNull(result.session().get("email"));
		
	}
}
