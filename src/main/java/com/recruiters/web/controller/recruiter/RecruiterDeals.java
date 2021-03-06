package com.recruiters.web.controller.recruiter;

import com.recruiters.model.Deal;
import com.recruiters.model.User;
import com.recruiters.service.RecruiterService;
import com.recruiters.service.exception.ServiceException;
import com.recruiters.web.controller.utils.UserUtils;
import com.recruiters.web.helper.UrlResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;

/**
 * Shows deals for recruiter with all corresponding actions
 */
@Controller
public class RecruiterDeals {

    /** User utils for obtaining any session user information */
    @Autowired
    private UserUtils userUtils = null;
    /** Recruiter Service provides all Recruiter related methods */
    @Autowired
    private RecruiterService recruiterService = null;
    /** Url Builder */
    @Autowired
    private UrlResolver urlResolver;

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
    @RequestMapping(value = "/recruiter-active-deals", method = RequestMethod.GET)
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
            return null;
        }

        return activeDeals;
    }

    /**
     * Clear fire deals for current recruiter
     * @param request        Http Request
     * @param response       Http Response
     * @return redirect to list of all active deals for current recruiter,
     * Internal Server Error page if something is wrong with performing
     * command due to technical or any other reasons
     * @throws Exception in very rare circumstances: it should be runtime
     * or servlet Exception to be thrown
     */
    @RequestMapping(value = "/recruiter-clear-fired-deals", method = RequestMethod.GET)
    public String clearFiredDeals(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws  Exception {
        try {
            User user = userUtils.getCurrentUser(request);
            recruiterService.clearFiredDealsForRecruiter(user.getRecruiterId());
        } catch (ServiceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
        Locale locale = RequestContextUtils.getLocale(request);

        return  urlResolver.buildRedirectUri("recruiter-active-deals", locale);
    }

    /**
     * Clear approved deals for current recruiter
     * @param request        Http Request
     * @param response       Http Response
     * @return redirect to list of all active deals for current recruiter,
     * Internal Server Error page if something is wrong with performing
     * command due to technical or any other reasons
     * @throws Exception in very rare circumstances: it should be runtime
     * or servlet Exception to be thrown
     */
    @RequestMapping(value = "/recruiter-clear-approved-deals", method = RequestMethod.GET)
    public String clearApprovedDeals(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws  Exception {
        try {
            User user = userUtils.getCurrentUser(request);
            recruiterService.clearApprovedDealsForRecruiter(user.getRecruiterId());
        } catch (ServiceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
        Locale locale = RequestContextUtils.getLocale(request);

        return  urlResolver.buildRedirectUri("recruiter-active-deals", locale);
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
