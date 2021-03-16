package com.kamrul.blog.politicalPostFilter.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TextRequest {
    private String fullText;
    private String bangla;
    private String banglish;
    private String english;
}
