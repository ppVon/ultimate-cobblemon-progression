# Ultimate Cobblemon Progression

A Fabric **Cobblemon** side-mod that introduces a **Trainer Level progression system**, adding a sense of growth and scaling to wild Pokémon encounters. As your trainer level increases, new species become available in the wild, and spawn levels and weights are dynamically influenced by your progression tier.

---

## Overview

**Ultimate Cobblemon Progression (UCP)** introduces a player-bound **Trainer Level** that directly affects Pokémon spawning behavior, rarity, and strength.  
This system aims to make the world feel more progressive — starting with early‑route Pokémon and gradually unlocking stronger species as your trainer grows.

UCP works through two main parts:

- **Trainer Level Component** — stores the player’s trainer level.
- **Trainer Level Influence** — modifies spawnability, weights, and level distributions using the player’s tier.

> **Note:** Natural trainer‑leveling is **not implemented yet**. Use commands for now:
>
> ```
> /trainerlevel set <level>
> ```

---

## Features

### Trainer Level → Progression Tier
Your **trainer level** maps to a **progression tier** (defined in data). Tiers control which Pokémon can spawn and what their cap/level distribution looks like.

### Spawnability (Tier‑Gated Species)
- Tier files define what **species** unlock at each tier.
- If a species is not found in any tier, the spawn is **blocked by default** (see config to allow unknown species).

### Spawn Weight Scaling
- Pokémon **in your current tier** get a **weight buff**.
- Pokémon **below your tier** get a **per‑tier decay** to their weight, but never below a minimum.

The general weight-scaling formula is:
```
newWeight = baseWeight × max(1 − (tierDifference × WEIGHT_DECAY_PER_TIER), WEIGHT_MIN_FACTOR)
```

**Example:**

- Pokemon Tier: 1
- Pokemon Weight: 300
- Player Level: 5
- WEIGHT_DECAY_PER_TIER = 0.20 (default)
- WEIGHT_MIN_FACTOR = 0.15 (default)
```
New weight = 300 × (1 − (4 × 0.20)) = 60  (doesn’t drop below 0.15×)
```

### Level Scaling
Wild Pokémon levels use a **triangle distribution** centered near the tier’s **level cap**. The curve edges and center are controlled by min/avg/max scaling ratios.  
Lower‑tier species get a **cap boost** when the player is much higher tier (so low‑tier spawns don’t stay permanently low level), but they still remain weaker than higher‑tier species.

```
capBoost = (playerTierCap - pokemonTierCap) * TIER_CAP_SCALING
newBaseLevel = pokemonTierCap + capBoost

minLevel = MIN_LEVEL_SCALING * newBaseLevel
avgLevel = AVG_LEVEL_SCALING * newBaseLevel
maxLevel = MAX_LEVEL_SCALING * newBaseLevel
```

**Example:**

- Pokemon's tier cap: **15**
- Player's tier cap (at trainer level 5): **69**
- TIER_CAP_SCALING = 0.25 (defult)
- MIN_LEVEL_SCALING = 0.45 (default)
- AVG_LEVEL_SCALING = 0.75 (default)
- MAX_LEVEL_SCALING = 1.1 (default)
```
capBoost = (69 - 15) * 0.25 ~= 14
newBaseLevel = 15 + 14 = 29

minLevel = 0.45 * 29 ~= 13
avgLevel = 0.75 * 29 ~= 22
maxLevel = 1.1 * 29 ~= 32
```
With defaults, Tier‑1 spawns (at player Tier 5) land roughly **13–32**, avg ~**22**.

---

## Data / Datapack Integration

Tiers and unlock lists are defined via datapack JSON. A typical layout is:

```
data/ultimate-cobblemon-progression/tiers/
  tier_1.json
  tier_2.json
  ...
```

**Example `tier_3.json`:**
```json
{
  "level_cap": 10,
  "species": [
    "cobblemon:pidgeotto",
    "cobblemon:mankey",
    "cobblemon:nidorino"
  ]
}
```

> You can structure tiers as you like per namespace; the mod only requires that it can resolve a tier → species list and a level cap for that tier.

The default tiers are auto-generated via a script that organizes mons by Base Stat Total:

| Tier | Boundaries (BST Range) | Pokémon Count | Level cap |
|------|------------------------|----------------|--------|
| 1 | [175, 305] | 214 | 15 |
| 2 | (305, 354] | 186 | 27 |
| 3 | (354, 440] | 200 | 40 |
| 4 | (440, 475] | 214 | 54 |
| 5 | (475, 500] | 186 | 69 |
| 6 | (500, 570] | 244 | 85 |
| 7 | (570, 1125] | 155 | 100 |

The default data includes species from the following addons and sidemods, but the mod itself does not require them:
- Myths and Legends
- Complete Cobblemon Collection + Myths and Legends
- Mega Showdown
- MysticMons

And some fun stats about Pokemon BST:

**Total Pokémon:** 1399

| Metric | Value |
|--------|--------|
| **BST Range** | 175 – 1125 |
| **Mean** | 442.62 |
| **Median** | 472.00 |
| **Standard Deviation** | 116.98 |

**Percentiles**

| Percentile | BST |
|-------------|-----|
| 10th | 290 |
| 25th | 336 |
| 50th (Median) | 472 |
| 75th | 514 |
| 90th | 580 |


---

## Configuration

UCP uses the **Moonlight** config system (common, synced). The generated file name typically looks like:

```
config/ultimatecobblemonprogression-common.toml
```

### Current Options

### Spawn Influence Options
These values are used by the influence logic today (defined in code). They may be promoted to user‑configurable settings over time:

| Key | Purpose |
|---|---|
| `BLOCK_UNKNOWN_SPECIES` | If `true`, species not found in any tier are blocked; if `false`, they’re allowed. |
| `WEIGHT_CURRENT_TIER_BUFF` | Multiplier applied to spawn weight for species in the player’s current tier. |
| `WEIGHT_DECAY_PER_TIER` | How much to reduce weight per tier below the player. |
| `WEIGHT_MIN_FACTOR` | Floor for weight after decays. |
| `MIN_LEVEL_SCALING` / `AVG_LEVEL_SCALING` / `MAX_LEVEL_SCALING` | Shape the triangle distribution around a tier’s cap. |
| `TIER_CAP_SCALING` | How much lower‑tier caps get boosted when the player is many tiers higher. |

> See `TrainerLevelComponent` and `TrainerLevelInfluence` for the authoritative source and inline docs.

---

## Commands

For development and testing:

```
/trainerlevel set <level>
/trainerlevel get
```
---

## Roadmap

- Natural trainer level progression.
    - I'm weighing my options on whether to use RAD Trainers or RAD Gyms to affect progression
    - RAD Trainers specifically has its own level cap system that is tightly coupled with its series system
          which may prove difficult to integrate with
    - Current implementation can be used with FTB Quests to increase trainer level using command rewards
- Cobblemon Raid Den integrations
- Alpha Project integration

---