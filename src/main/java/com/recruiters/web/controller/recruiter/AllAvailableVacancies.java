package com.recruiters.web.controller.recruiter;

import com.recruiters.model.User;
import com.recruiters.service.RecruiterService;
import com.recruiters.web.controller.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller Class for R11 "Show active vacancies"
 */
@Controller
public class AllAvailableVacancies {

    @Autowired
    private RecruiterService recruiterService = null;
    @Autowired
    private UserUtils userUtils = null;

    /**
     * Controller for R11 "Active vacancies list"
     * @return model and view with list of active vacancies
     */
    @RequestMapping(value = "recruiter-find-new-vacancies", method = RequestMethod.GET)
    public ModelAndView showAllAvailableVacancies(final HttpServletRequest request) {

        ModelAndView allAvailableVacancies = new ModelAndView("recruiter/recruiter-find-new-vacancies.jade");
        User user = userUtils.getCurrentUser(request);
        allAvailableVacancies.addObject("vacancies", getRecruiterService().findAvailableVacanciesForRecruiter(user.getRecruiterId()));

        return allAvailableVacancies;
    }

    public RecruiterService getRecruiterService() {
        return recruiterService;
    }

    public void setRecruiterService(final RecruiterService recruiterService) {
        this.recruiterService = recruiterService;
    }

    public UserUtils getUserUtils() {
        return userUtils;
    }

    public void setUserUtils(final UserUtils userUtils) {
        this.userUtils = userUtils;
    }
}