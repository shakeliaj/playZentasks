package controllers;

import org.junit.*;

import static org.junit.Assert.*;

import java.util.*;
import play.mvc.*;
import play.mvc.Http.*;
import play.libs.*;
import play.test.*;
import play.Logger;
import static play.test.Helpers.*;


import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;



public class LoginTest extends WithApplication{
	
	static RequestBuilder requestBuilder = new RequestBuilder();
	
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
		Map<String,String> fillInfo = new HashMap<>();
		fillInfo.put("email","bob@example.com");
		fillInfo.put("password","secret");
		final Call tryAuthenticate = controllers.routes.HomeController.authenticate();
		Result result = route(requestBuilder.
				method(tryAuthenticate.method())
				.uri(tryAuthenticate.url())
				.bodyForm(fillInfo)
		);
		assertEquals(303, result.status());
		assertEquals("bob@example.com",result.session().get("email"));
	}
	@Test
	public void authenticateFailure(){
		Map<String,String> info = new HashMap<>();
		info.put("email","bob@example.com");
		info.put("password","badpassword");
		final Call tryAuthenticate = controllers.routes.HomeController.authenticate();
		Result result = route(requestBuilder.
				method(tryAuthenticate.method())
				.uri(tryAuthenticate.url())
				.bodyForm(info)	
		);
		assertEquals(400,result.status());
		assertNull(result.session().get("email"));
	}
	@Test
	public void authenticated(){
		Map<String,String> fillInfo = new HashMap<>();
		fillInfo.put("email","bob@example.com");
		fillInfo.put("password","secret");
		Call tryThis = controllers.routes.HomeController.index();
		Result result = route(requestBuilder
				.method(tryThis.method())
				.uri(tryThis.url())
				.session(fillInfo)
		);
		assertEquals(200,result.status());
	}
	@Test
	public void notAuthenticated(){
		Call tryThis = controllers.routes.HomeController.index();
		Result result = route(requestBuilder
				.method(tryThis.method())
				.uri(tryThis.url())
		);
		assertEquals(303,result.status());
		assertEquals("/login",result.header("Location").get());
	}
}
