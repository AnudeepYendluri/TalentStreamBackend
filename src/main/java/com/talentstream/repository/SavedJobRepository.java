package com.talentstream.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.talentstream.entity.Applicant;
import com.talentstream.entity.ApplicantProfile;
import com.talentstream.entity.Job;
import com.talentstream.entity.SavedJob;
@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {

	List<SavedJob> findByApplicantId(long applicantId);

	boolean existsByApplicantAndJob(Applicant applicant, Job job);
	
	@Query(value = "SELECT COUNT(*) FROM applicant_savedjob WHERE applicantregistration_id = :applicantId", nativeQuery = true)
    long countByApplicantId(@Param("applicantId") long applicantId);
}
