package eu.scy.core;

import eu.scy.core.model.SCYGrantedAuthority;
import eu.scy.core.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.des.2009
 * Time: 10:25:49
 * To change this template use File | Settings | File Templates.
 */
public interface UserService extends BaseService{

    public User getUser(String username);

    User createUser(String username, String password, String role);

    public void deleteUser(User user);

    public List<User> getUsers();

    public List<User> getStudents();

    public User save(User user);

    public List <SCYGrantedAuthority> getGrantedAuthorities();
}
