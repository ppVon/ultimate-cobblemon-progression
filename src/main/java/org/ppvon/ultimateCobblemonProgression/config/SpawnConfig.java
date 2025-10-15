package org.ppvon.ultimateCobblemonProgression.config;

public class SpawnConfig {
    public boolean doSpeciesBlocking = true;
    public boolean blockUnknownSpecies = true;

    public boolean doLevelScaling = true;
    public double tierCapScaling = 0.25;
    public double minLevelScaling = 0.45;
    public double avgLevelScaling = 0.75;
    public double maxLevelScaling = 1.1;

    public boolean doWeightScaling = true;
    public double weightDecayPerTier = 0.20;
    public double weightMinFactor = 0.15;
    public double weightCurrentTierBuff = 2.0;

}
