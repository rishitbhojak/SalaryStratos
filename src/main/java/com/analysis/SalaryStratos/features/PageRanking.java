package com.analysis.SalaryStratos.features;

import com.analysis.SalaryStratos.dataStructures.array.SortedArray;
import com.analysis.SalaryStratos.dataStructures.trie.TrieDS;
import com.analysis.SalaryStratos.dataStructures.trie.TrieNode;
import com.analysis.SalaryStratos.models.Job;
import com.analysis.SalaryStratos.services.JobDataTrie;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.*;

@Service
public class PageRanking {

    public SortedArray<Job> searchInvertedIndexedData(String[] searchTerms, TrieDS trie, JobDataTrie jobData) throws FileNotFoundException, InterruptedException {
        ArrayList<Job> jobs= jobData.getJobsDataFromJson();

        SortedArray<Job> sortedJobs = new SortedArray<>(Comparator.comparingInt(Job::getWordFrequency));
        for (String term: searchTerms) {
            System.out.println(term);
            TrieNode node = trie.searchInTrie(term.toLowerCase());
            Set<String> jobIds = node.getJobIds();
            TreeMap<String, Integer> wordFrequency = node.getWordFrequency();

            for (String jobId: jobIds) {
                Job jobById = jobs.stream()
                        .filter(job -> Objects.equals(job.getId(), jobId))
                        .findFirst()
                        .orElse(null);

                if(jobById != null) {
                    if ( jobById.getWordFrequency() == 0) {
                        jobById.setWord(term);
                        jobById.setWordFrequency(wordFrequency.get(jobId));

                        sortedJobs.insert(jobById);
                    } else {
                        sortedJobs.delete(jobById);
                        jobById.setWord(jobById.getWord() + ", " + term);
                        jobById.setWordFrequency(jobById.getWordFrequency() + wordFrequency.get(jobId));
                        sortedJobs.insert(jobById);

                    }



                }

            }
        }

        return sortedJobs;
    }

    public SortedArray<Job> searchInvertedIndexedDataBySalary(String[] searchTerms, TrieDS trie, JobDataTrie jobData) throws FileNotFoundException, InterruptedException {
        ArrayList<Job> jobs= jobData.getJobsDataFromJson();

        SortedArray<Job> sortedJobs = new SortedArray<>(Comparator.comparingInt(Job::getMaxSalary));
        for (String term: searchTerms) {
            System.out.println(term);
            TrieNode node = trie.searchInTrie(term.toLowerCase());
            Set<String> jobIds = node.getJobIds();
            TreeMap<String, Integer> wordFrequency = node.getWordFrequency();

            for (String jobId: jobIds) {
                Job jobById = jobs.stream()
                        .filter(job -> Objects.equals(job.getId(), jobId))
                        .findFirst()
                        .orElse(null);

                if(jobById != null) {
                    if ( jobById.getWordFrequency() == 0) {
                        jobById.setWord(term);
                        jobById.setWordFrequency(wordFrequency.get(jobId));

                        sortedJobs.insert(jobById);
                    } else {
                        sortedJobs.delete(jobById);
                        jobById.setWord(jobById.getWord() + ", " + term);
                        jobById.setWordFrequency(jobById.getWordFrequency() + wordFrequency.get(jobId));
                        sortedJobs.insert(jobById);

                    }
                }

            }
        }

        return sortedJobs;
    }
}
