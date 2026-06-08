package gui.admin;

import dto.OverviewDTO;

public interface OverviewView {

    void displayOverview(
            OverviewDTO dto
    );

    void showError(
            String message
    );
}