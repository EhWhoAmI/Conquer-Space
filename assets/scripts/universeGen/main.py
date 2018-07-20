# universeGen/main.py -- generates universe, loads it into xml file
# Get universe config, then parse the XML into file

# Variables in this script:
# LOGGER -- Logger for this script. Check out org.apache.logging.log4j.Logger.
# universeConfig -- Universe Config object. Check out ConquerSpace.game.universe.UniverseConfig

from ConquerSpace.game import UniversePath
from ConquerSpace.game.universe import GalaticLocation
from ConquerSpace.game.universe.civilization.controllers.AIController import AIController
from ConquerSpace.game.universe.civilization.controllers.PlayerController import PlayerController
from ConquerSpace.game.universe.civilization import Civilization
from ConquerSpace.game.universe.spaceObjects import Planet
from ConquerSpace.game.universe.spaceObjects import Sector
from ConquerSpace.game.universe.spaceObjects import Star
from ConquerSpace.game.universe.spaceObjects import StarSystem
from ConquerSpace.game.universe.spaceObjects import Universe
from ConquerSpace.game.universe.spaceObjects import StarTypes
from ConquerSpace.game.universe.spaceObjects import PlanetTypes
from ConquerSpace.game.universe.spaceObjects.pSectors import RawResource
from ConquerSpace.game.universe.resources import RawResourceTypes

import generation
from constants import *
from java.awt import Color
import math
from os import *
from os.path import *
import random

# universe size -- change this when universe sizes change
universeSize = {'Small': 5, 'Medium': 10, 'Large': 20}[universeConfig.getUniverseSize()]

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
universeObject = Universe(seed)

LOGGER.trace("Loading sectors")
# Create sectors
# Get sector count
layer = 1
degCounter = 0
sizeOfPolygon = 0
sidesLeft = 0
for i in range(universeSize):
    # Set galatic location
    LOGGER.trace("Deg counter = " + str(degCounter))
    secdegs = degCounter
    secdist = (((layer-1)*((SECTOR_MAX_RADIUS)) * 2))
    
    
    if layer == 0:
        secdegs = 0
        secdist = 0
    
    sectorLoc = GalaticLocation(secdegs, secdist)
    # Sector
    sector = Sector(sectorLoc, i)
    
    starSystemCount = random.randint(SECTOR_MIN_SYSTEM, SECTOR_MIN_SYSTEM)
    
    # Add star systems
    for r in range(starSystemCount):
        # Galatic location
        sysdegs = (random.randint(0, 360 * 4) / 4)
        sysdist = random.randint(0, SECTOR_MAX_RADIUS)
        systemLoc = GalaticLocation(sysdegs, sysdist)
        starSystem = StarSystem(r, systemLoc)
        # Create star
        starType = random.randint(1, 100)
        if starType < 55:
            starType = StarTypes.YELLOW
        elif starType < 75:
            starType = StarTypes.BLUE
        elif starType < 96:
            starType = StarTypes.RED
        else:
            starType = StarTypes.BROWN
        starSize = random.randint(1, 10)
        # 0 because there is only one star so far.
        star = Star(starType, starSize, 0)
        starSystem.addStar(star)
        
        planets = random.randint(0, 9)
        
        lastDist = 1
        for n in range(planets):
            # Planets
            ptype = random.randint(0, 1)
            toadd = 0
            if n == 0:
                toadd = 1
            else:
                toadd = n
            orbitalDistance = random.randint(lastDist + 1, lastDist+ 1 + (lastDist/2))
            lastDist = orbitalDistance
            planetSize = random.randint(PLANET_MIN_SIZE, PLANET_MAX_SIZE)
            planet = Planet(ptype, orbitalDistance, planetSize, n, r, i)
            # Set planet sectors
            for b in range(planet.getPlanetSectorCount()):
                if planet.getPlanetType() == PlanetTypes.ROCK:
                    # Set to all gas raw resource
                    rawr = RawResource()
                    # Add other resources
                    # Types of resources
                    # One planet sector can have a max of 3 kinds of resources.
                    resourceTypesCount = random.randint(0, 3)
                    resourceTypes = list(range(RAW_RESOURCE_TYPES_COUNT))
                    for k in range(resourceTypesCount):
                        resourceSelection = random.choice(resourceTypes)
                        resourceTypes.remove(resourceSelection)
                        rawr.addResource(resourceSelection, random.randint(RAW_RESOURCE_MIN, RAW_RESOURCE_CAP))
                        
                    planet.setPlanetSector(b, rawr)
                else:
                    # Select random raw resource
                    rawr = RawResource()
                    rawr.addResource(RawResourceTypes.GAS, random.randint(RAW_RESOURCE_MIN, RAW_RESOURCE_CAP))
                    planet.setPlanetSector(b, rawr)
                    
            starSystem.addPlanet(planet)
            
        sector.addStarSystem(starSystem)
        
    if i == 0:
        centerSize = sector.getSize()
        degCounter = 360
    else:
        # Add degrees
        degCounter = degCounter + math.floor((360 / sizeOfPolygon))
        sidesLeft = sidesLeft - 1
        LOGGER.trace("Size of polygon: " + str(sizeOfPolygon))
        LOGGER.trace("Deg counter = " + str(degCounter))
    
    if sidesLeft == 0:
        # Reset degrees counter
        degCounter = 0
        # Increment layer 
        layer = layer + 1
        
        # Calculate size of polygon
        radius = ((layer-1) * SECTOR_MAX_RADIUS * 2)
        LOGGER.trace("Radius: " + str(radius))
        circurmference = math.pi * radius * 2
        LOGGER.trace("Circurmference: " + str(circurmference))
        # Divide and round up.
        sizeOfPolygon = math.floor(circurmference/(SECTOR_MAX_RADIUS * 2))
        sidesLeft = sizeOfPolygon
        if (universeSize - i) < sidesLeft:
            sidesLeft = (universeSize - i)
                
    universeObject.addSector(sector)
    
LOGGER.info("Done Creating Sectors")
# Generate Civs

LOGGER.trace("Creating Civilizations")
# Number of civs is half of sectors
# So that we will always have `empty` sectors.
civCount = int(math.floor(universeSize/2))
    
LOGGER.info("Civilization Count: " + str(civCount))

# Init player civ
# List for all the civs
sectorList = range(universeSize)

# Player Civ options
civConf = universeConfig.getCivilizationConfig()

civColor = civConf.getCivColor()
civSymbol = civConf.getCivSymbol()
civHomePlanetName = civConf.getHomePlanetName()
civName = civConf.getCivilizationName()
speciesName = civConf.getSpeciesName()
civPreferredClimate = {'Varied': 0, 'Cold':1, 'Hot':2}[civConf.getCivilizationPreferredClimate()]

# Sector 
# Id is 0 because it is the first one.
playerCiv = Civilization(0, universeObject)

playerCiv.setColor(civColor)
playerCiv.setHomePlanetName(civHomePlanetName)
playerCiv.setName(civName)
playerCiv.setSpeciesName(speciesName)
playerCiv.setCivilizationPreferredClimate(civPreferredClimate)
playerCiv.setCivilizationSymbol(civSymbol)
playerCiv.setController(PlayerController())

LOGGER.trace('Civ symbol: "' + civSymbol + '"')
HomesectorID = random.choice(sectorList)
sectorList.remove(HomesectorID)

start = generation.selectRandomSuitablePlanet(universeObject.getSector(HomesectorID), civConf.getCivilizationPreferredClimate())
playerCiv.setStartingPlanet(UniversePath(HomesectorID, start[0], start[1]))
# Get planet then add 1 population center
# Now, choose one random thingy, and enter it.
#homeP = universeObject.getSector(HomesectorID).getStarSystem(start[0]).getPlanet(start[1])
#homeP.setName(civHomePlanetName)
#planetSectorID = random.randint(0, homeP.getPlanetSectorCount())
# Owner id is 0 for player
# homeP.setPlanetSector(planetSectorID, PopulationStorage(CIV_STARTING_POP_STORAGE_MAX, CIV_STARTING_POPULATION, 100, planetSectorID, 0))
universeObject.addCivilization(playerCiv)

# Civ list
symbolList = list('ABCDEFGHIJKLNMOPQRSTUVWXYZ')

# Remove this civ symbol
symbolList.remove(civSymbol)

# Still need to set the player's
for p in range(civCount):
    civ = Civilization(p + 1, universeObject)
    # Civ name list
    civNameList = [
        ["He", "Be", "Das", "Kas", "Mak", "Aef", "Len", "Las", "Ke"],
        ["le", "as", "ma", "\'ea", "mal", "\'as", "had", "'kah", "fad"],
        ["ese", "", "et", "\'tese", "mad", "las", "bas", "ish", ""]
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
    
    # Figure out home system and planet
    # Choose random sector
    HomesectorID = random.choice(sectorList)
    sectorList.remove(HomesectorID)

    # figure out home system by finding a suitable starsystem.
    # rules: the home planet is determined by the position of the planet from the star.
    # So, those in `hot` places will be 1-5
    # `cold` places will be 5-20
    # `varied` will be 1-20.
    
    LOGGER.trace("Choosing home star system")

    start = generation.selectRandomSuitablePlanet(universeObject.getSector(HomesectorID), random.randint(0, 2))
    civ.setStartingPlanet(UniversePath(HomesectorID, start[0], start[1]))
    
    # Get planet then add 1 population center
    # Now, choose one random thingy, and enter it.
    # homeP = universeObject.getSector(HomesectorID).getStarSystem(start[0]).getPlanet(start[1])
    # planetSectorID = random.randint(0, homeP.getPlanetSectorCount()-1)
    
    # homeP.setPlanetSector(planetSectorID, PopulationStorage(CIV_STARTING_POP_STORAGE_MAX, CIV_STARTING_POPULATION, 100, planetSectorID, p+1));
    # homeP.setName(civ.getHomePlanetName())
    
    symbol = random.choice(symbolList)
    symbolList.remove(symbol)
    civ.setCivilizationSymbol(symbol)
    civ.setController(AIController())
    
    universeObject.addCivilization(civ)