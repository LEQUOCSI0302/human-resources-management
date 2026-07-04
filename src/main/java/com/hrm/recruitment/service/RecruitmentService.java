package com.hrm.recruitment.service;

import com.hrm.profile.factory.EmployeeFactory;
import com.hrm.profile.model.EmployeeProfile;
import com.hrm.recruitment.model.Application;
import com.hrm.recruitment.model.ApplicationStatus;
import com.hrm.recruitment.model.Candidate;
import com.hrm.recruitment.model.Interview;
import com.hrm.recruitment.model.JobPosting;
import com.hrm.recruitment.model.JobPostingStatus;
import com.hrm.recruitment.model.RecruitmentNotification;
import com.hrm.recruitment.repository.ApplicationRepository;
import com.hrm.recruitment.repository.CandidateRepository;
import com.hrm.recruitment.repository.InterviewRepository;
import com.hrm.recruitment.repository.JobPostingRepository;
import com.hrm.recruitment.repository.NotificationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecruitmentService {

    private final JobPostingRepository jobPostingRepository;
    private final CandidateRepository candidateRepository;
    private final ApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;
    private final NotificationRepository notificationRepository;

    private final EmployeeFactory employeeFactory;
    private final List<EmployeeProfile> employees;

    public RecruitmentService(EmployeeFactory employeeFactory) {
        this(employeeFactory, new ArrayList<>());
    }

    public RecruitmentService(
            EmployeeFactory employeeFactory,
            List<EmployeeProfile> employees
    ) {
        this(
                employeeFactory,
                employees,
                new JobPostingRepository(),
                new CandidateRepository(),
                new ApplicationRepository(),
                new InterviewRepository(),
                new NotificationRepository()
        );
    }

    public RecruitmentService(
            EmployeeFactory employeeFactory,
            List<EmployeeProfile> employees,
            JobPostingRepository jobPostingRepository,
            CandidateRepository candidateRepository,
            ApplicationRepository applicationRepository,
            InterviewRepository interviewRepository,
            NotificationRepository notificationRepository
    ) {
        if (employeeFactory == null) {
            throw new IllegalArgumentException(
                    "EmployeeFactory không được để trống."
            );
        }

        this.employeeFactory = employeeFactory;
        this.employees = employees == null ? new ArrayList<>() : employees;
        this.jobPostingRepository = jobPostingRepository;
        this.candidateRepository = candidateRepository;
        this.applicationRepository = applicationRepository;
        this.interviewRepository = interviewRepository;
        this.notificationRepository = notificationRepository;
    }

    // QUẢN LÝ TIN TUYỂN DỤNG

    public JobPosting createJobPosting(
            String title,
            String description,
            String requirements,
            String department,
            String position,
            int quantity,
            LocalDate deadline
    ) {
        validateJobPosting(
                title,
                description,
                requirements,
                department,
                position,
                quantity,
                deadline
        );

        JobPosting jobPosting = new JobPosting(
                generateId("JOB"),
                title.trim(),
                description.trim(),
                requirements.trim(),
                department.trim(),
                position.trim(),
                quantity,
                deadline,
                JobPostingStatus.DANG_HIEN_THI
        );

        jobPostingRepository.save(jobPosting);
        return jobPosting;
    }

    public void updateJobPosting(
            String jobId,
            String title,
            String description,
            String requirements,
            String department,
            String position,
            int quantity,
            LocalDate deadline
    ) {
        validateJobPosting(
                title,
                description,
                requirements,
                department,
                position,
                quantity,
                deadline
        );

        JobPosting jobPosting = getRequiredJobPosting(jobId);

        jobPosting.update(
                title.trim(),
                description.trim(),
                requirements.trim(),
                department.trim(),
                position.trim(),
                quantity,
                deadline
        );
    }

    public void changeJobStatus(
            String jobId,
            JobPostingStatus status
    ) {
        JobPosting jobPosting = getRequiredJobPosting(jobId);

        if (status == null) {
            throw new IllegalArgumentException(
                    "Trạng thái tin tuyển dụng không được để trống."
            );
        }

        if (status == JobPostingStatus.DANG_HIEN_THI
                && jobPosting.getDeadline().isBefore(LocalDate.now())) {
            throw new IllegalStateException(
                    "Không thể hiển thị lại tin tuyển dụng đã hết hạn."
            );
        }

        jobPosting.changeStatus(status);
    }

    public List<JobPosting> getAllJobPostings() {
        return jobPostingRepository.findAll();
    }

    public List<JobPosting> getActiveJobPostings() {
        return jobPostingRepository.findAllActive();
    }

    public JobPosting getJobPostingById(String jobId) {
        return getRequiredJobPosting(jobId);
    }

    // NỘP HỒ SƠ ỨNG TUYỂN

    public void receiveApplication(Candidate candidate) {
        saveOrUpdateCandidate(candidate);
    }

    /**
     * Nộp hồ sơ ứng tuyển cho một tin tuyển dụng.
     */
    public Application submitApplication(
            Candidate candidate,
            String jobId
    ) {
        JobPosting jobPosting = getRequiredJobPosting(jobId);

        if (!jobPosting.isActive()) {
            throw new IllegalStateException(
                    "Tin tuyển dụng không còn hiệu lực để nộp hồ sơ."
            );
        }

        Candidate savedCandidate = saveOrUpdateCandidate(candidate);

        boolean alreadyApplied = applicationRepository.existsByCandidateAndJob(
                savedCandidate.getId(),
                jobPosting.getJobId()
        );

        if (alreadyApplied) {
            throw new IllegalStateException(
                    "Ứng viên đã nộp hồ sơ cho tin tuyển dụng này."
            );
        }

        Application application = new Application(
                generateId("APP"),
                savedCandidate,
                jobPosting
        );

        applicationRepository.save(application);

        createNotification(
                application,
                "TIEP_NHAN_HO_SO",
                "Hồ sơ " + application.getApplicationId()
                        + " đã được tiếp nhận cho vị trí "
                        + jobPosting.getTitle() + "."
        );

        return application;
    }

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public List<Application> getApplicationsByJob(String jobId) {
        getRequiredJobPosting(jobId);
        return applicationRepository.findByJobId(jobId);
    }

    public Application getApplicationById(String applicationId) {
        return getRequiredApplication(applicationId);
    }

    // SÀNG LỌC HỒ SƠ

    public void markApplicationNeedsInterview(
            String applicationId,
            String screeningNote
    ) {
        Application application = getRequiredApplication(applicationId);

        ensureNotFinal(application);

        application.setScreeningNote(screeningNote);
        application.changeStatus(ApplicationStatus.CAN_PHONG_VAN);
    }

    public void rejectApplication(
            String applicationId,
            String reason
    ) {
        Application application = getRequiredApplication(applicationId);

        ensureNotConvertedToEmployee(application);

        application.setScreeningNote(reason);
        application.changeStatus(ApplicationStatus.KHONG_PHU_HOP);

        createNotification(
                application,
                "TU_CHOI",
                "Hồ sơ " + application.getApplicationId()
                        + " chưa phù hợp với vị trí "
                        + application.getJobPosting().getTitle()
                        + ". Lý do: " + normalizeNote(reason)
        );
    }

    // PHỎNG VẤN

    public Interview scheduleInterview(
            String applicationId,
            LocalDateTime scheduledAt,
            String location,
            String interviewer
    ) {
        Application application = getRequiredApplication(applicationId);

        ensureNotFinal(application);

        if (scheduledAt == null
                || scheduledAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Thời gian phỏng vấn phải là thời điểm hiện tại hoặc tương lai."
            );
        }

        if (isBlank(location) || isBlank(interviewer)) {
            throw new IllegalArgumentException(
                    "Địa điểm và người phỏng vấn không được để trống."
            );
        }

        application.changeStatus(ApplicationStatus.CAN_PHONG_VAN);

        Interview interview = new Interview(
                generateId("INT"),
                application,
                scheduledAt,
                location.trim(),
                interviewer.trim()
        );

        interviewRepository.save(interview);

        application.changeStatus(
                ApplicationStatus.DA_LEN_LICH_PHONG_VAN
        );

        createNotification(
                application,
                "MOI_PHONG_VAN",
                "Mời ứng viên tham gia phỏng vấn lúc "
                        + scheduledAt
                        + " tại "
                        + location.trim()
                        + "."
        );

        return interview;
    }

    public void recordInterviewResult(
            String interviewId,
            boolean passed,
            String notes
    ) {
        Interview interview = getRequiredInterview(interviewId);
        Application application = interview.getApplication();

        ensureNotConvertedToEmployee(application);

        if (interview.hasResult()) {
            throw new IllegalStateException(
                    "Buổi phỏng vấn này đã có kết quả."
            );
        }

        if (application.getStatus()
                != ApplicationStatus.DA_LEN_LICH_PHONG_VAN) {
            throw new IllegalStateException(
                    "Hồ sơ chưa ở trạng thái đã lên lịch phỏng vấn."
            );
        }

        String result = passed ? "DAT" : "KHONG_DAT";

        interview.recordResult(result, notes);
        application.setScreeningNote(notes);

        if (passed) {
            application.changeStatus(ApplicationStatus.TRUNG_TUYEN);

            createNotification(
                    application,
                    "TRUNG_TUYEN",
                    "Chúc mừng! Hồ sơ "
                            + application.getApplicationId()
                            + " đã trúng tuyển vị trí "
                            + application.getJobPosting().getTitle()
                            + "."
            );
        } else {
            application.changeStatus(
                    ApplicationStatus.KHONG_PHU_HOP
            );

            createNotification(
                    application,
                    "TU_CHOI",
                    "Hồ sơ "
                            + application.getApplicationId()
                            + " không đạt sau vòng phỏng vấn. Ghi chú: "
                            + normalizeNote(notes)
            );
        }
    }

    // CHUYỂN ỨNG VIÊN TRÚNG TUYỂN THÀNH NHÂN VIÊN

    public EmployeeProfile hireCandidate(
            String applicationId,
            String employeeType,
            String employeeId,
            String department,
            String position
    ) {
        Application application = getRequiredApplication(applicationId);

        if (application.getStatus()
                != ApplicationStatus.TRUNG_TUYEN) {
            throw new IllegalStateException(
                    "Chỉ có thể tạo hồ sơ nhân viên cho ứng viên đã trúng tuyển."
            );
        }

        if (application.hasBeenConvertedToEmployee()) {
            throw new IllegalStateException(
                    "Hồ sơ này đã được chuyển thành nhân viên: "
                            + application.getHiredEmployeeId()
            );
        }

        if (isBlank(employeeType)
                || isBlank(employeeId)
                || isBlank(department)
                || isBlank(position)) {
            throw new IllegalArgumentException(
                    "Loại nhân viên, mã nhân viên, phòng ban và chức vụ không được để trống."
            );
        }

        for (EmployeeProfile employee : employees) {
            if (employeeId.trim().equalsIgnoreCase(employee.getId())) {
                throw new IllegalStateException(
                        "Mã nhân viên đã tồn tại."
                );
            }
        }

        EmployeeProfile employee = employeeFactory.createEmployee(
                employeeType.trim(),
                employeeId.trim(),
                application.getCandidate().getName(),
                department.trim(),
                position.trim()
        );

        employees.add(employee);

        application.markHiredEmployee(employee.getId());

        createNotification(
                application,
                "TAO_NHAN_VIEN",
                "Đã tạo hồ sơ nhân viên "
                        + employee.getId()
                        + " cho ứng viên "
                        + application.getCandidate().getName()
                        + "."
        );

        return employee;
    }

    // TRA CỨU

    public List<Interview> getInterviewsByApplication(
            String applicationId
    ) {
        getRequiredApplication(applicationId);
        return interviewRepository.findByApplicationId(applicationId);
    }

    public List<RecruitmentNotification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public List<RecruitmentNotification> getNotificationsByApplication(
            String applicationId
    ) {
        getRequiredApplication(applicationId);
        return notificationRepository.findByApplicationId(applicationId);
    }

    // HÀM HỖ TRỢ NỘI BỘ

    private Candidate saveOrUpdateCandidate(Candidate candidate) {
        validateCandidate(candidate);

        Candidate existingCandidate = candidateRepository.findByEmail(
                candidate.getEmail().trim()
        );

        if (existingCandidate != null) {
            existingCandidate.setName(candidate.getName().trim());
            existingCandidate.setPhone(candidate.getPhone().trim());
            existingCandidate.setCvPath(candidate.getCvPath().trim());

            return existingCandidate;
        }

        if (isBlank(candidate.getId())) {
            candidate.setId(generateId("CAND"));
        }

        candidate.setName(candidate.getName().trim());
        candidate.setEmail(candidate.getEmail().trim());
        candidate.setPhone(candidate.getPhone().trim());
        candidate.setCvPath(candidate.getCvPath().trim());
        candidate.setStatus("ACTIVE");

        candidateRepository.save(candidate);

        return candidate;
    }

    private void validateCandidate(Candidate candidate) {
        if (candidate == null) {
            throw new IllegalArgumentException(
                    "Thông tin ứng viên không được để trống."
            );
        }

        if (isBlank(candidate.getName())) {
            throw new IllegalArgumentException(
                    "Họ tên ứng viên không được để trống."
            );
        }

        if (isBlank(candidate.getEmail())
                || !candidate.getEmail().contains("@")) {
            throw new IllegalArgumentException(
                    "Email ứng viên không hợp lệ."
            );
        }

        if (isBlank(candidate.getPhone())) {
            throw new IllegalArgumentException(
                    "Số điện thoại ứng viên không được để trống."
            );
        }

        if (isBlank(candidate.getCvPath())) {
            throw new IllegalArgumentException(
                    "Đường dẫn CV không được để trống."
            );
        }
    }

    private void validateJobPosting(
            String title,
            String description,
            String requirements,
            String department,
            String position,
            int quantity,
            LocalDate deadline
    ) {
        if (isBlank(title)
                || isBlank(description)
                || isBlank(requirements)
                || isBlank(department)
                || isBlank(position)) {
            throw new IllegalArgumentException(
                    "Các thông tin của tin tuyển dụng không được để trống."
            );
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Số lượng cần tuyển phải lớn hơn 0."
            );
        }

        if (deadline == null || deadline.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Hạn nộp hồ sơ phải từ ngày hiện tại trở đi."
            );
        }
    }

    private JobPosting getRequiredJobPosting(String jobId) {
        if (isBlank(jobId)) {
            throw new IllegalArgumentException(
                    "Mã tin tuyển dụng không được để trống."
            );
        }

        JobPosting jobPosting = jobPostingRepository.findById(
                jobId.trim()
        );

        if (jobPosting == null) {
            throw new IllegalArgumentException(
                    "Không tìm thấy tin tuyển dụng: " + jobId
            );
        }

        return jobPosting;
    }

    private Application getRequiredApplication(String applicationId) {
        if (isBlank(applicationId)) {
            throw new IllegalArgumentException(
                    "Mã hồ sơ không được để trống."
            );
        }

        Application application = applicationRepository.findById(
                applicationId.trim()
        );

        if (application == null) {
            throw new IllegalArgumentException(
                    "Không tìm thấy hồ sơ ứng tuyển: " + applicationId
            );
        }

        return application;
    }

    private Interview getRequiredInterview(String interviewId) {
        if (isBlank(interviewId)) {
            throw new IllegalArgumentException(
                    "Mã phỏng vấn không được để trống."
            );
        }

        Interview interview = interviewRepository.findById(
                interviewId.trim()
        );

        if (interview == null) {
            throw new IllegalArgumentException(
                    "Không tìm thấy lịch phỏng vấn: " + interviewId
            );
        }

        return interview;
    }

    private void ensureNotFinal(Application application) {
        ApplicationStatus status = application.getStatus();

        if (status == ApplicationStatus.KHONG_PHU_HOP
                || status == ApplicationStatus.TRUNG_TUYEN
                || status == ApplicationStatus.DA_TAO_NHAN_VIEN) {
            throw new IllegalStateException(
                    "Hồ sơ đã có kết quả cuối cùng, không thể tiếp tục xử lý."
            );
        }
    }

    private void ensureNotConvertedToEmployee(Application application) {
        if (application.hasBeenConvertedToEmployee()) {
            throw new IllegalStateException(
                    "Hồ sơ đã được chuyển thành nhân viên."
            );
        }
    }

    private void createNotification(
            Application application,
            String type,
            String content
    ) {
        RecruitmentNotification notification = new RecruitmentNotification(
                generateId("NOTI"),
                application,
                type,
                content
        );

        notificationRepository.save(notification);
    }

    private String generateId(String prefix) {
        return prefix
                + "-"
                + UUID.randomUUID().toString()
                .substring(0, 8)
                .toUpperCase();
    }

    private String normalizeNote(String note) {
        if (isBlank(note)) {
            return "Không có ghi chú.";
        }

        return note.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}