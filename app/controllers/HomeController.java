package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import javax.inject.*;

import services.InitialData;
import models.*;
import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
	
	@Inject
	InitialData initialData;
	FormFactory formFactory;

    public Result index() {
        return ok(index.render(Project.find.all(),Task.find.all()));
    }

    public Result login(){
    	return ok(login.render(formFactory.form(Login.class)));
    }
    
    public static class Login{
    	public String email;
    	public String password;
    }
}
