# Rubidia

Rubidia is a french minecraft MMORPG server with everything you can imagine: levels/experience, skills,
quests, guilds, weapon customization, pets, wars, cities.
This project was started the February 9th, 2015.

## Plugins

Rubidia is made of several plugins, which were initially designed to work independently
but for the sake of simplicity finally work together as a whole. They cannot be removed independently.
Here's a list of what each plugin is made for:

#### RubidiaCore
The Core plugin of Rubidia, which handles every gameplay details: sittable stairs, smileys, weapons, packets, ...
Each player, when logging in, is assigned (once and for all) an RPlayer which is saved in a Collection of RPlayer named RPlayerColl.
This collection hashmaps player UUIDs to their corresponding RPlayer. RPlayer class saves player data, which is made of maximum 4
characters: SPlayer. SPlayer saves character data such as levels and experience.

#### RubidiaGuilds
The plugin that handles all events and data related to guilds: claims, wars, relationships, ...
Each player, when logging in, is assigned (once and for all) a GMember which is saved in a GMemberColl, similar to an RPlayerColl.
GMember hence saves all data related to a player belonging to a guild. By default, a GMember belongs to the default guild with UUID
0-0-0-0-0.

#### RubidiaMonsters
The plugin that handles all events related to monsters: monsters, regions, characteristics...
Main classes: Monsters, Regions

#### RubidiaPets
Main classes: Pets

Basically, when looking for something, first search in appropriate plugin (based on the plugins names). Then, look for it in RubidiaCore.
