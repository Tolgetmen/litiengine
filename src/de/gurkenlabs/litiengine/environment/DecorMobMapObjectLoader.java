package de.gurkenlabs.litiengine.environment;

import java.util.Collection;

import de.gurkenlabs.core.Align;
import de.gurkenlabs.core.Valign;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.DecorMob;
import de.gurkenlabs.litiengine.entities.DecorMob.MovementBehavior;
import de.gurkenlabs.litiengine.entities.IEntity;
import de.gurkenlabs.litiengine.environment.tilemap.IMapObject;
import de.gurkenlabs.litiengine.environment.tilemap.MapObjectProperty;
import de.gurkenlabs.litiengine.environment.tilemap.MapObjectType;

public class DecorMobMapObjectLoader extends MapObjectLoader {

  protected DecorMobMapObjectLoader() {
    super(MapObjectType.DECORMOB);
  }

  @Override
  public Collection<IEntity> load(IMapObject mapObject) {
    if (MapObjectType.get(mapObject.getType()) != MapObjectType.DECORMOB) {
      throw new IllegalArgumentException("Cannot load a mapobject of the type " + mapObject.getType() + " with a loader of the type " + DecorMobMapObjectLoader.class);
    }

    if (mapObject.getCustomProperty(MapObjectProperty.SPRITESHEETNAME) == null) {
      throw new IllegalArgumentException("A DecorMob requires a " + MapObjectProperty.SPRITESHEETNAME + " to be set.");
    }

    // TODO: this is a very weird default value
    short velocity = (short) (100 / Game.getInfo().getDefaultRenderScale());
    if (mapObject.getCustomProperty(MapObjectProperty.DECORMOB_VELOCITY) != null) {
      velocity = Short.parseShort(mapObject.getCustomProperty(MapObjectProperty.DECORMOB_VELOCITY));
    }

    final DecorMob mob = new DecorMob(mapObject.getLocation(), mapObject.getCustomProperty(MapObjectProperty.SPRITESHEETNAME), MovementBehavior.get(mapObject.getCustomProperty(MapObjectProperty.DECORMOB_BEHAVIOUR)), velocity);
    this.loadProperties(mob, mapObject);
    mob.setIndestructible(mapObject.getCustomPropertyBool(MapObjectProperty.PROP_INDESTRUCTIBLE));
    mob.setCollision(mapObject.getCustomPropertyBool(MapObjectProperty.COLLISION));
    mob.setCollisionBoxWidth(mapObject.getCustomPropertyFloat(MapObjectProperty.COLLISIONBOX_WIDTH));
    mob.setCollisionBoxHeight(mapObject.getCustomPropertyFloat(MapObjectProperty.COLLISIONBOX_HEIGHT));

    mob.setCollisionBoxAlign(Align.get(mapObject.getCustomProperty(MapObjectProperty.COLLISION_ALGIN)));
    mob.setCollisionBoxValign(Valign.get(mapObject.getCustomProperty(MapObjectProperty.COLLISION_VALGIN)));

    Collection<IEntity> entities = super.load(mapObject);
    entities.add(mob);
    return entities;
  }
}
