package com.hrm.console;

import com.hrm.profile.model.EmployeeProfile;
import com.hrm.recruitment.builder.CandidateBuilder;
import com.hrm.recruitment.model.Application;
import com.hrm.recruitment.model.ApplicationStatus;
import com.hrm.recruitment.model.Candidate;
import com.hrm.recruitment.model.Interview;
import com.hrm.recruitment.model.JobPosting;
import com.hrm.recruitment.model.JobPostingStatus;
import com.hrm.recruitment.model.RecruitmentNotification;
import com.hrm.recruitment.service.RecruitmentService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;


public class RecruitmentMenu {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final RecruitmentService recruitmentService;
    private final Scanner scanner;

    public RecruitmentMenu(RecruitmentService recruitmentService) {
        this(recruitmentService, new Scanner(System.in));
    }

    public RecruitmentMenu(
            RecruitmentService recruitmentService,
            Scanner scanner
    ) {
        if (recruitmentService == null) {
            throw new IllegalArgumentException(
                    "RecruitmentService không được để trống."
            );
        }

        if (scanner == null) {
            throw new IllegalArgumentException(
                    "Scanner không được để trống."
            );
        }

        this.recruitmentService = recruitmentService;
        this.scanner = scanner;
    }

    public void displayMenu() {
        int choice;

        do {
            printMainMenu();
            choice = readInt("Chọn chức năng (0-7): ");

            try {
                switch (choice) {
                    case 1 -> handleViewActiveJobs();
                    case 2 -> handleViewJobDetail();
                    case 3 -> handleSubmitApplication();
                    case 4 -> handleManageJobPostings();
                    case 5 -> handleProcessApplications();
                    case 6 -> handleViewNotifications();
                    case 7 -> handleHireCandidate();
                    case 0 -> System.out.println(
                            "Đang quay lại Menu chính..."
                    );
                    default -> System.out.println(
                            "Lựa chọn không hợp lệ. Vui lòng chọn từ 0 đến 7."
                    );
                }
            } catch (IllegalArgumentException | IllegalStateException ex) {
                System.out.println("❌ " + ex.getMessage());
            }

        } while (choice != 0);
    }

    private void printMainMenu() {
        System.out.println("\n======================================");
        System.out.println("       QUẢN LÝ TUYỂN DỤNG - HRM");
        System.out.println("======================================");
        System.out.println("1. Xem danh sách tin tuyển dụng đang hiển thị");
        System.out.println("2. Xem chi tiết tin tuyển dụng");
        System.out.println("3. Nộp hồ sơ ứng tuyển");
        System.out.println("4. Quản lý tin tuyển dụng");
        System.out.println("5. Xem và xử lý hồ sơ ứng viên");
        System.out.println("6. Xem thông báo tuyển dụng");
        System.out.println("7. Tạo hồ sơ nhân viên từ ứng viên trúng tuyển");
        System.out.println("0. Quay lại Menu chính");
    }

    // ỨNG VIÊN: XEM TIN VÀ NỘP HỒ SƠ

    private void handleViewActiveJobs() {
        List<JobPosting> jobs =
                recruitmentService.getActiveJobPostings();

        printJobList(
                jobs,
                "DANH SÁCH TIN TUYỂN DỤNG ĐANG HIỂN THỊ"
        );
    }

    private void handleViewJobDetail() {
        String jobId = readRequired("Nhập mã tin tuyển dụng: ");

        JobPosting job =
                recruitmentService.getJobPostingById(jobId);

        printJobDetail(job);
    }

    private void handleSubmitApplication() {
        List<JobPosting> jobs =
                recruitmentService.getActiveJobPostings();

        printJobList(jobs, "CÁC TIN CÓ THỂ NỘP HỒ SƠ");

        if (jobs.isEmpty()) {
            return;
        }

        String jobId =
                readRequired("Nhập mã tin muốn ứng tuyển: ");

        JobPosting job =
                recruitmentService.getJobPostingById(jobId);

        if (!job.isActive()) {
            throw new IllegalStateException(
                    "Tin này không còn hiệu lực để nộp hồ sơ."
            );
        }

        System.out.println("\n--- NHẬP HỒ SƠ ỨNG TUYỂN ---");

        String name = readRequired("Họ và tên: ");
        String email = readRequired("Email: ");
        String phone = readRequired("Số điện thoại: ");
        String cvPath = readRequired("Đường dẫn hoặc tên file CV: ");

        Candidate candidate = new CandidateBuilder()
                .setName(name)
                .setEmail(email)
                .setPhone(phone)
                .setCv(cvPath)
                .build();

        Application application =
                recruitmentService.submitApplication(candidate, jobId);

        System.out.println("✅ Nộp hồ sơ thành công.");
        System.out.println(
                "   Mã hồ sơ: " + application.getApplicationId()
        );
        System.out.println(
                "   Trạng thái: "
                        + formatApplicationStatus(application.getStatus())
        );
    }

    // NHÂN VIÊN TUYỂN DỤNG: QUẢN LÝ TIN

    private void handleManageJobPostings() {
        int choice;

        do {
            System.out.println("\n----- QUẢN LÝ TIN TUYỂN DỤNG -----");
            System.out.println("1. Xem toàn bộ tin tuyển dụng");
            System.out.println("2. Tạo tin tuyển dụng");
            System.out.println("3. Cập nhật tin tuyển dụng");
            System.out.println("4. Ẩn hoặc đóng tin tuyển dụng");
            System.out.println("0. Quay lại");

            choice = readInt("Chọn chức năng (0-4): ");

            try {
                switch (choice) {
                    case 1 -> printJobList(
                            recruitmentService.getAllJobPostings(),
                            "TOÀN BỘ TIN TUYỂN DỤNG"
                    );
                    case 2 -> handleCreateJob();
                    case 3 -> handleUpdateJob();
                    case 4 -> handleChangeJobStatus();
                    case 0 -> {
                    }
                    default -> System.out.println(
                            "Lựa chọn không hợp lệ."
                    );
                }
            } catch (IllegalArgumentException | IllegalStateException ex) {
                System.out.println("❌ " + ex.getMessage());
            }

        } while (choice != 0);
    }

    private void handleCreateJob() {
        System.out.println("\n--- TẠO TIN TUYỂN DỤNG ---");

        String title = readRequired("Tiêu đề tin: ");
        String description = readRequired("Mô tả công việc: ");
        String requirements = readRequired("Yêu cầu ứng viên: ");
        String department = readRequired("Phòng ban: ");
        String position = readRequired("Chức vụ/vị trí: ");
        int quantity = readPositiveInt("Số lượng cần tuyển: ");
        LocalDate deadline =
                readDate("Hạn nộp (yyyy-MM-dd): ");

        JobPosting job = recruitmentService.createJobPosting(
                title,
                description,
                requirements,
                department,
                position,
                quantity,
                deadline
        );

        System.out.println(
                "✅ Tạo tin tuyển dụng thành công. Mã tin: "
                        + job.getJobId()
        );
    }

    private void handleUpdateJob() {
        String jobId =
                readRequired("Nhập mã tin cần cập nhật: ");

        JobPosting currentJob =
                recruitmentService.getJobPostingById(jobId);

        printJobDetail(currentJob);

        System.out.println(
                "Để giữ nguyên một giá trị, hãy nhấn Enter."
        );

        String title = readOptional(
                "Tiêu đề mới [" + currentJob.getTitle() + "]: ",
                currentJob.getTitle()
        );

        String description = readOptional(
                "Mô tả mới [" + currentJob.getDescription() + "]: ",
                currentJob.getDescription()
        );

        String requirements = readOptional(
                "Yêu cầu mới [" + currentJob.getRequirements() + "]: ",
                currentJob.getRequirements()
        );

        String department = readOptional(
                "Phòng ban mới [" + currentJob.getDepartment() + "]: ",
                currentJob.getDepartment()
        );

        String position = readOptional(
                "Vị trí mới [" + currentJob.getPosition() + "]: ",
                currentJob.getPosition()
        );

        int quantity = readOptionalPositiveInt(
                "Số lượng mới [" + currentJob.getQuantity() + "]: ",
                currentJob.getQuantity()
        );

        LocalDate deadline = readOptionalDate(
                "Hạn nộp mới ["
                        + currentJob.getDeadline().format(DATE_FORMAT)
                        + "] (yyyy-MM-dd): ",
                currentJob.getDeadline()
        );

        recruitmentService.updateJobPosting(
                jobId,
                title,
                description,
                requirements,
                department,
                position,
                quantity,
                deadline
        );

        System.out.println("✅ Cập nhật tin tuyển dụng thành công.");
    }

    private void handleChangeJobStatus() {
        String jobId =
                readRequired("Nhập mã tin cần thay đổi trạng thái: ");

        JobPosting job =
                recruitmentService.getJobPostingById(jobId);

        System.out.println(
                "Trạng thái hiện tại: "
                        + formatJobStatus(job.getStatus())
        );

        System.out.println("1. Đang hiển thị");
        System.out.println("2. Ẩn tin");
        System.out.println("3. Đóng tin");

        int statusChoice =
                readInt("Chọn trạng thái mới (1-3): ");

        JobPostingStatus status = switch (statusChoice) {
            case 1 -> JobPostingStatus.DANG_HIEN_THI;
            case 2 -> JobPostingStatus.AN;
            case 3 -> JobPostingStatus.DONG;
            default -> throw new IllegalArgumentException(
                    "Trạng thái được chọn không hợp lệ."
            );
        };

        recruitmentService.changeJobStatus(jobId, status);

        System.out.println(
                "✅ Đã cập nhật trạng thái tin thành: "
                        + formatJobStatus(status)
        );
    }

    // NHÂN VIÊN TUYỂN DỤNG: XỬ LÝ HỒ SƠ

    private void handleProcessApplications() {
        int choice;

        do {
            System.out.println("\n----- XỬ LÝ HỒ SƠ ỨNG VIÊN -----");
            System.out.println("1. Xem hồ sơ theo tin tuyển dụng");
            System.out.println("2. Xem chi tiết một hồ sơ");
            System.out.println("3. Đánh dấu hồ sơ cần phỏng vấn");
            System.out.println("4. Lên lịch phỏng vấn");
            System.out.println("5. Ghi nhận kết quả phỏng vấn");
            System.out.println("6. Từ chối hồ sơ");
            System.out.println("7. Xem lịch phỏng vấn của hồ sơ");
            System.out.println("0. Quay lại");

            choice = readInt("Chọn chức năng (0-7): ");

            try {
                switch (choice) {
                    case 1 -> handleViewApplicationsByJob();
                    case 2 -> handleViewApplicationDetail();
                    case 3 -> handleMarkNeedsInterview();
                    case 4 -> handleScheduleInterview();
                    case 5 -> handleRecordInterviewResult();
                    case 6 -> handleRejectApplication();
                    case 7 -> handleViewInterviewsByApplication();
                    case 0 -> {
                    }
                    default -> System.out.println(
                            "Lựa chọn không hợp lệ."
                    );
                }
            } catch (IllegalArgumentException | IllegalStateException ex) {
                System.out.println("❌ " + ex.getMessage());
            }

        } while (choice != 0);
    }

    private void handleViewApplicationsByJob() {
        printJobList(
                recruitmentService.getAllJobPostings(),
                "CHỌN TIN ĐỂ XEM HỒ SƠ"
        );

        String jobId = readRequired("Nhập mã tin tuyển dụng: ");

        List<Application> applications =
                recruitmentService.getApplicationsByJob(jobId);

        printApplicationList(applications);
    }

    private void handleViewApplicationDetail() {
        String applicationId = readRequired("Nhập mã hồ sơ: ");

        Application application =
                recruitmentService.getApplicationById(applicationId);

        printApplicationDetail(application);
    }

    private void handleMarkNeedsInterview() {
        String applicationId = readRequired("Nhập mã hồ sơ: ");
        String note = readRequired("Ghi chú sàng lọc: ");

        recruitmentService.markApplicationNeedsInterview(
                applicationId,
                note
        );

        System.out.println(
                "✅ Đã cập nhật hồ sơ sang trạng thái cần phỏng vấn."
        );
    }

    private void handleScheduleInterview() {
        String applicationId = readRequired("Nhập mã hồ sơ: ");

        LocalDateTime scheduledAt = readDateTime(
                "Thời gian phỏng vấn (yyyy-MM-dd HH:mm): "
        );

        String location = readRequired(
                "Địa điểm hoặc hình thức phỏng vấn: "
        );

        String interviewer = readRequired("Người phỏng vấn: ");

        Interview interview = recruitmentService.scheduleInterview(
                applicationId,
                scheduledAt,
                location,
                interviewer
        );

        System.out.println(
                "✅ Đã lên lịch phỏng vấn. Mã lịch: "
                        + interview.getInterviewId()
        );
    }

    private void handleRecordInterviewResult() {
        String interviewId = readRequired("Nhập mã lịch phỏng vấn: ");

        System.out.println("1. Ứng viên đạt");
        System.out.println("2. Ứng viên không đạt");

        int choice = readInt("Chọn kết quả (1-2): ");

        boolean passed = switch (choice) {
            case 1 -> true;
            case 2 -> false;
            default -> throw new IllegalArgumentException(
                    "Kết quả được chọn không hợp lệ."
            );
        };

        String notes = readRequired("Ghi chú kết quả: ");

        recruitmentService.recordInterviewResult(
                interviewId,
                passed,
                notes
        );

        System.out.println(
                "✅ Đã lưu kết quả phỏng vấn và cập nhật trạng thái hồ sơ."
        );
    }

    private void handleRejectApplication() {
        String applicationId =
                readRequired("Nhập mã hồ sơ cần từ chối: ");

        String reason = readRequired("Lý do từ chối: ");

        recruitmentService.rejectApplication(applicationId, reason);

        System.out.println(
                "✅ Đã cập nhật trạng thái không phù hợp và tạo thông báo."
        );
    }

    private void handleViewInterviewsByApplication() {
        String applicationId = readRequired("Nhập mã hồ sơ: ");

        List<Interview> interviews =
                recruitmentService.getInterviewsByApplication(applicationId);

        if (interviews.isEmpty()) {
            System.out.println("(Hồ sơ chưa có lịch phỏng vấn)");
            return;
        }

        System.out.println("\n--- LỊCH PHỎNG VẤN ---");

        for (Interview interview : interviews) {
            System.out.printf(
                    "Mã: %s | Thời gian: %s | Địa điểm: %s | "
                            + "Người PV: %s | Kết quả: %s%n",
                    interview.getInterviewId(),
                    interview.getScheduledAt().format(DATE_TIME_FORMAT),
                    interview.getLocation(),
                    interview.getInterviewer(),
                    interview.hasResult()
                            ? interview.getResult()
                            : "CHƯA_CÓ"
            );
        }
    }

    // CHUYỂN ỨNG VIÊN THÀNH NHÂN VIÊN

    private void handleHireCandidate() {
        String applicationId =
                readRequired("Nhập mã hồ sơ trúng tuyển: ");

        Application application =
                recruitmentService.getApplicationById(applicationId);

        if (application.getStatus()
                != ApplicationStatus.TRUNG_TUYEN) {
            throw new IllegalStateException(
                    "Hồ sơ chưa ở trạng thái trúng tuyển."
            );
        }

        System.out.println(
                "Ứng viên: " + application.getCandidate().getName()
        );

        System.out.println("1. Full-time");
        System.out.println("2. Part-time");

        int typeChoice =
                readInt("Chọn loại nhân viên (1-2): ");

        String type = switch (typeChoice) {
            case 1 -> "FULLTIME";
            case 2 -> "PARTTIME";
            default -> throw new IllegalArgumentException(
                    "Loại nhân viên không hợp lệ."
            );
        };

        String employeeId = readRequired("Mã nhân viên mới: ");
        String department = readRequired("Phòng ban: ");
        String position = readRequired("Chức vụ: ");

        EmployeeProfile employee = recruitmentService.hireCandidate(
                applicationId,
                type,
                employeeId,
                department,
                position
        );

        System.out.println(
                "✅ Đã tạo hồ sơ nhân viên: "
                        + employee.getId()
                        + " - "
                        + employee.getName()
        );
    }

    // THÔNG BÁO

    private void handleViewNotifications() {
        List<RecruitmentNotification> notifications =
                recruitmentService.getAllNotifications();

        if (notifications.isEmpty()) {
            System.out.println("(Chưa có thông báo tuyển dụng)");
            return;
        }

        System.out.println("\n--- THÔNG BÁO TUYỂN DỤNG ---");

        for (RecruitmentNotification notification : notifications) {
            System.out.printf(
                    "[%s] %s | Hồ sơ: %s | %s%n",
                    notification.getType(),
                    notification.getCreatedAt().format(DATE_TIME_FORMAT),
                    notification.getApplication().getApplicationId(),
                    notification.getContent()
            );
        }
    }

    // HIỂN THỊ DỮ LIỆU

    private void printJobList(List<JobPosting> jobs, String title) {
        System.out.println("\n--- " + title + " ---");

        if (jobs.isEmpty()) {
            System.out.println("(Không có dữ liệu)");
            return;
        }

        for (JobPosting job : jobs) {
            System.out.printf(
                    "Mã: %s | Tiêu đề: %s | Phòng: %s | "
                            + "Vị trí: %s | SL: %d | Hạn: %s | "
                            + "Trạng thái: %s%n",
                    job.getJobId(),
                    job.getTitle(),
                    job.getDepartment(),
                    job.getPosition(),
                    job.getQuantity(),
                    job.getDeadline().format(DATE_FORMAT),
                    formatJobStatus(job.getStatus())
            );
        }
    }

    private void printJobDetail(JobPosting job) {
        System.out.println("\n--- CHI TIẾT TIN TUYỂN DỤNG ---");
        System.out.println("Mã tin: " + job.getJobId());
        System.out.println("Tiêu đề: " + job.getTitle());
        System.out.println("Phòng ban: " + job.getDepartment());
        System.out.println("Vị trí: " + job.getPosition());
        System.out.println("Số lượng: " + job.getQuantity());
        System.out.println(
                "Hạn nộp: "
                        + job.getDeadline().format(DATE_FORMAT)
        );
        System.out.println(
                "Trạng thái: "
                        + formatJobStatus(job.getStatus())
        );
        System.out.println("Mô tả: " + job.getDescription());
        System.out.println("Yêu cầu: " + job.getRequirements());
    }

    private void printApplicationList(
            List<Application> applications
    ) {
        System.out.println("\n--- DANH SÁCH HỒ SƠ ỨNG TUYỂN ---");

        if (applications.isEmpty()) {
            System.out.println("(Chưa có hồ sơ)");
            return;
        }

        for (Application application : applications) {
            System.out.printf(
                    "Mã HS: %s | Ứng viên: %s | Email: %s | "
                            + "Nộp lúc: %s | Trạng thái: %s%n",
                    application.getApplicationId(),
                    application.getCandidate().getName(),
                    application.getCandidate().getEmail(),
                    application.getSubmittedAt().format(DATE_TIME_FORMAT),
                    formatApplicationStatus(application.getStatus())
            );
        }
    }

    private void printApplicationDetail(Application application) {
        Candidate candidate = application.getCandidate();
        JobPosting job = application.getJobPosting();

        System.out.println("\n--- CHI TIẾT HỒ SƠ ỨNG TUYỂN ---");
        System.out.println(
                "Mã hồ sơ: " + application.getApplicationId()
        );
        System.out.println(
                "Tin tuyển dụng: "
                        + job.getTitle()
                        + " ("
                        + job.getJobId()
                        + ")"
        );
        System.out.println("Ứng viên: " + candidate.getName());
        System.out.println("Email: " + candidate.getEmail());
        System.out.println("Số điện thoại: " + candidate.getPhone());
        System.out.println("CV: " + candidate.getCvPath());
        System.out.println(
                "Ngày nộp: "
                        + application.getSubmittedAt()
                        .format(DATE_TIME_FORMAT)
        );
        System.out.println(
                "Trạng thái: "
                        + formatApplicationStatus(application.getStatus())
        );
        System.out.println(
                "Ghi chú: " + application.getScreeningNote()
        );

        if (application.hasBeenConvertedToEmployee()) {
            System.out.println(
                    "Mã nhân viên đã tạo: "
                            + application.getHiredEmployeeId()
            );
        }
    }

    // HÀM NHẬP DỮ LIỆU

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);

            String value = scanner.nextLine().trim();

            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                System.out.println(
                        "Vui lòng nhập một số nguyên hợp lệ."
                );
            }
        }
    }

    private int readPositiveInt(String prompt) {
        while (true) {
            int value = readInt(prompt);

            if (value > 0) {
                return value;
            }

            System.out.println("Giá trị phải lớn hơn 0.");
        }
    }

    private int readOptionalPositiveInt(
            String prompt,
            int defaultValue
    ) {
        while (true) {
            System.out.print(prompt);

            String value = scanner.nextLine().trim();

            if (value.isEmpty()) {
                return defaultValue;
            }

            try {
                int result = Integer.parseInt(value);

                if (result > 0) {
                    return result;
                }
            } catch (NumberFormatException ignored) {
            }

            System.out.println(
                    "Vui lòng nhập số nguyên lớn hơn 0 "
                            + "hoặc nhấn Enter để giữ nguyên."
            );
        }
    }

    private String readRequired(String prompt) {
        while (true) {
            System.out.print(prompt);

            String value = scanner.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println(
                    "Thông tin này không được để trống."
            );
        }
    }

    private String readOptional(
            String prompt,
            String defaultValue
    ) {
        System.out.print(prompt);

        String value = scanner.nextLine().trim();

        return value.isEmpty() ? defaultValue : value;
    }

    private LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                return LocalDate.parse(
                        scanner.nextLine().trim(),
                        DATE_FORMAT
                );
            } catch (DateTimeParseException ex) {
                System.out.println(
                        "Ngày không đúng định dạng yyyy-MM-dd."
                );
            }
        }
    }

    private LocalDate readOptionalDate(
            String prompt,
            LocalDate defaultValue
    ) {
        while (true) {
            System.out.print(prompt);

            String value = scanner.nextLine().trim();

            if (value.isEmpty()) {
                return defaultValue;
            }

            try {
                return LocalDate.parse(value, DATE_FORMAT);
            } catch (DateTimeParseException ex) {
                System.out.println(
                        "Ngày không đúng định dạng yyyy-MM-dd."
                );
            }
        }
    }

    private LocalDateTime readDateTime(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                return LocalDateTime.parse(
                        scanner.nextLine().trim(),
                        DATE_TIME_FORMAT
                );
            } catch (DateTimeParseException ex) {
                System.out.println(
                        "Thời gian không đúng định dạng "
                                + "yyyy-MM-dd HH:mm."
                );
            }
        }
    }

    private String formatJobStatus(JobPostingStatus status) {
        return switch (status) {
            case DANG_HIEN_THI -> "ĐANG HIỂN THỊ";
            case AN -> "ẨN";
            case DONG -> "ĐÓNG";
        };
    }

    private String formatApplicationStatus(
            ApplicationStatus status
    ) {
        return switch (status) {
            case MOI_TIEP_NHAN -> "MỚI TIẾP NHẬN";
            case CAN_PHONG_VAN -> "CẦN PHỎNG VẤN";
            case DA_LEN_LICH_PHONG_VAN ->
                    "ĐÃ LÊN LỊCH PHỎNG VẤN";
            case TRUNG_TUYEN -> "TRÚNG TUYỂN";
            case KHONG_PHU_HOP -> "KHÔNG PHÙ HỢP";
            case DA_TAO_NHAN_VIEN ->
                    "ĐÃ TẠO HỒ SƠ NHÂN VIÊN";
        };
    }
}