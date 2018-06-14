package org.launchcode.controllers;

import org.launchcode.models.*;
import org.launchcode.models.forms.JobForm;
import org.launchcode.models.data.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.lang.model.element.Name;
import javax.validation.Valid;
import java.util.ArrayList;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, int id) {

        Job jobs = jobData.findById(id);

        model.addAttribute("jobs", jobs);
        model.addAttribute("title", "Jobs with ID [" + id + "]");

        return "job-detail";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @Valid JobForm jobForm, Errors errors) {

        // TODO #6 - Validate the JobForm model, and if valid, create a
        // new Job and add it to the jobData data store. Then
        // redirect to the job detail view for the new Job.
        model.addAttribute(jobForm);
        if (errors.hasErrors()) {
            model.addAttribute(jobForm);
            return "new-job";
        }
            Job newJob = new Job();
            newJob.setName(jobForm.getName());

            // Get the employer id
            int employerId = jobForm.getEmployerId();
            // Get all employers
            ArrayList<Employer> employers = jobForm.getEmployers();

            // Iterate through all employers
            for(Employer employer : employers) {
                // If this current employer has the same id as what was given to us,
                // then we have found the correct employer.
                if(employer.getId() == employerId) {
                    newJob.setEmployer(employer);
                    break;
                }
            }

            String location = jobForm.getLocation();
            ArrayList<Location> locations = jobForm.getLocations();
            for (Location aLocation : locations) {
                if (aLocation.getValue().equals(location)) {
                    newJob.setLocation(aLocation);
                    break;
                }
            }

        String coreCompetency = jobForm.getCoreCompetency();
        ArrayList<CoreCompetency> skills = jobForm.getCoreCompetencies();
        for (CoreCompetency skill : skills) {
            if (skill.getValue().equals(coreCompetency)) {
                newJob.setCoreCompetency(skill);
                break;
            }
        }

        String positionType = jobForm.getPositionType();
        ArrayList<PositionType> positions = jobForm.getPositionTypes();
        for (PositionType position : positions) {
            if (position.getValue().equals(positionType)) {
                newJob.setPositionType(position);
                break;
            }
        }

            jobData.add(newJob);


            model.addAttribute("jobs", newJob);

        return "redirect:/job?id=" + newJob.getId();
    }
}
