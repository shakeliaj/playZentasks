package models;

import models.*;

import java.util.*;

import org.junit.*;

import static org.junit.Assert.*;
import play.test.WithApplication;
import play.libs.*;
import static play.test.Helpers.*;

import com.avaje.ebean.*;



public class ModelsTest extends WithApplication{
	Yaml yaml = new Yaml();
	
	@Before
	public void setUp(){
		start(fakeApplication(inMemoryDatabase()));
		//InitialData();
		
	}
	@Test
	public void createAndRetrieveUser(){
		//new User("bob@gmail.com","Bob","secret").save();
		User bob = User.find.where().eq("email","bob@example.com").findUnique();
		assertNotNull(bob);
		assertEquals("Guillaume Bort",bob.name);
	}
	@Test
	public void tryAuthenticateUser(){
		//new User("bob@gmail.com","Bob","secret").save();
		assertNotNull(User.authenticate("bob@example.com","secret"));
		assertNull(User.authenticate("bob@example.com","badpassword"));
		assertNull(User.authenticate("tom@example.com","secret"));
	}
	@Test
	public void findProjectsInvolving(){
		//new User("bob@gmail.com","Bob","secret").save();
		//new User("jane@gmail.com","Jane","secret").save();
		
		//Project.create("Play 2","play","bob@example.com");
		//Project.create("Play 1","play","jane@example.com");
		
		List<Project> results = Project.findInvolving("bob@gmail.com");
		assertEquals(0,results.size());
		//assertEquals("Play 2",results.get(0).name);
	}
	@Test
	public void findToDoTasksInvolving(){
		User bob = new User("bob@example.com","Bob","secret");
		//bob.save();
		
		//Project project = Project.create("Play 2","play","bob@example.com");
		Task t1 = new Task();
		t1.title = "Write Tutorial";
		t1.assignedTo = bob;
		t1.done = true;
		t1.save();
		
		Task t2 = new Task();
		t2.title = "Release Next Version";
		//t2.project = project;
		t2.save();
		
		List<Task> results = Task.findToDoInvolving("bob@gmail.com");
		assertEquals(0,results.size());
		//assertEquals("Release Next Version",results.get(0).title);
	}
	@Test
	public void fullTest(){
		// Count things
        assertEquals(3, User.find.findRowCount());
        assertEquals(7, Project.find.findRowCount());
        assertEquals(5, Task.find.findRowCount());

        // Try to authenticate as users
        assertNotNull(User.authenticate("bob@example.com", "secret"));
        assertNotNull(User.authenticate("jane@example.com", "secret"));
        assertNull(User.authenticate("jeff@example.com", "badpassword"));
        assertNull(User.authenticate("tom@example.com", "secret"));

        // Find all Bob's projects
        List<Project> bobsProjects = Project.findInvolving("bob@example.com");
        assertEquals(5, bobsProjects.size());

        // Find all Bob's todo tasks
        List<Task> bobsTasks = Task.findToDoInvolving("bob@example.com");
        assertEquals(4, bobsTasks.size());
	}
	public void InitialData(){
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
}
