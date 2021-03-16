package com.kamrul.blog.politicalPostFilter.models;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TextAnalyzingResponse {
    LanguageProbability languageProbability;
    Double probabilityOfBeingPolitical;
    Double banglaProbability;
    Double banglishProbability;
    Double englishProbability;

}
