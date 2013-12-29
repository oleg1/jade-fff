package com.recruiters.web.controller.employer;

import com.recruiters.model.Bid;
import com.recruiters.model.Employer;
import com.recruiters.service.EmployerService;
import com.recruiters.web.controller.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller for C5 "Show recruiter info to employer"
 */
@Controller
public class EmployerRecruiterDeal {

    @Autowired
    private EmployerService employerService = null;
    @Autowired
    private UserUtils userUtils = null;

    /**
     * Method for C5 "Show recruiter info to employer page"
     * @param bidId    Id of corresponding bid
     * @param request  Http request
     * @return model and view with data
     */
    @RequestMapping(value = "employer-recruiter-show/{bidId}")
    public ModelAndView employerShowRecruiterBid(@PathVariable final Long bidId,
                                                 final HttpServletRequest request) {

        ModelAndView showBid = new ModelAndView("employer/employer-recruiter-show.jade");
        Long userId = userUtils.getCurrentUserId(request);
        Employer employer = employerService.findEmployerByUser(userId);

        if (employer != null) {
            Bid bid = employerService.findBidById(bidId, employer);
            showBid.addObject("bid", bid);
        }

        return showBid;
    }

    public EmployerService getEmployerService() {
        return employerService;
    }

    public void setEmployerService(final EmployerService employerService) {
        this.employerService = employerService;
    }
}
