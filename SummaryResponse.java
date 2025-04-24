package com.example.mylyrics;

import java.util.List;

public class SummaryResponse {
    private List<Summary> summary;

    public List<Summary> getSummary() {
        return summary;
    }

    public static class Summary {
        private String summary_text;

        public String getSummary_text() {
            return summary_text;
        }
    }
}
