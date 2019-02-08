Satellites are written differently compared to the other JSON files you may
have written.

Here are the few things that all satellites need:
name -- Name of the Satellite
type -- Type of satellite as refrerenced below.
tech -- Techs required for building this satellite
mass -- weight of satellite
cost -- To be implemented when resources are.

Satellite types:
None -- No purpose. Maybe to show off?
    No other options
telescope -- to see planets, or stuff like that.
    - Range - range, in ltyr.
        - Can be modified by some values:
           - If you start a text with a #, then the value is a multiplier, and if it starts with a @ it becomes a value
           - So an example: #range * @scanner then you are multiplying the range by the scanner
           - Operators that work: + - * /
    - Quality - Quality of vision, like clear, blurry, etc. Might not be implemented, ignore for now.
