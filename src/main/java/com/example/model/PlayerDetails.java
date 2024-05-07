package com.example.model;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "Represents a player details")
public class PlayerDetails {

    @NotNull
    @Schema(description = "The name of the player ", required = true)
    private String playerName;

    @Schema(description = "The score of the player", defaultValue = "0")
    private int score;

    @NotNull
    @Schema(description = "The country of the player", required = true)
    private String country;
}

