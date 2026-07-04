package com.hrm;

import com.hrm.auth.pattern.SessionManager;
import com.hrm.console.*;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class MainMenu {
        private LoginMenu loginMenu;
        private ProfileMenu profileMenu;
        private RecruitmentMenu recruitmentMenu;
        private AttendanceMenu attendanceMenu;
        private PayrollMenu payrollMenu;
        private TrainingMenu trainingMenu;
        private DisciplineMenu disciplineMenu;
        private BudgetMenu budgetMenu;
        private ReportMenu reportMenu;
        private SessionManager session;

        private Scanner scanner;

        // 2. Constructor nhận đầy đủ 9 tham số Menu con đúng theo sơ đồ lớp
    public MainMenu(LoginMenu loginMenu,
                ProfileMenu profileMenu,
                RecruitmentMenu recruitmentMenu,
                AttendanceMenu attendanceMenu,
                PayrollMenu payrollMenu,
                TrainingMenu trainingMenu,
                DisciplineMenu disciplineMenu,
                BudgetMenu budgetMenu,
                ReportMenu reportMenu, SessionManager session) {

            this.loginMenu = loginMenu;
            this.profileMenu = profileMenu;
            this.recruitmentMenu = recruitmentMenu;
            this.attendanceMenu = attendanceMenu;
            this.payrollMenu = payrollMenu;
            this.trainingMenu = trainingMenu;
            this.disciplineMenu = disciplineMenu;
            this.budgetMenu = budgetMenu;
            this.reportMenu = reportMenu;
            this.scanner = new Scanner(System.in);
            this.session = session;
        }

        // 3. Phương thức hiển thị và điều hướng Menu chính
        public void displayMenu() {
            int choice;
            do {
                System.out.println("\n=========================================");
                System.out.println("   HỆ THỐNG QUẢN LÝ NHÂN SỰ TOÀN DIỆN HRM ");
                System.out.println("=========================================");
                System.out.println("1. Phân hệ: Đăng nhập & Tài khoản (Login)");
                System.out.println("2. Phân hệ: Quản lý Nhân viên (Profile)");
                System.out.println("3. Phân hệ: Quản lý Tuyển dụng (Recruitment)");
                System.out.println("4. Phân hệ: Quản lý Chấm công (Attendance)");
                System.out.println("5. Phân hệ: Quản lý Lương (Payroll)");
                System.out.println("6. Phân hệ: Quản lý Đào tạo & KPI (Training)");
                System.out.println("7. Phân hệ: Quản lý Kỷ luật (Discipline)");
                System.out.println("8. Phân hệ: Quản lý Ngân sách (Budget)");
                System.out.println("9. Phân hệ: Báo cáo & Thống kê (Report)");
                System.out.println("0. Thoát chương trình");
                System.out.print("Chọn phân hệ chức năng (0-9): ");

                try {
                    choice = Integer.parseInt(scanner.nextLine());
                    switch (choice) {
                        case 1: loginMenu.displayMenu(); break;
                        case 2:
                            if(!session.isLoggedIn()){
                                System.out.println("Bạn chưa đăng nhập!");
                                break;
                            }
                            profileMenu.displayMenu();
                            break;
                        //case 3: recruitmentMenu.displayMenu(); break;
                        //case 4: attendanceMenu.displayMenu(); break;
                        //case 5: payrollMenu.displayMenu(); break;
                        case 6: if (!session.isLoggedIn()) {
                            System.out.println("Bạn chưa đăng nhập!");
                            break;
                        }
                            String roleName = session.getCurrentAccount().getRole().getRoleName();
                            if (!isTrainingRoleAllowed(roleName)) {
                                System.out.println("Bạn không có quyền truy cập phân hệ Đào tạo & KPI! (Chỉ ADMIN/HR/MANAGER)");
                                break;
                            }
                            trainingMenu.displayMenu();
                            break;
                        //case 7: disciplineMenu.displayMenu(); break;
                        //case 8: budgetMenu.displayMenu(); break;
                        case 9: reportMenu.displayMenu(); break;
                        case 10: reportMenu.displayMenu(); break;
                        case 0:
                            System.out.println("Đang thoát hệ thống... Tạm biệt!");
                            break;
                        default:
                            System.out.println("Lựa chọn không hợp lệ! Vui lòng chọn từ 0 đến 9.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Lỗi: Vui lòng chỉ nhập số từ 0 đến 9!");
                    choice = -1; // Kích hoạt lặp lại menu
                }
            } while (choice != 0);
        }
    // Kiểm tra role có được phép vào phân hệ Đào tạo & KPI hay không.
    // Chỉ ADMIN, HR, MANAGER được truy cập; các role khác (VD: EMPLOYEE) bị chặn.
    private boolean isTrainingRoleAllowed(String roleName) {
        if (roleName == null) return false;
        return roleName.equalsIgnoreCase("ADMIN")
                || roleName.equalsIgnoreCase("HR")
                || roleName.equalsIgnoreCase("MANAGER");
    }

}
