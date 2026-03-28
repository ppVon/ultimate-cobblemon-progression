package org.ppvon.ucp.common.access.trainer;

public interface TrainerLevelHolder {
    int ucp$getTrainerLevel();
    void ucp$setTrainerLevel(int level);
    boolean ucp$isTrainerLevelInitialized();
    void ucp$setTrainerLevelInitialized(boolean initialized);
}
