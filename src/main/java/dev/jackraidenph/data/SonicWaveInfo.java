package dev.jackraidenph.data;

public record SonicWaveInfo(boolean beaten, Integer percentage, Integer coins, String lastRecordAge) {
    public SonicWaveInfo(boolean beaten, int percentage, int coins) {
        this(beaten, percentage, coins, "Unknown");
    }
}
