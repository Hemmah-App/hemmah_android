package com.google.hemmah.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class WalkthroughItem {
    private int imageResourceId;
    private String title;
    private String description;
}
