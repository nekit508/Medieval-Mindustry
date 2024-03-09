package mdvlm.type.world.blocks.storage;

import arc.util.Log;
import mdvlm.gen.MdvlmCall;
import mdvlm.io.MdvlmTypeIO;
import mindustry.Vars;
import mindustry.annotations.Annotations;
import mindustry.content.Fx;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.graphics.Pal;
import mindustry.logic.Ranged;
import mindustry.type.UnitType;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.ControlBlock;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.Env;

public class CastleBlock extends CoreBlock {
    public float buildRadius = 80;

    public CastleBlock(String name) {
        super(name);

        unitType = new UnitType(name + "-unit"){{
            constructor = BlockUnitUnit::create;

            speed = 0f;
            hitSize = 0f;
            health = 1;
            rotateSpeed = 360f;
            itemCapacity = 0;
            buildSpeed = 1f;
            buildRange = buildRadius;

            internal = true;
            isEnemy = false;
            hidden = true;
            useUnitCap = false;
            logicControllable = false;
            playerControllable = true;
            allowedInPayloads = false;
            createWreck = false;
            envEnabled = Env.any;
            envDisabled = Env.none;
        }};
    }

    @Annotations.Remote(called = Annotations.Loc.server)
    public static void playerSpawn(Tile tile, Player player){
        if(player == null || tile == null || !(tile.build instanceof CastleBuild core)) return;

        player.set(core);
        core.unit.controller(player);
    }

    public class CastleBuild extends CoreBuild implements ControlBlock, Ranged {
        public BlockUnitc unit;

        @Override
        public void created() {
            super.created();
            unit = (BlockUnitc) unitType.create(team);
        }

        @Override
        public Unit unit() {
            unit.tile(this);
            unit.team(team);
            return (Unit) unit;
        }

        @Override
        public void update() {
            super.update();

            unit.tile(this);
            unit.rotation(rotation);
            unit.team(team);

            unit.update();
        }

        @Override
        public void updateTile() {
            super.updateTile();
        }

        @Override
        public boolean shouldAutoTarget() {
            return false;
        }

        @Override
        public float range() {
            return buildRadius;
        }

        @Override
        public void requestSpawn(Player player) {
            MdvlmCall.playerSpawn(tile, player);
        }
    }
}
