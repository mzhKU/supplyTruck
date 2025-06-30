Ongoing:
- Position2D instead of X, Y

Todo:
- Refuel should also happen when cannon moves next to supply truck
- Highlight hovered grid unit
- Render name of unit on unit
- Refuel supply truck at base
- Refuel cannon at base
- Drop fuel depots on grid
- Deploy solar panels if supply truck or cannon run out of fuel
- Show maximum travel range such that return to base is possible with three risk levels, safe, medium, risky
- Show current fuel on unit
- Fuel consumption depends on vehicle weight
- Vehicle weight depends on (frame weight, fuel in tank, shells loaded)
- External Vehicle configuration
- Playable without graphics
- Effect when trying to reposition but not enough fuel
- Highligh available positions
- Express fuel / distance capacity as ratios between vehicles
- Log the travel and the fuel cost
- Avoid duplicate calculation of fuel cost

Player control
- Vehicle class which can move, consumes fuel
- Personel class which has no fuel
- Static entities which cannot move

Non player controlled:
- Rough terrain
- Blocked terrain


Supply vehicle:
- Fuel transfer rate
- Supply vehicle can refuel itself
- Control if supply vehicle should refuel neighbour vehicle or not
- What happens if its next to multiple receiving vehicles?


Done:
- Observer pattern for change detection
