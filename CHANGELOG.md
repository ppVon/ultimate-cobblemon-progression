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
