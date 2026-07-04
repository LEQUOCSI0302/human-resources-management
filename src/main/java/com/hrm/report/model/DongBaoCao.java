package com.hrm.report.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DongBaoCao - Một dòng dữ liệu (row) trong bảng kết quả báo cáo.
 * Dùng LinkedHashMap để giữ đúng thứ tự cột hiển thị, đồng thời linh hoạt
 * cho cả 3 loại báo cáo (Nhân sự / Lương / Chấm công) mà không cần 3 class riêng.
 */
public class DongBaoCao {
    private final Map<String, Object> cacCot = new LinkedHashMap<>();

    public DongBaoCao them(String tenCot, Object giaTri) {
        cacCot.put(tenCot, giaTri);
        return this;
    }

    public Object get(String tenCot) {
        return cacCot.get(tenCot);
    }

    public Map<String, Object> getCacCot() {
        return cacCot;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Object> e : cacCot.entrySet()) {
            if (!first) sb.append(" | ");
            sb.append(e.getKey()).append(": ").append(dinhDangGiaTri(e.getValue()));
            first = false;
        }
        return sb.toString();
    }

    /**
     * Định dạng giá trị hiển thị: tránh Double.toString() trả về ký hiệu khoa học
     * (VD: 3.15E7) cho các cột số tiền lớn như "Lương thực nhận".
     */
    private static String dinhDangGiaTri(Object giaTri) {
        if (giaTri instanceof Double d) {
            if (d == Math.floor(d) && !d.isInfinite()) {
                return String.format("%,.0f", d);
            }
            return String.format("%,.2f", d);
        }
        return String.valueOf(giaTri);
    }
}