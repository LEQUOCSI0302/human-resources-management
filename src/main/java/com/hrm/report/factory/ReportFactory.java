package com.hrm.report.factory;

import com.hrm.report.service.ReportService;

import java.util.HashMap;
import java.util.Map;

public class ReportFactory {
    private Map<String, ReportService> registry = new HashMap<>();

    public void register(String type, ReportService service) {
        registry.put(type, service);
    }

    public ReportService getReportService(String type) {
        return registry.get(type);
    }
}
