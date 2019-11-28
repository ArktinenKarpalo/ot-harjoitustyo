# Level format v.1

Levels are saved as .zip files
## Files
Zip file should contain the following files:
**level.ogg** - Music of the level in .ogg format
**level.mp4** - Background video of the level in h264
**info** - text-file in UTF-8
---
## Info-file contents
1st line: Version of the savefile format, for example, 1

2nd line: name of the level, string

3rd line: type of the background, one of the following strings: [video]

4th line: scrolling speed of the level (pixels/millisecond), float ie. 0.16

Remaining lines contain LevelEvents in the following for
mat, in arbitrary order, line contents separated by space:
**type - time -- key -- (duration)**

**type** - One of the following: [KEY_HOLD, KEY_PRESS]

**time** - When the event should happen, milliseconds since the start of the level

**key** - integer from range [0...3]

**duration** - if type of the event is KEY_HOLD, duration of key hold should follow, in milliseconds

None of the events should overlap each other on the same key.

#### Example info-file
```
1
Level 1
video
0.16
KEY_PRESS 1500 3
KEY_PRESS 2000 2
KEY_HOLD 3000 0 750
KEY_PRESS 3000 1
```
