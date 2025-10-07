package org.ppvon.ultimateCobblemonProgression.common.component;

import org.ladysnake.cca.api.v3.component.ComponentV3;

/**
 * Trainer level applies many scalings in org.ppvon.ultimateCobblemonProgression.influence
 *
 * NOTE
 * Currently there is no way to naturally increase trainer level other than console commands
 * /trainerlevel set x
 *
 * I am still deciding whether to integrate with RAD Gyms or RAD Trainers.
 * RAD Trainers has their own level cap which seems pretty tightly coupled with their
 * series feature, which may be a bit to complex on top of this level cap system.
 */
public interface TrainerLevelComponent extends ComponentV3 {
    int getLevel();
    void setLevel(int level);
}
