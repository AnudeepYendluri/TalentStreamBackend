package com.talentstream.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.talentstream.entity.AppliedApplicantInfo;
import com.talentstream.entity.ApplyJob;
import com.talentstream.entity.Job;
import com.talentstream.entity.Applicant;
@Repository
public interface ApplyJobRepository extends JpaRepository<ApplyJob, Long> {
	List<ApplyJob> findByApplicantId(long applicantId);

	@Query("SELECT NEW com.talentstream.entity.AppliedApplicantInfo(" +
		       " aj.applyjobid,a.name,a.id, a.email, a.mobilenumber, j.newStatus, j.jobTitle, aj.applicantStatus, " +
		       " j.minimumExperience, s.skillName, " +
		       "j.minimumQualification, j.location) " +
		       "FROM ApplyJob aj " +
		       "INNER JOIN aj.applicant a " +
		       "INNER JOIN aj.job j " +
		       "INNER JOIN j.skillsRequired s " +
		       "INNER JOIN j.jobRecruiter r " +
		       "WHERE r.id = :jobRecruiterId")
		List<AppliedApplicantInfo> findAppliedApplicantsInfo(@Param("jobRecruiterId") long jobRecruiterId);
	
	@Query("SELECT NEW com.talentstream.entity.AppliedApplicantInfo(" +
		       " aj.applyjobid,a.name,a.id, a.email, a.mobilenumber, j.newStatus, j.jobTitle, aj.applicantStatus, " +
		       " j.minimumExperience, s.skillName, " +
		       " j.minimumQualification, j.location) " +
		       "FROM ApplyJob aj " +
		       "INNER JOIN aj.applicant a " +
		       "INNER JOIN aj.job j " +
		       "INNER JOIN j.skillsRequired s " +
		       "INNER JOIN j.jobRecruiter r " +
		       "WHERE r.id = :jobRecruiterId AND j.id = :id")
		List<AppliedApplicantInfo> findAppliedApplicantsInfoWithJobId(
		    @Param("jobRecruiterId") long jobRecruiterId,
		    @Param("id") long id
		);
	boolean existsByApplicantAndJob(Applicant applicant, Job job);	
	@Query("SELECT COUNT(ja) FROM ApplyJob ja WHERE ja.job.jobRecruiter.id = :recruiterId")
    long countJobApplicantsByRecruiterId(@Param("recruiterId") Long recruiterId);	
	long countByApplicantStatus(String applicantStatus);	
	long countByApplicantStatusIn(List<String> applicantStatusList);	
	List<ApplyJob> findByJobId(Long jobId);
	
	@Query("SELECT DISTINCT aj " +
		       "FROM ApplyJob aj " +
		       "LEFT JOIN FETCH aj.job j " +
		       "LEFT JOIN FETCH j.skillsRequired s " +
		       "WHERE (:jobTitle IS NULL OR LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :jobTitle, '%'))) " +
		       "AND (:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
		       "AND (:skillName IS NULL OR LOWER(s.skillName) = LOWER(:skillName))")
		List<ApplyJob> findByJobTitleAndLocationAndSkillName(
		        @Param("jobTitle") String jobTitle,
		        @Param("location") String location,
		        @Param("skillName") String skillName);

	@Query("SELECT COUNT(a) FROM ApplyJob a WHERE a.applicant.id = :applicantId")
    long countByApplicantId(@Param("applicantId") long applicantId);
	
	@Query(value = "SELECT COUNT(*) FROM apply_job aj " +
            "JOIN Job j ON aj.job_id = j.id " +
            "WHERE j.job_recruiter_recruiter_id = :recruiterId " +
            "AND aj.applicant_status IN :statusList", nativeQuery = true)
long countShortlistedAndInterviewedApplicants(@Param("recruiterId") Long recruiterId,
                                           @Param("statusList") List<String> statusList);

	ApplyJob findByJobAndApplicant(Job job, Applicant applicant);
	
	
}
