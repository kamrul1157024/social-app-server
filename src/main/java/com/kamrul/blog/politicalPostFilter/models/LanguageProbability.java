package com.kamrul.blog.politicalPostFilter.models;

import lombok.*;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LanguageProbability {
     private Double bangla;
     private Double banglish;
     private Double english;
}
