    package com.hrm.attendance.service;

    import com.hrm.attendance.model.AttendanceRecord;
    import com.hrm.attendance.model.LeaveRequest;
    import com.hrm.attendance.model.Shift;
    import com.hrm.attendance.repository.AttendanceRepository;
    import com.hrm.profile.model.EmployeeProfile;

    import java.time.LocalDate;
    import java.time.LocalTime;
    import java.util.ArrayList;
    import java.util.List;

    public class AttendanceService {

        private AttendanceRepository repo;
        private List<LeaveRequest> leaveRequestRepo = new ArrayList<>();

        public AttendanceService(AttendanceRepository repo) {
            this.repo = repo;
        }

    // xử lý chấm công
        public void processAttendance(EmployeeProfile emp, Shift shift, boolean validLocation) {
            //3.1.2 validate đầu vào
            if (emp == null) {
                System.out.println("Không tìm thấy nhân viên.");
                return;
            }

            if (shift == null) {
                System.out.println("Nhân viên chưa được phân ca.");
                return;
            }


            //3.1.2 check vtri
            if (!validLocation) {
                System.out.println("Bạn không ở đúng vị trí chấm công.");
                return;
            }

            //3.1.2 Lấy ngày hiện tại
            String today = LocalDate.now().toString();

            //3.1.2 Tra cứu lịch sử chấm công hôm nay
            AttendanceRecord record = repo.findToday(emp, today);


    //        3.1.3 lấy thờ gian hệ thống hiện ta
            String currentTime = LocalTime.now().withNano(0).toString();


            //3.1.4 chưa có record => check in
            if (record == null) {
                AttendanceRecord newRecord = new AttendanceRecord(emp, today, shift, true);
                newRecord.checkIn(currentTime);


                //3.1.3 check trạng thái
                if (currentTime.compareTo(shift.getStartTime()) > 0) {
                    newRecord.setStatus("Đi muộn");
                } else {
                    newRecord.setStatus("Đúng giờ");
                }

        //3.1.4 Insert
                repo.save(newRecord);
                System.out.println("Check In thành công.");
                return;

            }

        //3.1.4 đã có record
            record.checkOut(currentTime);

        //3.1.3 check về sớm
            if (currentTime.compareTo(shift.getEndTime()) < 0) {
                record.setStatus(record.getStatus() + " | Về sớm");
            }

        //3.1.4
            repo.update(record);
            System.out.println(" Check Out thành công.");

        }

        //checkin

        public void checkIn(EmployeeProfile emp, String date, Shift shift) {
            AttendanceRecord record = new AttendanceRecord(emp, date, shift, true);
            repo.save(record);

        }

           //Tính tổng giờ làm
        public double getTotalHoursByEmployee(EmployeeProfile emp, String month) {
            double total = 0;

            for (AttendanceRecord r : repo.findByEmployee(emp)) {
                if (r.getDate().startsWith(month)) {
                    total += r.getShift().getWorkHours();

                }
            }
            return total;
        }

        //gưi yêu câ nghỉ phép
        public void submitLeaveRequest(LeaveRequest request) {
            leaveRequestRepo.add(request);
            System.out.println(" Đã gửi yêu cầu nghỉ phép: " + request.getRequestId());
        }

     //phê duyệt nghỉ phép

        public void approveLeave(String requestId) {
            for (LeaveRequest req : leaveRequestRepo) {
                if (req.getRequestId().equals(requestId)) {
                    req.approve();
                    System.out.println(" Yêu cầu " + requestId + " đã được phê duyệt.");
                    return;
                }
            }
        }
    }