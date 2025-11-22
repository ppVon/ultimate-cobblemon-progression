# UCP 1.3.0

Supports (and requires) Cobblemon 1.7!

The rules and spawning affects from this mod still affect pokemon spawned via bait or PokeSnack

# UCP 1.2.2

## Fixes
- Fixed a crash caused by a deprecated GSON function

# UCP 1.2.1

## Fixes
- Tier 7 can now be reached naturally via dex progression as intended.  It was bugged before.  It also displays a message and /trainerlevel get will just show that you are max level.
- Tier up message now shows an accurate new species number based on species that are actually registered with cobblemon

# UCP 1.2.0

## Fixes
Performance has been improved by a large factor.  UCP now adds a field to the species class using mixins to store the tier on the species directly, instead of maintaining a hash table of species:tier.

# UCP 1.1.1

## Features
- /trainerlevel get now shows the requirements to level up

## Fixes
- Removed debug log on dex data changed
- Removed runtime dependency of modmenu to make UCP server-side safe
- /trainerlevel set now requires op level 2

# UCP 1.1.0

## Features
- Implemented party level cap (i forgor 💀)
- Trainer level can now be increased naturally via dex seen/caught counts defined per-tier
- Trainer level increases are announced to the player but not the server (yet)

## Config
- Configs no longer use Moonlight so it is no longer a dependency, but modmenu is still supported
- Config files now live in /config/ultimate-cobblemon-progression
- New Options:
  - doLevelCap: Whether to enforce party level caps based on trainer level
  - doDexProgression: Whether to do natural trainer level progression based on dex entries
  - doSpeciesBlocking: Whether to enforce tiers' species lists
  - doLevelScaling: Whether to scale mon spawn levels to their tier's level cap
  - doWeightScaling: Whether to affect the weight of spawned mons to the player's current tier

## Datapacks
- `level-cap` is now `levelCap`
- Dex requirements to reach a tier (other than tier_1) can be defined as:

```json
"requirements": {
  "dex": {
    "seen": 10,
    "caught": 20    
  }
}
```
- `seen` and `caught` are both optional on a per-tier basis
