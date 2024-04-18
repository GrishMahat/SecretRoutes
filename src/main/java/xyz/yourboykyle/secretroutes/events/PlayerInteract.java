/*
 * Secret Routes Mod - Secret Route Waypoints for Hypixel Skyblock Dungeons
 * Copyright 2023 yourboykyle
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package xyz.yourboykyle.secretroutes.events;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.yourboykyle.secretroutes.Main;
import xyz.yourboykyle.secretroutes.utils.Room;

public class PlayerInteract {
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            EntityPlayer p = e.entityPlayer;
            BlockPos pos = e.pos;
            Block block = e.world.getBlockState(e.pos).getBlock();

            if(block != Blocks.chest && block != Blocks.trapped_chest && block != Blocks.lever && block != Blocks.skull) {
                return;
            }

            if (Main.currentRoom.getSecretType() == Room.SECRET_TYPES.INTERACT) {
                BlockPos interactPos = Main.currentRoom.getSecretLocation();

                if (pos.getX() == interactPos.getX() && pos.getY() == interactPos.getY() && pos.getZ() == interactPos.getZ()) {
                    Main.currentRoom.nextSecret();
                    System.out.println("Interacted with block at " + interactPos);
                }
            }

            // Route Recording
            if(block == Blocks.lever || block == Blocks.skull) {
                // If the block is a lever or skull (essence), then it is a waypoint on the route, going to a secret
                if (Main.routeRecording.recording) {
                    Main.routeRecording.addWaypoint(Room.WAYPOINT_TYPES.INTERACTS, e.pos);
                }
            } else if(block == Blocks.chest || block == Blocks.trapped_chest) {
                // If the block is a chest or a trapped chest (mimic chest), then it is a waypoint for a secret, so start a new secret waypoint list
                if (Main.routeRecording.recording) {
                    System.out.println("Interact Secret At " + e.pos);
                    Main.routeRecording.addWaypoint(Room.SECRET_TYPES.INTERACT, e.pos);
                    Main.routeRecording.newSecret();
                }
            }
        }
    }
}