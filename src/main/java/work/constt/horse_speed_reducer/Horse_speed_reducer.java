package work.constt.horse_speed_reducer;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class Horse_speed_reducer implements ModInitializer {

    // Packet identifier for reducing horse speed
    public static final Identifier SPEED_REDUCE_PACKET_ID = new Identifier("my_mario", "reduce_horse_speed");

    // Minimum speed threshold (set to prevent values like 9.9E-4 or lower)
    private static final double MINIMUM_SPEED = 0.001;

    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(SPEED_REDUCE_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                if (player.getVehicle() instanceof HorseEntity) {
                    HorseEntity horse = (HorseEntity) player.getVehicle();
                    EntityAttributeInstance speedAttribute = horse.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                    if (speedAttribute != null) {
                        double currentSpeed = speedAttribute.getBaseValue();
                        if (currentSpeed <= MINIMUM_SPEED) {
                            player.sendMessage(Text.of("§cYou can't decrease the horse's speed any further!"), true);
                            return;
                        }
                        double newSpeed = currentSpeed * 0.98;
                        if (newSpeed < MINIMUM_SPEED) {
                            newSpeed = MINIMUM_SPEED;
                        }
                        speedAttribute.setBaseValue(newSpeed);
                        player.sendMessage(Text.of("§aReduced horse speed by 2%! Current speed: " + newSpeed), true);
                    }
                } else {
                    player.sendMessage(Text.of("§cYou are not riding a horse!"), true);
                }
            });
        });
    }
}
