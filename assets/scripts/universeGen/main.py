# universeGen/main.py -- generates universe, loads it into xml file
# Get universe config, then parse the XML into file

from datetime import datetime
import random
import xml.etree.ElementTree as ET
from os.path import *
from os import *

# universe size
universeSize = {'Small': 1, 'Medium': 2, 'Large': 3}[universeConfig.getUniverseSize()]

# Universe Shape
universeShape = {'Spiral': 1, 'Irregular': 2, 'Elliptical': 3}[universeConfig.getUniverseShape()]

# Universe Age -- Doesnt really matter that much, but keep it anyway
universeHistory = {'Short': 1, 'Medium': 2, 'Long': 3, 'Ancient': 4}[universeConfig.getUniverseAge()]

# Civ count
civCount = {'Sparse': 1, 'Common': 2}[universeConfig.getCivilizationCount()]

# Planet count
planetCommonality = {'Common': 1, 'Sparse': 2}[universeConfig.getPlanetCommonality()]

# Set root element
root = ET.Element('root')

# Set up version elements
versionE = ET.SubElement(root, 'version')
versionE.text = version.toString()
# Set up date
dateE = ET.SubElement(root, 'date')
dateE.text = str(datetime.now())

# Set universe
universeE = ET.SubElement(root, 'universe')
sectors = 0
if universeSize == 1:
    #small -- 2 sectors
    sectors = 2
elif universeSize == 2:
    # Medium -- 3 sectors
    sectors = 3
else:
    sectors = 4

for i in range(sectors):
    # For sectors, create galatic location
    sectorDegrees = (random.randint(0, 90) * 4) #90 for 360/4
    sectorDistance = random.randint(1, 100)
    sectorE = ET.SubElement(universeE, 'sector', {'id':str(i), 'degs':str(sectorDegrees), 'dist':str(sectorDistance)})
    # Add Starsystems
    starSystems = random.randint(10, 20)
    for n in range(starSystems):
        # For star systems, create galatic location
        systemDegrees = (random.randint(0, 90) * 4) #90 for 360/4
        systemDistance = random.randint(1, 100)
        starSystem = ET.SubElement(sectorE, 'star-system', {'id':str(n), 'degs':str(systemDegrees), 'dist':str(systemDistance)})
        # Stars
        stars = 1
        for b in range(stars):
            star = ET.SubElement(starSystem, 'star', {'id':str(b)})
            # Star types
            # 1 is brown
            # 2 is yellow
            # 3 is red
            # 4 is blue
            
            starType = random.randint(1, 4)
            starTypeE = ET.SubElement(star, 'star-type')
            starTypeE.text = str(starType)
            # Star sizes -- check research/star/starsizes.txt
            ssize = 0
            if starType == 1:
                # Min Size: 0.085
                # Max size: unknown, but put at 2
                ssize = round(random.random() * 100, 2)
            elif starType == 2:
                # Min size: 0.5
                # max size: 5
                ssize = round(random.randint(4, 20) / 8, 2)
            elif starType == 3:
                # min size: 3
                # max size: 100
                ssize = round(random.randint(12, 400) / 8, 2)
            elif starType == 4:
                # min size: 10
                # max size: 100
                ssize = round(random.randint(40, 400) / 8, 2)
            
            ssizeE = ET.SubElement(star, 'star-size')
            ssizeE.text = str(ssize)
        # Planets...
        planets = random.randint(0, 9)
        # Oribital resonance -- one planet orbits compared to the one after that
        orbitalResonance = {0:2, 1:1.5, 2:1.25, 3:1.75, 4:1.33}[random.randint(0, 4)]
            
        # first planet distance
        planetOrbit = random.randint(1, 10)
        for b in range(planets):
            planet = ET.SubElement(starSystem, 'planet', {'id':str(b)})
            # Planet distance
            pdist = ET.SubElement(planet, 'planet-distance')
            pdist.text = str(planetOrbit)
            # Next planet distance
            planetOrbit = int(round(planetOrbit * orbitalResonance))
            
            # Planet type and size
            # Planet types -- 1 is rock, 2 is gas
            ptype = random.randint(1, 2)
            
            ptypeE = ET.SubElement(planet, 'planet-type')
            ptypeE.text = str(ptype)
            # Check research/planet/planet-sizes.txt
            psize = 0
            if ptype == 1:
                # Select random nuber from 0.3 to 5.
                while psize == 0:
                    psize = float(round(random.random() * 10, 2))
                    
            elif ptype == 2:
                # random int from 1 to 15
                psize = random.randint(1, 15)
                
            psizeE = ET.SubElement(planet, 'planet-size')
            psizeE.text = str(psize)

# civs todo
# Write to file
tree = ET.ElementTree(root)
homedir= expanduser("~")
if not exists(homedir + '/.conquerspace'):
    makedirs(homedir + '/.conquerspace')
    
tree.write(homedir + '/.conquerspace/save1.xml')