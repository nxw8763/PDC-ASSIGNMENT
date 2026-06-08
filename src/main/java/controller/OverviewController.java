package controller;

import dto.OverviewDTO;
import gui.admin.OverviewView;
import model.users.Admin;
import service.OverviewService;

public class OverviewController {

    private final OverviewService overviewService;

    public OverviewController(
            OverviewService overviewService
    ) {
        this.overviewService = overviewService;
    }

    public void loadOverview(
            Admin admin,
            OverviewView view
    ) {

        try {

            OverviewDTO dto =
                    new OverviewDTO(
                            overviewService.getStatusCounts(admin),
                            overviewService.getPriorityCounts(admin),
                            overviewService.getCategoryCounts(admin),
                            overviewService.getTechnicianWorkload(admin),
                            overviewService.getTicketTrend(admin)
                    );

            view.displayOverview(dto);

        } catch (Exception ex) {

            view.showError(
                    ex.getMessage()
            );
        }
    }
}