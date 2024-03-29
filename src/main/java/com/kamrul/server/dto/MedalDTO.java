package com.kamrul.server.dto;

import com.kamrul.server.models.medal.Medal;
import com.kamrul.server.models.medal.MedalType;
import com.kamrul.server.utils.Converters;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedalDTO {

    private UserDTO user;
    private MedalType medalType;

    public MedalDTO(Medal medal) {
        this.user = Converters.convert(medal.getUser());
        this.medalType = medal.getMedalType();
    }
}
