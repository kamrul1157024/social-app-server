package com.kamrul.server.politicalPostFilter.models;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class TextAnalyzingRequest {

    private String fullText;
    private String bangla;
    private String banglish;
    private String english;

}
