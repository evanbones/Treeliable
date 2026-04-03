# Treeliable

<a href='https://files.minecraftforge.net'><img alt="forge" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/forge_vector.svg"></a>
<a href='https://fabricmc.net'><img alt="fabric" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/fabric_vector.svg"></a>
<a href='https://neoforged.net/'><img alt="neoforge" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/neoforge_vector.svg"></a>

**Treeliable** is a highly configurable, simple mod that adds balanced tree felling mechanics!

### Dependencies

* [Cloth Config](https://modrinth.com/mod/cloth-config) is recommended to configure the mod.

## Features

* Chop down entire trees by breaking a single block. By default, trees must have leaves connected to them to be considered a tree, preventing you from accidentally destroying wooden structures.
  * The time it takes to chop down a tree scales dynamically based on its size. You can configure this algorithm to be logarithmic or linear to suit your modpack's balance.
* Trees can be configured to break layer-by-layer with customizable delays and exponential speedup, instead of instantly vanishing.
* An on-screen icon lets you know when you're looking at a tree that can be felled. There's also a spiderweb-like breaking animation on the tree layers as you chop.
* The mod calculates and applies tool damage and player food exhaustion based on the size of the tree being felled. 
* Sneaking can either prevent chopping or activate it (configurable).
* There's also the option to toggle alternative felling mechanics inspired by Hytale! Chopping a layer of a tree will cause the whole tree to fall.

## Mod Compatibility

* Most mods should work out of the box, as long as their logs and leaves are properly tagged with #minecraft:logs and #minecraft:leaves respectively.
* There are additional features for Apotheosis axes, Silent Gear saws, and handling for more troublesome modded trees like from Alex's Caves and Tropicraft.

## For Developers

Treeliable provides an accessible API (`TreeliableAPI`) allowing other mods to easily integrate. You can:
* Override choppable blocks and leaves.
* Register custom block behaviors.
* Implement custom chopping items with unique chop-counting logic.
* Use the `TreeDetectorBuilder` to scan for non-standard tree shapes.

## License

[![Code license (MIT)](https://img.shields.io/badge/code%20license-MIT-green.svg?style=flat-square)](https://github.com/evanbones/Treeliable/blob/1.20.1/LICENSE)
[![Based on HT's TreeChop](https://img.shields.io/badge/based%20on-HT's%20TreeChop-blue)](https://github.com/hammertater/treechop)

## Credits

Treeliable is a heavily modified fork of [HT's TreeChop](https://github.com/hammertater/treechop) by hammertater, used under its [MIT License](https://github.com/hammertater/treechop/blob/main/LICENSE). This project is built upon their original work and logic for tree detection and chopping algorithms.

---

[![discord-plural](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/social/discord-plural_vector.svg)](https://discord.com/invite/JcGRdT6Pbx) [![github-plural](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/social/github-plural_vector.svg)](https://github.com/evanbones/Treeliable)
