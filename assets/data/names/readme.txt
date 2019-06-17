How to make a name generator.
You will have a json file
name - the name of the name generator
rules - the rules for the formatting. Will get into that later
counters - counters for the name generator

...
names of syllables and syllables in arrays
...

Rules:

Rules will look like this: "s1\\s2\\[a]"

s1 and s2 are syllables, separated by double backslashes.

'a' surrounded by square brackets is a counter. Counters can be modified.

starts with a c/ means that the first letter is capitalized
ends with a /t means it turns to text. One, Two, etc...
ends with a /r means it turns to roman numerals. I, II, III, IV, etc...
ends with a /o means it turns to ordinal numbers. First, Second, Third, etc...

