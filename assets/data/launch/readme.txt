How to create your own launch system

A launch system is to send things, anything, into space.
Mainly, in the beginning of the game, it would be used for sending satellites into space,
and everything starts from there.

How to write one.
First, you need to create the necessary techs in the techs folder. Add a 'space travel'
tag to it. Do whatever you want to put into there in the proper tech.

Here is what each of the individual stats and what they do
name -- name of launch type
tech -- the name of the tech you created
size -- The bigger the number, the less the ports you can put in an area.
safety -- The bigger the number, the more the launches blow up
cost -- Cost per production of the launch vehicle
reusable -- the reusability of the ship launch vehicle. Can it be reused?
 - reuse cost -- the cost to reuse
cargo -- Max cargo space of the launch vehicle in kgs

Everything can be changed by techs, perhaps we add a multiplier like
<launch system name>_<value>
