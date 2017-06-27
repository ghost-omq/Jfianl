package omq.helloTest;

import com.jfinal.core.Controller;

public class HelloJfinal extends Controller{

	public void index(){
		render("index.html");
	}
}
