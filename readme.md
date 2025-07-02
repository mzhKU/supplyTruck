Ongoing:
- Refuel should also happen when cannon moves next to supply truck

Todo:
- Add a data header
- API for agent control
- Fuel level max limit -> cannot overfuel.
- Decouple MovementComponent from FuelComponent, maybe introduce a FuelMovementComponent
- Troops dont have fuel but they can move
- Integration test
- Clarify distinction between supply truck fuel and supply truck refuel supply
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
- Handle case when cannon next to truck and base
- Supply truck refuels other supply truck?
- Cannon supplies grenades to other cannon?
- Vehicles have orientation, need to be correctly aligned for refueling
- Refuel also over diagonal

Player control
- Vehicle class which can move, consumes fuel
- Personel class which has no fuel
- Static entities which cannot move

Non player controlled:
- Rough terrain
- Blocked terrain

EntityManager:
- Get neighbours of position


Supply vehicle:
- Fuel transfer rate
- Supply vehicle can refuel itself
- Control if supply vehicle should refuel neighbour vehicle or not
- What happens if its next to multiple receiving vehicles?


Done:
- Observer pattern for change detection
- Position2D instead of X, Y
