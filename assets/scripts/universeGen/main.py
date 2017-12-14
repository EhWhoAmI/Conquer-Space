# universeGen/main.py -- generates universe, loads it into xml file
# Get universe config, then parse the XML into file

import xml.etree.ElementTree as ET
from datetime import datetime
import random
 
# universe size
universeSize = { 'Small' : 1, 'Medium' : 2, 'Large' : 3}[universeConfig.getUniverseSize()]

# Universe Shape
universeShape = {'Spiral' : 1, 'Irregular' : 2, 'Elliptical' : 3 }[universeConfig.getUniverseShape()]

# Universe Age -- Doesnt really matter that much, but keep it anyway
universeHistory = {'Short' : 1, 'Medium' : 2, 'Long' : 3, 'Ancient': 4}[universeConfig.getUniverseAge()]

# Civ count
civCount = {'Sparse' : 1, 'Common' : 2}[universeConfig.getCivilizationCount()]

# Planet count
planetCommonality = {'Common' : 1, 'Sparse' : 2}[universeConfig.getPlanetCommonality()]

# Set root element
root = ET.Element('root')

# Set up version elements
versionE = ET.SubElement(root, 'version')
versionE.text = version.toString()
# Set up date
dateE = ET.SubElement(root, 'date')
dateE.text = str(datetime.now())

# Set universe
universeE = ET.SubElement(versionE, 'universe')
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
    sectorE = ET.SubElement(universeE, 'sector', {'id':str(i)})
    # Add Starsystems
    starSystems = random.randint(10, 20)
    for n in range(starSystems):
        starSystem = ET.SubElement(sectorE, 'star-system', {'id':str(n)})
        # Stars
        stars = 1
        for b in range(stars):
            star = ET.SubElement(starSystem, 'star', {'id':str(b)})
        # Planets...
        planets = random.randint(0, 9)
        for b in range(planets):
            planet = ET.SubElement(starSystem, 'planet', {'id':str(b)})
            
# Write to file
tree = ET.ElementTree(root)
tree.write('text.xml')