package com.recruiters.web.controller.employer;

import com.recruiters.model.Deal;
import com.recruiters.model.User;
import com.recruiters.service.*;
import com.recruiters.service.exception.NotAffiliatedException;
import com.recruiters.service.exception.NotFoundException;
import com.recruiters.service.exception.ServiceException;
import com.recruiters.web.controller.utils.UserUtils;
import com.recruiters.web.helper.UrlResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Show Deal for Employer with all corresponding actions
 */
@Controller
public class EmployerDeal {

    /** Employer Service provides all Employer related methods */
    @Autowired
    private EmployerService employerService = null;
    /** User utils for obtaining any session user information */
    @Autowired
    private UserUtils userUtils = null;
    /** Url Builder */
    @Autowired
    private UrlResolver urlResolver;

    /**
     * Show progress of vacancy (Deal)
     * @param dealId      Id of deal
     * @param request     Http request
     * @param response    Http response
     * @return model and view with deal details,
     * Forbidden page if requested deal does not belong to this employer,
     * Not Found page if there is no deal with such id,
     * Internal Server Error page if something is wrong with obtaining data
     * due to technical or any other reasons
     * @throws Exception in very rare circumstances: it should be runtime
     * or servlet Exception to be thrown
     */
    @RequestMapping(value = "/employer-progress-vacancy-show/{dealId}", method = RequestMethod.GET)
    public ModelAndView showVacancyProgressForEmployer(
            @PathVariable final Long dealId,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        ModelAndView vacancyProgress =  new ModelAndView("employer/employer-progress-vacancy-show.jade");
        try {
            User user = userUtils.getCurrentUser(request);
            Deal deal = employerService.findDeal(dealId, user.getEmployerId());
            vacancyProgress.addObject("deal", deal);
        } catch (NotAffiliatedException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        } catch (ServiceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        } catch (NotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        return vacancyProgress;
    }

    /**
     * Fire Recruiter for Employer
     * @param dealId      Id of deal
     * @param request     Http request
     * @param response    Http response
     * @return redirect to "vacancies list" page if everything goes fine,
     * Forbidden page if this deal does not belong to this employer,
     * Internal Server Error page if something is wrong with obtaining data
     * due to technical or any other reasons
     * @throws Exception in very rare circumstances: it should be runtime
     * or servlet Exception to be thrown
     */
    @RequestMapping(value = "/employer-fire-recruiter", method = RequestMethod.POST)
    public String fireRecruiter(
            @RequestParam(value = "id", required = true) final Long dealId,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        try {
            User user = userUtils.getCurrentUser(request);
            employerService.fireRecruiter(dealId, user.getEmployerId());
        } catch (NotAffiliatedException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        } catch (ServiceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
        Locale locale = RequestContextUtils.getLocale(request);

        return  urlResolver.buildRedirectUri("employer-progress-vacancy-show", dealId, locale);
    }


    /**
     * Leave feedback for deal ended
     * @param feedback    Feedback
     * @param dealId      Id of deal
     * @param request     Http request
     * @param response    Http response
     * @return redirect to "vacancies list" page if everything goes fine,
     * Forbidden page if this deal does not belong to this employer,
     * Internal Server Error page if something is wrong with obtaining data
     * due to technical or any other reasons
     * @throws Exception in very rare circumstances: it should be runtime
     * or servlet Exception to be thrown
     */
    @RequestMapping(value = "/employer-deal-leave-feedback", method = RequestMethod.POST)
    public String leaveFeedback(
            @RequestParam(value = "feedback", required = false) final String feedback,
            @RequestParam(value = "id", required = true) final Long dealId,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        try {
            User user = userUtils.getCurrentUser(request);
            employerService.leaveFeedback(dealId, feedback, user.getEmployerId());
        } catch (NotAffiliatedException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        } catch (ServiceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
        Locale locale = RequestContextUtils.getLocale(request);

        return  urlResolver.buildRedirectUri("employer-progress-vacancies-list", locale);
    }


    public EmployerService getEmployerService() {
        return employerService;
    }

    public void setEmployerService(final EmployerService employerService) {
        this.employerService = employerService;
    }
}
