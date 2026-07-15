package dev.jackraidenph.data;

import java.util.Map;

import static dev.jackraidenph.Utils.parseIntSafe;

public record LevelScorePartial(
        String userName,
        Integer playerID,
        Integer percentage,
        Integer ranking,
        Integer coins,
        Integer accountID,
        String age
) {
    public LevelScorePartial(Map<Integer, String> params) {
        this(
                params.get(1),
                parseIntSafe(params.get(2)),
                parseIntSafe(params.get(3)),
                parseIntSafe(params.get(6)),
                parseIntSafe(params.get(13)),
                parseIntSafe(params.get(16)),
                params.get(42)
        );
    }
}
