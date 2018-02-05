# universeGen/main.py -- generates universe, loads it into xml file
# Get universe config, then parse the XML into file

# Variables in this script:
# LOGGER -- Logger for this script. Check out org.apache.logging.log4j.Logger.
# universeConfig -- Universe Config object. Check out ConquerSpace.game.universe.UniverseConfig

import random
from os.path import *
from os import *

# Import universe files
from ConquerSpace.game.universe import GalaticLocation
from ConquerSpace.game.universe.spaceObjects import Universe, Sector, StarSystem, Star, Planet
from ConquerSpace.game.universe.civilizations import Civilization
from ConquerSpace.game.universe.civControllers import AIController, PlayerController
from java.awt import Color

# universe size -- change this when universe sizes change
universeSize = {'Small': 10, 'Medium': 20, 'Large': 30}[universeConfig.getUniverseSize()]

# Universe Shape
universeShape = {'Spiral': 1, 'Irregular': 2, 'Elliptical': 3}[universeConfig.getUniverseShape()]

# Universe Age -- Doesnt really matter that much, but keep it anyway
universeHistory = {'Short': 1, 'Medium': 2, 'Long': 3, 'Ancient': 4}[universeConfig.getUniverseAge()]

# Civ count
civCount = {'Sparse': 1, 'Common': 2}[universeConfig.getCivilizationCount()]

# Planet count
planetCommonality = {'Common': 1, 'Sparse': 2}[universeConfig.getPlanetCommonality()]

# Random seed
seed = universeConfig.getSeed();
# Seed random
random.seed(seed)

# Create universe generation object
universeObject = Universe()


LOGGER.info("Loading sectors")
# Create sectors
# Get sector count
for i in range(universeSize):
    # Set galatic location
    # random degrees, 360 degrees. Take 360 * 4, then select a random one.
    secdegs = (random.randint(0, 360))
    secdist = random.randint(0, (25 * universeSize))
    sectorLoc = GalaticLocation(secdegs, secdist)
    # Sector
    sector = Sector(sectorLoc, i)
    
    starSystemCount = random.randint(10, 20)
    
    # Add star systems
    for r in range(starSystemCount):
        # Galatic location
        sysdegs = (random.randint(0, 360 * 4)/4)
        sysdist = random.randint(0, 100)
        systemLoc = GalaticLocation(sysdegs, sysdist)
        starSystem = StarSystem(r, systemLoc)
        # Create star
        starType = random.randint(0, 3)
        starSize = random.randint(1, 10)
        # 0 because there is only one star so far.
        star = Star(starType, starSize, 0)
        starSystem.addStar(star)
        
        planets = random.randint(0, 9)
        
        lastDist = 1
        for n in range(planets):
            # Planets
            ptype = random.randint(0, 1)
            orbitalDistance = random.randint(lastDist, lastDist + 5*n)
            lastDist = orbitalDistance
            planetSize = random.randint(1, 50)
            planet = Planet(ptype, orbitalDistance, planetSize, n)
            starSystem.addPlanet(planet)
            
        sector.addStarSystem(starSystem)
        
    universeObject.addSector(sector)
    
LOGGER.info("Done Creating Sectors")
# Generate Civs

LOGGER.info("Creating Civilizations")
# Get approximate number of starSystems
systemCount = universeSize * 15
LOGGER.info("Universe system approx count: " + str(systemCount))
# Civ count
if civCount == 1:
    civCount = (systemCount/40)
elif civCount == 2:
    civCount = (systemCount/20)
    
LOGGER.info("Civilization Count: " + str(civCount))

# Init player civ

# Player Civ options
civConf = universeConfig.getCivilizationConfig()

civColor = civConf.getCivColor()
civSymbol = civConf.getCivSymbol()
civHomePlanetName = civConf.getHomePlanetName()
civName = civConf.getCivilizationName()
speciesName = civConf.getSpeciesName()
civPreferredClimate = {'Varied': 0, 'Cold':1, 'Hot':2}[civConf.getCivilizationPreferredClimate()]

# Id is 0 because it is the first one.
playerCiv = Civilization(0)

playerCiv.setColor(civColor)
playerCiv.setHomePlanetName(civHomePlanetName)
playerCiv.setName(civName)
playerCiv.setSpeciesName(speciesName)
playerCiv.setCivilizationPreferredClimate(civPreferredClimate)
playerCiv.setCivilizationSymbol(civSymbol)
playerCiv.setController(PlayerController())

LOGGER.info('Civ symbol: "' + civSymbol + '"')
universeObject.addCivilization(playerCiv)
symbolList = list('ABCDEFGHIJKLNMOPQRSTUVWXYZ')

symbolList.remove(civSymbol)

for p in range(civCount):
    civ = Civilization(p + 1)
    # Civ name list
    civNameList = [
        ["He", "Be", "Das", "Kas", "Mak", "Aef", "Len"],
        ["le", "as", "ma", "\'ea", "mal", "\'as", "had"],
        ["ese", "", "et", "\'tese", "mad", "las", "bas"]
    ]
    civName = random.choice(civNameList[0]) + random.choice(civNameList[1]) + random.choice(civNameList[2])
    
    civ.setName(civName)
    
    civ.setColor(Color(random.randint(0, 255), random.randint(0, 255), random.randint(0, 255)))

    # Home Planet name list
    planetNameList = [
        ["Ke", "Ba", "Dam", "Ka", "Mak", "Bef", "Zen"],
        ["la", "es", "da", "eaf", "mal", "\'az", "lad"],
        ["les", "", "et", "\'tese", "mar", "zas", "bas"]
    ]
    civ.setHomePlanetName(random.choice(planetNameList[0]) + random.choice(planetNameList[1]) + random.choice(planetNameList[2]))
    civ.setSpeciesName(civName)
    civ.setCivilizationPreferredClimate(random.randint(0,2))
    
    symbol = random.choice(symbolList)
    symbolList.remove(symbol)
    civ.setCivilizationSymbol(symbol)
    civ.setController(AIController())

    universeObject.addCivilization(civ)