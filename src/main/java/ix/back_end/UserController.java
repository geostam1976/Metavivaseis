package ix.back_end;

import java.util.Optional;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@RestController // This means that this class is a Controller
@RequestMapping(path="/user") // This means URL's start with /user (after Application path)
public class UserController {
	@Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
	private UserRepository userRepository;
	

	 @PostMapping(path="/add") // Map ONLY POST Requests
	 public @ResponseBody String addNewUser(@RequestBody User newUser){
            // @ResponseBody means the returned String is the response, not a view name
	    // @RequestParam means it is a parameter from the GET or POST request

	    User item = new User(newUser.getRole(), newUser.getEmail(), newUser.getPassword(), 
                    newUser.getFname(), newUser.getLname(), newUser.getAfm());
	    userRepository.save(item);
	    return "Saved";
	  }

	 
	  @GetMapping(path="/all")
	  public @ResponseBody Iterable<User> getAllUsers() {
	    // This returns a JSON or XML with the users
	    return userRepository.findAll();
	  }
	  
	  
	  @GetMapping("/get/{id}")
	  Optional<User> one(@PathVariable Integer id) {
		// Single record
	    return userRepository.findById(id);
	  }
	
	  @PostMapping("/edit/{id}")
	  User replaceUser(@RequestBody User newUser, @PathVariable Integer id) {
	    //edit or create if not exists
	    return userRepository.findById(id)
	      .map(user -> {
	    	user.setRole(newUser.getRole());
	    	user.setEmail(newUser.getEmail());
	    	user.setPassword(newUser.getPassword());
	    	user.setFname(newUser.getFname());
	    	user.setLname(newUser.getLname());
	    	user.setAfm(newUser.getAfm());
	        return userRepository.save(user);
	      })
	      .orElseGet(() -> {
	        newUser.setId(id);
	        return userRepository.save(newUser);
	      });
	  }

        @PostMapping("/delete/{id}")
	  public @ResponseBody String  delete(@RequestBody User newUser, @PathVariable Integer id) {
	    //edit or create if not exists
	     userRepository.deleteById(id);
             return "deleted";
	  }
  
          
          
        @PostMapping(path="/login")
	User login(@RequestBody User newUser) {
                // Single user
                
                Iterable<User> users = userRepository.findAll();
                for(User u: users){
                    if(newUser.getEmail().equals(u.getEmail()) && newUser.getPassword().equals(u.getPassword())){
                        return u;
                    }
                }
                return null;
	  }

}