package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import play.data.validation.Constraints.*;
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
	@Inject
	FormFactory formFactory;

	@Security.Authenticated(Secured.class)
    public Result index() {
        return ok(index.render(Project.find.all(),Task.find.all()));
    }

    public Result login(){
    	return ok(login.render(formFactory.form(Login.class)));
    }
    
    public Result authenticate(){
    	Form<Login> loginForm = formFactory.form(Login.class).bindFromRequest();
    	if(loginForm.hasErrors()){
    		return badRequest(login.render(loginForm));
    	}
    	else{
    		session().clear();
    		session("email",loginForm.get().email);
    		return redirect(controllers.routes.HomeController.index());
    	}
    }
    
    public static class Login{
    	public String email;
    	public String password;
    	
    	public String validate(){
    		if(User.authenticate(email,password)==null){
    			return "Invalid user or password";
    		}
    		return null;
    	}
    }
}
