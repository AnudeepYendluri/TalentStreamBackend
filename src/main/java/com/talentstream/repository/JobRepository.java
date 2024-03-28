package com.talentstream.repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.talentstream.entity.ApplicantSkills;
import com.talentstream.entity.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job>{
	@Query("SELECT DISTINCT j FROM Job j JOIN j.skillsRequired s WHERE " +
	        "(:skillName is null or s.skillName = :skillName) or " +
	        "(:jobTitle is null or j.jobTitle = :jobTitle) or " +
	        "(:location is null or j.location = :location) or " +
	        "(:industryType is null or j.industryType = :industryType) or " +
	        "(:employeeType is null or j.employeeType = :employeeType) or " +
	        "(:minimumQualification is null or j.minimumQualification = :minimumQualification) or " +
	        "(:specialization is null or j.specialization = :specialization)")
	List<Job> searchJobs(
	    @Param("skillName") String skillName,
	    @Param("jobTitle") String jobTitle,
	    @Param("location") String location,
	    @Param("industryType") String industryType,
	    @Param("employeeType") String employeeType,
	    @Param("minimumQualification") String minimumQualification,
	    @Param("specialization") String specialization
	    
	);

	
	
	@Query("SELECT DISTINCT j FROM Job j " +
	           "JOIN j.skillsRequired r " +
	           "WHERE LOWER(r.skillName) IN :skillNames")
	 List<Job> findBySkillsRequiredIgnoreCaseAndSkillNameIn(Set<String> skillNames);
	@Query("SELECT j FROM Job j WHERE j.jobRecruiter.id = :jobRecruiterId")
    List<Job> findByJobRecruiterId(@Param("jobRecruiterId") Long jobRecruiterId);
	@Query("SELECT COUNT(j) FROM Job j WHERE j.jobRecruiter.id = :recruiterId AND j.status = 'active'")
    long countJobsByRecruiterId(@Param("recruiterId") Long recruiterId);
	
	@Query("SELECT j FROM Job j JOIN j.skillsRequired s WHERE LOWER(s.skillName) LIKE LOWER(CONCAT('%', :skillName, '%'))")
    List<Job> findJobsBySkillNameIgnoreCase(@Param("skillName") String skillName);
	
	@Query("SELECT DISTINCT j FROM Job j JOIN j.skillsRequired s WHERE s.skillName = :skillName")
    Page<Job> findJobsBySkillName(String skillName, Pageable pageable);

	@Query("SELECT j FROM Job j WHERE j.alertCount > 0 AND j.recentApplicationDateTime >= :minDateTime AND j.jobRecruiter.recruiterId = :recruiterId")
	List<Job> findJobsWithAlertCountAndRecentDateTimeGreaterThanAndRecruiterId(
	    @Param("minDateTime") LocalDateTime minDateTime,
	    @Param("recruiterId") Long recruiterId
	);

	@Query("SELECT DISTINCT j, asj.saveJobStatus FROM Job j " +
		       "LEFT JOIN SavedJob asj ON asj.job = j  AND asj.applicant.id = :applicantId " +
		       "WHERE  " +
		       "(j.promote = :promote)")
	List<Object[]> findByPromote(@Param("applicantId") long applicantId,@Param("promote") String promote);
	
	@Query("SELECT DISTINCT j, asj.saveJobStatus FROM Job j " +
		       "JOIN j.skillsRequired s " +
		       "LEFT JOIN SavedJob asj ON asj.job = j  AND asj.applicant.id = :applicantId " +
		       "WHERE  " +
		       "((LOWER(s.skillName) IN :skillNames) or " +
		       "(j.location IN :preferredLocations) or " +
		       "(j.minimumExperience = :experience) or " +
		       "(j.specialization = :specialization))")
		List<Object[]> findJobsMatchingApplicantProfile(
				@Param("applicantId") long applicantId,
		       @Param("skillNames") Set<String> skillNames,
		       @Param("preferredLocations") Set<String> preferredLocations,
		       @Param("experience") Integer experience,
		       @Param("specialization") String specialization);

	
}
