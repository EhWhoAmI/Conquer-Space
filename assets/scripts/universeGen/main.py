# universeGen/main.py -- generates universe, loads it into xml file
# Get universe config, then parse the XML into file

from datetime import datetime
import random
import xml.etree.ElementTree as ET
from os.path import *
from os import *

# Import universe files
from ConquerSpace.game.universe import Universe, Sector, StarSystem, Star, Planet, PlanetTypes, StarTypes 

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

# Create universe generation object
universeObject = Universe()