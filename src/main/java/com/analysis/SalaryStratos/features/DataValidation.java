package com.analysis.SalaryStratos.features;

import com.analysis.SalaryStratos.models.Job;
import com.analysis.SalaryStratos.models.JobValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

@Service
public class DataValidation {
    @Autowired
    private final FetchAndUpdateData jobService;

    List<JobValidation> jobValidationList = new ArrayList<>();
    //Make sure that they contain only letters and spaces.
    static String jobTitleRegex = "^[A-Za-z\\s]+$";
    //Validate that company names consist of letters, spaces, &, ,
    static String companyNameRegex = "^[A-Za-z0-9\\s&,-]+$";
    //Validate Link of the website
    static String linkRegex = "(\\bhttps?:\\S+\\b)";
    //salary format ensure it's a positive integer
    static String salaryRegex = "^[0-9]\\d*$";
    //Ensure that locations contain anything other than !@#$%^&*+={}[]|\/'"?<>
    static String locationRegex = "^[^!@#$%^&*+={}\\[\\]|\\\\/'\"?<>]+$";
    //Ensure there are at least 10 words (one or more non-whitespace characters followed by zero or more whitespace characters){10,}
    static String descriptionRegex = "(\\S+\\s*){10,}";


    public DataValidation(FetchAndUpdateData jobService) {
        this.jobService = jobService;
    }

    //Data validation using regular expression
    public List<JobValidation> validateScrapedData(){

        Objects.requireNonNull(FetchAndUpdateData.readJsonData()).forEach(job->{
            JobValidation jobValidation = new JobValidation();
            AtomicBoolean allValid = new AtomicBoolean(true);
            jobValidation.setId(job.getId());


            //Validate Job Title
            jobValidation.setJobTitle(validateField(job.getJobTitle(),jobTitleRegex, allValid));

            //Validate Company Name
            jobValidation.setCompanyName(validateField(job.getCompanyName(),companyNameRegex, allValid));

            //Validate Link
            jobValidation.setJobWebsiteLink(validateField(job.getJobWebsiteLink(),linkRegex, allValid));

            //Validate Salary
            jobValidation.setMinSalary(validateField(String.valueOf(job.getMinSalary()), salaryRegex, allValid));

            if(Objects.nonNull(job.getMaxSalary()) && job.getMaxSalary()>0) {
                jobValidation.setMaxSalary(validateField(String.valueOf(job.getMaxSalary()), salaryRegex, allValid));
            }else{
                allValid.set(Boolean.FALSE);
            }
            //Validate Location
            jobValidation.setLocation(validateField(job.getLocation(),locationRegex, allValid));

            //Validate Description
            jobValidation.setJobDescription(validateField(job.getJobDescription(),descriptionRegex, allValid));

            jobValidation.setAllFieldsValid(allValid.get());
            jobValidationList.add(jobValidation);

        });
        return jobValidationList;
    }

    //Data validation using regular expression for One Object
    public static Boolean validateDataForOneObject(Job job){

        JobValidation jobValidation = new JobValidation();
        AtomicBoolean allValid = new AtomicBoolean(true);
        jobValidation.setId(job.getId());


        //Validate Job Title
        jobValidation.setJobTitle(validateField(job.getJobTitle(),jobTitleRegex, allValid));

        //Validate Company Name
        jobValidation.setCompanyName(validateField(job.getCompanyName(),companyNameRegex, allValid));

        //Validate Link
        jobValidation.setJobWebsiteLink(validateField(job.getJobWebsiteLink(),linkRegex, allValid));

        //Validate Salary
        jobValidation.setMinSalary(validateField(String.valueOf(job.getMinSalary()), salaryRegex, allValid));

        if(Objects.nonNull(job.getMaxSalary()) && job.getMaxSalary()>0) {
            jobValidation.setMaxSalary(validateField(String.valueOf(job.getMaxSalary()), salaryRegex, allValid));
        }else{
            allValid.set(Boolean.FALSE);
        }

        //Validate Location
        jobValidation.setLocation(validateField(job.getLocation(),locationRegex, allValid));

        //Validate Description
        jobValidation.setJobDescription(validateField(job.getJobDescription(),descriptionRegex, allValid));

        jobValidation.setAllFieldsValid(allValid.get());

        return allValid.get();
    }

    // Generic validation method for fields using regex
    private static Boolean validateField(String field, String regex, AtomicBoolean allValid) {
        if(Objects.nonNull(field) && !field.isBlank()) {
            Boolean patternMatched = Pattern.matches(regex, field);
            //If field is not valid then set all fields valid as false
            if(!patternMatched) {
                allValid.set(Boolean.FALSE);
            }return patternMatched;
        }else {
            allValid.set(Boolean.FALSE);
            return Boolean.FALSE;
        }
    }

    public static List<String> validateRequest(String searchTerm) {
        String processedString = searchTerm.replaceAll("[^a-zA-Z0-9\\s]", "");

        String[] processedStringsList = processedString.split("\\s");
        List<String> finalStringSet = new ArrayList<>();

        for (String eachString: processedStringsList) {
            String trimmedString = eachString.trim();
            if (!trimmedString.isEmpty()) {
                finalStringSet.add(trimmedString);
            }
        }

        return finalStringSet;

    }
}
