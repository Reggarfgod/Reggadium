<div align="center">

# Reggadium

**High-Performance FPS and Create Mod Optimization for Minecraft 1.21.1 (NeoForge)**

[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-brightgreen.svg?style=for-the-badge&logo=minecraft)](https://minecraft.net/)
[![NeoForge](https://img.shields.io/badge/NeoForge-21.1.265+-orange.svg?style=for-the-badge)](https://neoforged.net/)
[![Java](https://img.shields.io/badge/Java-21-red.svg?style=for-the-badge&logo=openjdk)](https://openjdk.org/)
[![License](https://img.shields.io/badge/License-LGPL--3.0-blue.svg?style=for-the-badge)](LICENSE)

<br/>

Reggadium is a lightweight, client-side performance mod designed to give you maximum FPS, eliminate stutter, and keep your game completely smooth, especially when building massive factories with the Create mod.

</div>

---

## Performance Benchmarks

### Normal Gameplay

<div align="center">
  <p><b>Without Shaders: 1300+ FPS</b></p>
  <img src="img/without%20shader.png" alt="Without Shaders" width="100%" />
</div>

<br/>

<div align="center">
  <p><b>With Shaders: 570+ FPS</b></p>
  <img src="img/withshader.png" alt="With Shaders" width="100%" />
</div>

---

### Heavy Factory Stress Test (30,000+ Create Blocks)

<div align="center">
  <p><b>30,000 Create Blocks (No Shaders): 400 - 600 FPS</b></p>
  <img src="img/withoutshader-30k-Create-block.png" alt="30k Create Blocks No Shaders" width="100%" />
</div>

<br/>

<div align="center">
  <p><b>30,000 Create Blocks (With Shaders): 250 - 500 FPS</b></p>
  <img src="img/withshader-30k-Create-block.png.png" alt="30k Create Blocks With Shaders" width="100%" />
</div>

---

## Features

### Create Mod Optimization
* Speeds up rotating shafts, cogwheels, gearboxes, and flywheels so large setups no longer drop your framerate.
* Smart belt and fan performance: stops lag caused by distant belts and air streams.
* Stops motionless machines from wasting performance when turned off.

### Smart Culling
* Only renders what you can actually see.
* Hides distant or off-screen items, experience orbs, particles, and shadows.
* Fixes chests, beacons, and machines disappearing when looking around.

### Performance HUD
* Clean, customizable in-game stats overlay: FPS, coordinates, memory (RAM), GPU, and ping.
* Can be moved to any corner and customized in Video Settings.

---

## Requirements

* Minecraft: 1.21.1
* Mod Loader: NeoForge (21.1.265 or higher)
* Compatible with: Create Mod, Embeddium, Sodium, and Shaders (Oculus / Iris / Neoculus)

---

## How to Configure

Press **Escape -> Options -> Video Settings** to configure all options, or edit `config/reggadium-client.toml`.
