package com.recruiters.web.controller.recruiter;

import com.recruiters.model.Deal;
import com.recruiters.model.User;
import com.recruiters.service.RecruiterService;
import com.recruiters.service.ServiceException;
import com.recruiters.web.controller.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Shows deals for recruiter
 */
@Controller
public class RecruiterDeals {

    /** User utils for obtaining any session user information */
    @Autowired
    private UserUtils userUtils = null;
    /** Recruiter Service provides all Recruiter related methods */
    @Autowired
    private RecruiterService recruiterService = null;

    /**
     * Displays all active deals for current recruiter
     * @param request        Http Request
     * @param response       Http Response
     * @return model and view with list of all current recruiter deals,
     * Internal Server Error page if something is wrong with obtaining
     * data due to technical or any other reasons
     * @throws Exception in very rare circumstances: it should be runtime
     * or servlet Exception to be thrown
     */
    @RequestMapping(value = "/{locale}/recruiter-active-deals", method = RequestMethod.GET)
    public ModelAndView showMyVacancies(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws  Exception {
        ModelAndView activeDeals = new ModelAndView("recruiter/recruiter-active-deals.jade");
        try {
            User user = userUtils.getCurrentUser(request);
            List<Deal> deals = recruiterService.findActiveDealsForRecruiter(user.getRecruiterId());
            activeDeals.addObject("deals", deals);
        } catch (ServiceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        return activeDeals;
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
