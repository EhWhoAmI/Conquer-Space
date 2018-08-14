def calculatePopulationStorageCost(people):
    return (people * 1000)

def calculateSpacePortCost(type, number):
    val = {0:1000000, 1:100000}[type]
    return (val * number)