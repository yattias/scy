package eu.scy.server.controllers;

import eu.scy.core.MissionService;
import eu.scy.core.UserService;
import eu.scy.core.model.ELORef;
import eu.scy.core.model.User;
import eu.scy.core.model.impl.ELORefImpl;
import eu.scy.core.model.impl.SCYStudentUserDetails;
import eu.scy.core.model.impl.SCYUserImpl;
import eu.scy.core.model.impl.pedagogicalplan.MissionImpl;
import eu.scy.core.model.impl.playful.PlayfulAssessmentImpl;
import eu.scy.core.model.pedagogicalplan.Mission;
import eu.scy.core.model.playful.PlayfulAssessment;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bjoerge
 * @created 25.feb.2010 10:54:43
 */
public class CreateMockDataController extends PlayfulController {
	private UserService userService;
	private MissionService missionService;


	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

		Mission mission = createMockMission();

		ELORef eloRef = new ELORefImpl();
		eloRef.setELOURI("http://roolo//something");
		eloRef.setMission(mission);
		eloRef.setTitle("A funky mock-concept map");
		eloRef.setType("Concept map");
		List<User> students = userService.getStudents();
		eloRef.setAuthor(students.get(0));
		eloRef.setTool("SCYMapper");
		eloRef.setTopic("Global warming");
		eloRef.setVersion("1.0");

		getEloRefService().save(eloRef);

		crateMockAssessmentForEloRef(eloRef);

		return new ModelAndView();
	}

	private void crateMockAssessmentForEloRef(ELORef eloRef) {

		List<String> comments = new ArrayList<String>() {{
			add("Nice ELO");
			add("Awesome ELO");
			add("Ugly ELO");
			add("Awful ELO");
		}};
		List<Integer> ratings = new ArrayList<Integer>() {{
			add(8);
			add(10);
			add(4);
			add(1);
		}};

		List<User> students = userService.getStudents();
		for (int i = 0; i < students.size(); i++) {
			PlayfulAssessment assessment = new PlayfulAssessmentImpl();
			User student = students.get(i);
			assessment.setReviewer(student);
			if (comments.size() > i)
				assessment.setComment(comments.get(i));
			else
				assessment.setComment("Default mock comment!");
			if (ratings.size() > i)
				assessment.setScore(ratings.get(i));
			else
				assessment.setScore(5);

			assessment.setELORef(eloRef);
			getPlayfulAssessmentService().save(assessment);
		}
	}

	private Mission createMockMission() {
		Mission mission = new MissionImpl();
		mission.setDescription("Create C02 friendly house");
		missionService.save(mission);
		return mission;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setMissionService(MissionService missionService) {
		this.missionService = missionService;
	}

	public MissionService getMissionService() {
		return missionService;
	}
}
